package com.reggarf.mods.reggadium;

import com.reggarf.mods.reggadium.compat.ReggadiumEmbeddiumCompat;
import com.reggarf.mods.reggadium.extra.ReggadiumExtraHud;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

@Mod(Reggadium.MODID)
public class ReggadiumNeoForge {
    public ReggadiumNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        ConfigNeoForge.init();
        modEventBus.addListener(this::commonSetup);
        modContainer.registerConfig(ModConfig.Type.CLIENT, ConfigNeoForge.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        Reggadium.LOGGER.info("[Reggadium] Initializing Reggadium Optimization Engine for NeoForge 1.21.1");
    }

    @EventBusSubscriber(modid = Reggadium.MODID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            Reggadium.LOGGER.info("[Reggadium] Client optimization initialized with Embeddium compatibility");

            if (ModList.get().isLoaded("embeddium")) {
                try {
                    ReggadiumEmbeddiumCompat.init();
                    Reggadium.LOGGER.info("[Reggadium] Successfully registered optimization tab into Embeddium Video Options GUI");
                } catch (Throwable t) {
                    Reggadium.LOGGER.warn("[Reggadium] Could not hook into Embeddium OptionGUIConstructionEvent: {}", t.getMessage());
                }
            }
        }

        @SubscribeEvent
        public static void onRegisterGuiLayers(RegisterGuiLayersEvent event) {
            event.registerAboveAll(ReggadiumExtraHud.LAYER_ID, new ReggadiumExtraHud());
        }
    }
}
