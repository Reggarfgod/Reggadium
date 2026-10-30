package com.reggarf.mods.reggadium;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public class Reggadium {
    public static final String MODID = "reggadium";
    public static final String NAME = "Reggadium";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void init() {
        LOGGER.info("[Reggadium] Initializing Reggadium Optimization Engine for Minecraft 1.20.1");
    }
}
