package com.reggarf.mods.reggadium;

import com.mojang.logging.LogUtils;
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
import org.slf4j.Logger;

@Mod(Reggadium.MODID)
public class Reggadium {
    public static final String MODID = "reggadium";
    public static final String NAME = "Reggadium";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Reggadium(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modContainer.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("[Reggadium] Initializing Reggadium Optimization Engine for Minecraft and Create");
    }

    @EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("[Reggadium] Client optimization initialized with Embeddium compatibility");

            // Initialize Embeddium Options API tab
            if (ModList.get().isLoaded("embeddium")) {
                try {
                    ReggadiumEmbeddiumCompat.init();
                    LOGGER.info("[Reggadium] Successfully registered optimization tab into Embeddium Video Options GUI");
                } catch (Throwable t) {
                    LOGGER.warn("[Reggadium] Could not hook into Embeddium OptionGUIConstructionEvent: {}", t.getMessage());
                }
            }
        }

        @SubscribeEvent
        public static void onRegisterGuiLayers(RegisterGuiLayersEvent event) {
            event.registerAboveAll(ReggadiumExtraHud.LAYER_ID, new ReggadiumExtraHud());
        }
    }
}
