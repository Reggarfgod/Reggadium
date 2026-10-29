package com.reggarf.mods.reggadium.mixin.memory;

import com.reggarf.mods.reggadium.Config;
import com.reggarf.mods.reggadium.memory.ReggadiumMemoryManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

/**
 * Patches the prominent ClientLevel memory leak in Minecraft.
 *
 * In vanilla Minecraft, clearClientLevel() unloads the level reference without
 * dispatching LevelEvent.Unload on the client side. As a result, client subsystems,
 * Flywheel visualization managers, sound engines, and mod capabilities never receive
 * notice that the world was closed and retain the entire ClientLevel object graph in heap.
 *
 * This mixin dispatches LevelEvent.Unload and clears residual picking state.
 */
@Mixin(Minecraft.class)
public class MinecraftLevelUnloadMixin {

    @Shadow
    @Nullable
    public ClientLevel level;

    @Inject(
            method = "clearClientLevel",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/Minecraft;level:Lnet/minecraft/client/multiplayer/ClientLevel;"
            )
    )
    private void reggadium$dispatchClientLevelUnload(Screen nextScreen, CallbackInfo ci) {
        if (Config.fixMemoryLeaksEnabled && Config.fixClientLevelLeakEnabled && this.level != null) {
            NeoForge.EVENT_BUS.post(new LevelEvent.Unload(this.level));
            ReggadiumMemoryManager.clearClientPickEntities();
        }
    }
}
