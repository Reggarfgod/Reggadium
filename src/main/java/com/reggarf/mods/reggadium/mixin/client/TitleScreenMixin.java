package com.reggarf.mods.reggadium.mixin.client;

import com.reggarf.mods.reggadium.memory.ReggadiumMemoryManager;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Triggers post-bootstrap memory cleanup when reaching the Title Screen.
 */
@Mixin(TitleScreen.class)
public class TitleScreenMixin {

    @Inject(method = "init", at = @At("TAIL"))
    private void reggadium$onTitleScreenInit(CallbackInfo ci) {
        ReggadiumMemoryManager.cleanMixinClassInfo();
    }
}
