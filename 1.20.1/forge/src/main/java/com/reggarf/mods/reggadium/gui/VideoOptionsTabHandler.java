package com.reggarf.mods.reggadium.gui;

import com.reggarf.mods.reggadium.Reggadium;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reggadium.MODID, value = Dist.CLIENT)
public class VideoOptionsTabHandler {
    // Handled universally by VideoSettingsScreenMixin
    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event) {
    }
}
