package com.reggarf.mods.reggadium.mixin.client;

import com.reggarf.mods.reggadium.Config;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @ModifyVariable(method = "setupRender", at = @At("HEAD"), argsOnly = true, ordinal = 0, require = 0)
    private boolean reggadium$forceSmartCull(boolean spectator) {
        if (Config.optimizationsEnabled && Config.aggressiveTickSleepEnabled) {
            return false;
        }
        return spectator;
    }
}
