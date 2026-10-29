package com.reggarf.mods.reggadium.mixin.render;

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

        if (ReggadiumCuller.isBeyondDistance(blockEntity.getBlockPos(), 2304.0)
                || !ReggadiumCuller.isBlockEntityVisible(blockEntity)) {
            ci.cancel();
        }
    }
}
