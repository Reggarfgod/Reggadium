package com.reggarf.mods.reggadium.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.reggarf.mods.reggadium.Config;
import com.reggarf.mods.reggadium.culling.ReggadiumCuller;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityRenderDispatcher.class)
public class BlockEntityRenderDispatcherMixin {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public <E extends BlockEntity> void onRender(E blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, CallbackInfo ci) {
        if (!Config.optimizationsEnabled || !Config.cullBlockEntitiesEnabled) {
            return;
        }

        // Distance Culling: beyond 80 blocks (6400 sq distance), skip rendering standard block entities
        // (chests, signs, skulls, campfires, hoppers) to save CPU draw call dispatching in massive bases
        if (ReggadiumCuller.isBeyondDistance(blockEntity.getBlockPos(), 6400.0)) {
            ci.cancel();
            return;
        }

        // Frustum Culling: skip rendering when outside camera view
        if (!ReggadiumCuller.isBlockEntityVisible(blockEntity)) {
            ci.cancel();
        }
    }
}
