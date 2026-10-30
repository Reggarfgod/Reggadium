package com.reggarf.mods.reggadium.mixin.create.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.reggarf.mods.reggadium.Config;
import com.reggarf.mods.reggadium.culling.ReggadiumCuller;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Method;

@Mixin(value = KineticBlockEntityRenderer.class, remap = false)
public class KineticBlockEntityRendererMixin {

    private static final float DEG_TO_RAD = (float) (Math.PI / 180.0);

    @Inject(method = "renderSafe(Lcom/simibubi/create/content/kinetics/base/KineticBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V", at = @At("HEAD"), cancellable = true, require = 0)
    public void onRenderSafe(KineticBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay, CallbackInfo ci) {
        if (!Config.optimizationsEnabled) {
            return;
        }

        double distSq = ReggadiumCuller.distanceSq(be.getBlockPos());

        if (Config.kineticCullingEnabled) {
            if (distSq > 4096.0 || !ReggadiumCuller.isBlockEntityVisible(be)) {
                ci.cancel();
                return;
            }
        }

        if (Config.skipStationaryKineticsEnabled && be.getSpeed() == 0 && distSq > 256.0) {
            ci.cancel();
        }
    }

    @Inject(method = "getAngleForBe", at = @At("HEAD"), cancellable = true, require = 0)
    private static void onGetAngleForBe(KineticBlockEntity be, BlockPos pos, Direction.Axis axis, CallbackInfoReturnable<Float> cir) {
        if (!Config.optimizationsEnabled || !Config.kineticLodEnabled) {
            return;
        }

        double distSq = ReggadiumCuller.distanceSq(pos);
        if (distSq > 256.0) {
            float time = getCreateRenderTime(be.getLevel());
            float steps = distSq > 1024.0 ? 10.0f : 20.0f;
            time = ((int) (time * steps)) / steps;
            float offset = KineticBlockEntityRenderer.getRotationOffsetForPosition(be, pos, axis);
            float angle = (((time * be.getSpeed() * 0.3f) + offset) % 360.0f) * DEG_TO_RAD;
            cir.setReturnValue(angle);
        }
    }

    private static float getCreateRenderTime(Level world) {
        try {
            Class<?> catnipClass = Class.forName("net.createmod.catnip.animation.AnimationTickHolder");
            Method m = catnipClass.getMethod("getRenderTime", Level.class);
            return ((Number) m.invoke(null, world)).floatValue();
        } catch (Throwable t) {
            try {
                Class<?> createClass = Class.forName("com.simibubi.create.foundation.utility.AnimationTickHolder");
                Method m = createClass.getMethod("getRenderTime", Level.class);
                return ((Number) m.invoke(null, world)).floatValue();
            } catch (Throwable t2) {
                return world != null ? (float) world.getGameTime() : 0.0f;
            }
        }
    }
}
