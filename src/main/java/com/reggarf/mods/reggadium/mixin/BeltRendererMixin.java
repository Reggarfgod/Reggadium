package com.reggarf.mods.reggadium.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.reggarf.mods.reggadium.Config;
import com.reggarf.mods.reggadium.culling.ReggadiumCuller;
import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;
import com.simibubi.create.content.kinetics.belt.BeltRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BeltRenderer.class, remap = false)
public class BeltRendererMixin {

    @Inject(method = "shouldRenderOffScreen(Lcom/simibubi/create/content/kinetics/belt/BeltBlockEntity;)Z", at = @At("HEAD"), cancellable = true, require = 0)
    public void onShouldRenderOffScreen(BeltBlockEntity be, CallbackInfoReturnable<Boolean> cir) {
        if (!Config.optimizationsEnabled || !Config.cullBeltsEnabled) {
            return;
        }

        // If the belt is not in frustum or beyond 48 blocks, don't force off-screen rendering
        if (!ReggadiumCuller.isBlockEntityVisible(be) || ReggadiumCuller.isBeyondDistance(be.getBlockPos(), 2304.0)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "renderSafe(Lcom/simibubi/create/content/kinetics/belt/BeltBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V", at = @At("HEAD"), cancellable = true, require = 0)
    public void onRenderSafe(BeltBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay, CallbackInfo ci) {
        if (!Config.optimizationsEnabled || !Config.cullBeltsEnabled) {
            return;
        }

        if (ReggadiumCuller.isBeyondDistance(be.getBlockPos(), 4096.0)) {
            ci.cancel();
            return;
        }

        if (!ReggadiumCuller.isBlockEntityVisible(be)) {
            ci.cancel();
        }
    }

    @Inject(method = "renderItems", at = @At("HEAD"), cancellable = true, require = 0)
    public void onRenderItems(BeltBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay, CallbackInfo ci) {
        if (!Config.optimizationsEnabled || !Config.cullBeltsEnabled) {
            return;
        }

        // Distance culling for transported items: beyond 32 blocks (1024 sq), skip item rendering entirely
        if (ReggadiumCuller.isBeyondDistance(be.getBlockPos(), 1024.0)) {
            ci.cancel();
            return;
        }

        // Frustum culling: if belt is not visible, don't render items
        if (!ReggadiumCuller.isBlockEntityVisible(be)) {
            ci.cancel();
        }
    }
}
