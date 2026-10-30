package com.reggarf.mods.reggadium.compat;

import com.google.common.collect.ImmutableList;
import com.reggarf.mods.reggadium.Config;
import com.reggarf.mods.reggadium.Reggadium;
import com.reggarf.mods.reggadium.extra.BackgroundOpacity;
import com.reggarf.mods.reggadium.extra.CoordPrecision;
import com.reggarf.mods.reggadium.extra.ExtraSettings;
import com.reggarf.mods.reggadium.extra.HudColor;
import com.reggarf.mods.reggadium.extra.OverlayCorner;
import com.reggarf.mods.reggadium.extra.TextContrast;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.embeddedt.embeddium.api.OptionGUIConstructionEvent;
import org.embeddedt.embeddium.client.gui.options.OptionIdentifier;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpact;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.storage.OptionStorage;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ReggadiumEmbeddiumCompat {

    public static class ReggadiumOptionsStorage implements OptionStorage<Void> {
        public static final ReggadiumOptionsStorage INSTANCE = new ReggadiumOptionsStorage();

        @Override
        public Void getData() {
            return null;
        }

        @Override
        public void save() {
            Config.save();
        }
    }

    public static void init() {
        try {
            OptionGUIConstructionEvent.BUS.addListener(ReggadiumEmbeddiumCompat::onOptionGuiConstruction);
        } catch (Throwable ignored) {
        }
    }

    private static void onOptionGuiConstruction(OptionGUIConstructionEvent event) {
        event.addPage(createKineticsPage());
        event.addPage(createCullingPage());
        event.addPage(createPipelinePage());
        event.addPage(createMemoryPage());
        event.addPage(createDiagnosticsPage());
    }

    // --- Option Builders ---

    public static OptionImpl<Void, Boolean> createEnableOptimizationsOption() {
        return OptionImpl.createBuilder(Boolean.class, ReggadiumOptionsStorage.INSTANCE)
                .setId(new ResourceLocation(Reggadium.MODID, "enable_optimizations"))
                .setName(Component.translatable("reggadium.options.enable_optimizations.name"))
                .setTooltip(Component.translatable("reggadium.options.enable_optimizations.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, val) -> {
                    Config.ENABLE_OPTIMIZATIONS.set(val);
                    Config.optimizationsEnabled = val;
                    Config.save();
                }, opts -> Config.optimizationsEnabled)
                .setImpact(OptionImpact.HIGH)
                .build();
    }

    public static OptionImpl<Void, Boolean> createEnableExtraHudOption() {
        return OptionImpl.createBuilder(Boolean.class, ReggadiumOptionsStorage.INSTANCE)
                .setId(new ResourceLocation(Reggadium.MODID, "enable_extra_hud"))
                .setName(Component.translatable("reggadium.extra.enable_extra_hud.name"))
                .setTooltip(Component.translatable("reggadium.extra.enable_extra_hud.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, val) -> {
                    Config.ENABLE_EXTRA_HUD.set(val);
                    Config.extraHudEnabled = val;
                    ExtraSettings.enabled = val;
                    Config.save();
                }, opts -> Config.extraHudEnabled)
                .setImpact(OptionImpact.LOW)
                .build();
    }

    // --- Extra HUD Display Metrics (Inspired by Rubidium / Sodium Extra) ---

    public static OptionImpl<Void, Boolean> createShowFpsOption() {
        return OptionImpl.createBuilder(Boolean.class, ReggadiumOptionsStorage.INSTANCE)
                .setId(new ResourceLocation(Reggadium.MODID, "show_fps"))
                .setName(Component.translatable("reggadium.extra.show_fps.name"))
                .setTooltip(Component.translatable("reggadium.extra.show_fps.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, val) -> {
                    Config.SHOW_FPS.set(val);
                    Config.showFpsEnabled = val;
                    ExtraSettings.showFps = val;
                    Config.save();
                }, opts -> Config.showFpsEnabled)
                .setImpact(OptionImpact.LOW)
                .build();
    }

    public static OptionImpl<Void, Boolean> createShowFpsExtendedOption() {
        return OptionImpl.createBuilder(Boolean.class, ReggadiumOptionsStorage.INSTANCE)
                .setId(new ResourceLocation(Reggadium.MODID, "show_fps_extended"))
                .setName(Component.translatable("reggadium.extra.show_fps_extended.name"))
                .setTooltip(Component.translatable("reggadium.extra.show_fps_extended.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, val) -> {
                    Config.SHOW_FPS_EXTENDED.set(val);
                    Config.showFpsExtendedEnabled = val;
                    ExtraSettings.showFpsExtended = val;
                    Config.save();
                }, opts -> Config.showFpsExtendedEnabled)
                .setImpact(OptionImpact.LOW)
                .build();
    }

    public static OptionImpl<Void, Boolean> createShowCoordsOption() {
        return OptionImpl.createBuilder(Boolean.class, ReggadiumOptionsStorage.INSTANCE)
                .setId(new ResourceLocation(Reggadium.MODID, "show_coords"))
                .setName(Component.translatable("reggadium.extra.show_coords.name"))
                .setTooltip(Component.translatable("reggadium.extra.show_coords.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, val) -> {
                    Config.SHOW_COORDS.set(val);
                    Config.showCoordsEnabled = val;
                    ExtraSettings.showCoords = val;
                    Config.save();
                }, opts -> Config.showCoordsEnabled)
                .setImpact(OptionImpact.LOW)
                .build();
    }

    public static OptionImpl<Void, Boolean> createShowFacingOption() {
        return OptionImpl.createBuilder(Boolean.class, ReggadiumOptionsStorage.INSTANCE)
                .setId(new ResourceLocation(Reggadium.MODID, "show_facing"))
                .setName(Component.translatable("reggadium.extra.show_facing.name"))
                .setTooltip(Component.translatable("reggadium.extra.show_facing.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, val) -> {
                    Config.SHOW_FACING.set(val);
                    Config.showFacingEnabled = val;
                    ExtraSettings.showFacing = val;
                    Config.save();
                }, opts -> Config.showFacingEnabled)
                .setImpact(OptionImpact.LOW)
                .build();
    }

    public static OptionImpl<Void, Boolean> createShowBiomeOption() {
        return OptionImpl.createBuilder(Boolean.class, ReggadiumOptionsStorage.INSTANCE)
                .setId(new ResourceLocation(Reggadium.MODID, "show_biome"))
                .setName(Component.translatable("reggadium.extra.show_biome.name"))
                .setTooltip(Component.translatable("reggadium.extra.show_biome.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, val) -> {
                    Config.SHOW_BIOME.set(val);
                    Config.showBiomeEnabled = val;
                    ExtraSettings.showBiome = val;
                    Config.save();
                }, opts -> Config.showBiomeEnabled)
                .setImpact(OptionImpact.LOW)
                .build();
    }

    public static OptionImpl<Void, Boolean> createShowMemoryOption() {
        return OptionImpl.createBuilder(Boolean.class, ReggadiumOptionsStorage.INSTANCE)
                .setId(new ResourceLocation(Reggadium.MODID, "show_memory"))
                .setName(Component.translatable("reggadium.extra.show_memory.name"))
                .setTooltip(Component.translatable("reggadium.extra.show_memory.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, val) -> {
                    Config.SHOW_MEMORY.set(val);
                    Config.showMemoryEnabled = val;
                    ExtraSettings.showMemory = val;
                    Config.save();
                }, opts -> Config.showMemoryEnabled)
                .setImpact(OptionImpact.LOW)
                .build();
    }

    public static OptionImpl<Void, Boolean> createShowCpuOption() {
        return OptionImpl.createBuilder(Boolean.class, ReggadiumOptionsStorage.INSTANCE)
                .setId(new ResourceLocation(Reggadium.MODID, "show_cpu"))
                .setName(Component.translatable("reggadium.extra.show_cpu.name"))
                .setTooltip(Component.translatable("reggadium.extra.show_cpu.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, val) -> {
                    Config.SHOW_CPU.set(val);
                    Config.showCpuEnabled = val;
                    ExtraSettings.showCpu = val;
                    Config.save();
                }, opts -> Config.showCpuEnabled)
                .setImpact(OptionImpact.LOW)
                .build();
    }

    public static OptionImpl<Void, Boolean> createShowGpuOption() {
        return OptionImpl.createBuilder(Boolean.class, ReggadiumOptionsStorage.INSTANCE)
                .setId(new ResourceLocation(Reggadium.MODID, "show_gpu"))
                .setName(Component.translatable("reggadium.extra.show_gpu.name"))
                .setTooltip(Component.translatable("reggadium.extra.show_gpu.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, val) -> {
                    Config.SHOW_GPU.set(val);
                    Config.showGpuEnabled = val;
                    ExtraSettings.showGpu = val;
                    Config.save();
                }, opts -> Config.showGpuEnabled)
                .setImpact(OptionImpact.LOW)
                .build();
    }

    public static OptionImpl<Void, Boolean> createShowGameTimeOption() {
        return OptionImpl.createBuilder(Boolean.class, ReggadiumOptionsStorage.INSTANCE)
                .setId(new ResourceLocation(Reggadium.MODID, "show_gametime"))
                .setName(Component.translatable("reggadium.extra.show_gametime.name"))
                .setTooltip(Component.translatable("reggadium.extra.show_gametime.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, val) -> {
                    Config.SHOW_GAME_TIME.set(val);
                    Config.showGameTimeEnabled = val;
                    ExtraSettings.showGameTime = val;
                    Config.save();
                }, opts -> Config.showGameTimeEnabled)
                .setImpact(OptionImpact.LOW)
                .build();
    }

    public static OptionImpl<Void, Boolean> createShowPingOption() {
        return OptionImpl.createBuilder(Boolean.class, ReggadiumOptionsStorage.INSTANCE)
                .setId(new ResourceLocation(Reggadium.MODID, "show_ping"))
                .setName(Component.translatable("reggadium.extra.show_ping.name"))
                .setTooltip(Component.translatable("reggadium.extra.show_ping.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, val) -> {
                    Config.SHOW_PING.set(val);
                    Config.showPingEnabled = val;
                    ExtraSettings.showPing = val;
                    Config.save();
                }, opts -> Config.showPingEnabled)
                .setImpact(OptionImpact.LOW)
                .build();
    }

    // --- Extra HUD Layout & Styling ---

    public static OptionImpl<Void, OverlayCorner> createOverlayCornerOption() {
        Component[] names = Arrays.stream(OverlayCorner.values())
                .map(OverlayCorner::getDisplayName)
                .toArray(Component[]::new);
        return OptionImpl.createBuilder(OverlayCorner.class, ReggadiumOptionsStorage.INSTANCE)
                .setId(new ResourceLocation(Reggadium.MODID, "overlay_corner"))
                .setName(Component.translatable("reggadium.extra.overlay_corner.name"))
                .setTooltip(Component.translatable("reggadium.extra.overlay_corner.tooltip"))
                .setControl(opt -> new CyclingControl<>(opt, OverlayCorner.class, names))
                .setBinding((opts, val) -> {
                    Config.OVERLAY_CORNER.set(val);
                    Config.overlayCorner = val;
                    ExtraSettings.overlayCorner = val;
                    Config.save();
                }, opts -> Config.overlayCorner)
                .setImpact(OptionImpact.LOW)
                .build();
    }

    public static OptionImpl<Void, Integer> createHudScaleOption() {
        return OptionImpl.createBuilder(Integer.class, ReggadiumOptionsStorage.INSTANCE)
                .setId(new ResourceLocation(Reggadium.MODID, "hud_scale"))
                .setName(Component.translatable("reggadium.extra.hud_scale.name"))
                .setTooltip(Component.translatable("reggadium.extra.hud_scale.tooltip"))
                .setControl(opt -> new SliderControl(opt, 50, 200, 5, ControlValueFormatter.percentage()))
                .setBinding((opts, val) -> {
                    Config.HUD_SCALE.set(val);
                    Config.hudScale = val;
                    ExtraSettings.hudScale = val;
                    Config.save();
                }, opts -> Config.hudScale)
                .setImpact(OptionImpact.LOW)
                .build();
    }

    public static OptionImpl<Void, TextContrast> createTextContrastOption() {
        Component[] names = Arrays.stream(TextContrast.values())
                .map(TextContrast::getDisplayName)
                .toArray(Component[]::new);
        return OptionImpl.createBuilder(TextContrast.class, ReggadiumOptionsStorage.INSTANCE)
                .setId(new ResourceLocation(Reggadium.MODID, "text_contrast"))
                .setName(Component.translatable("reggadium.extra.text_contrast.name"))
                .setTooltip(Component.translatable("reggadium.extra.text_contrast.tooltip"))
                .setControl(opt -> new CyclingControl<>(opt, TextContrast.class, names))
                .setBinding((opts, val) -> {
                    Config.TEXT_CONTRAST.set(val);
                    Config.textContrast = val;
                    ExtraSettings.textContrast = val;
                    Config.save();
                }, opts -> Config.textContrast)
                .setImpact(OptionImpact.LOW)
                .build();
    }

    public static OptionImpl<Void, BackgroundOpacity> createBackgroundOpacityOption() {
        Component[] names = Arrays.stream(BackgroundOpacity.values())
                .map(BackgroundOpacity::getDisplayName)
                .toArray(Component[]::new);
        return OptionImpl.createBuilder(BackgroundOpacity.class, ReggadiumOptionsStorage.INSTANCE)
                .setId(new ResourceLocation(Reggadium.MODID, "background_opacity"))
                .setName(Component.translatable("reggadium.extra.background_opacity.name"))
                .setTooltip(Component.translatable("reggadium.extra.background_opacity.tooltip"))
                .setControl(opt -> new CyclingControl<>(opt, BackgroundOpacity.class, names))
                .setBinding((opts, val) -> {
                    Config.BACKGROUND_OPACITY.set(val);
                    Config.backgroundOpacity = val;
                    ExtraSettings.backgroundOpacity = val;
                    Config.save();
                }, opts -> Config.backgroundOpacity)
                .setImpact(OptionImpact.LOW)
                .build();
    }

    public static OptionImpl<Void, CoordPrecision> createCoordPrecisionOption() {
        Component[] names = Arrays.stream(CoordPrecision.values())
                .map(CoordPrecision::getDisplayName)
                .toArray(Component[]::new);
        return OptionImpl.createBuilder(CoordPrecision.class, ReggadiumOptionsStorage.INSTANCE)
                .setId(new ResourceLocation(Reggadium.MODID, "coord_precision"))
                .setName(Component.translatable("reggadium.extra.coord_precision.name"))
                .setTooltip(Component.translatable("reggadium.extra.coord_precision.tooltip"))
                .setControl(opt -> new CyclingControl<>(opt, CoordPrecision.class, names))
                .setBinding((opts, val) -> {
                    Config.COORD_PRECISION.set(val);
                    Config.coordPrecision = val;
                    ExtraSettings.coordPrecision = val;
                    Config.save();
                }, opts -> Config.coordPrecision)
                .setImpact(OptionImpact.LOW)
                .build();
    }

    // --- Extra HUD Colors ---

    private static OptionImpl<Void, HudColor> createColorOption(
            String id,
            String nameKey,
            String tooltipKey,
            Consumer<HudColor> setter,
            Supplier<HudColor> getter
    ) {
        Component[] names = Arrays.stream(HudColor.values())
                .map(HudColor::getDisplayName)
                .toArray(Component[]::new);
        return OptionImpl.createBuilder(HudColor.class, ReggadiumOptionsStorage.INSTANCE)
                .setId(new ResourceLocation(Reggadium.MODID, id))
                .setName(Component.translatable(nameKey))
                .setTooltip(Component.translatable(tooltipKey))
                .setControl(opt -> new CyclingControl<>(opt, HudColor.class, names))
                .setBinding((opts, val) -> {
                    setter.accept(val);
                    Config.save();
                }, opts -> getter.get())
                .setImpact(OptionImpact.LOW)
                .build();
    }

    public static OptionImpl<Void, HudColor> createFpsColorOption() {
        return createColorOption("fps_color", "reggadium.extra.color.fps.name", "reggadium.extra.color.fps.tooltip",
                val -> { Config.FPS_COLOR.set(val); Config.fpsColor = val; ExtraSettings.fpsColor = val; },
                () -> Config.fpsColor);
    }

    public static OptionImpl<Void, HudColor> createCoordsColorOption() {
        return createColorOption("coords_color", "reggadium.extra.color.coords.name", "reggadium.extra.color.coords.tooltip",
                val -> { Config.COORDS_COLOR.set(val); Config.coordsColor = val; ExtraSettings.coordsColor = val; },
                () -> Config.coordsColor);
    }

    public static OptionImpl<Void, HudColor> createFacingColorOption() {
        return createColorOption("facing_color", "reggadium.extra.color.facing.name", "reggadium.extra.color.facing.tooltip",
                val -> { Config.FACING_COLOR.set(val); Config.facingColor = val; ExtraSettings.facingColor = val; },
                () -> Config.facingColor);
    }

    public static OptionImpl<Void, HudColor> createBiomeColorOption() {
        return createColorOption("biome_color", "reggadium.extra.color.biome.name", "reggadium.extra.color.biome.tooltip",
                val -> { Config.BIOME_COLOR.set(val); Config.biomeColor = val; ExtraSettings.biomeColor = val; },
                () -> Config.biomeColor);
    }

    public static OptionImpl<Void, HudColor> createMemoryColorOption() {
        return createColorOption("memory_color", "reggadium.extra.color.memory.name", "reggadium.extra.color.memory.tooltip",
                val -> { Config.MEMORY_COLOR.set(val); Config.memoryColor = val; ExtraSettings.memoryColor = val; },
                () -> Config.memoryColor);
    }

    public static OptionImpl<Void, HudColor> createCpuColorOption() {
        return createColorOption("cpu_color", "reggadium.extra.color.cpu.name", "reggadium.extra.color.cpu.tooltip",
                val -> { Config.CPU_COLOR.set(val); Config.cpuColor = val; ExtraSettings.cpuColor = val; },
                () -> Config.cpuColor);
    }

    public static OptionImpl<Void, HudColor> createGpuColorOption() {
        return createColorOption("gpu_color", "reggadium.extra.color.gpu.name", "reggadium.extra.color.gpu.tooltip",
                val -> { Config.GPU_COLOR.set(val); Config.gpuColor = val; ExtraSettings.gpuColor = val; },
                () -> Config.gpuColor);
    }

    public static OptionImpl<Void, HudColor> createTimeColorOption() {
        return createColorOption("time_color", "reggadium.extra.color.time.name", "reggadium.extra.color.time.tooltip",
                val -> { Config.TIME_COLOR.set(val); Config.timeColor = val; ExtraSettings.timeColor = val; },
                () -> Config.timeColor);
    }

    public static OptionImpl<Void, HudColor> createPingColorOption() {
        return createColorOption("ping_color", "reggadium.extra.color.ping.name", "reggadium.extra.color.ping.tooltip",
                val -> { Config.PING_COLOR.set(val); Config.pingColor = val; ExtraSettings.pingColor = val; },
                () -> Config.pingColor);
    }

    // --- Performance Option Builders ---

    public static OptionImpl<Void, Boolean> createFastSbbOption() {
        return OptionImpl.createBuilder(Boolean.class, ReggadiumOptionsStorage.INSTANCE)
                .setId(new ResourceLocation(Reggadium.MODID, "fast_sbb"))
                .setName(Component.translatable("reggadium.options.fast_sbb.name"))
                .setTooltip(Component.translatable("reggadium.options.fast_sbb.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, val) -> {
                    Config.FAST_SUPER_BYTE_BUFFER.set(val);
                    Config.fastSbbEnabled = val;
                    Config.save();
                }, opts -> Config.fastSbbEnabled)
                .setImpact(OptionImpact.HIGH)
                .build();
    }

    public static OptionImpl<Void, Boolean> createBackfaceCullingOption() {
        return OptionImpl.createBuilder(Boolean.class, ReggadiumOptionsStorage.INSTANCE)
                .setId(new ResourceLocation(Reggadium.MODID, "backface_culling"))
                .setName(Component.translatable("reggadium.options.backface_culling.name"))
                .setTooltip(Component.translatable("reggadium.options.backface_culling.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, val) -> {
                    Config.BACKFACE_CULLING.set(val);
                    Config.backfaceCullingEnabled = val;
                    Config.save();
                }, opts -> Config.backfaceCullingEnabled)
                .setImpact(OptionImpact.MEDIUM)
                .build();
    }

    public static OptionImpl<Void, Boolean> createFastLightCacheOption() {
        return OptionImpl.createBuilder(Boolean.class, ReggadiumOptionsStorage.INSTANCE)
                .setId(new ResourceLocation(Reggadium.MODID, "fast_light_cache"))
                .setName(Component.translatable("reggadium.options.fast_light_cache.name"))
                .setTooltip(Component.translatable("reggadium.options.fast_light_cache.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, val) -> {
                    Config.FAST_LIGHT_CACHE.set(val);
                    Config.fastLightCacheEnabled = val;
                    Config.save();
                }, opts -> Config.fastLightCacheEnabled)
                .setImpact(OptionImpact.MEDIUM)
                .build();
    }

    public static OptionImpl<Void, Boolean> createCullBeltsOption() {
        return OptionImpl.createBuilder(Boolean.class, ReggadiumOptionsStorage.INSTANCE)
                .setId(new ResourceLocation(Reggadium.MODID, "cull_belts"))
                .setName(Component.translatable("reggadium.options.cull_belts.name"))
                .setTooltip(Component.translatable("reggadium.options.cull_belts.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, val) -> {
                    Config.CULL_BELTS.set(val);
                    Config.cullBeltsEnabled = val;
                    Config.save();
                }, opts -> Config.cullBeltsEnabled)
                .setImpact(OptionImpact.HIGH)
                .build();
    }

    public static OptionImpl<Void, Boolean> createKineticCullingOption() {
        return OptionImpl.createBuilder(Boolean.class, ReggadiumOptionsStorage.INSTANCE)
                .setId(new ResourceLocation(Reggadium.MODID, "kinetic_culling"))
                .setName(Component.translatable("reggadium.options.kinetic_culling.name"))
                .setTooltip(Component.translatable("reggadium.options.kinetic_culling.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, val) -> {
                    Config.KINETIC_CULLING.set(val);
                    Config.kineticCullingEnabled = val;
                    Config.save();
                }, opts -> Config.kineticCullingEnabled)
                .setImpact(OptionImpact.HIGH)
                .build();
    }

    public static OptionImpl<Void, Boolean> createSkipStationaryKineticsOption() {
        return OptionImpl.createBuilder(Boolean.class, ReggadiumOptionsStorage.INSTANCE)
                .setId(new ResourceLocation(Reggadium.MODID, "skip_stationary_kinetics"))
                .setName(Component.translatable("reggadium.options.skip_stationary_kinetics.name"))
                .setTooltip(Component.translatable("reggadium.options.skip_stationary_kinetics.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, val) -> {
                    Config.SKIP_STATIONARY_KINETICS.set(val);
                    Config.skipStationaryKineticsEnabled = val;
                    Config.save();
                }, opts -> Config.skipStationaryKineticsEnabled)
                .setImpact(OptionImpact.MEDIUM)
                .build();
    }

    public static OptionImpl<Void, Boolean> createKineticAudioThrottlingOption() {
        return OptionImpl.createBuilder(Boolean.class, ReggadiumOptionsStorage.INSTANCE)
                .setId(new ResourceLocation(Reggadium.MODID, "kinetic_audio_throttling"))
                .setName(Component.translatable("reggadium.options.kinetic_audio_throttling.name"))
                .setTooltip(Component.translatable("reggadium.options.kinetic_audio_throttling.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, val) -> {
                    Config.KINETIC_AUDIO_THROTTLING.set(val);
                    Config.kineticAudioThrottlingEnabled = val;
                    Config.save();
                }, opts -> Config.kineticAudioThrottlingEnabled)
                .setImpact(OptionImpact.HIGH)
                .build();
    }

    public static OptionImpl<Void, Boolean> createKineticLodOption() {
        return OptionImpl.createBuilder(Boolean.class, ReggadiumOptionsStorage.INSTANCE)
                .setId(new ResourceLocation(Reggadium.MODID, "kinetic_lod"))
                .setName(Component.translatable("reggadium.options.kinetic_lod.name"))
                .setTooltip(Component.translatable("reggadium.options.kinetic_lod.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, val) -> {
                    Config.KINETIC_LOD.set(val);
                    Config.kineticLodEnabled = val;
                    Config.save();
                }, opts -> Config.kineticLodEnabled)
                .setImpact(OptionImpact.HIGH)
                .build();
    }

    public static OptionImpl<Void, Boolean> createReduceBlockEntityLagOption() {
        return OptionImpl.createBuilder(Boolean.class, ReggadiumOptionsStorage.INSTANCE)
                .setId(new ResourceLocation(Reggadium.MODID, "reduce_block_entity_lag"))
                .setName(Component.translatable("reggadium.options.reduce_block_entity_lag.name"))
                .setTooltip(Component.translatable("reggadium.options.reduce_block_entity_lag.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, val) -> {
                    Config.REDUCE_BLOCK_ENTITY_LAG.set(val);
                    Config.reduceBlockEntityLagEnabled = val;
                    Config.save();
                }, opts -> Config.reduceBlockEntityLagEnabled)
                .setImpact(OptionImpact.LOW)
                .build();
    }

    public static OptionImpl<Void, Boolean> createCullBlockEntitiesOption() {
        return OptionImpl.createBuilder(Boolean.class, ReggadiumOptionsStorage.INSTANCE)
                .setId(new ResourceLocation(Reggadium.MODID, "cull_block_entities"))
                .setName(Component.translatable("reggadium.options.cull_block_entities.name"))
                .setTooltip(Component.translatable("reggadium.options.cull_block_entities.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, val) -> {
                    Config.CULL_BLOCK_ENTITIES.set(val);
                    Config.cullBlockEntitiesEnabled = val;
                    Config.save();
                }, opts -> Config.cullBlockEntitiesEnabled)
                .setImpact(OptionImpact.HIGH)
                .build();
    }

    public static OptionImpl<Void, Boolean> createCullEntitiesOption() {
        return OptionImpl.createBuilder(Boolean.class, ReggadiumOptionsStorage.INSTANCE)
                .setId(new ResourceLocation(Reggadium.MODID, "cull_entities"))
                .setName(Component.translatable("reggadium.options.cull_entities.name"))
                .setTooltip(Component.translatable("reggadium.options.cull_entities.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, val) -> {
                    Config.CULL_ENTITIES.set(val);
                    Config.cullEntitiesEnabled = val;
                    Config.save();
                }, opts -> Config.cullEntitiesEnabled)
                .setImpact(OptionImpact.HIGH)
                .build();
    }

    public static OptionImpl<Void, Boolean> createCullShadowsOption() {
        return OptionImpl.createBuilder(Boolean.class, ReggadiumOptionsStorage.INSTANCE)
                .setId(new ResourceLocation(Reggadium.MODID, "cull_shadows"))
                .setName(Component.translatable("reggadium.options.cull_shadows.name"))
                .setTooltip(Component.translatable("reggadium.options.cull_shadows.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, val) -> {
                    Config.CULL_SHADOWS.set(val);
                    Config.cullShadowsEnabled = val;
                    Config.save();
                }, opts -> Config.cullShadowsEnabled)
                .setImpact(OptionImpact.MEDIUM)
                .build();
    }

    public static OptionImpl<Void, Boolean> createCreateFxCullOption() {
        return OptionImpl.createBuilder(Boolean.class, ReggadiumOptionsStorage.INSTANCE)
                .setId(new ResourceLocation(Reggadium.MODID, "create_fx_cull"))
                .setName(Component.translatable("reggadium.options.create_fx_cull.name"))
                .setTooltip(Component.translatable("reggadium.options.create_fx_cull.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, val) -> {
                    Config.CREATE_FX_CULL.set(val);
                    Config.createFxCullEnabled = val;
                    Config.save();
                }, opts -> Config.createFxCullEnabled)
                .setImpact(OptionImpact.HIGH)
                .build();
    }

    public static OptionImpl<Void, Boolean> createAggressiveTickSleepOption() {
        return OptionImpl.createBuilder(Boolean.class, ReggadiumOptionsStorage.INSTANCE)
                .setId(new ResourceLocation(Reggadium.MODID, "aggressive_tick_sleep"))
                .setName(Component.translatable("reggadium.options.aggressive_tick_sleep.name"))
                .setTooltip(Component.translatable("reggadium.options.aggressive_tick_sleep.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, val) -> {
                    Config.AGGRESSIVE_TICK_SLEEP.set(val);
                    Config.aggressiveTickSleepEnabled = val;
                    Config.save();
                }, opts -> Config.aggressiveTickSleepEnabled)
                .setImpact(OptionImpact.HIGH)
                .build();
    }

    public static OptionImpl<Void, Boolean> createCullParticlesOption() {
        return OptionImpl.createBuilder(Boolean.class, ReggadiumOptionsStorage.INSTANCE)
                .setId(new ResourceLocation(Reggadium.MODID, "cull_particles"))
                .setName(Component.translatable("reggadium.options.cull_particles.name"))
                .setTooltip(Component.translatable("reggadium.options.cull_particles.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, val) -> {
                    Config.CULL_PARTICLES.set(val);
                    Config.cullParticlesEnabled = val;
                    Config.save();
                }, opts -> Config.cullParticlesEnabled)
                .setImpact(OptionImpact.HIGH)
                .build();
    }

    public static OptionImpl<Void, Boolean> createOptimizeContraptionsOption() {
        return OptionImpl.createBuilder(Boolean.class, ReggadiumOptionsStorage.INSTANCE)
                .setId(new ResourceLocation(Reggadium.MODID, "optimize_contraptions"))
                .setName(Component.translatable("reggadium.options.optimize_contraptions.name"))
                .setTooltip(Component.translatable("reggadium.options.optimize_contraptions.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, val) -> {
                    Config.OPTIMIZE_CONTRAPTIONS.set(val);
                    Config.optimizeContraptionsEnabled = val;
                    Config.save();
                }, opts -> Config.optimizeContraptionsEnabled)
                .setImpact(OptionImpact.MEDIUM)
                .build();
    }

    public static OptionImpl<Void, Boolean> createOptimizeEntityRenderingOption() {
        return OptionImpl.createBuilder(Boolean.class, ReggadiumOptionsStorage.INSTANCE)
                .setId(new ResourceLocation(Reggadium.MODID, "optimize_entity_rendering"))
                .setName(Component.translatable("reggadium.options.optimize_entity_rendering.name"))
                .setTooltip(Component.translatable("reggadium.options.optimize_entity_rendering.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, val) -> {
                    Config.OPTIMIZE_ENTITY_RENDERING.set(val);
                    Config.optimizeEntityRenderingEnabled = val;
                    Config.save();
                }, opts -> Config.optimizeEntityRenderingEnabled)
                .setImpact(OptionImpact.MEDIUM)
                .build();
    }

    // --- Memory Leak Mitigation Option Builders ---

    public static OptionImpl<Void, Boolean> createFixMemoryLeaksOption() {
        return OptionImpl.createBuilder(Boolean.class, ReggadiumOptionsStorage.INSTANCE)
                .setId(new ResourceLocation(Reggadium.MODID, "fix_memory_leaks"))
                .setName(Component.translatable("reggadium.options.fix_memory_leaks.name"))
                .setTooltip(Component.translatable("reggadium.options.fix_memory_leaks.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, val) -> {
                    Config.FIX_MEMORY_LEAKS.set(val);
                    Config.fixMemoryLeaksEnabled = val;
                    Config.save();
                }, opts -> Config.fixMemoryLeaksEnabled)
                .setImpact(OptionImpact.HIGH)
                .build();
    }

    public static OptionImpl<Void, Boolean> createFixClientLevelLeakOption() {
        return OptionImpl.createBuilder(Boolean.class, ReggadiumOptionsStorage.INSTANCE)
                .setId(new ResourceLocation(Reggadium.MODID, "fix_client_level_leak"))
                .setName(Component.translatable("reggadium.options.fix_client_level_leak.name"))
                .setTooltip(Component.translatable("reggadium.options.fix_client_level_leak.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, val) -> {
                    Config.FIX_CLIENT_LEVEL_LEAK.set(val);
                    Config.fixClientLevelLeakEnabled = val;
                    Config.save();
                }, opts -> Config.fixClientLevelLeakEnabled)
                .setImpact(OptionImpact.HIGH)
                .build();
    }

    public static OptionImpl<Void, Boolean> createFixFakePlayerLeakOption() {
        return OptionImpl.createBuilder(Boolean.class, ReggadiumOptionsStorage.INSTANCE)
                .setId(new ResourceLocation(Reggadium.MODID, "fix_fake_player_leak"))
                .setName(Component.translatable("reggadium.options.fix_fake_player_leak.name"))
                .setTooltip(Component.translatable("reggadium.options.fix_fake_player_leak.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, val) -> {
                    Config.FIX_FAKE_PLAYER_LEAK.set(val);
                    Config.fixFakePlayerLeakEnabled = val;
                    Config.save();
                }, opts -> Config.fixFakePlayerLeakEnabled)
                .setImpact(OptionImpact.HIGH)
                .build();
    }

    public static OptionImpl<Void, Boolean> createFixDebugSubscriptionLeakOption() {
        return OptionImpl.createBuilder(Boolean.class, ReggadiumOptionsStorage.INSTANCE)
                .setId(new ResourceLocation(Reggadium.MODID, "fix_debug_subscription_leak"))
                .setName(Component.translatable("reggadium.options.fix_debug_subscription_leak.name"))
                .setTooltip(Component.translatable("reggadium.options.fix_debug_subscription_leak.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, val) -> {
                    Config.FIX_DEBUG_SUBSCRIPTION_LEAK.set(val);
                    Config.fixDebugSubscriptionLeakEnabled = val;
                    Config.save();
                }, opts -> Config.fixDebugSubscriptionLeakEnabled)
                .setImpact(OptionImpact.LOW)
                .build();
    }

    // --- Group Builders ---

    public static OptionGroup createKineticStreamingGroup() {
        return OptionGroup.createBuilder()
                .setId(new ResourceLocation(Reggadium.MODID, "kinetic_streaming_group"))
                .add(createFastSbbOption())
                .add(createBackfaceCullingOption())
                .add(createFastLightCacheOption())
                .build();
    }

    public static OptionGroup createFactoryCullingGroup() {
        return OptionGroup.createBuilder()
                .setId(new ResourceLocation(Reggadium.MODID, "factory_culling_group"))
                .add(createCullBeltsOption())
                .add(createKineticCullingOption())
                .add(createSkipStationaryKineticsOption())
                .add(createKineticAudioThrottlingOption())
                .add(createKineticLodOption())
                .add(createReduceBlockEntityLagOption())
                .add(createCreateFxCullOption())
                .add(createAggressiveTickSleepOption())
                .build();
    }

    public static OptionGroup createFrustumCullingGroup() {
        return OptionGroup.createBuilder()
                .setId(new ResourceLocation(Reggadium.MODID, "frustum_culling_group"))
                .add(createCullBlockEntitiesOption())
                .add(createCullParticlesOption())
                .add(createCullEntitiesOption())
                .add(createCullShadowsOption())
                .build();
    }

    public static OptionGroup createContraptionLodGroup() {
        return OptionGroup.createBuilder()
                .setId(new ResourceLocation(Reggadium.MODID, "contraption_lod_group"))
                .add(createOptimizeContraptionsOption())
                .build();
    }

    public static OptionGroup createVertexPipelineGroup() {
        return OptionGroup.createBuilder()
                .setId(new ResourceLocation(Reggadium.MODID, "vertex_pipeline_group"))
                .add(createOptimizeEntityRenderingOption())
                .build();
    }

    public static OptionGroup createMemoryMasterGroup() {
        return OptionGroup.createBuilder()
                .setId(new ResourceLocation(Reggadium.MODID, "memory_master_group"))
                .add(createFixMemoryLeaksOption())
                .build();
    }

    public static OptionGroup createMemoryMitigationsGroup() {
        return OptionGroup.createBuilder()
                .setId(new ResourceLocation(Reggadium.MODID, "memory_mitigations_group"))
                .add(createFixClientLevelLeakOption())
                .add(createFixFakePlayerLeakOption())
                .add(createFixDebugSubscriptionLeakOption())
                .build();
    }

    public static OptionGroup createExtraHudMasterGroup() {
        return OptionGroup.createBuilder()
                .setId(new ResourceLocation(Reggadium.MODID, "extra_hud_master_group"))
                .add(createEnableExtraHudOption())
                .build();
    }

    public static OptionGroup createExtraHudMetricsGroup() {
        return OptionGroup.createBuilder()
                .setId(new ResourceLocation(Reggadium.MODID, "extra_hud_metrics_group"))
                .add(createShowFpsOption())
                .add(createShowFpsExtendedOption())
                .add(createShowCoordsOption())
                .add(createShowFacingOption())
                .add(createShowBiomeOption())
                .add(createShowMemoryOption())
                .add(createShowCpuOption())
                .add(createShowGpuOption())
                .add(createShowGameTimeOption())
                .add(createShowPingOption())
                .build();
    }

    public static OptionGroup createExtraHudLayoutGroup() {
        return OptionGroup.createBuilder()
                .setId(new ResourceLocation(Reggadium.MODID, "extra_hud_layout_group"))
                .add(createOverlayCornerOption())
                .add(createHudScaleOption())
                .add(createTextContrastOption())
                .add(createBackgroundOpacityOption())
                .add(createCoordPrecisionOption())
                .build();
    }

    public static OptionGroup createExtraHudColorsGroup() {
        return OptionGroup.createBuilder()
                .setId(new ResourceLocation(Reggadium.MODID, "extra_hud_colors_group"))
                .add(createFpsColorOption())
                .add(createCoordsColorOption())
                .add(createFacingColorOption())
                .add(createBiomeColorOption())
                .add(createMemoryColorOption())
                .add(createCpuColorOption())
                .add(createGpuColorOption())
                .add(createTimeColorOption())
                .add(createPingColorOption())
                .build();
    }

    public static OptionGroup createSystemGroup() {
        return OptionGroup.createBuilder()
                .setId(new ResourceLocation(Reggadium.MODID, "system_group"))
                .add(createEnableOptimizationsOption())
                .build();
    }

    // --- Sub-Pages (Tabs under Reggadium) ---

    public static OptionPage createKineticsPage() {
        OptionIdentifier<Void> pageId = OptionIdentifier.create(new ResourceLocation(Reggadium.MODID, "kinetics"));
        Component pageTitle = Component.translatable("reggadium.options.page.kinetics");
        return new OptionPage(pageId, pageTitle, ImmutableList.of(
                createKineticStreamingGroup(),
                createFactoryCullingGroup()
        ));
    }

    public static OptionPage createCullingPage() {
        OptionIdentifier<Void> pageId = OptionIdentifier.create(new ResourceLocation(Reggadium.MODID, "culling"));
        Component pageTitle = Component.translatable("reggadium.options.page.culling");
        return new OptionPage(pageId, pageTitle, ImmutableList.of(
                createFrustumCullingGroup(),
                createContraptionLodGroup()
        ));
    }

    public static OptionPage createPipelinePage() {
        OptionIdentifier<Void> pageId = OptionIdentifier.create(new ResourceLocation(Reggadium.MODID, "pipeline"));
        Component pageTitle = Component.translatable("reggadium.options.page.pipeline");
        return new OptionPage(pageId, pageTitle, ImmutableList.of(
                createVertexPipelineGroup()
        ));
    }

    public static OptionPage createMemoryPage() {
        OptionIdentifier<Void> pageId = OptionIdentifier.create(new ResourceLocation(Reggadium.MODID, "memory"));
        Component pageTitle = Component.translatable("reggadium.options.page.memory");
        return new OptionPage(pageId, pageTitle, ImmutableList.of(
                createMemoryMasterGroup(),
                createMemoryMitigationsGroup()
        ));
    }

    public static OptionPage createDiagnosticsPage() {
        OptionIdentifier<Void> pageId = OptionIdentifier.create(new ResourceLocation(Reggadium.MODID, "diagnostics"));
        Component pageTitle = Component.translatable("reggadium.options.page.diagnostics");
        return new OptionPage(pageId, pageTitle, ImmutableList.of(
                createExtraHudMasterGroup(),
                createExtraHudMetricsGroup(),
                createExtraHudLayoutGroup(),
                createExtraHudColorsGroup(),
                createSystemGroup()
        ));
    }
}
