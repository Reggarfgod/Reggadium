package com.reggarf.mods.reggadium.culling;

import com.reggarf.mods.reggadium.mixin.render.FrustumInvoker;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class ReggadiumCuller {

    public static final byte KIND_UNKNOWN = 0;
    public static final byte KIND_CUSTOM = 1;
    public static final byte KIND_STANDARD = 2;
    public static final byte KIND_CHEST = 3;
    public static final byte KIND_ALWAYS_VISIBLE = 4;

    private static final ClassValue<Boolean> CUSTOM_RENDER_BOX = new ClassValue<>() {
        @Override
        protected Boolean computeValue(Class<?> type) {
            try {
                return type.getMethod("getRenderBoundingBox", BlockEntity.class).getDeclaringClass() != BlockEntityRenderer.class;
            } catch (ReflectiveOperationException e) {
                return Boolean.TRUE;
            }
        }
    };

    private static double camX;
    private static double camY;
    private static double camZ;
    private static boolean hasCamPos = false;
    private static Frustum cachedFrustum = null;

    public static void beginFrame(@Nullable Camera camera, @Nullable Frustum frustum) {
        if (camera != null) {
            Vec3 pos = camera.getPosition();
            camX = pos.x;
            camY = pos.y;
            camZ = pos.z;
            hasCamPos = true;
        } else {
            hasCamPos = false;
        }
        cachedFrustum = frustum;
    }

    private static void ensureCamPos() {
        if (!hasCamPos) {
            Vec3 pos = cameraPos();
            if (pos != null) {
                camX = pos.x;
                camY = pos.y;
                camZ = pos.z;
                hasCamPos = true;
            }
        }
    }

    private static Frustum getFrustum() {
        if (cachedFrustum != null) {
            return cachedFrustum;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.levelRenderer != null) {
            return ((com.reggarf.mods.reggadium.mixin.client.LevelRendererAccessor) mc.levelRenderer).reggadium$getCullingFrustum();
        }
        return null;
    }

    private static Vec3 cameraPos() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.gameRenderer == null) {
            return null;
        }
        Camera camera = mc.gameRenderer.getMainCamera();
        return camera == null ? null : camera.getPosition();
    }

    public static boolean isCubeVisible(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        Frustum frustum = getFrustum();
        if (frustum == null) {
            return true;
        }
        return ((FrustumInvoker) frustum).reggadium$cubeInFrustum(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public static boolean isBlockEntityVisible(BlockEntity be) {
        return isBlockEntityVisible(be, null);
    }

    public static boolean isBlockEntityVisible(BlockEntity be, @Nullable BlockEntityRenderer<?> renderer) {
        if (be == null) {
            return true;
        }

        if (be instanceof IReggadiumCullableBlockEntity cullable) {
            byte kind = cullable.reggadium$getBoxKind();
            if (kind == KIND_UNKNOWN) {
                if (renderer != null && CUSTOM_RENDER_BOX.get(renderer.getClass())) {
                    kind = KIND_CUSTOM;
                } else {
                    BlockState state = be.getBlockState();
                    Block block = state.getBlock();
                    if (block == Blocks.BEACON || block == Blocks.END_GATEWAY
                            || block == Blocks.END_PORTAL || block == Blocks.STRUCTURE_BLOCK) {
                        kind = KIND_ALWAYS_VISIBLE;
                    } else if (block == Blocks.CHEST || block == Blocks.TRAPPED_CHEST) {
                        kind = KIND_CHEST;
                    } else {
                        kind = KIND_STANDARD;
                    }
                }
                cullable.reggadium$setBoxKind(kind);
            }

            if (kind == KIND_ALWAYS_VISIBLE) {
                return true;
            }

            BlockPos pos = be.getBlockPos();
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();

            if (kind == KIND_CHEST) {
                return isCubeVisible(x - 1.0, y, z - 1.0, x + 2.0, y + 2.0, z + 2.0);
            }

            if (kind == KIND_STANDARD) {
                return isCubeVisible(x, y, z, x + 1.0, y + 1.0, z + 1.0);
            }

            if (kind == KIND_CUSTOM) {
                BlockState state = be.getBlockState();
                double[] box = cullable.reggadium$getRelativeBox();
                if (box == null || cullable.reggadium$getBoxState() != state) {
                    if (renderer != null) {
                        AABB aabb = getRenderBoundingBox(renderer, be);
                        if (aabb != null) {
                            box = new double[] {
                                    aabb.minX - x, aabb.minY - y, aabb.minZ - z,
                                    aabb.maxX - x, aabb.maxY - y, aabb.maxZ - z
                            };
                        }
                    }
                    if (box == null) {
                        box = new double[] { 0.0, 0.0, 0.0, 1.0, 1.0, 1.0 };
                    }
                    cullable.reggadium$setRelativeBox(state, box);
                }
                return isCubeVisible(x + box[0], y + box[1], z + box[2],
                                     x + box[3], y + box[4], z + box[5]);
            }
        }

        return isPosVisible(be.getBlockPos());
    }

    private static AABB getRenderBoundingBox(BlockEntityRenderer<?> renderer, BlockEntity be) {
        try {
            java.lang.reflect.Method m = renderer.getClass().getMethod("getRenderBoundingBox", BlockEntity.class);
            return (AABB) m.invoke(renderer, be);
        } catch (Throwable ignored) {
            BlockPos pos = be.getBlockPos();
            return new AABB(pos);
        }
    }

    public static boolean isPosVisible(BlockPos pos) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        return isCubeVisible(x, y, z, x + 1.0, y + 1.0, z + 1.0);
    }

    public static boolean isAABBVisible(AABB box) {
        if (box == null) {
            return true;
        }
        return isCubeVisible(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
    }

    public static boolean isPointVisible(double x, double y, double z, double radius) {
        return isCubeVisible(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius);
    }

    public static boolean isParticleVisible(Particle p, float partialTicks) {
        if (p == null) {
            return true;
        }
        AABB box = p.getBoundingBox();
        if (box == null) {
            return true;
        }
        return isAABBVisible(box);
    }

    public static boolean isBeyondDistance(double x, double y, double z, double maxDistanceSq) {
        ensureCamPos();
        if (!hasCamPos) {
            return false;
        }
        double dx = x - camX;
        double dy = y - camY;
        double dz = z - camZ;
        return (dx * dx + dy * dy + dz * dz) > maxDistanceSq;
    }

    public static boolean isBeyondDistance(BlockPos pos, double maxDistanceSq) {
        return isBeyondDistance(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, maxDistanceSq);
    }

    public static boolean isBeyondDistance(Vec3 pos, double maxDistanceSq) {
        return isBeyondDistance(pos.x, pos.y, pos.z, maxDistanceSq);
    }

    public static double distanceSq(BlockPos pos) {
        return distanceSq(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
    }

    public static double distanceSq(double x, double y, double z) {
        ensureCamPos();
        if (!hasCamPos) {
            return 0.0;
        }
        double dx = x - camX;
        double dy = y - camY;
        double dz = z - camZ;
        return dx * dx + dy * dy + dz * dz;
    }
}
