package com.reggarf.mods.reggadium.mixin;

import com.reggarf.mods.reggadium.Config;
import com.reggarf.mods.reggadium.culling.ReggadiumCuller;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = KineticBlockEntity.class, remap = false)
public class KineticBlockEntityMixin {

    @Inject(method = "tickAudio", at = @At("HEAD"), cancellable = true, require = 0)
    public void onTickAudio(CallbackInfo ci) {
        if (!Config.optimizationsEnabled || !Config.kineticAudioThrottlingEnabled) {
            return;
        }

        KineticBlockEntity be = (KineticBlockEntity) (Object) this;

        // Skip audio calculation if stationary
        if (be.getSpeed() == 0) {
            ci.cancel();
            return;
        }

        // Distance culling for kinetic soundscapes: beyond 24 blocks (576 sq), sound is inaudible
        if (ReggadiumCuller.isBeyondDistance(be.getBlockPos(), 576.0)) {
            ci.cancel();
            return;
        }

        // Rate throttling: update soundscape audio every 4 ticks instead of every tick
        if (be.getLevel() != null && ((be.getLevel().getGameTime() + be.getBlockPos().hashCode()) & 3) != 0) {
            ci.cancel();
        }
    }
}
