package com.reggarf.mods.reggadium.mixin.create.kinetic;

import com.reggarf.mods.reggadium.Config;
import com.reggarf.mods.reggadium.culling.ReggadiumCuller;
import com.simibubi.create.content.kinetics.fan.AirCurrent;
import com.simibubi.create.content.kinetics.fan.IAirCurrentSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AirCurrent.class, remap = false)
public class AirCurrentMixin {

    @Shadow
    public IAirCurrentSource source;

    @Inject(method = "tick()V", at = @At("HEAD"), cancellable = true, require = 0)
    private void reggadium$throttleAirCurrent(CallbackInfo ci) {
        if (!Config.optimizationsEnabled || !Config.createFxCullEnabled || this.source == null) {
            return;
        }
        Level level = this.source.getAirCurrentWorld();
        if (level == null || !level.isClientSide) {
            return;
        }
        BlockPos pos = this.source.getAirCurrentPos();
        if (pos != null && ReggadiumCuller.isBeyondDistance(pos, 1024.0)) {
            ci.cancel();
        }
    }

    @Inject(method = "rebuild", at = @At("HEAD"), cancellable = true, require = 0)
    private void reggadium$throttleRebuild(CallbackInfo ci) {
        if (!Config.optimizationsEnabled || !Config.createFxCullEnabled || this.source == null) {
            return;
        }
        Level level = this.source.getAirCurrentWorld();
        if (level == null || !level.isClientSide) {
            return;
        }
        BlockPos pos = this.source.getAirCurrentPos();
        if (pos != null && ReggadiumCuller.isBeyondDistance(pos, 2304.0)) {
            ci.cancel();
        }
    }
}
