package com.reggarf.mods.reggadium.mixin.particle;

import com.reggarf.mods.reggadium.Config;
import com.reggarf.mods.reggadium.culling.ReggadiumCuller;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleEngine.class)
public class ParticleEngineMixin {

    @Inject(method = "add", at = @At("HEAD"), cancellable = true)
    private void reggadium$cullAdd(Particle particle, CallbackInfo ci) {
        if (!Config.optimizationsEnabled || !Config.cullParticlesEnabled || particle == null) {
            return;
        }
        ParticleAccessor accessor = (ParticleAccessor) particle;
        double x = accessor.reggadium$getX();
        double y = accessor.reggadium$getY();
        double z = accessor.reggadium$getZ();
        if (ReggadiumCuller.isBeyondDistance(x, y, z, 2304.0)) {
            ci.cancel();
        }
    }
}
