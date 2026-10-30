package com.reggarf.mods.reggadium.extra;

import net.minecraft.network.chat.Component;

public enum BackgroundOpacity {
    SUBTLE("reggadium.extra.bg_opacity.subtle", 0x40000000),
    MEDIUM("reggadium.extra.bg_opacity.medium", 0x80000000),
    HEAVY("reggadium.extra.bg_opacity.heavy", 0xC0000000),
    SOLID("reggadium.extra.bg_opacity.solid", 0xFF000000);

    private final String key;
    private final int colorArgb;

    BackgroundOpacity(String key, int colorArgb) {
        this.key = key;
        this.colorArgb = colorArgb;
    }

    public String getKey() {
        return key;
    }

    public int getColorArgb() {
        return colorArgb;
    }

    public int getAlpha() {
        return (colorArgb >>> 24) & 0xFF;
    }

    public Component getDisplayName() {
        return Component.translatable(key);
    }
}
