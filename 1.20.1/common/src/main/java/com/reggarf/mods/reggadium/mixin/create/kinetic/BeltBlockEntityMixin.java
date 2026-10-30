package com.reggarf.mods.reggadium.mixin.create.kinetic;

import com.reggarf.mods.reggadium.Config;
import com.reggarf.mods.reggadium.culling.ReggadiumCuller;
import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BeltBlockEntity.class, remap = false)
public class BeltBlockEntityMixin {

    @Inject(method = "tick()V", at = @At("HEAD"), cancellable = true, require = 0)
    private void reggadium$throttleBelt(CallbackInfo ci) {
        if (!Config.optimizationsEnabled || !Config.cullBeltsEnabled) {
            return;
        }
        BeltBlockEntity be = (BeltBlockEntity) (Object) this;
        Level level = be.getLevel();
        if (level == null || !level.isClientSide) {
            return;
        }
        double distSq = ReggadiumCuller.distanceSq(be.getBlockPos());
        if (distSq > 1024.0) {
            if (((level.getGameTime() + be.getBlockPos().getX()) & 7) != 0) {
                ci.cancel();
            }
        } else if (distSq > 256.0 && Config.aggressiveTickSleepEnabled) {
            if (((level.getGameTime() + be.getBlockPos().getX()) & 1) != 0) {
                ci.cancel();
            }
        }
    }
}
