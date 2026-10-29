package com.reggarf.mods.reggadium.mixin.create.kinetic;

import com.simibubi.create.content.kinetics.base.KineticEffectHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = KineticEffectHandler.class, remap = false)
public interface KineticEffectHandlerAccessor {
    @Accessor("overStressedEffect")
    float getOverStressedEffect();
}
