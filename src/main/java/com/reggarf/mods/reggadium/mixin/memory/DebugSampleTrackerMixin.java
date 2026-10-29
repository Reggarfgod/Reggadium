package com.reggarf.mods.reggadium.mixin.memory;

import com.llamalad7.mixinextras.sugar.Local;
import com.reggarf.mods.reggadium.Config;
import net.minecraft.util.debugchart.DebugSampleSubscriptionTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;

/**
 * Patches the DebugSampleSubscriptionTracker memory leak in Minecraft 1.21.1.
 *
 * In vanilla 1.21.1, handleSubscriptions iterates over the subscription request queue
 * without calling iterator.remove(), causing processed requests to accumulate
 * indefinitely and leak server player references.
 */
@Mixin(DebugSampleSubscriptionTracker.class)
public class DebugSampleTrackerMixin {

    @Inject(
            method = "handleSubscriptions",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Iterator;next()Ljava/lang/Object;",
                    shift = At.Shift.AFTER
            )
    )
    private void reggadium$removeProcessedSubscription(
            long millis,
            int tick,
            CallbackInfo ci,
            @Local Iterator<?> iterator
    ) {
        if (Config.fixMemoryLeaksEnabled && Config.fixDebugSubscriptionLeakEnabled && iterator != null) {
            iterator.remove();
        }
    }
}
