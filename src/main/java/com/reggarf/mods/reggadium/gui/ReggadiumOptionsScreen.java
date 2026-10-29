package com.reggarf.mods.reggadium.gui;

import com.reggarf.mods.reggadium.Config;
import com.reggarf.mods.reggadium.extra.BackgroundOpacity;
import com.reggarf.mods.reggadium.extra.CoordPrecision;
import com.reggarf.mods.reggadium.extra.ExtraSettings;
import com.reggarf.mods.reggadium.extra.HudColor;
import com.reggarf.mods.reggadium.extra.OverlayCorner;
import com.reggarf.mods.reggadium.extra.TextContrast;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.List;

public class ReggadiumOptionsScreen extends Screen {
    public enum Tab {
        KINETICS,
        CULLING,
        MEMORY,
        EXTRA
    }

    private final Screen lastScreen;
    private Tab currentTab = Tab.KINETICS;

    public ReggadiumOptionsScreen(Screen lastScreen) {
        super(Component.translatable("reggadium.options.screen.title"));
        this.lastScreen = lastScreen;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int tabWidth = 84;
        int tabHeight = 20;
        int tabSpacing = 4;
        int totalTabsWidth = (tabWidth * 4) + (tabSpacing * 3);
        int tabStartX = centerX - totalTabsWidth / 2;
        int tabY = 22;

        // Navigation Tabs
        this.addRenderableWidget(Button.builder(
                Component.literal((currentTab == Tab.KINETICS ? "[ " : "") + Component.translatable("reggadium.options.page.kinetics").getString() + (currentTab == Tab.KINETICS ? " ]" : "")),
                btn -> switchTab(Tab.KINETICS)
        ).bounds(tabStartX, tabY, tabWidth, tabHeight).build());

        this.addRenderableWidget(Button.builder(
                Component.literal((currentTab == Tab.CULLING ? "[ " : "") + Component.translatable("reggadium.options.page.culling").getString() + (currentTab == Tab.CULLING ? " ]" : "")),
                btn -> switchTab(Tab.CULLING)
        ).bounds(tabStartX + (tabWidth + tabSpacing), tabY, tabWidth, tabHeight).build());

        this.addRenderableWidget(Button.builder(
                Component.literal((currentTab == Tab.MEMORY ? "[ " : "") + Component.translatable("reggadium.options.page.memory").getString() + (currentTab == Tab.MEMORY ? " ]" : "")),
                btn -> switchTab(Tab.MEMORY)
        ).bounds(tabStartX + (tabWidth + tabSpacing) * 2, tabY, tabWidth, tabHeight).build());

        this.addRenderableWidget(Button.builder(
                Component.literal((currentTab == Tab.EXTRA ? "[ " : "") + Component.translatable("reggadium.options.page.extra").getString() + (currentTab == Tab.EXTRA ? " ]" : "")),
                btn -> switchTab(Tab.EXTRA)
        ).bounds(tabStartX + (tabWidth + tabSpacing) * 3, tabY, tabWidth, tabHeight).build());

        int startY = 48;
        int spacing = 22;
        int buttonWidth = 155;
        int buttonHeight = 20;
        int leftColX = centerX - buttonWidth - 5;
        int rightColX = centerX + 5;

        switch (currentTab) {
            case KINETICS -> initKineticsTab(leftColX, rightColX, startY, spacing, buttonWidth, buttonHeight);
            case CULLING -> initCullingTab(leftColX, rightColX, startY, spacing, buttonWidth, buttonHeight);
            case MEMORY -> initMemoryTab(centerX, startY, spacing, buttonWidth * 2 + 10, buttonHeight);
            case EXTRA -> initExtraTab(leftColX, rightColX, startY, spacing, buttonWidth, buttonHeight);
        }

        // Done button
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, btn -> {
            Config.save();
            if (this.minecraft != null) {
                this.minecraft.setScreen(this.lastScreen);
            }
        }).bounds(centerX - 100, Math.max(startY + spacing * 9 + 4, this.height - 26), 200, 20).build());
    }

    private void switchTab(Tab tab) {
        this.currentTab = tab;
        this.clearWidgets();
        this.init();
    }

    private void initKineticsTab(int leftColX, int rightColX, int startY, int spacing, int buttonWidth, int buttonHeight) {
        // Master Optimization Switch
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.optimizationsEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.options.enable_optimizations.tooltip")))
                .create(leftColX, startY, buttonWidth, buttonHeight,
                        Component.translatable("reggadium.options.enable_optimizations.name"),
                        (btn, val) -> {
                            Config.ENABLE_OPTIMIZATIONS.set(val);
                            Config.optimizationsEnabled = val;
                            Config.save();
                        }));

        // Fast SuperByteBuffer
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.fastSbbEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.options.fast_sbb.tooltip")))
                .create(leftColX, startY + spacing, buttonWidth, buttonHeight,
                        Component.translatable("reggadium.options.fast_sbb.name"),
                        (btn, val) -> {
                            Config.FAST_SUPER_BYTE_BUFFER.set(val);
                            Config.fastSbbEnabled = val;
                            Config.save();
                        }));

        // Backface Culling
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.backfaceCullingEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.options.backface_culling.tooltip")))
                .create(leftColX, startY + spacing * 2, buttonWidth, buttonHeight,
                        Component.translatable("reggadium.options.backface_culling.name"),
                        (btn, val) -> {
                            Config.BACKFACE_CULLING.set(val);
                            Config.backfaceCullingEnabled = val;
                            Config.save();
                        }));

        // Fast Light Cache
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.fastLightCacheEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.options.fast_light_cache.tooltip")))
                .create(leftColX, startY + spacing * 3, buttonWidth, buttonHeight,
                        Component.translatable("reggadium.options.fast_light_cache.name"),
                        (btn, val) -> {
                            Config.FAST_LIGHT_CACHE.set(val);
                            Config.fastLightCacheEnabled = val;
                            Config.save();
                        }));

        // Skip Stationary Kinetics
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.skipStationaryKineticsEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.options.skip_stationary_kinetics.tooltip")))
                .create(leftColX, startY + spacing * 4, buttonWidth, buttonHeight,
                        Component.translatable("reggadium.options.skip_stationary_kinetics.name"),
                        (btn, val) -> {
                            Config.SKIP_STATIONARY_KINETICS.set(val);
                            Config.skipStationaryKineticsEnabled = val;
                            Config.save();
                        }));

        // Kinetic Audio Throttling
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.kineticAudioThrottlingEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.options.kinetic_audio_throttling.tooltip")))
                .create(rightColX, startY, buttonWidth, buttonHeight,
                        Component.translatable("reggadium.options.kinetic_audio_throttling.name"),
                        (btn, val) -> {
                            Config.KINETIC_AUDIO_THROTTLING.set(val);
                            Config.kineticAudioThrottlingEnabled = val;
                            Config.save();
                        }));

        // Kinetic Rotation LOD
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.kineticLodEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.options.kinetic_lod.tooltip")))
                .create(rightColX, startY + spacing, buttonWidth, buttonHeight,
                        Component.translatable("reggadium.options.kinetic_lod.name"),
                        (btn, val) -> {
                            Config.KINETIC_LOD.set(val);
                            Config.kineticLodEnabled = val;
                            Config.save();
                        }));

        // Kinetic State Sleep
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.reduceBlockEntityLagEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.options.reduce_block_entity_lag.tooltip")))
                .create(rightColX, startY + spacing * 2, buttonWidth, buttonHeight,
                        Component.translatable("reggadium.options.reduce_block_entity_lag.name"),
                        (btn, val) -> {
                            Config.REDUCE_BLOCK_ENTITY_LAG.set(val);
                            Config.reduceBlockEntityLagEnabled = val;
                            Config.save();
                        }));

        // Create FX Throttling
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.createFxCullEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.options.create_fx_cull.tooltip")))
                .create(rightColX, startY + spacing * 3, buttonWidth, buttonHeight,
                        Component.translatable("reggadium.options.create_fx_cull.name"),
                        (btn, val) -> {
                            Config.CREATE_FX_CULL.set(val);
                            Config.createFxCullEnabled = val;
                            Config.save();
                        }));

        // Aggressive Tick Sleep
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.aggressiveTickSleepEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.options.aggressive_tick_sleep.tooltip")))
                .create(rightColX, startY + spacing * 4, buttonWidth, buttonHeight,
                        Component.translatable("reggadium.options.aggressive_tick_sleep.name"),
                        (btn, val) -> {
                            Config.AGGRESSIVE_TICK_SLEEP.set(val);
                            Config.aggressiveTickSleepEnabled = val;
                            Config.save();
                        }));
    }

    private void initCullingTab(int leftColX, int rightColX, int startY, int spacing, int buttonWidth, int buttonHeight) {
        // Belt & Item Culling
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.cullBeltsEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.options.cull_belts.tooltip")))
                .create(leftColX, startY, buttonWidth, buttonHeight,
                        Component.translatable("reggadium.options.cull_belts.name"),
                        (btn, val) -> {
                            Config.CULL_BELTS.set(val);
                            Config.cullBeltsEnabled = val;
                            Config.save();
                        }));

        // Kinetic Frustum Culling
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.kineticCullingEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.options.kinetic_culling.tooltip")))
                .create(leftColX, startY + spacing, buttonWidth, buttonHeight,
                        Component.translatable("reggadium.options.kinetic_culling.name"),
                        (btn, val) -> {
                            Config.KINETIC_CULLING.set(val);
                            Config.kineticCullingEnabled = val;
                            Config.save();
                        }));

        // Block Entity Frustum Culling
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.cullBlockEntitiesEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.options.cull_block_entities.tooltip")))
                .create(leftColX, startY + spacing * 2, buttonWidth, buttonHeight,
                        Component.translatable("reggadium.options.cull_block_entities.name"),
                        (btn, val) -> {
                            Config.CULL_BLOCK_ENTITIES.set(val);
                            Config.cullBlockEntitiesEnabled = val;
                            Config.save();
                        }));

        // Particle Frustum Culling
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.cullParticlesEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.options.cull_particles.tooltip")))
                .create(leftColX, startY + spacing * 3, buttonWidth, buttonHeight,
                        Component.translatable("reggadium.options.cull_particles.name"),
                        (btn, val) -> {
                            Config.CULL_PARTICLES.set(val);
                            Config.cullParticlesEnabled = val;
                            Config.save();
                        }));

        // Entity Distance Culling
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.cullEntitiesEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.options.cull_entities.tooltip")))
                .create(rightColX, startY, buttonWidth, buttonHeight,
                        Component.translatable("reggadium.options.cull_entities.name"),
                        (btn, val) -> {
                            Config.CULL_ENTITIES.set(val);
                            Config.cullEntitiesEnabled = val;
                            Config.save();
                        }));

        // Distant Shadow Culling
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.cullShadowsEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.options.cull_shadows.tooltip")))
                .create(rightColX, startY + spacing, buttonWidth, buttonHeight,
                        Component.translatable("reggadium.options.cull_shadows.name"),
                        (btn, val) -> {
                            Config.CULL_SHADOWS.set(val);
                            Config.cullShadowsEnabled = val;
                            Config.save();
                        }));

        // Contraption Distance LOD
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.optimizeContraptionsEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.options.optimize_contraptions.tooltip")))
                .create(rightColX, startY + spacing * 2, buttonWidth, buttonHeight,
                        Component.translatable("reggadium.options.optimize_contraptions.name"),
                        (btn, val) -> {
                            Config.OPTIMIZE_CONTRAPTIONS.set(val);
                            Config.optimizeContraptionsEnabled = val;
                            Config.save();
                        }));

        // Native Vertex Buffer Formatting
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.optimizeEntityRenderingEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.options.optimize_entity_rendering.tooltip")))
                .create(rightColX, startY + spacing * 3, buttonWidth, buttonHeight,
                        Component.translatable("reggadium.options.optimize_entity_rendering.name"),
                        (btn, val) -> {
                            Config.OPTIMIZE_ENTITY_RENDERING.set(val);
                            Config.optimizeEntityRenderingEnabled = val;
                            Config.save();
                        }));
    }

    private void initMemoryTab(int centerX, int startY, int spacing, int fullWidth, int buttonHeight) {
        int colX = centerX - fullWidth / 2;

        // 1: Master Memory Leak Fixer
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.fixMemoryLeaksEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.options.fix_memory_leaks.tooltip")))
                .create(colX, startY, fullWidth, buttonHeight,
                        Component.translatable("reggadium.options.fix_memory_leaks.name"),
                        (btn, val) -> {
                            Config.FIX_MEMORY_LEAKS.set(val);
                            Config.fixMemoryLeaksEnabled = val;
                            Config.save();
                        }));

        // 2: Fix ClientLevel Leak
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.fixClientLevelLeakEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.options.fix_client_level_leak.tooltip")))
                .create(colX, startY + spacing, fullWidth, buttonHeight,
                        Component.translatable("reggadium.options.fix_client_level_leak.name"),
                        (btn, val) -> {
                            Config.FIX_CLIENT_LEVEL_LEAK.set(val);
                            Config.fixClientLevelLeakEnabled = val;
                            Config.save();
                        }));

        // 3: Fix FakePlayer Leak
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.fixFakePlayerLeakEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.options.fix_fake_player_leak.tooltip")))
                .create(colX, startY + spacing * 2, fullWidth, buttonHeight,
                        Component.translatable("reggadium.options.fix_fake_player_leak.name"),
                        (btn, val) -> {
                            Config.FIX_FAKE_PLAYER_LEAK.set(val);
                            Config.fixFakePlayerLeakEnabled = val;
                            Config.save();
                        }));

        // 4: Fix Debug Subscription Leak
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.fixDebugSubscriptionLeakEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.options.fix_debug_subscription_leak.tooltip")))
                .create(colX, startY + spacing * 3, fullWidth, buttonHeight,
                        Component.translatable("reggadium.options.fix_debug_subscription_leak.name"),
                        (btn, val) -> {
                            Config.FIX_DEBUG_SUBSCRIPTION_LEAK.set(val);
                            Config.fixDebugSubscriptionLeakEnabled = val;
                            Config.save();
                        }));

        // Quick Action Buttons: [ Enable All Memory Fixes ]  [ Disable All Memory Fixes ]
        int halfWidth = (fullWidth - 6) / 2;
        this.addRenderableWidget(Button.builder(
                Component.translatable("reggadium.options.memory.enable_all"),
                btn -> {
                    Config.FIX_MEMORY_LEAKS.set(true); Config.fixMemoryLeaksEnabled = true;
                    Config.FIX_CLIENT_LEVEL_LEAK.set(true); Config.fixClientLevelLeakEnabled = true;
                    Config.FIX_FAKE_PLAYER_LEAK.set(true); Config.fixFakePlayerLeakEnabled = true;
                    Config.FIX_DEBUG_SUBSCRIPTION_LEAK.set(true); Config.fixDebugSubscriptionLeakEnabled = true;
                    Config.save();
                    this.clearWidgets();
                    this.init();
                }
        ).bounds(colX, startY + spacing * 4 + 6, halfWidth, buttonHeight).build());

        this.addRenderableWidget(Button.builder(
                Component.translatable("reggadium.options.memory.disable_all"),
                btn -> {
                    Config.FIX_MEMORY_LEAKS.set(false); Config.fixMemoryLeaksEnabled = false;
                    Config.FIX_CLIENT_LEVEL_LEAK.set(false); Config.fixClientLevelLeakEnabled = false;
                    Config.FIX_FAKE_PLAYER_LEAK.set(false); Config.fixFakePlayerLeakEnabled = false;
                    Config.FIX_DEBUG_SUBSCRIPTION_LEAK.set(false); Config.fixDebugSubscriptionLeakEnabled = false;
                    Config.save();
                    this.clearWidgets();
                    this.init();
                }
        ).bounds(colX + halfWidth + 6, startY + spacing * 4 + 6, halfWidth, buttonHeight).build());
    }

    private void initExtraTab(int leftColX, int rightColX, int startY, int spacing, int buttonWidth, int buttonHeight) {
        // Master Extra HUD Switch
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.extraHudEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.extra.enable_extra_hud.tooltip")))
                .create(leftColX, startY, buttonWidth, buttonHeight,
                        Component.translatable("reggadium.extra.enable_extra_hud.name"),
                        (btn, val) -> {
                            Config.ENABLE_EXTRA_HUD.set(val);
                            Config.extraHudEnabled = val;
                            ExtraSettings.enabled = val;
                            Config.save();
                        }));

        // Show FPS
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.showFpsEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.extra.show_fps.tooltip")))
                .create(leftColX, startY + spacing, buttonWidth, buttonHeight,
                        Component.translatable("reggadium.extra.show_fps.name"),
                        (btn, val) -> {
                            Config.SHOW_FPS.set(val);
                            Config.showFpsEnabled = val;
                            ExtraSettings.showFps = val;
                            Config.save();
                        }));

        // Show Extended FPS
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.showFpsExtendedEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.extra.show_fps_extended.tooltip")))
                .create(leftColX, startY + spacing * 2, buttonWidth, buttonHeight,
                        Component.translatable("reggadium.extra.show_fps_extended.name"),
                        (btn, val) -> {
                            Config.SHOW_FPS_EXTENDED.set(val);
                            Config.showFpsExtendedEnabled = val;
                            ExtraSettings.showFpsExtended = val;
                            Config.save();
                        }));

        // Show Coordinates
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.showCoordsEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.extra.show_coords.tooltip")))
                .create(leftColX, startY + spacing * 3, buttonWidth, buttonHeight,
                        Component.translatable("reggadium.extra.show_coords.name"),
                        (btn, val) -> {
                            Config.SHOW_COORDS.set(val);
                            Config.showCoordsEnabled = val;
                            ExtraSettings.showCoords = val;
                            Config.save();
                        }));

        // Show Direction
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.showFacingEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.extra.show_facing.tooltip")))
                .create(leftColX, startY + spacing * 4, buttonWidth, buttonHeight,
                        Component.translatable("reggadium.extra.show_facing.name"),
                        (btn, val) -> {
                            Config.SHOW_FACING.set(val);
                            Config.showFacingEnabled = val;
                            ExtraSettings.showFacing = val;
                            Config.save();
                        }));

        // Show Biome
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.showBiomeEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.extra.show_biome.tooltip")))
                .create(rightColX, startY, buttonWidth, buttonHeight,
                        Component.translatable("reggadium.extra.show_biome.name"),
                        (btn, val) -> {
                            Config.SHOW_BIOME.set(val);
                            Config.showBiomeEnabled = val;
                            ExtraSettings.showBiome = val;
                            Config.save();
                        }));

        // Show Memory
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.showMemoryEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.extra.show_memory.tooltip")))
                .create(rightColX, startY + spacing, buttonWidth, buttonHeight,
                        Component.translatable("reggadium.extra.show_memory.name"),
                        (btn, val) -> {
                            Config.SHOW_MEMORY.set(val);
                            Config.showMemoryEnabled = val;
                            ExtraSettings.showMemory = val;
                            Config.save();
                        }));

        // Show CPU
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.showCpuEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.extra.show_cpu.tooltip")))
                .create(rightColX, startY + spacing * 2, buttonWidth, buttonHeight,
                        Component.translatable("reggadium.extra.show_cpu.name"),
                        (btn, val) -> {
                            Config.SHOW_CPU.set(val);
                            Config.showCpuEnabled = val;
                            ExtraSettings.showCpu = val;
                            Config.save();
                        }));

        // Show GPU
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.showGpuEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.extra.show_gpu.tooltip")))
                .create(rightColX, startY + spacing * 3, buttonWidth, buttonHeight,
                        Component.translatable("reggadium.extra.show_gpu.name"),
                        (btn, val) -> {
                            Config.SHOW_GPU.set(val);
                            Config.showGpuEnabled = val;
                            ExtraSettings.showGpu = val;
                            Config.save();
                        }));

        // Show Ping
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.showPingEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.extra.show_ping.tooltip")))
                .create(rightColX, startY + spacing * 4, buttonWidth, buttonHeight,
                        Component.translatable("reggadium.extra.show_ping.name"),
                        (btn, val) -> {
                            Config.SHOW_PING.set(val);
                            Config.showPingEnabled = val;
                            ExtraSettings.showPing = val;
                            Config.save();
                        }));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 8, 0xFFFFFF);
    }

    @Override
    public void onClose() {
        Config.save();
        if (this.minecraft != null) {
            this.minecraft.setScreen(this.lastScreen);
        }
    }
}
