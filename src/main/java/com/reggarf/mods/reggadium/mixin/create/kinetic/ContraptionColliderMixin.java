package com.reggarf.mods.reggadium.mixin.create.kinetic;

import com.reggarf.mods.reggadium.Config;
import com.reggarf.mods.reggadium.culling.ReggadiumCuller;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.ContraptionCollider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ContraptionCollider.class, remap = false)
public class ContraptionColliderMixin {

    @Inject(method = "collideEntities", at = @At("HEAD"), cancellable = true, require = 0)
    private static void reggadium$skipDistantCollision(AbstractContraptionEntity contraptionEntity, CallbackInfo ci) {
        if (!Config.optimizationsEnabled || !Config.optimizeContraptionsEnabled || contraptionEntity == null) {
            return;
        }
        if (contraptionEntity.level() == null || !contraptionEntity.level().isClientSide) {
            return;
        }
        if (ReggadiumCuller.isBeyondDistance(contraptionEntity.position(), 2304.0)) {
            ci.cancel();
        }
    }
}
