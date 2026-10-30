package com.reggarf.mods.reggadium.gui;

import com.reggarf.mods.reggadium.Reggadium;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.VideoSettingsScreen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;

@EventBusSubscriber(modid = Reggadium.MODID, value = Dist.CLIENT)
public class VideoOptionsTabHandler {

    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event) {
        Screen screen = event.getScreen();
        if (screen == null) return;

        if (isSodiumOrEmbeddiumPresent()) {
            return;
        }

        String className = screen.getClass().getName();
        if (screen instanceof VideoSettingsScreen && !className.contains("Embeddium") && !className.contains("Sodium")) {
            Button reggadiumBtn = Button.builder(
                    Component.translatable("reggadium.options.button"),
                    btn -> {
                        if (screen.getMinecraft() != null) {
                            screen.getMinecraft().setScreen(new ReggadiumOptionsScreen(screen));
                        }
                    }
            ).bounds(screen.width / 2 + 5, 6, 150, 20).build();
            event.addListener(reggadiumBtn);
        }
    }

    private static boolean isSodiumOrEmbeddiumPresent() {
        try {
            Class.forName("net.caffeinemc.mods.sodium.api.config.ConfigEntryPoint");
            return true;
        } catch (Throwable ignored) {
        }
        try {
            Class.forName("org.embeddedt.embeddium.api.OptionGUIConstructionEvent");
            return true;
        } catch (Throwable ignored) {
        }
        return false;
    }
}
