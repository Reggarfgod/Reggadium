package com.reggarf.mods.reggadium.mixin.create.render;

import com.reggarf.mods.reggadium.Config;
import com.reggarf.mods.reggadium.culling.ReggadiumCuller;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.render.ContraptionEntityRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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
}
