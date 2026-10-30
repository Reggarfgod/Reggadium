package com.reggarf.mods.reggadium;

import com.mojang.logging.LogUtils;
import com.reggarf.mods.reggadium.fabric.compat.ReggadiumEmbeddiumCompat;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;

public class ReggadiumFabric implements ClientModInitializer {
    private static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public void onInitializeClient() {
        LOGGER.info("[Reggadium] Initializing Reggadium Optimization Engine for Fabric 1.20.1");

        if (FabricLoader.getInstance().isModLoaded("embeddium")) {
            try {
                ReggadiumEmbeddiumCompat.init();
                LOGGER.info("[Reggadium] Successfully registered optimization tabs into Embeddium folder");
            } catch (Throwable t) {
                LOGGER.warn("[Reggadium] Could not hook into Embeddium OptionGUIConstructionEvent: {}", t.getMessage());
            }
        } else if (FabricLoader.getInstance().isModLoaded("sodium")) {
            LOGGER.info("[Reggadium] Sodium detected, hooking into Sodium Options GUI via mixin");
        }
    }
}
