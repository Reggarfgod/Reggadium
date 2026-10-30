package com.reggarf.mods.reggadium.mixin.memory;

import com.reggarf.mods.reggadium.Config;
import com.reggarf.mods.reggadium.memory.ReggadiumMemoryManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Patches the prominent ClientLevel memory leak in Minecraft 1.21.1.
 *
 * In vanilla Minecraft, clearClientLevel() unloads the level reference without
 * clearing residual crosshair picking entities and without notifying Flywheel.
 *
 * This mixin clears residual picking state and dispatches unload to subsystems.
 */
@Mixin(Minecraft.class)
public class MinecraftLevelUnloadMixin {

    @Shadow
    public ClientLevel level;

    @Inject(
            method = "clearClientLevel(Lnet/minecraft/client/gui/screens/Screen;)V",
            at = @At("HEAD")
    )
    private void reggadium$dispatchClientLevelUnload(Screen nextScreen, CallbackInfo ci) {
        if (Config.fixMemoryLeaksEnabled && Config.fixClientLevelLeakEnabled && this.level != null) {
            ReggadiumMemoryManager.clearClientPickEntities();
            ReggadiumMemoryManager.onClientLevelUnload(this.level);
        }
    }
}
