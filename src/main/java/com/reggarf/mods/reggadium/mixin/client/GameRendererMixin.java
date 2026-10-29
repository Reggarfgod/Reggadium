package com.reggarf.mods.reggadium.mixin.client;

import com.reggarf.mods.reggadium.Config;
import com.reggarf.mods.reggadium.renderer.ReggadiumByteBuffer;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "render", at = @At("HEAD"), require = 0)
    private void reggadium$beginFrame(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo ci) {
        if (Config.optimizationsEnabled && Config.fastLightCacheEnabled) {
            ReggadiumByteBuffer.clearLightCache();
        }
    }
}
