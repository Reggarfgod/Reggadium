package com.reggarf.mods.reggadium.mixin.create.kinetic;

import com.reggarf.mods.reggadium.Config;
import com.simibubi.create.content.kinetics.base.KineticEffectHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = KineticEffectHandler.class, remap = false)
public class KineticEffectHandlerMixin {

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true, require = 0)
    private void reggadium$skipIdleEffects(CallbackInfo ci) {
        if (!Config.optimizationsEnabled || !Config.createFxCullEnabled) {
            return;
        }
        try {
            float effect = ((KineticEffectHandlerAccessor) this).getOverStressedEffect();
            if (effect == 0.0f) {
                ci.cancel();
            }
        } catch (Throwable ignored) {
        }
    }
}
