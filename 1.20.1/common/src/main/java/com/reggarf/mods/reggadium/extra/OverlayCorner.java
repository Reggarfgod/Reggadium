package com.reggarf.mods.reggadium.extra;

import net.minecraft.network.chat.Component;

public enum OverlayCorner {
    TOP_LEFT("reggadium.extra.overlay_corner.top_left"),
    TOP_RIGHT("reggadium.extra.overlay_corner.top_right"),
    BOTTOM_LEFT("reggadium.extra.overlay_corner.bottom_left"),
    BOTTOM_RIGHT("reggadium.extra.overlay_corner.bottom_right");

    private final String key;

    OverlayCorner(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public Component getDisplayName() {
        return Component.translatable(key);
    }
}
