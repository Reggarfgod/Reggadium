package com.reggarf.mods.reggadium;

import com.mojang.logging.LogUtils;
import com.reggarf.mods.reggadium.compat.ReggadiumEmbeddiumCompat;
import com.reggarf.mods.reggadium.extra.ReggadiumExtraHud;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Reggadium.MODID)
public class ReggadiumForge {
    private static final Logger LOGGER = LogUtils.getLogger();

    public ReggadiumForge() {
        ConfigForge.init();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigForge.SPEC);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("[Reggadium] Initializing Reggadium Optimization Engine for Forge 1.20.1");
    }

    @SubscribeEvent
    public void onRenderGuiOverlay(RenderGuiOverlayEvent.Post event) {
        if (event.getOverlay().id().equals(VanillaGuiOverlay.CHAT_PANEL.id())) {
            ReggadiumExtraHud.render(event.getGuiGraphics());
        }
    }

    @Mod.EventBusSubscriber(modid = Reggadium.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("[Reggadium] Client optimization initialized with Embeddium compatibility");
            if (ModList.get().isLoaded("embeddium")) {
                try {
                    ReggadiumEmbeddiumCompat.init();
                    LOGGER.info("[Reggadium] Successfully registered optimization tab into Embeddium Video Options GUI");
                } catch (Throwable t) {
                    LOGGER.warn("[Reggadium] Could not hook into Embeddium OptionGUIConstructionEvent: {}", t.getMessage());
                }
            }
        }
    }
}
