package com.reggarf.mods.reggadium.mixin.create.kinetic;

import com.reggarf.mods.reggadium.Config;
import com.reggarf.mods.reggadium.culling.ReggadiumCuller;
import com.simibubi.create.content.kinetics.fan.EncasedFanBlockEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EncasedFanBlockEntity.class, remap = false)
public class EncasedFanBlockEntityMixin {

    @Inject(method = "tick()V", at = @At("HEAD"), cancellable = true, require = 0)
    private void reggadium$throttleFan(CallbackInfo ci) {
        if (!Config.optimizationsEnabled || !Config.createFxCullEnabled) {
            return;
        }
        EncasedFanBlockEntity be = (EncasedFanBlockEntity) (Object) this;
        Level level = be.getLevel();
        if (level == null || !level.isClientSide) {
            return;
        }
        if (ReggadiumCuller.isBeyondDistance(be.getBlockPos(), 1024.0)) {
            if (((level.getGameTime() + be.getBlockPos().getX() + be.getBlockPos().getZ()) & 7) != 0) {
                ci.cancel();
            }
        }
    }
}
