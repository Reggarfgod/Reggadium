package com.reggarf.mods.reggadium.compat;

import com.reggarf.mods.reggadium.Config;
import com.reggarf.mods.reggadium.Reggadium;
import com.reggarf.mods.reggadium.extra.*;
import net.caffeinemc.mods.sodium.api.config.ConfigEntryPoint;
import net.caffeinemc.mods.sodium.api.config.ConfigEntryPointForge;
import net.caffeinemc.mods.sodium.api.config.option.OptionImpact;
import net.caffeinemc.mods.sodium.api.config.structure.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;
import java.util.function.Supplier;

@ConfigEntryPointForge("reggadium")
public class ReggadiumSodiumCompat implements ConfigEntryPoint {

    @Override
    public void registerConfigLate(ConfigBuilder builder) {
        ModOptionsBuilder modOptions = builder.registerOwnModOptions();
        modOptions.setName("Reggadium");

        modOptions.addPage(createKineticsPage(builder));
        modOptions.addPage(createCullingPage(builder));
        modOptions.addPage(createPipelinePage(builder));
        modOptions.addPage(createExtraPage(builder));
    }

    private static OptionPageBuilder createKineticsPage(ConfigBuilder builder) {
        OptionPageBuilder page = builder.createOptionPage()
                .setName(Component.translatable("reggadium.options.page.kinetics"));

        // Group 1: Kinetic Geometry & Streaming
        OptionGroupBuilder streamingGroup = builder.createOptionGroup()
                .setName(Component.translatable("reggadium.options.group.kinetic_streaming"))
                .addOption(builder.createBooleanOption(ResourceLocation.fromNamespaceAndPath(Reggadium.MODID, "fast_sbb"))
                        .setName(Component.translatable("reggadium.options.fast_sbb.name"))
                        .setTooltip(Component.translatable("reggadium.options.fast_sbb.tooltip"))
                        .setStorageHandler(Config::save)
                        .setBinding(val -> { Config.FAST_SUPER_BYTE_BUFFER.set(val); Config.fastSbbEnabled = val; }, () -> Config.fastSbbEnabled)
                        .setDefaultValue(true)
                        .setImpact(OptionImpact.HIGH))
                .addOption(builder.createBooleanOption(ResourceLocation.fromNamespaceAndPath(Reggadium.MODID, "backface_culling"))
                        .setName(Component.translatable("reggadium.options.backface_culling.name"))
                        .setTooltip(Component.translatable("reggadium.options.backface_culling.tooltip"))
                        .setStorageHandler(Config::save)
                        .setBinding(val -> { Config.BACKFACE_CULLING.set(val); Config.backfaceCullingEnabled = val; }, () -> Config.backfaceCullingEnabled)
                        .setDefaultValue(true)
                        .setImpact(OptionImpact.MEDIUM))
                .addOption(builder.createBooleanOption(ResourceLocation.fromNamespaceAndPath(Reggadium.MODID, "fast_light_cache"))
                        .setName(Component.translatable("reggadium.options.fast_light_cache.name"))
                        .setTooltip(Component.translatable("reggadium.options.fast_light_cache.tooltip"))
                        .setStorageHandler(Config::save)
                        .setBinding(val -> { Config.FAST_LIGHT_CACHE.set(val); Config.fastLightCacheEnabled = val; }, () -> Config.fastLightCacheEnabled)
                        .setDefaultValue(true)
                        .setImpact(OptionImpact.MEDIUM));

        page.addOptionGroup(streamingGroup);

        // Group 2: Factory & Kinetic Culling
        OptionGroupBuilder cullingGroup = builder.createOptionGroup()
                .setName(Component.translatable("reggadium.options.group.factory_culling"))
                .addOption(builder.createBooleanOption(ResourceLocation.fromNamespaceAndPath(Reggadium.MODID, "cull_belts"))
                        .setName(Component.translatable("reggadium.options.cull_belts.name"))
                        .setTooltip(Component.translatable("reggadium.options.cull_belts.tooltip"))
                        .setStorageHandler(Config::save)
                        .setBinding(val -> { Config.CULL_BELTS.set(val); Config.cullBeltsEnabled = val; }, () -> Config.cullBeltsEnabled)
                        .setDefaultValue(true)
                        .setImpact(OptionImpact.HIGH))
                .addOption(builder.createBooleanOption(ResourceLocation.fromNamespaceAndPath(Reggadium.MODID, "kinetic_culling"))
                        .setName(Component.translatable("reggadium.options.kinetic_culling.name"))
                        .setTooltip(Component.translatable("reggadium.options.kinetic_culling.tooltip"))
                        .setStorageHandler(Config::save)
                        .setBinding(val -> { Config.KINETIC_CULLING.set(val); Config.kineticCullingEnabled = val; }, () -> Config.kineticCullingEnabled)
                        .setDefaultValue(true)
                        .setImpact(OptionImpact.HIGH))
                .addOption(builder.createBooleanOption(ResourceLocation.fromNamespaceAndPath(Reggadium.MODID, "skip_stationary_kinetics"))
                        .setName(Component.translatable("reggadium.options.skip_stationary_kinetics.name"))
                        .setTooltip(Component.translatable("reggadium.options.skip_stationary_kinetics.tooltip"))
                        .setStorageHandler(Config::save)
                        .setBinding(val -> { Config.SKIP_STATIONARY_KINETICS.set(val); Config.skipStationaryKineticsEnabled = val; }, () -> Config.skipStationaryKineticsEnabled)
                        .setDefaultValue(true)
                        .setImpact(OptionImpact.MEDIUM))
                .addOption(builder.createBooleanOption(ResourceLocation.fromNamespaceAndPath(Reggadium.MODID, "kinetic_audio_throttling"))
                        .setName(Component.translatable("reggadium.options.kinetic_audio_throttling.name"))
                        .setTooltip(Component.translatable("reggadium.options.kinetic_audio_throttling.tooltip"))
                        .setStorageHandler(Config::save)
                        .setBinding(val -> { Config.KINETIC_AUDIO_THROTTLING.set(val); Config.kineticAudioThrottlingEnabled = val; }, () -> Config.kineticAudioThrottlingEnabled)
                        .setDefaultValue(true)
                        .setImpact(OptionImpact.HIGH))
                .addOption(builder.createBooleanOption(ResourceLocation.fromNamespaceAndPath(Reggadium.MODID, "kinetic_lod"))
                        .setName(Component.translatable("reggadium.options.kinetic_lod.name"))
                        .setTooltip(Component.translatable("reggadium.options.kinetic_lod.tooltip"))
                        .setStorageHandler(Config::save)
                        .setBinding(val -> { Config.KINETIC_LOD.set(val); Config.kineticLodEnabled = val; }, () -> Config.kineticLodEnabled)
                        .setDefaultValue(true)
                        .setImpact(OptionImpact.HIGH))
                .addOption(builder.createBooleanOption(ResourceLocation.fromNamespaceAndPath(Reggadium.MODID, "reduce_block_entity_lag"))
                        .setName(Component.translatable("reggadium.options.reduce_block_entity_lag.name"))
                        .setTooltip(Component.translatable("reggadium.options.reduce_block_entity_lag.tooltip"))
                        .setStorageHandler(Config::save)
                        .setBinding(val -> { Config.REDUCE_BLOCK_ENTITY_LAG.set(val); Config.reduceBlockEntityLagEnabled = val; }, () -> Config.reduceBlockEntityLagEnabled)
                        .setDefaultValue(true)
                        .setImpact(OptionImpact.LOW))
                .addOption(builder.createBooleanOption(ResourceLocation.fromNamespaceAndPath(Reggadium.MODID, "create_fx_cull"))
                        .setName(Component.translatable("reggadium.options.create_fx_cull.name"))
                        .setTooltip(Component.translatable("reggadium.options.create_fx_cull.tooltip"))
                        .setStorageHandler(Config::save)
                        .setBinding(val -> { Config.CREATE_FX_CULL.set(val); Config.createFxCullEnabled = val; }, () -> Config.createFxCullEnabled)
                        .setDefaultValue(true)
                        .setImpact(OptionImpact.HIGH))
                .addOption(builder.createBooleanOption(ResourceLocation.fromNamespaceAndPath(Reggadium.MODID, "aggressive_tick_sleep"))
                        .setName(Component.translatable("reggadium.options.aggressive_tick_sleep.name"))
                        .setTooltip(Component.translatable("reggadium.options.aggressive_tick_sleep.tooltip"))
                        .setStorageHandler(Config::save)
                        .setBinding(val -> { Config.AGGRESSIVE_TICK_SLEEP.set(val); Config.aggressiveTickSleepEnabled = val; }, () -> Config.aggressiveTickSleepEnabled)
                        .setDefaultValue(true)
                        .setImpact(OptionImpact.HIGH));

        page.addOptionGroup(cullingGroup);
        return page;
    }

