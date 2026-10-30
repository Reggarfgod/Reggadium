package com.reggarf.mods.reggadium.fabric.mixin;

import com.reggarf.mods.reggadium.fabric.compat.ReggadiumSodiumCompat;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Pseudo
@Mixin(targets = "me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI", remap = false)
public abstract class SodiumOptionsGUIMixin extends Screen {

    @Shadow(remap = false)
    private List<OptionPage> pages;

    protected SodiumOptionsGUIMixin(Component title) {
        super(title);
    }

    @Inject(method = "<init>", at = @At("RETURN"), remap = false)
    private void reggadium$addSodiumPages(Screen prevScreen, CallbackInfo ci) {
        // If Embeddium is present, pages are registered via OptionGUIConstructionEvent in ReggadiumEmbeddiumCompat
        // with their proper OptionIdentifier so Embeddium organizes them inside the Reggadium folder.
        if (FabricLoader.getInstance().isModLoaded("embeddium")) {
            return;
        }
        if (this.pages != null) {
            ReggadiumSodiumCompat.registerPages(this.pages);
        }
    }
}
