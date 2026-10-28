package com.reggarf.mods.reggadium.mixin;

import com.reggarf.mods.reggadium.Config;
import com.reggarf.mods.reggadium.extra.ExtraSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    /**
     * By default, Vanilla Minecraft only initiates the hardware GPU TimerQuery when the F3 debug
     * overlay is actively open (debugOverlay.showDebugScreen()), and explicitly resets gpuUtilization to 0.0
     * every frame when F3 is closed.
     *
     * This redirect activates Minecraft's internal GPU TimerQuery whenever the Reggadium Extra GPU HUD
     * is enabled, allowing real-time hardware GPU utilization to be measured accurately without requiring
     * the full F3 debug screen.
     */
    @Redirect(
            method = "runTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/DebugScreenOverlay;showDebugScreen()Z",
                    ordinal = 0
            )
    )
    private boolean reggadium$enableGpuTimerForExtraHud(DebugScreenOverlay debugOverlay) {
        return debugOverlay.showDebugScreen() || (Config.optimizationsEnabled && ExtraSettings.showGpu);
    }
}
