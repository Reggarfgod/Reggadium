package com.reggarf.mods.reggadium.compat;

import com.reggarf.mods.reggadium.gui.ReggadiumOptionsScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ReggadiumModMenuCompat implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ReggadiumOptionsScreen::new;
    }
}
