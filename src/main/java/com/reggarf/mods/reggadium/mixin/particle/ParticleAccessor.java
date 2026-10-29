package com.reggarf.mods.reggadium.mixin.particle;

import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Particle.class)
public interface ParticleAccessor {
    @Accessor("x")
    double reggadium$getX();

    @Accessor("y")
    double reggadium$getY();

    @Accessor("z")
    double reggadium$getZ();
}