    private static OptionPageBuilder createCullingPage(ConfigBuilder builder) {
        OptionPageBuilder page = builder.createOptionPage()
                .setName(Component.translatable("reggadium.options.page.culling"));

        OptionGroupBuilder frustumGroup = builder.createOptionGroup()
                .setName(Component.translatable("reggadium.options.group.frustum_culling"))
                .addOption(builder.createBooleanOption(ResourceLocation.fromNamespaceAndPath(Reggadium.MODID, "cull_block_entities"))
                        .setName(Component.translatable("reggadium.options.cull_block_entities.name"))
                        .setTooltip(Component.translatable("reggadium.options.cull_block_entities.tooltip"))
                        .setStorageHandler(Config::save)
                        .setBinding(val -> { Config.CULL_BLOCK_ENTITIES.set(val); Config.cullBlockEntitiesEnabled = val; }, () -> Config.cullBlockEntitiesEnabled)
                        .setDefaultValue(true)
                        .setImpact(OptionImpact.HIGH))
                .addOption(builder.createBooleanOption(ResourceLocation.fromNamespaceAndPath(Reggadium.MODID, "cull_particles"))
                        .setName(Component.translatable("reggadium.options.cull_particles.name"))
                        .setTooltip(Component.translatable("reggadium.options.cull_particles.tooltip"))
                        .setStorageHandler(Config::save)
                        .setBinding(val -> { Config.CULL_PARTICLES.set(val); Config.cullParticlesEnabled = val; }, () -> Config.cullParticlesEnabled)
                        .setDefaultValue(true)
                        .setImpact(OptionImpact.HIGH))
                .addOption(builder.createBooleanOption(ResourceLocation.fromNamespaceAndPath(Reggadium.MODID, "cull_entities"))
                        .setName(Component.translatable("reggadium.options.cull_entities.name"))
                        .setTooltip(Component.translatable("reggadium.options.cull_entities.tooltip"))
                        .setStorageHandler(Config::save)
                        .setBinding(val -> { Config.CULL_ENTITIES.set(val); Config.cullEntitiesEnabled = val; }, () -> Config.cullEntitiesEnabled)
                        .setDefaultValue(true)
                        .setImpact(OptionImpact.HIGH))
                .addOption(builder.createBooleanOption(ResourceLocation.fromNamespaceAndPath(Reggadium.MODID, "cull_shadows"))
                        .setName(Component.translatable("reggadium.options.cull_shadows.name"))
                        .setTooltip(Component.translatable("reggadium.options.cull_shadows.tooltip"))
                        .setStorageHandler(Config::save)
                        .setBinding(val -> { Config.CULL_SHADOWS.set(val); Config.cullShadowsEnabled = val; }, () -> Config.cullShadowsEnabled)
                        .setDefaultValue(true)
                        .setImpact(OptionImpact.MEDIUM));

        page.addOptionGroup(frustumGroup);

        OptionGroupBuilder lodGroup = builder.createOptionGroup()
                .setName(Component.translatable("reggadium.options.group.contraption_lod"))
                .addOption(builder.createBooleanOption(ResourceLocation.fromNamespaceAndPath(Reggadium.MODID, "optimize_contraptions"))
                        .setName(Component.translatable("reggadium.options.optimize_contraptions.name"))
                        .setTooltip(Component.translatable("reggadium.options.optimize_contraptions.tooltip"))
                        .setStorageHandler(Config::save)
                        .setBinding(val -> { Config.OPTIMIZE_CONTRAPTIONS.set(val); Config.optimizeContraptionsEnabled = val; }, () -> Config.optimizeContraptionsEnabled)
                        .setDefaultValue(true)
                        .setImpact(OptionImpact.MEDIUM));

        page.addOptionGroup(lodGroup);
        return page;
    }

