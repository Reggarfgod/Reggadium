package com.reggarf.mods.reggadium.mixin.client;

import com.reggarf.mods.reggadium.gui.ReggadiumOptionsScreen;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.client.gui.screens.options.VideoSettingsScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VideoSettingsScreen.class)
public abstract class VideoSettingsScreenMixin extends OptionsSubScreen {

    protected VideoSettingsScreenMixin(Screen lastScreen, Options options, Component title) {
        super(lastScreen, options, title);
    }

    @Inject(method = "addOptions", at = @At("RETURN"))
    private void reggadium$addVideoSettingsButton(CallbackInfo ci) {
        String className = this.getClass().getName();
        if (!className.contains("Embeddium") && !className.contains("Sodium")) {
            this.addRenderableWidget(Button.builder(
                    Component.translatable("reggadium.options.button"),
                    btn -> {
                        if (this.minecraft != null) {
                            this.minecraft.setScreen(new ReggadiumOptionsScreen(this));
                        }
                    }
            ).bounds(this.width - 106, 6, 100, 20).build());
        }
    }
}
