package com.reggarf.mods.reggadium.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.reggarf.mods.reggadium.Config;
import com.reggarf.mods.reggadium.mixin.render.BufferBuilderAccessor;
import it.unimi.dsi.fastutil.longs.Long2IntMap;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import net.createmod.catnip.render.SpriteShiftEntry;
import net.createmod.catnip.render.SuperByteBuffer;
import net.createmod.catnip.render.TemplateMesh;
import net.createmod.catnip.utility.theme.Color;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import org.jetbrains.annotations.Nullable;
import org.joml.*;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

public class ReggadiumByteBuffer implements SuperByteBuffer {

    private final TemplateMesh template;
    private final int[] shadeSwapVertices;

    private final PoseStack transforms = new PoseStack();

    private int vertexColor; // aabbggrr
    private boolean disableDiffuse;

    @Nullable
    private SpriteShiftFunc spriteShiftFunc;

    private boolean hasCustomOverlay;
    private int overlay;

    private boolean hasCustomLight;
    private int packedLight;
    private boolean useLevelLight;
    @Nullable
    private BlockAndTintGetter levelWithLight;
    @Nullable
    private Matrix4f lightTransform;

    // Reused matrix and vector objects
    private static final Matrix4f MODEL_MAT = new Matrix4f();
    private final Matrix3f normalMat = new Matrix3f();
    private final ShiftOutput shiftOutput = new ShiftOutput();
    private static final Vector3f LIGHT_DIR_0 = new Vector3f(0.2F, 1.0F, -0.7F).normalize();
    private static final Vector3f LIGHT_DIR_1 = new Vector3f(-0.2F, 1.0F, 0.7F).normalize();
    private static final Vector3f FLOAT3 = new Vector3f();
    private static final Vector3f POS0 = new Vector3f();
    private static final Vector3f POS1 = new Vector3f();
    private static final Vector3f POS2 = new Vector3f();
    private static final Vector3f POS3 = new Vector3f();
    private static final Vector2f UV0 = new Vector2f();
    private static final Vector2f UV1 = new Vector2f();
    private static final Vector2f UV2 = new Vector2f();
    private static final Vector2f UV3 = new Vector2f();

    // Native vertex scratch buffer for direct writing
    private static final int BUFFER_VERTEX_COUNT = 256;
    private static final MemoryStack STACK = MemoryStack.create();
    private static final int BUFFER_SIZE = BUFFER_VERTEX_COUNT * EntityVertex.STRIDE;
    private static final long SCRATCH_BUFFER = MemoryUtil.nmemAlignedAlloc(64, BUFFER_SIZE);
    private static long BUFFER_PTR = SCRATCH_BUFFER;
    private static int BUFFED_VERTEX = 0;

    public ReggadiumByteBuffer(TemplateMesh template, int[] shadeSwapVertices) {
        this.template = template;
        this.shadeSwapVertices = shadeSwapVertices;
        reset();
    }

    public ReggadiumByteBuffer(TemplateMesh template) {
        this(template, new int[0]);
    }

    @Override
    public ReggadiumByteBuffer reset() {
        while (!transforms.clear()) {
            transforms.popPose();
        }
        transforms.pushPose();

        vertexColor = 0xffffffff;
        disableDiffuse = false;

        spriteShiftFunc = null;
        hasCustomOverlay = false;
        overlay = OverlayTexture.NO_OVERLAY;
        hasCustomLight = false;
        packedLight = 0;
        useLevelLight = false;
        levelWithLight = null;
        lightTransform = null;
        return this;
    }

    @Override
    public boolean isEmpty() {
        return template.isEmpty();
    }

    @Override
    public PoseStack getTransforms() {
        return transforms;
    }

    @Override
    public SuperByteBuffer scale(float factorX, float factorY, float factorZ) {
        transforms.scale(factorX, factorY, factorZ);
        return this;
    }

    @Override
    public SuperByteBuffer rotate(Quaternionfc quaternion) {
        var last = transforms.last();
        last.pose().rotate(quaternion);
        last.normal().rotate(quaternion);
        return this;
    }

    @Override
    public SuperByteBuffer translate(float x, float y, float z) {
        transforms.translate(x, y, z);
        return this;
    }

