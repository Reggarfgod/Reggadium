package com.reggarf.mods.reggadium.extra;

import net.minecraft.network.chat.Component;

public enum CoordPrecision {
    INTEGER("reggadium.extra.coord_precision.integer", "%.0f"),
    ONE_DECIMAL("reggadium.extra.coord_precision.one_decimal", "%.1f"),
    TWO_DECIMALS("reggadium.extra.coord_precision.two_decimals", "%.2f");

    private final String key;
    private final String format;

    CoordPrecision(String key, String format) {
        this.key = key;
        this.format = format;
    }

    public String getKey() {
        return key;
    }

    public String getFormat() {
        return format;
    }

    public Component getDisplayName() {
        return Component.translatable(key);
    }
}