    private static OptionPageBuilder createPipelinePage(ConfigBuilder builder) {
        OptionPageBuilder page = builder.createOptionPage()
                .setName(Component.translatable("reggadium.options.page.pipeline"));

        OptionGroupBuilder pipelineGroup = builder.createOptionGroup()
                .setName(Component.translatable("reggadium.options.group.vertex_pipeline"))
                .addOption(builder.createBooleanOption(ResourceLocation.fromNamespaceAndPath(Reggadium.MODID, "optimize_entity_rendering"))
                        .setName(Component.translatable("reggadium.options.optimize_entity_rendering.name"))
                        .setTooltip(Component.translatable("reggadium.options.optimize_entity_rendering.tooltip"))
                        .setStorageHandler(Config::save)
                        .setBinding(val -> { Config.OPTIMIZE_ENTITY_RENDERING.set(val); Config.optimizeEntityRenderingEnabled = val; }, () -> Config.optimizeEntityRenderingEnabled)
                        .setDefaultValue(true)
                        .setImpact(OptionImpact.MEDIUM));

        page.addOptionGroup(pipelineGroup);
        return page;
    }

    private static OptionPageBuilder createExtraPage(ConfigBuilder builder) {
        OptionPageBuilder page = builder.createOptionPage()
                .setName(Component.translatable("reggadium.options.page.extra"));
        // Extra HUD Master Switch
        OptionGroupBuilder masterGroup = builder.createOptionGroup()
                .setName(Component.translatable("reggadium.options.group.extra_hud"))
                .addOption(builder.createBooleanOption(ResourceLocation.fromNamespaceAndPath(Reggadium.MODID, "enable_extra_hud"))
                        .setName(Component.translatable("reggadium.extra.enable_extra_hud.name"))
                        .setTooltip(Component.translatable("reggadium.extra.enable_extra_hud.tooltip"))
                        .setStorageHandler(Config::save)
                        .setBinding(val -> { Config.ENABLE_EXTRA_HUD.set(val); Config.extraHudEnabled = val; ExtraSettings.enabled = val; }, () -> Config.extraHudEnabled)
                        .setDefaultValue(true)
                        .setImpact(OptionImpact.LOW));

        page.addOptionGroup(masterGroup);

        // Extra HUD Metrics
        OptionGroupBuilder metricsGroup = builder.createOptionGroup()
                .setName(Component.translatable("reggadium.options.group.extra_hud_metrics"))
                .addOption(builder.createBooleanOption(ResourceLocation.fromNamespaceAndPath(Reggadium.MODID, "show_fps"))
                        .setName(Component.translatable("reggadium.extra.show_fps.name"))
                        .setTooltip(Component.translatable("reggadium.extra.show_fps.tooltip"))
                        .setStorageHandler(Config::save)
                        .setBinding(val -> { Config.SHOW_FPS.set(val); Config.showFpsEnabled = val; ExtraSettings.showFps = val; }, () -> Config.showFpsEnabled)
                        .setDefaultValue(true)
                        .setImpact(OptionImpact.LOW))
                .addOption(builder.createBooleanOption(ResourceLocation.fromNamespaceAndPath(Reggadium.MODID, "show_fps_extended"))
                        .setName(Component.translatable("reggadium.extra.show_fps_extended.name"))
                        .setTooltip(Component.translatable("reggadium.extra.show_fps_extended.tooltip"))
                        .setStorageHandler(Config::save)
                        .setBinding(val -> { Config.SHOW_FPS_EXTENDED.set(val); Config.showFpsExtendedEnabled = val; ExtraSettings.showFpsExtended = val; }, () -> Config.showFpsExtendedEnabled)
                        .setDefaultValue(false)
                        .setImpact(OptionImpact.LOW))
                .addOption(builder.createBooleanOption(ResourceLocation.fromNamespaceAndPath(Reggadium.MODID, "show_coords"))
                        .setName(Component.translatable("reggadium.extra.show_coords.name"))
                        .setTooltip(Component.translatable("reggadium.extra.show_coords.tooltip"))
                        .setStorageHandler(Config::save)
                        .setBinding(val -> { Config.SHOW_COORDS.set(val); Config.showCoordsEnabled = val; ExtraSettings.showCoords = val; }, () -> Config.showCoordsEnabled)
                        .setDefaultValue(false)
                        .setImpact(OptionImpact.LOW))
                .addOption(builder.createBooleanOption(ResourceLocation.fromNamespaceAndPath(Reggadium.MODID, "show_facing"))
                        .setName(Component.translatable("reggadium.extra.show_facing.name"))
                        .setTooltip(Component.translatable("reggadium.extra.show_facing.tooltip"))
                        .setStorageHandler(Config::save)
                        .setBinding(val -> { Config.SHOW_FACING.set(val); Config.showFacingEnabled = val; ExtraSettings.showFacing = val; }, () -> Config.showFacingEnabled)
                        .setDefaultValue(false)
                        .setImpact(OptionImpact.LOW))
                .addOption(builder.createBooleanOption(ResourceLocation.fromNamespaceAndPath(Reggadium.MODID, "show_biome"))
                        .setName(Component.translatable("reggadium.extra.show_biome.name"))
                        .setTooltip(Component.translatable("reggadium.extra.show_biome.tooltip"))
                        .setStorageHandler(Config::save)
                        .setBinding(val -> { Config.SHOW_BIOME.set(val); Config.showBiomeEnabled = val; ExtraSettings.showBiome = val; }, () -> Config.showBiomeEnabled)
                        .setDefaultValue(false)
                        .setImpact(OptionImpact.LOW))
                .addOption(builder.createBooleanOption(ResourceLocation.fromNamespaceAndPath(Reggadium.MODID, "show_memory"))
                        .setName(Component.translatable("reggadium.extra.show_memory.name"))
                        .setTooltip(Component.translatable("reggadium.extra.show_memory.tooltip"))
                        .setStorageHandler(Config::save)
                        .setBinding(val -> { Config.SHOW_MEMORY.set(val); Config.showMemoryEnabled = val; ExtraSettings.showMemory = val; }, () -> Config.showMemoryEnabled)
                        .setDefaultValue(false)
                        .setImpact(OptionImpact.LOW))
                .addOption(builder.createBooleanOption(ResourceLocation.fromNamespaceAndPath(Reggadium.MODID, "show_cpu"))
                        .setName(Component.translatable("reggadium.extra.show_cpu.name"))
                        .setTooltip(Component.translatable("reggadium.extra.show_cpu.tooltip"))
                        .setStorageHandler(Config::save)
                        .setBinding(val -> { Config.SHOW_CPU.set(val); Config.showCpuEnabled = val; ExtraSettings.showCpu = val; }, () -> Config.showCpuEnabled)
                        .setDefaultValue(false)
                        .setImpact(OptionImpact.LOW))
                .addOption(builder.createBooleanOption(ResourceLocation.fromNamespaceAndPath(Reggadium.MODID, "show_gpu"))
                        .setName(Component.translatable("reggadium.extra.show_gpu.name"))
                        .setTooltip(Component.translatable("reggadium.extra.show_gpu.tooltip"))
                        .setStorageHandler(Config::save)
                        .setBinding(val -> { Config.SHOW_GPU.set(val); Config.showGpuEnabled = val; ExtraSettings.showGpu = val; }, () -> Config.showGpuEnabled)
                        .setDefaultValue(false)
                        .setImpact(OptionImpact.LOW))
                .addOption(builder.createBooleanOption(ResourceLocation.fromNamespaceAndPath(Reggadium.MODID, "show_gametime"))
                        .setName(Component.translatable("reggadium.extra.show_gametime.name"))
                        .setTooltip(Component.translatable("reggadium.extra.show_gametime.tooltip"))
                        .setStorageHandler(Config::save)
                        .setBinding(val -> { Config.SHOW_GAME_TIME.set(val); Config.showGameTimeEnabled = val; ExtraSettings.showGameTime = val; }, () -> Config.showGameTimeEnabled)
                        .setDefaultValue(false)
                        .setImpact(OptionImpact.LOW))
                .addOption(builder.createBooleanOption(ResourceLocation.fromNamespaceAndPath(Reggadium.MODID, "show_ping"))
                        .setName(Component.translatable("reggadium.extra.show_ping.name"))
                        .setTooltip(Component.translatable("reggadium.extra.show_ping.tooltip"))
                        .setStorageHandler(Config::save)
                        .setBinding(val -> { Config.SHOW_PING.set(val); Config.showPingEnabled = val; ExtraSettings.showPing = val; }, () -> Config.showPingEnabled)
                        .setDefaultValue(false)
                        .setImpact(OptionImpact.LOW));

        page.addOptionGroup(metricsGroup);

        // Extra HUD Layout & Styling
        OptionGroupBuilder layoutGroup = builder.createOptionGroup()
                .setName(Component.translatable("reggadium.options.group.extra_hud_layout"))
                .addOption(builder.createEnumOption(ResourceLocation.fromNamespaceAndPath(Reggadium.MODID, "overlay_corner"), OverlayCorner.class)
                        .setName(Component.translatable("reggadium.extra.overlay_corner.name"))
                        .setTooltip(Component.translatable("reggadium.extra.overlay_corner.tooltip"))
                        .setElementNameProvider(OverlayCorner::getDisplayName)
                        .setStorageHandler(Config::save)
                        .setBinding(val -> { Config.OVERLAY_CORNER.set(val); Config.overlayCorner = val; ExtraSettings.overlayCorner = val; }, () -> Config.overlayCorner)
                        .setDefaultValue(OverlayCorner.TOP_LEFT)
                        .setImpact(OptionImpact.LOW))
                .addOption(builder.createIntegerOption(ResourceLocation.fromNamespaceAndPath(Reggadium.MODID, "hud_scale"))
                        .setName(Component.translatable("reggadium.extra.hud_scale.name"))
                        .setTooltip(Component.translatable("reggadium.extra.hud_scale.tooltip"))
                        .setRange(50, 200, 5)
                        .setValueFormatter(val -> Component.literal(val + "%"))
                        .setStorageHandler(Config::save)
                        .setBinding(val -> { Config.HUD_SCALE.set(val); Config.hudScale = val; ExtraSettings.hudScale = val; }, () -> Config.hudScale)
                        .setDefaultValue(100)
                        .setImpact(OptionImpact.LOW))
                .addOption(builder.createEnumOption(ResourceLocation.fromNamespaceAndPath(Reggadium.MODID, "text_contrast"), TextContrast.class)
                        .setName(Component.translatable("reggadium.extra.text_contrast.name"))
                        .setTooltip(Component.translatable("reggadium.extra.text_contrast.tooltip"))
                        .setElementNameProvider(TextContrast::getDisplayName)
                        .setStorageHandler(Config::save)
                        .setBinding(val -> { Config.TEXT_CONTRAST.set(val); Config.textContrast = val; ExtraSettings.textContrast = val; }, () -> Config.textContrast)
                        .setDefaultValue(TextContrast.SHADOW)
                        .setImpact(OptionImpact.LOW))
                .addOption(builder.createEnumOption(ResourceLocation.fromNamespaceAndPath(Reggadium.MODID, "background_opacity"), BackgroundOpacity.class)
                        .setName(Component.translatable("reggadium.extra.background_opacity.name"))
                        .setTooltip(Component.translatable("reggadium.extra.background_opacity.tooltip"))
                        .setElementNameProvider(BackgroundOpacity::getDisplayName)
                        .setStorageHandler(Config::save)
                        .setBinding(val -> { Config.BACKGROUND_OPACITY.set(val); Config.backgroundOpacity = val; ExtraSettings.backgroundOpacity = val; }, () -> Config.backgroundOpacity)
                        .setDefaultValue(BackgroundOpacity.MEDIUM)
                        .setImpact(OptionImpact.LOW))
                .addOption(builder.createEnumOption(ResourceLocation.fromNamespaceAndPath(Reggadium.MODID, "coord_precision"), CoordPrecision.class)
                        .setName(Component.translatable("reggadium.extra.coord_precision.name"))
                        .setTooltip(Component.translatable("reggadium.extra.coord_precision.tooltip"))
                        .setElementNameProvider(CoordPrecision::getDisplayName)
                        .setStorageHandler(Config::save)
                        .setBinding(val -> { Config.COORD_PRECISION.set(val); Config.coordPrecision = val; ExtraSettings.coordPrecision = val; }, () -> Config.coordPrecision)
                        .setDefaultValue(CoordPrecision.TWO_DECIMALS)
                        .setImpact(OptionImpact.LOW));

        page.addOptionGroup(layoutGroup);

        // Extra HUD Colors
        OptionGroupBuilder colorsGroup = builder.createOptionGroup()
                .setName(Component.translatable("reggadium.options.group.extra_hud_colors"))
                .addOption(createColorOption(builder, "fps_color", "reggadium.extra.color.fps.name", "reggadium.extra.color.fps.tooltip",
                        val -> { Config.FPS_COLOR.set(val); Config.fpsColor = val; ExtraSettings.fpsColor = val; }, () -> Config.fpsColor, HudColor.DYNAMIC))
                .addOption(createColorOption(builder, "coords_color", "reggadium.extra.color.coords.name", "reggadium.extra.color.coords.tooltip",
                        val -> { Config.COORDS_COLOR.set(val); Config.coordsColor = val; ExtraSettings.coordsColor = val; }, () -> Config.coordsColor, HudColor.WHITE))
                .addOption(createColorOption(builder, "facing_color", "reggadium.extra.color.facing.name", "reggadium.extra.color.facing.tooltip",
                        val -> { Config.FACING_COLOR.set(val); Config.facingColor = val; ExtraSettings.facingColor = val; }, () -> Config.facingColor, HudColor.GRAY))
                .addOption(createColorOption(builder, "biome_color", "reggadium.extra.color.biome.name", "reggadium.extra.color.biome.tooltip",
                        val -> { Config.BIOME_COLOR.set(val); Config.biomeColor = val; ExtraSettings.biomeColor = val; }, () -> Config.biomeColor, HudColor.CYAN))
                .addOption(createColorOption(builder, "memory_color", "reggadium.extra.color.memory.name", "reggadium.extra.color.memory.tooltip",
                        val -> { Config.MEMORY_COLOR.set(val); Config.memoryColor = val; ExtraSettings.memoryColor = val; }, () -> Config.memoryColor, HudColor.DYNAMIC))
                .addOption(createColorOption(builder, "cpu_color", "reggadium.extra.color.cpu.name", "reggadium.extra.color.cpu.tooltip",
                        val -> { Config.CPU_COLOR.set(val); Config.cpuColor = val; ExtraSettings.cpuColor = val; }, () -> Config.cpuColor, HudColor.DYNAMIC))
                .addOption(createColorOption(builder, "gpu_color", "reggadium.extra.color.gpu.name", "reggadium.extra.color.gpu.tooltip",
                        val -> { Config.GPU_COLOR.set(val); Config.gpuColor = val; ExtraSettings.gpuColor = val; }, () -> Config.gpuColor, HudColor.DYNAMIC))
                .addOption(createColorOption(builder, "time_color", "reggadium.extra.color.time.name", "reggadium.extra.color.time.tooltip",
                        val -> { Config.TIME_COLOR.set(val); Config.timeColor = val; ExtraSettings.timeColor = val; }, () -> Config.timeColor, HudColor.GOLD))
                .addOption(createColorOption(builder, "ping_color", "reggadium.extra.color.ping.name", "reggadium.extra.color.ping.tooltip",
                        val -> { Config.PING_COLOR.set(val); Config.pingColor = val; ExtraSettings.pingColor = val; }, () -> Config.pingColor, HudColor.DYNAMIC));

        page.addOptionGroup(colorsGroup);

        // System Settings
        OptionGroupBuilder systemGroup = builder.createOptionGroup()
                .setName(Component.translatable("reggadium.options.group.system"))
                .addOption(builder.createBooleanOption(ResourceLocation.fromNamespaceAndPath(Reggadium.MODID, "enable_optimizations"))
                        .setName(Component.translatable("reggadium.options.enable_optimizations.name"))
                        .setTooltip(Component.translatable("reggadium.options.enable_optimizations.tooltip"))
                        .setStorageHandler(Config::save)
                        .setBinding(val -> { Config.ENABLE_OPTIMIZATIONS.set(val); Config.optimizationsEnabled = val; }, () -> Config.optimizationsEnabled)
                        .setDefaultValue(true)
                        .setImpact(OptionImpact.HIGH));

        page.addOptionGroup(systemGroup);

        return page;
    }

    private static EnumOptionBuilder<HudColor> createColorOption(
            ConfigBuilder builder,
            String id,
            String nameKey,
            String tooltipKey,
            Consumer<HudColor> setter,
            Supplier<HudColor> getter,
            HudColor defaultValue
    ) {
        return builder.createEnumOption(ResourceLocation.fromNamespaceAndPath(Reggadium.MODID, id), HudColor.class)
                .setName(Component.translatable(nameKey))
                .setTooltip(Component.translatable(tooltipKey))
                .setElementNameProvider(HudColor::getDisplayName)
                .setStorageHandler(Config::save)
                .setBinding(setter::accept, getter::get)
                .setDefaultValue(defaultValue)
                .setImpact(OptionImpact.LOW);
    }
}
