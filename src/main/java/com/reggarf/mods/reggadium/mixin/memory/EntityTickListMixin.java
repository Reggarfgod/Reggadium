package com.reggarf.mods.reggadium.mixin.memory;

import com.reggarf.mods.reggadium.Config;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityTickList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

/**
 * Patches entity reference leakage in EntityTickList.
 *
 * During forEach iterations, entities added to the tick list accumulate in the 'passive'
 * map. Clearing the passive map after iteration terminates prevents references to unloaded
 * or dead entities from remaining in heap.
 */
@Mixin(EntityTickList.class)
public class EntityTickListMixin {

    @Shadow
    private Int2ObjectMap<Entity> passive;

    @Inject(method = "forEach", at = @At("RETURN"))
    private void reggadium$clearPassiveOnIterationEnd(Consumer<Entity> consumer, CallbackInfo ci) {
        if (Config.fixMemoryLeaksEnabled && Config.fixEntityTickListLeakEnabled && this.passive != null && !this.passive.isEmpty()) {
            this.passive.clear();
        }
    }
}
