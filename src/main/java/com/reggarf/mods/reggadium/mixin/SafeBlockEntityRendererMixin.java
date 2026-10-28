package com.reggarf.mods.reggadium.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.reggarf.mods.reggadium.Config;
import com.reggarf.mods.reggadium.culling.ReggadiumCuller;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SafeBlockEntityRenderer.class, remap = false)
public abstract class SafeBlockEntityRendererMixin {

    @Shadow
    public abstract AABB getRenderBoundingBox(BlockEntity be);

    @Inject(method = "render(Lnet/minecraft/world/level/block/entity/BlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V", at = @At("HEAD"), cancellable = true, require = 0)
    public void onRender(BlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay, CallbackInfo ci) {
        if (!Config.optimizationsEnabled) {
            return;
        }

        // 1. Distance Culling: beyond 64 blocks (4096 sq), skip Create block entity rendering
        if (Config.kineticCullingEnabled) {
            if (ReggadiumCuller.isBeyondDistance(be.getBlockPos(), 4096.0)) {
                ci.cancel();
                return;
            }

            // 2. Frustum Culling
            try {
                AABB boundingBox = getRenderBoundingBox(be);
                if (boundingBox != null) {
                    if (!ReggadiumCuller.isAABBVisible(boundingBox)) {
                        ci.cancel();
                        return;
                    }
                } else if (!ReggadiumCuller.isBlockEntityVisible(be)) {
                    ci.cancel();
                    return;
                }
            } catch (Throwable t) {
                if (!ReggadiumCuller.isBlockEntityVisible(be)) {
                    ci.cancel();
                    return;
                }
            }
        }

        // 3. Stationary Kinetic Sleep: if machine speed is 0 and distance > 24 blocks (576 sq), skip rendering
        if (Config.skipStationaryKineticsEnabled && be instanceof KineticBlockEntity kbe) {
            if (kbe.getSpeed() == 0 && ReggadiumCuller.isBeyondDistance(be.getBlockPos(), 576.0)) {
                ci.cancel();
            }
        }
    }
}
