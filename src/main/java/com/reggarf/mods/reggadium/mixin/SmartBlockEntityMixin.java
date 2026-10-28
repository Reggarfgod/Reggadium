package com.reggarf.mods.reggadium.mixin;

import com.reggarf.mods.reggadium.Config;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.function.Consumer;

@Mixin(value = SmartBlockEntity.class, remap = false)
public class SmartBlockEntityMixin {

    @Shadow
    @Final
    private Map<BehaviourType<?>, BlockEntityBehaviour> behaviours;

    /**
     * In massive Create setups with 20k-30k components (Shafts, Cogs, Gearboxes), the vast majority
     * have no custom behaviours. Skipping forEachBehaviour when empty avoids allocating collection
     * iterators or array loops 20 times per second for every block entity.
     */
    @Inject(method = "forEachBehaviour", at = @At("HEAD"), cancellable = true, require = 0)
    public void onForEachBehaviour(Consumer<BlockEntityBehaviour> consumer, CallbackInfo ci) {
        if (!Config.optimizationsEnabled) {
            return;
        }

        if (this.behaviours == null || this.behaviours.isEmpty()) {
            ci.cancel();
        }
    }
}