    @Override
    public SuperByteBuffer mulPose(Matrix4fc pose) {
        transforms.last().pose().mul(pose);
        return this;
    }

    @Override
    public SuperByteBuffer mulNormal(Matrix3fc normal) {
        transforms.last().normal().mul(normal);
        return this;
    }

    @Override
    public SuperByteBuffer pushPose() {
        transforms.pushPose();
        return this;
    }

    @Override
    public SuperByteBuffer popPose() {
        transforms.popPose();
        return this;
    }

    @Override
    public SuperByteBuffer color(int r, int g, int b, int a) {
        this.vertexColor = (a & 0xff) << 24 | (b & 0xff) << 16 | (g & 0xff) << 8 | (r & 0xff);
        return this;
    }

    public SuperByteBuffer color(float r, float g, float b, float a) {
        return color((int) (r * 255.0f), (int) (g * 255.0f), (int) (b * 255.0f), (int) (a * 255.0f));
    }

    @Override
    public SuperByteBuffer color(int color) {
        this.vertexColor = 0xff000000 | ((color & 0xFF) << 16) | (color & 0xFF00) | ((color & 0xFF0000) >>> 16);
        return this;
    }

    @Override
    public SuperByteBuffer color(Color c) {
        return color(c.getRGB());
    }

    @Override
    public SuperByteBuffer disableDiffuse() {
        disableDiffuse = true;
        return this;
    }

    @Override
    public SuperByteBuffer shiftUV(SpriteShiftEntry entry) {
        spriteShiftFunc = (u, v, output) -> output.accept(entry.getTargetU(u), entry.getTargetV(v));
        return this;
    }

    @Override
    public SuperByteBuffer shiftUVScrolling(SpriteShiftEntry entry, float scrollV) {
        return shiftUVScrolling(entry, 0, scrollV);
    }

    @Override
    public SuperByteBuffer shiftUVScrolling(SpriteShiftEntry entry, float scrollU, float scrollV) {
        float du = entry.getTarget().getU0() - entry.getOriginal().getU0() + scrollU;
        float dv = entry.getTarget().getV0() - entry.getOriginal().getV0() + scrollV;
        spriteShiftFunc = (u, v, output) -> output.accept(u + du, v + dv);
        return this;
    }

    @Override
    public SuperByteBuffer shiftUVtoSheet(SpriteShiftEntry entry, float uTarget, float vTarget, int sheetSize) {
        float invSheetSize = 1.0f / sheetSize;
        spriteShiftFunc = (u, v, output) -> {
            float targetU = entry.getTarget().getU((SpriteShiftEntry.getUnInterpolatedU(entry.getOriginal(), u) * invSheetSize) + uTarget);
            float targetV = entry.getTarget().getV((SpriteShiftEntry.getUnInterpolatedV(entry.getOriginal(), v) * invSheetSize) + vTarget);
            output.accept(targetU, targetV);
        };
        return this;
    }

    @Override
    public SuperByteBuffer overlay(int overlay) {
        hasCustomOverlay = true;
        this.overlay = overlay;
        return this;
    }

    @Override
    public SuperByteBuffer light(int packedLight) {
        hasCustomLight = true;
        this.packedLight = packedLight;
        return this;
    }

    @Override
    public SuperByteBuffer useLevelLight(BlockAndTintGetter level) {
        useLevelLight = true;
        levelWithLight = level;
        return this;
    }

    @Override
    public SuperByteBuffer useLevelLight(BlockAndTintGetter level, Matrix4f lightTransform) {
        useLevelLight = true;
        levelWithLight = level;
        this.lightTransform = lightTransform;
        return this;
    }

    private static float calculateDiffuse(Vector3fc normal, Vector3fc lightDir0, Vector3fc lightDir1) {
        float light0 = java.lang.Math.max(0.0f, lightDir0.dot(normal));
        float light1 = java.lang.Math.max(0.0f, lightDir1.dot(normal));
        return java.lang.Math.min(1.0f, (light0 + light1) * 0.6f + 0.4f);
    }

    public static void clearLightCache() {
        ReggadiumLightCache.clear();
    }

