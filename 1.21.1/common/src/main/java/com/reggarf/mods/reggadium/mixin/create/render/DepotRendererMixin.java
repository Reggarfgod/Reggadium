package com.reggarf.mods.reggadium.mixin.create.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.reggarf.mods.reggadium.Config;
import com.reggarf.mods.reggadium.culling.ReggadiumCuller;
import com.simibubi.create.content.logistics.depot.DepotBlockEntity;
import com.simibubi.create.content.logistics.depot.DepotRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = DepotRenderer.class, remap = false)
public class DepotRendererMixin {

    @Inject(method = "renderSafe(Lcom/simibubi/create/content/logistics/depot/DepotBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V", at = @At("HEAD"), cancellable = true, require = 0)
    private void reggadium$cullDepot(DepotBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay, CallbackInfo ci) {
        if (!Config.optimizationsEnabled || !Config.cullBlockEntitiesEnabled) {
            return;
        }
        if (ReggadiumCuller.isBeyondDistance(be.getBlockPos(), 1024.0) || !ReggadiumCuller.isBlockEntityVisible(be)) {
            ci.cancel();
        }
    }
}
