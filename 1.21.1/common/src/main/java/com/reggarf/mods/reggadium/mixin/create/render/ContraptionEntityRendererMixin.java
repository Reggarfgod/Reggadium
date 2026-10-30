package com.reggarf.mods.reggadium.mixin.create.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.reggarf.mods.reggadium.Config;
import com.reggarf.mods.reggadium.culling.ReggadiumCuller;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.render.ContraptionEntityRenderer;
import com.simibubi.create.content.contraptions.render.ContraptionMatrices;
import com.simibubi.create.foundation.virtualWorld.VirtualRenderWorld;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ContraptionEntityRenderer.class, remap = false)
public class ContraptionEntityRendererMixin {

    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true, require = 0)
    public void onShouldRender(AbstractContraptionEntity entity, Frustum frustum, double camX, double camY, double camZ, CallbackInfoReturnable<Boolean> cir) {
        if (!Config.optimizationsEnabled || !Config.optimizeContraptionsEnabled || entity == null) {
            return;
        }

        Entity e = (Entity) (Object) entity;
        if (ReggadiumCuller.isBeyondDistance(e.position(), 4096.0)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "renderActors", at = @At("HEAD"), cancellable = true, require = 0)
    private static void onRenderActors(Level world, VirtualRenderWorld renderWorld, Contraption contraption, ContraptionMatrices matrices, MultiBufferSource buffer, CallbackInfo ci) {
        if (!Config.optimizationsEnabled || !Config.optimizeContraptionsEnabled) {
            return;
        }

        if (contraption.entity != null) {
            Entity ce = (Entity) (Object) contraption.entity;
            if (ReggadiumCuller.isBeyondDistance(ce.position(), 576.0)) {
                ci.cancel();
            }
        }
    }
}