    public static int getLight(BlockAndTintGetter world, Vector3f lightPos) {
        if (world == null) return 0xf000f0;
        BlockPos pos = BlockPos.containing(lightPos.x(), lightPos.y(), lightPos.z());
        if (Config.fastLightCacheEnabled) {
            return ReggadiumLightCache.getLight(world, pos);
        } else {
            return LevelRenderer.getLightColor(world, pos);
        }
    }

    public int getLight(Vector3f lightPos) {
        return getLight(levelWithLight, lightTransform == null ? lightPos : lightPos.mulPosition(lightTransform));
    }

    private static boolean isBufferMax() {
        return BUFFED_VERTEX >= BUFFER_VERTEX_COUNT;
    }

    private static void flush(NativeVertexWriter writer, boolean force, VertexFormat format) {
        if (!force && !isBufferMax()) {
            return;
        }
        if (BUFFED_VERTEX == 0) return;
        STACK.push();
        writer.push(STACK, SCRATCH_BUFFER, BUFFED_VERTEX, format);
        STACK.pop();
        BUFFER_PTR = SCRATCH_BUFFER;
        BUFFED_VERTEX = 0;
    }

    private static boolean isPerspectiveProjection() {
        return RenderSystem.getProjectionMatrix().m33() == 0;
    }

    private static int calcColorSodiumFast(int quadColor, int vertexColor, int diffuseFactor, boolean applyDiffuse) {
        int r = ((((quadColor) & 0xFF) * ((vertexColor) & 0xFF)) + 0xFF) >>> 8;
        int g = ((((quadColor >>> 8) & 0xFF) * ((vertexColor >>> 8) & 0xFF)) + 0xFF) >>> 8;
        int b = ((((quadColor >>> 16) & 0xFF) * ((vertexColor >>> 16) & 0xFF)) + 0xFF) >>> 8;
        int a = ((((quadColor >>> 24) & 0xFF) * ((vertexColor >>> 24) & 0xFF)) + 0xFF) >>> 8;
        if (applyDiffuse) {
            r = (r * diffuseFactor + 255) >>> 8;
            g = (g * diffuseFactor + 255) >>> 8;
            b = (b * diffuseFactor + 255) >>> 8;
        }
        return (a << 24) | (b << 16) | (g << 8) | r;
    }

