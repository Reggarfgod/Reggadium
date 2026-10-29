package com.reggarf.mods.reggadium.gui;

import com.reggarf.mods.reggadium.Config;
import com.reggarf.mods.reggadium.extra.ExtraSettings;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.List;

public class ReggadiumOptionsScreen extends Screen {
    private final Screen lastScreen;

    public ReggadiumOptionsScreen(Screen lastScreen) {
        super(Component.translatable("reggadium.options.screen.title"));
        this.lastScreen = lastScreen;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int startY = 24;
        int spacing = 22;
        int buttonWidth = 155;
        int buttonHeight = 20;

        int leftColX = centerX - buttonWidth - 5;
        int rightColX = centerX + 5;

        // --- Left Column: Create Mod Optimizations ---
        // 1: Global Optimizations
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.optimizationsEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.options.enable_optimizations.tooltip")))
                .create(leftColX, startY, buttonWidth, buttonHeight,
                        Component.translatable("reggadium.options.enable_optimizations.name"),
                        (btn, val) -> {
                            Config.ENABLE_OPTIMIZATIONS.set(val);
                            Config.optimizationsEnabled = val;
                            Config.save();
                        }));

        // 2: Fast SuperByteBuffer
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.fastSbbEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.options.fast_sbb.tooltip")))
                .create(leftColX, startY + spacing, buttonWidth, buttonHeight,
                        Component.translatable("reggadium.options.fast_sbb.name"),
                        (btn, val) -> {
                            Config.FAST_SUPER_BYTE_BUFFER.set(val);
                            Config.fastSbbEnabled = val;
                            Config.save();
                        }));

        // 3: Backface Culling
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.backfaceCullingEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.options.backface_culling.tooltip")))
                .create(leftColX, startY + spacing * 2, buttonWidth, buttonHeight,
                        Component.translatable("reggadium.options.backface_culling.name"),
                        (btn, val) -> {
                            Config.BACKFACE_CULLING.set(val);
                            Config.backfaceCullingEnabled = val;
                            Config.save();
                        }));

        // 4: Fast Light Cache
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.fastLightCacheEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.options.fast_light_cache.tooltip")))
                .create(leftColX, startY + spacing * 3, buttonWidth, buttonHeight,
                        Component.translatable("reggadium.options.fast_light_cache.name"),
                        (btn, val) -> {
                            Config.FAST_LIGHT_CACHE.set(val);
                            Config.fastLightCacheEnabled = val;
                            Config.save();
                        }));

        // 5: Belt & Item Culling
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.cullBeltsEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.options.cull_belts.tooltip")))
                .create(leftColX, startY + spacing * 4, buttonWidth, buttonHeight,
                        Component.translatable("reggadium.options.cull_belts.name"),
                        (btn, val) -> {
                            Config.CULL_BELTS.set(val);
                            Config.cullBeltsEnabled = val;
                            Config.save();
                        }));

        // 6: Kinetic Frustum Culling
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.kineticCullingEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.options.kinetic_culling.tooltip")))
                .create(leftColX, startY + spacing * 5, buttonWidth, buttonHeight,
                        Component.translatable("reggadium.options.kinetic_culling.name"),
                        (btn, val) -> {
                            Config.KINETIC_CULLING.set(val);
                            Config.kineticCullingEnabled = val;
                            Config.save();
                        }));

        // 7: Skip Stationary Kinetics
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.skipStationaryKineticsEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.options.skip_stationary_kinetics.tooltip")))
                .create(leftColX, startY + spacing * 6, buttonWidth, buttonHeight,
                        Component.translatable("reggadium.options.skip_stationary_kinetics.name"),
                        (btn, val) -> {
                            Config.SKIP_STATIONARY_KINETICS.set(val);
                            Config.skipStationaryKineticsEnabled = val;
                            Config.save();
                        }));

        // 8: Kinetic Audio Throttling
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.kineticAudioThrottlingEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.options.kinetic_audio_throttling.tooltip")))
                .create(leftColX, startY + spacing * 7, buttonWidth, buttonHeight,
                        Component.translatable("reggadium.options.kinetic_audio_throttling.name"),
                        (btn, val) -> {
                            Config.KINETIC_AUDIO_THROTTLING.set(val);
                            Config.kineticAudioThrottlingEnabled = val;
                            Config.save();
                        }));

        // --- Right Column: Contraptions & General Performance ---
        // 1: Optimize Contraptions
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.optimizeContraptionsEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.options.optimize_contraptions.tooltip")))
                .create(rightColX, startY, buttonWidth, buttonHeight,
                        Component.translatable("reggadium.options.optimize_contraptions.name"),
                        (btn, val) -> {
                            Config.OPTIMIZE_CONTRAPTIONS.set(val);
                            Config.optimizeContraptionsEnabled = val;
                            Config.save();
                        }));

        // 2: Kinetic Rotation LOD
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.kineticLodEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.options.kinetic_lod.tooltip")))
                .create(rightColX, startY + spacing, buttonWidth, buttonHeight,
                        Component.translatable("reggadium.options.kinetic_lod.name"),
                        (btn, val) -> {
                            Config.KINETIC_LOD.set(val);
                            Config.kineticLodEnabled = val;
                            Config.save();
                        }));

        // 3: Block Entity Culling
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.cullBlockEntitiesEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.options.cull_block_entities.tooltip")))
                .create(rightColX, startY + spacing * 2, buttonWidth, buttonHeight,
                        Component.translatable("reggadium.options.cull_block_entities.name"),
                        (btn, val) -> {
                            Config.CULL_BLOCK_ENTITIES.set(val);
                            Config.cullBlockEntitiesEnabled = val;
                            Config.save();
                        }));

        // 4: Particle Culling
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.cullParticlesEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.options.cull_particles.tooltip")))
                .create(rightColX, startY + spacing * 3, buttonWidth, buttonHeight,
                        Component.translatable("reggadium.options.cull_particles.name"),
                        (btn, val) -> {
                            Config.CULL_PARTICLES.set(val);
                            Config.cullParticlesEnabled = val;
                            Config.save();
                        }));

        // 5: Optimize Entity Rendering
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.optimizeEntityRenderingEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.options.optimize_entity_rendering.tooltip")))
                .create(rightColX, startY + spacing * 4, buttonWidth, buttonHeight,
                        Component.translatable("reggadium.options.optimize_entity_rendering.name"),
                        (btn, val) -> {
                            Config.OPTIMIZE_ENTITY_RENDERING.set(val);
                            Config.optimizeEntityRenderingEnabled = val;
                            Config.save();
                        }));

        // 6: Kinetic Sleep Optimization
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.reduceBlockEntityLagEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.options.reduce_block_entity_lag.tooltip")))
                .create(rightColX, startY + spacing * 5, buttonWidth, buttonHeight,
                        Component.translatable("reggadium.options.reduce_block_entity_lag.name"),
                        (btn, val) -> {
                            Config.REDUCE_BLOCK_ENTITY_LAG.set(val);
                            Config.reduceBlockEntityLagEnabled = val;
                            Config.save();
                        }));

        // 7: Entity Distance Culling
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.cullEntitiesEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.options.cull_entities.tooltip")))
                .create(rightColX, startY + spacing * 6, buttonWidth, buttonHeight,
                        Component.translatable("reggadium.options.cull_entities.name"),
                        (btn, val) -> {
                            Config.CULL_ENTITIES.set(val);
                            Config.cullEntitiesEnabled = val;
                            Config.save();
                        }));

        // 8: Aggressive Tick Sleep
        this.addRenderableWidget(CycleButton.onOffBuilder(Config.aggressiveTickSleepEnabled)
                .withTooltip(val -> Tooltip.create(Component.translatable("reggadium.options.aggressive_tick_sleep.tooltip")))
                .create(rightColX, startY + spacing * 7, buttonWidth, buttonHeight,
                        Component.translatable("reggadium.options.aggressive_tick_sleep.name"),
                        (btn, val) -> {
                            Config.AGGRESSIVE_TICK_SLEEP.set(val);
                            Config.aggressiveTickSleepEnabled = val;
                            Config.save();
                        }));

        // Done button
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, btn -> {
            Config.save();
            if (this.minecraft != null) {
                this.minecraft.setScreen(this.lastScreen);
            }
        }).bounds(centerX - 100, Math.max(startY + spacing * 8 + 4, this.height - 26), 200, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 10, 0xFFFFFF);
    }

    @Override
    public void onClose() {
        Config.save();
        if (this.minecraft != null) {
            this.minecraft.setScreen(this.lastScreen);
        }
    }
}
