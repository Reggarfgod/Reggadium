package com.reggarf.mods.reggadium.mixin;

import com.reggarf.mods.reggadium.Config;
import com.reggarf.mods.reggadium.culling.ReggadiumCuller;
import com.simibubi.create.foundation.sound.SoundScapes;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SoundScapes.class, remap = false)
public class SoundScapesMixin {

    /**
     * Prevents SoundScapes from performing expensive hash map lookups, Pair allocations,
     * and BlockPos hashing when sound sources are distant (>24 blocks) or when audio throttling is active.
     */
    @Inject(method = "play", at = @At("HEAD"), cancellable = true, require = 0)
    private static void onPlay(SoundScapes.AmbienceGroup group, BlockPos pos, float pitch, CallbackInfo ci) {
        if (!Config.optimizationsEnabled || !Config.kineticAudioThrottlingEnabled) {
            return;
        }

        if (ReggadiumCuller.isBeyondDistance(pos, 576.0)) {
            ci.cancel();
        }
    }
}
