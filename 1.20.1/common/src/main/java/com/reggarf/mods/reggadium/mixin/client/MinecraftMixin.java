package com.reggarf.mods.reggadium.mixin.client;

import com.reggarf.mods.reggadium.extra.ExtraSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    /**
     * In 1.20.1, vanilla Minecraft checks options.renderDebug to decide whether to activate
     * the GPU TimerQuery and measure gpuUtilization.
     * This redirect activates the query when Reggadium Extra GPU HUD is enabled.
     */
    @Redirect(
            method = "runTick",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/Options;renderDebug:Z",
                    opcode = org.objectweb.asm.Opcodes.GETFIELD,
                    ordinal = 0
            ),
            require = 0
    )
    private boolean reggadium$enableGpuTimerForExtraHud(Options options) {
        return options.renderDebug || (ExtraSettings.enabled && ExtraSettings.showGpu) || ExtraSettings.showGpu;
    }
}
