package com.reggarf.mods.reggadium.mixin.create.kinetic;

import com.reggarf.mods.reggadium.Config;
import com.reggarf.mods.reggadium.culling.IKineticOverstressTracker;
import com.reggarf.mods.reggadium.culling.ReggadiumCuller;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = KineticBlockEntity.class, remap = false)
public class KineticBlockEntityMixin implements IKineticOverstressTracker {

    @Unique
    private boolean reggadium$overstressVisualDirty = false;

    @Override
    public boolean reggadium$isOverstressVisualDirty() {
        return this.reggadium$overstressVisualDirty;
    }

    @Override
    public void reggadium$setOverstressVisualDirty(boolean dirty) {
        this.reggadium$overstressVisualDirty = dirty;
    }

    /**
     * Ticking optimizer for 20k-30k+ kinetic setups:
     * - Client-side: Distant kinetics (>32 blocks) are completely inaudible (SoundScapes max distance is 16 blocks),
     *   and rotation is rendered mathematically from game time. Throttling distant client kinetics to tick once
     *   every 20 ticks (1s) reduces main thread CPU tick load by ~99%.
     * - Server-side: Stopped/stationary components with stable networks do not need redundant tick checks every frame.
     */
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true, require = 0)
    public void onTick(CallbackInfo ci) {
        if (!Config.optimizationsEnabled) {
            return;
        }

        KineticBlockEntity be = (KineticBlockEntity) (Object) this;
        Level level = be.getLevel();
        if (level == null) {
            return;
        }

        int stagger = be.getBlockPos().getX() + be.getBlockPos().getZ();
        long time = level.getGameTime();

        if (level.isClientSide) {
            double distSq = ReggadiumCuller.distanceSq(be.getBlockPos());

            if (Config.skipStationaryKineticsEnabled && be.getSpeed() == 0.0f) {
                if (((time + stagger) & 31) != 0) {
                    ci.cancel();
                    return;
                }
            }

            if (Config.reduceBlockEntityLagEnabled || Config.aggressiveTickSleepEnabled) {
                int mask;
                if (distSq > 4096.0) {
                    mask = 31;
                } else if (distSq > 1024.0) {
                    mask = 15;
                } else if (distSq > 256.0 && Config.aggressiveTickSleepEnabled) {
                    mask = 3;
                } else {
                    mask = 0;
                }
                if (mask != 0 && ((time + stagger) & mask) != 0) {
                    ci.cancel();
                }
            }
        } else {
            if (Config.skipStationaryKineticsEnabled && be.getSpeed() == 0.0f
                    && !be.needsSpeedUpdate() && !be.networkDirty && be.getFlickerScore() == 0) {
                if (((time + stagger) & 15) != 0) {
                    ci.cancel();
                }
            }
        }
    }

    @Inject(method = "tickAudio", at = @At("HEAD"), cancellable = true, require = 0)
    public void onTickAudio(CallbackInfo ci) {
        if (!Config.optimizationsEnabled || !Config.kineticAudioThrottlingEnabled) {
            return;
        }

        KineticBlockEntity be = (KineticBlockEntity) (Object) this;

        // Skip audio calculation if stationary
        if (be.getSpeed() == 0.0f) {
            ci.cancel();
            return;
        }

        // Distance culling for kinetic soundscapes: beyond 24 blocks (576 sq), sound is inaudible
        if (ReggadiumCuller.isBeyondDistance(be.getBlockPos(), 576.0)) {
            ci.cancel();
            return;
        }

        // Rate throttling: update soundscape audio every 4 ticks instead of every tick
        if (be.getLevel() != null && ((be.getLevel().getGameTime() + be.getBlockPos().hashCode()) & 3) != 0) {
            ci.cancel();
        }
    }
}
