package com.reggarf.mods.reggadium.mixin;

import com.reggarf.mods.reggadium.Config;
import com.reggarf.mods.reggadium.culling.IKineticOverstressTracker;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.KineticEffectHandler;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = KineticBlockEntityVisual.class, remap = false)
public class KineticBlockEntityVisualMixin {

    /**
     * In Create, SingleAxisRotatingVisual calls applyOverstressEffect every tick on ALL rotating components
     * (Shafts, Cogs, Gearboxes, etc.). Unconditionally calling instance.setColor(WHITE) and instance.setChanged()
     * marks every single instance buffer dirty, forcing Flywheel to re-upload tens of thousands of instances
     * to GPU memory every frame, causing catastrophic frame drops.
     *
     * Reggadium intercepts this and only allows updates when the overstressed effect is actively animating or
     * transitioning back to normal, completely eliminating redundant GPU memory transfers during steady state.
     */
    @Inject(method = "applyOverstressEffect", at = @At("HEAD"), cancellable = true, require = 0)
    private static void onApplyOverstressEffect(KineticBlockEntity be, RotatingInstance[] instances, CallbackInfo ci) {
        if (!Config.optimizationsEnabled) {
            return;
        }

        if (be instanceof IKineticOverstressTracker tracker) {
            float effect = 0.0f;
            try {
                KineticEffectHandler effects = ((KineticBlockEntityAccessor) be).getEffects();
                if (effects != null) {
                    effect = ((KineticEffectHandlerAccessor) effects).getOverStressedEffect();
                }
            } catch (Throwable ignored) {
            }

            if (effect == 0.0f) {
                if (!tracker.reggadium$isOverstressVisualDirty()) {
                    // Already in normal white state: skip instance.setColor and instance.setChanged()!
                    ci.cancel();
                    return;
                }
                // Transitioning from overstressed back to normal: let the original method run once to restore Color.WHITE and setChanged(), then clear the dirty flag.
                tracker.reggadium$setOverstressVisualDirty(false);
            } else {
                // Currently animating an active overstressed/understressed effect: mark dirty so it resets when complete
                tracker.reggadium$setOverstressVisualDirty(true);
            }
        }
    }
}
