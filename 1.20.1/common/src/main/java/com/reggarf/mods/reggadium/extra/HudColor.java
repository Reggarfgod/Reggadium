package com.reggarf.mods.reggadium.extra;

import net.minecraft.network.chat.Component;

public enum HudColor {
    DYNAMIC("reggadium.extra.color.dynamic", 0),
    WHITE("reggadium.extra.color.white", 0xFFFFFF),
    GREEN("reggadium.extra.color.green", 0x55FF55),
    CYAN("reggadium.extra.color.cyan", 0x55FFFF),
    GOLD("reggadium.extra.color.gold", 0xFFAA00),
    YELLOW("reggadium.extra.color.yellow", 0xFFFF55),
    RED("reggadium.extra.color.red", 0xFF5555),
    PURPLE("reggadium.extra.color.purple", 0xFF55FF),
    GRAY("reggadium.extra.color.gray", 0xAAAAAA);

    private final String key;
    private final int rgb;

    HudColor(String key, int rgb) {
        this.key = key;
        this.rgb = rgb;
    }

    public String getKey() {
        return key;
    }

    public int getRgb() {
        return rgb;
    }

    public int getColor(int dynamicFallback) {
        return this == DYNAMIC ? dynamicFallback : this.rgb;
    }

    public Component getDisplayName() {
        return Component.translatable(key);
    }
}
