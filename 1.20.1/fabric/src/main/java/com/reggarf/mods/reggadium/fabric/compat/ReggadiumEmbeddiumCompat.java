package com.reggarf.mods.reggadium.fabric.compat;

import com.google.common.collect.ImmutableList;
import com.reggarf.mods.reggadium.Reggadium;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.embeddedt.embeddium.api.OptionGUIConstructionEvent;
import org.embeddedt.embeddium.client.gui.options.OptionIdentifier;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;

import java.lang.reflect.Constructor;

public class ReggadiumEmbeddiumCompat {

    public static void init() {
        try {
            OptionGUIConstructionEvent.BUS.addListener(ReggadiumEmbeddiumCompat::onOptionGuiConstruction);
        } catch (Throwable ignored) {
        }
    }

    private static void onOptionGuiConstruction(OptionGUIConstructionEvent event) {
        event.addPage(createKineticsPage());
        event.addPage(createCullingPage());
        event.addPage(createMemoryPage());
        event.addPage(createDiagnosticsPage());
    }

    private static OptionPage createEmbeddiumPage(String idPath, Component title, ImmutableList<OptionGroup> groups) {
        try {
            OptionIdentifier<Void> pageId = OptionIdentifier.create(new ResourceLocation(Reggadium.MODID, idPath));
            Constructor<OptionPage> ctor = OptionPage.class.getConstructor(OptionIdentifier.class, Component.class, ImmutableList.class);
            return ctor.newInstance(pageId, title, groups);
        } catch (Throwable t) {
            return new OptionPage(title, groups);
        }
    }

    public static OptionPage createKineticsPage() {
        Component pageTitle = Component.translatable("reggadium.options.page.kinetics");
        return createEmbeddiumPage("kinetics", pageTitle, ImmutableList.of(
                ReggadiumSodiumCompat.createKineticStreamingGroup(),
                ReggadiumSodiumCompat.createFactoryCullingGroup()
        ));
    }

    public static OptionPage createCullingPage() {
        Component pageTitle = Component.translatable("reggadium.options.page.culling");
        return createEmbeddiumPage("culling", pageTitle, ImmutableList.of(
                ReggadiumSodiumCompat.createFrustumCullingGroup(),
                ReggadiumSodiumCompat.createContraptionLodGroup()
        ));
    }

    public static OptionPage createMemoryPage() {
        Component pageTitle = Component.translatable("reggadium.options.page.memory");
        return createEmbeddiumPage("memory", pageTitle, ImmutableList.of(
                ReggadiumSodiumCompat.createMemoryMasterGroup(),
                ReggadiumSodiumCompat.createMemoryMitigationsGroup()
        ));
    }

    public static OptionPage createDiagnosticsPage() {
        Component pageTitle = Component.translatable("reggadium.options.page.diagnostics");
        return createEmbeddiumPage("diagnostics", pageTitle, ImmutableList.of(
                ReggadiumSodiumCompat.createExtraHudMasterGroup(),
                ReggadiumSodiumCompat.createExtraHudMetricsGroup(),
                ReggadiumSodiumCompat.createExtraHudLayoutGroup(),
                ReggadiumSodiumCompat.createExtraHudColorsGroup(),
                ReggadiumSodiumCompat.createSystemGroup()
        ));
    }
}
