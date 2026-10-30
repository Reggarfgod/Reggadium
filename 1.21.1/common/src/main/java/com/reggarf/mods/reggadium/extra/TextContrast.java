package com.reggarf.mods.reggadium.extra;

import net.minecraft.network.chat.Component;

public enum TextContrast {
    NONE("reggadium.extra.text_contrast.none"),
    SHADOW("reggadium.extra.text_contrast.shadow"),
    BACKGROUND("reggadium.extra.text_contrast.background");

    private final String key;

    TextContrast(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public Component getDisplayName() {
        return Component.translatable(key);
    }
}
