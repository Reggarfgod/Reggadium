package com.reggarf.mods.reggadium.mixin.sound;

import com.reggarf.mods.reggadium.Config;
import com.reggarf.mods.reggadium.culling.ReggadiumCuller;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundEngine.class)
public class SoundEngineMixin {

    @Inject(method = "play(Lnet/minecraft/client/resources/sounds/SoundInstance;)V", at = @At("HEAD"), cancellable = true, require = 0)
    private void reggadium$cullDistantSound(SoundInstance sound, CallbackInfo ci) {
        if (!Config.optimizationsEnabled || !Config.kineticAudioThrottlingEnabled || sound == null) {
            return;
        }
        if (!sound.isRelative() && sound.canPlaySound()) {
            if (ReggadiumCuller.isBeyondDistance(sound.getX(), sound.getY(), sound.getZ(), 2304.0)) {
                ci.cancel();
            }
        }
    }
}
