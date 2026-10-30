package com.reggarf.mods.reggadium.mixin.memory;

import com.reggarf.mods.reggadium.Config;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Periodically triggers vanilla LivingEntity.getLastDamageSource() cleanup logic
 * every 41 ticks, preventing stale DamageSource instances from holding entity/level references.
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityTickMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void reggadium$clearStaleDamageSource(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (Config.fixMemoryLeaksEnabled && entity.tickCount % 41 == 0) {
            entity.getLastDamageSource();
        }
    }
}
