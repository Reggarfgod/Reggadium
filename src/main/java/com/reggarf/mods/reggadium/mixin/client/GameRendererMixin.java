package com.reggarf.mods.reggadium.mixin.client;

import com.reggarf.mods.reggadium.Config;
import com.reggarf.mods.reggadium.culling.ReggadiumCuller;
import com.reggarf.mods.reggadium.renderer.ReggadiumByteBuffer;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "render", at = @At("HEAD"), require = 0)
    private void reggadium$beginFrame(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo ci) {
        if (Config.optimizationsEnabled) {
            if (Config.fastLightCacheEnabled) {
                ReggadiumByteBuffer.clearLightCache();
            }
            if (this.minecraft.gameRenderer != null && this.minecraft.levelRenderer != null) {
                ReggadiumCuller.beginFrame(this.minecraft.gameRenderer.getMainCamera(), this.minecraft.levelRenderer.getFrustum());
            }
        }
    }
}
