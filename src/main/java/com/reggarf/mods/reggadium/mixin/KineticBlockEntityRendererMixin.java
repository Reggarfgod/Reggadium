package com.reggarf.mods.reggadium.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.reggarf.mods.reggadium.Config;
import com.reggarf.mods.reggadium.culling.ReggadiumCuller;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = KineticBlockEntityRenderer.class, remap = false)
public class KineticBlockEntityRendererMixin {

    @Inject(method = "renderSafe(Lcom/simibubi/create/content/kinetics/base/KineticBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V", at = @At("HEAD"), cancellable = true, require = 0)
    public void onRenderSafe(KineticBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay, CallbackInfo ci) {
        if (!Config.optimizationsEnabled) {
            return;
        }

        // Kinetic Frustum & Distance Culling
        if (Config.kineticCullingEnabled) {
            if (!ReggadiumCuller.isBlockEntityVisible(be)) {
                ci.cancel();
                return;
            }
            if (ReggadiumCuller.isBeyondDistance(be.getBlockPos(), 4096.0)) {
                ci.cancel();
                return;
            }
        }

        // Stationary Kinetic Sleep
        if (Config.skipStationaryKineticsEnabled && be.getSpeed() == 0) {
            if (ReggadiumCuller.isBeyondDistance(be.getBlockPos(), 576.0)) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "getAngleForBe", at = @At("HEAD"), cancellable = true, require = 0)
    private static void onGetAngleForBe(KineticBlockEntity be, BlockPos pos, Direction.Axis axis, CallbackInfoReturnable<Float> cir) {
        if (!Config.optimizationsEnabled || !Config.kineticLodEnabled) {
            return;
        }

        // Level of Detail: beyond 24 blocks (576 sq), quantize rotation animation to eliminate vertex transformation jitter
        if (ReggadiumCuller.isBeyondDistance(pos, 576.0)) {
            float time = AnimationTickHolder.getRenderTime(be.getLevel());
            // Quantize time to 25 FPS steps (0.04s)
            time = ((int) (time * 25.0f)) / 25.0f;
            float offset = KineticBlockEntityRenderer.getRotationOffsetForPosition(be, pos, axis);
            float angle = ((time * be.getSpeed() * 3f / 10f) + offset) % 360 / 180 * (float) Math.PI;
            cir.setReturnValue(angle);
        }
    }
}