    private void acceleratedRenderInto(PoseStack input, NativeVertexWriter writer, VertexFormat format) {
        MODEL_MAT.set(input.last().pose());
        Matrix4f localTransforms = transforms.last().pose();
        MODEL_MAT.mul(localTransforms);

        normalMat.set(input.last().normal());
        Matrix3f localNormalTransforms = transforms.last().normal();
        normalMat.mul(localNormalTransforms);

        boolean shaded = true;
        int shadeSwapIndex = 0;
        int nextShadeSwapVertex = shadeSwapIndex < shadeSwapVertices.length ? shadeSwapVertices[shadeSwapIndex] : Integer.MAX_VALUE;
        int unshadedDiffuse = 255;
        boolean applyDiffuse = !disableDiffuse;
        if (applyDiffuse && shadeSwapVertices.length > 0) {
            FLOAT3.set(0, 1, 0);
            unshadedDiffuse = (int) (255 * calculateDiffuse(FLOAT3, LIGHT_DIR_0, LIGHT_DIR_1));
        }

        boolean backfaceCulling = Config.backfaceCullingEnabled && isPerspectiveProjection();

        int vertexCount = template.vertexCount();
        for (int i = 0; i < vertexCount; i += 4) {
            if (i >= nextShadeSwapVertex) {
                shaded = !shaded;
                shadeSwapIndex++;
                nextShadeSwapVertex = shadeSwapIndex < shadeSwapVertices.length ? shadeSwapVertices[shadeSwapIndex] : Integer.MAX_VALUE;
            }

            int packedNormal = template.normal(i);
            float unpackedX = NormI8.unpackX(packedNormal);
            float unpackedY = NormI8.unpackY(packedNormal);
            float unpackedZ = NormI8.unpackZ(packedNormal);
            float nx = MatrixHelper.transformNormalX(normalMat, unpackedX, unpackedY, unpackedZ);
            float ny = MatrixHelper.transformNormalY(normalMat, unpackedX, unpackedY, unpackedZ);
            float nz = MatrixHelper.transformNormalZ(normalMat, unpackedX, unpackedY, unpackedZ);

            POS0.set(template.x(i), template.y(i), template.z(i)).mulPosition(MODEL_MAT);
            POS1.set(template.x(i + 1), template.y(i + 1), template.z(i + 1)).mulPosition(MODEL_MAT);
            POS2.set(template.x(i + 2), template.y(i + 2), template.z(i + 2)).mulPosition(MODEL_MAT);

            if (backfaceCulling) {
                float ex = POS1.x - POS0.x, ey = POS1.y - POS0.y, ez = POS1.z - POS0.z;
                float fx = POS2.x - POS0.x, fy = POS2.y - POS0.y, fz = POS2.z - POS0.z;
                float cx = ey * fz - ez * fy;
                float cy = ez * fx - ex * fz;
                float cz = ex * fy - ey * fx;
                if (nx * cx + ny * cy + nz * cz < 0) {
                    cx = -cx; cy = -cy; cz = -cz;
                }
                if (cx * (POS0.x + POS2.x) + cy * (POS0.y + POS2.y) + cz * (POS0.z + POS2.z) > 0) continue;
            }

            int normal = NormI8.pack(nx, ny, nz);
            POS3.set(template.x(i + 3), template.y(i + 3), template.z(i + 3)).mulPosition(MODEL_MAT);

            if (spriteShiftFunc != null) {
                spriteShiftFunc.shift(template.u(i), template.v(i), shiftOutput);
                UV0.set(shiftOutput.u, shiftOutput.v);

                spriteShiftFunc.shift(template.u(i + 1), template.v(i + 1), shiftOutput);
                UV1.set(shiftOutput.u, shiftOutput.v);

                spriteShiftFunc.shift(template.u(i + 2), template.v(i + 2), shiftOutput);
                UV2.set(shiftOutput.u, shiftOutput.v);

                spriteShiftFunc.shift(template.u(i + 3), template.v(i + 3), shiftOutput);
                UV3.set(shiftOutput.u, shiftOutput.v);
            } else {
                UV0.set(template.u(i), template.v(i));
                UV1.set(template.u(i + 1), template.v(i + 1));
                UV2.set(template.u(i + 2), template.v(i + 2));
                UV3.set(template.u(i + 3), template.v(i + 3));
            }

            int diffuseFactor = 255;
            if (applyDiffuse) {
                if (shaded) {
                    FLOAT3.set(nx, ny, nz);
                    diffuseFactor = (int) (255.0F * calculateDiffuse(FLOAT3, LIGHT_DIR_0, LIGHT_DIR_1));
                } else {
                    diffuseFactor = unshadedDiffuse;
                }
            }

            int c0 = template.color(i);
            int c1 = template.color(i + 1);
            int c2 = template.color(i + 2);
            int c3 = template.color(i + 3);
            int color0, color1, color2, color3;
            if (c0 == c1 && c0 == c2 && c0 == c3) {
                color0 = color1 = color2 = color3 = calcColorSodiumFast(c0, vertexColor, diffuseFactor, applyDiffuse);
            } else {
                color0 = calcColorSodiumFast(c0, vertexColor, diffuseFactor, applyDiffuse);
                color1 = calcColorSodiumFast(c1, vertexColor, diffuseFactor, applyDiffuse);
                color2 = calcColorSodiumFast(c2, vertexColor, diffuseFactor, applyDiffuse);
                color3 = calcColorSodiumFast(c3, vertexColor, diffuseFactor, applyDiffuse);
            }

            int light0 = template.light(i);
            int light1 = template.light(i + 1);
            int light2 = template.light(i + 2);
            int light3 = template.light(i + 3);
            if (hasCustomLight) {
                light0 = SuperByteBuffer.maxLight(light0, packedLight);
                light1 = SuperByteBuffer.maxLight(light1, packedLight);
                light2 = SuperByteBuffer.maxLight(light2, packedLight);
                light3 = SuperByteBuffer.maxLight(light3, packedLight);
            }

            if (useLevelLight) {
                FLOAT3.set(((template.x(i) - .5f) * 15 / 16f) + .5f, (template.y(i) - .5f) * 15 / 16f + .5f, (template.z(i) - .5f) * 15 / 16f + .5f).mulPosition(localTransforms);
                light0 = SuperByteBuffer.maxLight(light0, getLight(FLOAT3));
                FLOAT3.set(((template.x(i + 1) - .5f) * 15 / 16f) + .5f, (template.y(i + 1) - .5f) * 15 / 16f + .5f, (template.z(i + 1) - .5f) * 15 / 16f + .5f).mulPosition(localTransforms);
                light1 = SuperByteBuffer.maxLight(light1, getLight(FLOAT3));
                FLOAT3.set(((template.x(i + 2) - .5f) * 15 / 16f) + .5f, (template.y(i + 2) - .5f) * 15 / 16f + .5f, (template.z(i + 2) - .5f) * 15 / 16f + .5f).mulPosition(localTransforms);
                light2 = SuperByteBuffer.maxLight(light2, getLight(FLOAT3));
                FLOAT3.set(((template.x(i + 3) - .5f) * 15 / 16f) + .5f, (template.y(i + 3) - .5f) * 15 / 16f + .5f, (template.z(i + 3) - .5f) * 15 / 16f + .5f).mulPosition(localTransforms);
                light3 = SuperByteBuffer.maxLight(light3, getLight(FLOAT3));
            }

            if (format == BlockVertex.FORMAT) {
                BlockVertex.write(BUFFER_PTR, POS0.x, POS0.y, POS0.z, color0, UV0.x, UV0.y, light0, normal);
                BUFFER_PTR += BlockVertex.STRIDE;

                BlockVertex.write(BUFFER_PTR, POS1.x, POS1.y, POS1.z, color1, UV1.x, UV1.y, light1, normal);
                BUFFER_PTR += BlockVertex.STRIDE;

                BlockVertex.write(BUFFER_PTR, POS2.x, POS2.y, POS2.z, color2, UV2.x, UV2.y, light2, normal);
                BUFFER_PTR += BlockVertex.STRIDE;

                BlockVertex.write(BUFFER_PTR, POS3.x, POS3.y, POS3.z, color3, UV3.x, UV3.y, light3, normal);
                BUFFER_PTR += BlockVertex.STRIDE;
            } else {
                int overlay0, overlay1, overlay2, overlay3;
                if (hasCustomOverlay) {
                    overlay0 = overlay1 = overlay2 = overlay3 = overlay;
                } else {
                    overlay0 = template.overlay(i);
                    overlay1 = template.overlay(i + 1);
                    overlay2 = template.overlay(i + 2);
                    overlay3 = template.overlay(i + 3);
                }
                EntityVertex.write(BUFFER_PTR, POS0.x, POS0.y, POS0.z, color0, UV0.x, UV0.y, overlay0, light0, normal);
                BUFFER_PTR += EntityVertex.STRIDE;

                EntityVertex.write(BUFFER_PTR, POS1.x, POS1.y, POS1.z, color1, UV1.x, UV1.y, overlay1, light1, normal);
                BUFFER_PTR += EntityVertex.STRIDE;

                EntityVertex.write(BUFFER_PTR, POS2.x, POS2.y, POS2.z, color2, UV2.x, UV2.y, overlay2, light2, normal);
                BUFFER_PTR += EntityVertex.STRIDE;

                EntityVertex.write(BUFFER_PTR, POS3.x, POS3.y, POS3.z, color3, UV3.x, UV3.y, overlay3, light3, normal);
                BUFFER_PTR += EntityVertex.STRIDE;
            }

            BUFFED_VERTEX += 4;
            flush(writer, false, format);
        }

        flush(writer, true, format);
    }

    public void defaultRenderInto(PoseStack input, VertexConsumer builder) {
        Matrix4f modelMat = MODEL_MAT.set(input.last().pose());
        Matrix4f localTransforms = transforms.last().pose();
        modelMat.mul(localTransforms);

        Matrix3f normalMat = this.normalMat.set(input.last().normal());
        Matrix3f localNormalTransforms = transforms.last().normal();
        normalMat.mul(localNormalTransforms);

        boolean applyDiffuse = !disableDiffuse;
        boolean shaded = true;
        int shadeSwapIndex = 0;
        int nextShadeSwapVertex = shadeSwapIndex < shadeSwapVertices.length ? shadeSwapVertices[shadeSwapIndex] : -1;
        int unshadedDiffuse = 255;
        if (applyDiffuse && shadeSwapVertices.length > 0) {
            FLOAT3.set(0, 1, 0);
            unshadedDiffuse = (int) (255 * calculateDiffuse(FLOAT3, LIGHT_DIR_0, LIGHT_DIR_1));
        }

        int vertexCount = template.vertexCount();
        for (int i = 0; i < vertexCount; i++) {
            if (i == nextShadeSwapVertex) {
                shaded = !shaded;
                shadeSwapIndex++;
                nextShadeSwapVertex = shadeSwapIndex < shadeSwapVertices.length ? shadeSwapVertices[shadeSwapIndex] : -1;
            }

            float x = template.x(i);
            float y = template.y(i);
            float z = template.z(i);
            POS0.set(x, y, z).mulPosition(modelMat);

            int light = template.light(i);
            if (hasCustomLight) {
                light = SuperByteBuffer.maxLight(light, packedLight);
            }
            if (useLevelLight) {
                FLOAT3.set(((x - .5f) * 15 / 16f) + .5f, (y - .5f) * 15 / 16f + .5f, (z - .5f) * 15 / 16f + .5f);
                light = SuperByteBuffer.maxLight(light, getLight(FLOAT3));
            }

            int packedNormal = template.normal(i);
            float normalX = ((byte) (packedNormal & 0xFF)) / 127.0f;
            float normalY = ((byte) ((packedNormal >>> 8) & 0xFF)) / 127.0f;
            float normalZ = ((byte) ((packedNormal >>> 16) & 0xFF)) / 127.0f;
            FLOAT3.set(normalX, normalY, normalZ).mul(normalMat);

            int quadColor = template.color(i);
            int r = ((((quadColor) & 0xFF) * ((vertexColor) & 0xFF)) + 0xFF) >>> 8;
            int g = ((((quadColor >>> 8) & 0xFF) * ((vertexColor >>> 8) & 0xFF)) + 0xFF) >>> 8;
            int b = ((((quadColor >>> 16) & 0xFF) * ((vertexColor >>> 16) & 0xFF)) + 0xFF) >>> 8;
            int a = ((((quadColor >>> 24) & 0xFF) * ((vertexColor >>> 24) & 0xFF)) + 0xFF) >>> 8;
            if (applyDiffuse) {
                int factor = shaded ? (int) (255.0F * calculateDiffuse(FLOAT3, LIGHT_DIR_0, LIGHT_DIR_1)) : unshadedDiffuse;
                r = (r * factor + 255) >>> 8;
                g = (g * factor + 255) >>> 8;
                b = (b * factor + 255) >>> 8;
            }
            int color = (a << 24) | (r << 16) | (g << 8) | b;

            float u = template.u(i);
            float v = template.v(i);
            if (spriteShiftFunc != null) {
                spriteShiftFunc.shift(u, v, shiftOutput);
                u = shiftOutput.u;
                v = shiftOutput.v;
            }

            int overlay = hasCustomOverlay ? this.overlay : template.overlay(i);

            builder.addVertex(POS0.x, POS0.y, POS0.z).setColor(color).setUv(u, v).setOverlay(overlay).setLight(light).setNormal(FLOAT3.x, FLOAT3.y, FLOAT3.z);
        }
    }

    public boolean renderDirect(PoseStack input, VertexConsumer builder) {
        if (!Config.optimizationsEnabled || !Config.fastSbbEnabled) {
            return false;
        }

        try {
            NativeVertexWriter writer = NativeVertexWriter.tryOf(builder);
            if (writer != null && builder instanceof BufferBuilder bb) {
                VertexFormat format = ((BufferBuilderAccessor) bb).reggadium$getFormat();
                if (format == BlockVertex.FORMAT || (format == EntityVertex.FORMAT && Config.optimizeEntityRenderingEnabled)) {
                    acceleratedRenderInto(input, writer, format);
                    return true;
                }
            }
        } catch (Throwable ignored) {
        }

        return false;
    }

    @Override
    public void renderInto(PoseStack input, VertexConsumer builder) {
        if (!renderDirect(input, builder)) {
            defaultRenderInto(input, builder);
        }
        reset();
    }
}
