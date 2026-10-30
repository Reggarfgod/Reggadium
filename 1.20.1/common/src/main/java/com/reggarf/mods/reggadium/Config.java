package com.reggarf.mods.reggadium;

import com.reggarf.mods.reggadium.config.ConfigValue;
import com.reggarf.mods.reggadium.extra.BackgroundOpacity;
import com.reggarf.mods.reggadium.extra.CoordPrecision;
import com.reggarf.mods.reggadium.extra.ExtraSettings;
import com.reggarf.mods.reggadium.extra.HudColor;
import com.reggarf.mods.reggadium.extra.OverlayCorner;
import com.reggarf.mods.reggadium.extra.TextContrast;

public class Config {

    // General Category
    public static ConfigValue<Boolean> ENABLE_OPTIMIZATIONS = ConfigValue.of(true);
    public static ConfigValue<Boolean> SHOW_FPS = ConfigValue.of(true);

    // Extra / HUD Category
    public static ConfigValue<Boolean> ENABLE_EXTRA_HUD = ConfigValue.of(true);
    public static ConfigValue<Boolean> SHOW_FPS_EXTENDED = ConfigValue.of(false);
    public static ConfigValue<Boolean> SHOW_COORDS = ConfigValue.of(false);
    public static ConfigValue<Boolean> SHOW_FACING = ConfigValue.of(false);
    public static ConfigValue<Boolean> SHOW_BIOME = ConfigValue.of(false);
    public static ConfigValue<Boolean> SHOW_MEMORY = ConfigValue.of(false);
    public static ConfigValue<Boolean> SHOW_CPU = ConfigValue.of(false);
    public static ConfigValue<Boolean> SHOW_GPU = ConfigValue.of(false);
    public static ConfigValue<Boolean> SHOW_GAME_TIME = ConfigValue.of(false);
    public static ConfigValue<Boolean> SHOW_PING = ConfigValue.of(false);

    public static ConfigValue<OverlayCorner> OVERLAY_CORNER = ConfigValue.of(OverlayCorner.TOP_LEFT);
    public static ConfigValue<Integer> HUD_SCALE = ConfigValue.of(100);
    public static ConfigValue<TextContrast> TEXT_CONTRAST = ConfigValue.of(TextContrast.SHADOW);
    public static ConfigValue<BackgroundOpacity> BACKGROUND_OPACITY = ConfigValue.of(BackgroundOpacity.MEDIUM);
    public static ConfigValue<CoordPrecision> COORD_PRECISION = ConfigValue.of(CoordPrecision.TWO_DECIMALS);

    public static ConfigValue<HudColor> FPS_COLOR = ConfigValue.of(HudColor.DYNAMIC);
    public static ConfigValue<HudColor> COORDS_COLOR = ConfigValue.of(HudColor.WHITE);
    public static ConfigValue<HudColor> FACING_COLOR = ConfigValue.of(HudColor.GRAY);
    public static ConfigValue<HudColor> BIOME_COLOR = ConfigValue.of(HudColor.CYAN);
    public static ConfigValue<HudColor> MEMORY_COLOR = ConfigValue.of(HudColor.DYNAMIC);
    public static ConfigValue<HudColor> CPU_COLOR = ConfigValue.of(HudColor.DYNAMIC);
    public static ConfigValue<HudColor> GPU_COLOR = ConfigValue.of(HudColor.DYNAMIC);
    public static ConfigValue<HudColor> TIME_COLOR = ConfigValue.of(HudColor.GOLD);
    public static ConfigValue<HudColor> PING_COLOR = ConfigValue.of(HudColor.DYNAMIC);

    // Performance Category
    public static ConfigValue<Boolean> FAST_SUPER_BYTE_BUFFER = ConfigValue.of(true);
    public static ConfigValue<Boolean> BACKFACE_CULLING = ConfigValue.of(true);
    public static ConfigValue<Boolean> FAST_LIGHT_CACHE = ConfigValue.of(true);
    public static ConfigValue<Boolean> CULL_BELTS = ConfigValue.of(true);
    public static ConfigValue<Boolean> KINETIC_CULLING = ConfigValue.of(true);
    public static ConfigValue<Boolean> SKIP_STATIONARY_KINETICS = ConfigValue.of(true);
    public static ConfigValue<Boolean> KINETIC_AUDIO_THROTTLING = ConfigValue.of(true);
    public static ConfigValue<Boolean> KINETIC_LOD = ConfigValue.of(true);
    public static ConfigValue<Boolean> CULL_BLOCK_ENTITIES = ConfigValue.of(true);
    public static ConfigValue<Boolean> CULL_PARTICLES = ConfigValue.of(true);
    public static ConfigValue<Boolean> REDUCE_BLOCK_ENTITY_LAG = ConfigValue.of(true);
    public static ConfigValue<Boolean> CULL_ENTITIES = ConfigValue.of(true);
    public static ConfigValue<Boolean> CULL_SHADOWS = ConfigValue.of(true);
    public static ConfigValue<Boolean> CREATE_FX_CULL = ConfigValue.of(true);
    public static ConfigValue<Boolean> AGGRESSIVE_TICK_SLEEP = ConfigValue.of(true);

    // Memory & Leak Mitigation Category
    public static ConfigValue<Boolean> FIX_MEMORY_LEAKS = ConfigValue.of(true);
    public static ConfigValue<Boolean> FIX_CLIENT_LEVEL_LEAK = ConfigValue.of(true);
    public static ConfigValue<Boolean> FIX_FAKE_PLAYER_LEAK = ConfigValue.of(true);
    public static ConfigValue<Boolean> FIX_DEBUG_SUBSCRIPTION_LEAK = ConfigValue.of(true);

    // Quality Category
    public static ConfigValue<Boolean> OPTIMIZE_CONTRAPTIONS = ConfigValue.of(true);

    // Advanced Category
    public static ConfigValue<Boolean> OPTIMIZE_ENTITY_RENDERING = ConfigValue.of(true);

    // Fast-access primitive flags
    public static boolean optimizationsEnabled = true;
    public static boolean showFpsEnabled = true;
    public static boolean extraHudEnabled = true;

    public static boolean showFpsExtendedEnabled = false;
    public static boolean showCoordsEnabled = false;
    public static boolean showFacingEnabled = false;
    public static boolean showBiomeEnabled = false;
    public static boolean showMemoryEnabled = false;
    public static boolean showCpuEnabled = false;
    public static boolean showGpuEnabled = false;
    public static boolean showGameTimeEnabled = false;
    public static boolean showPingEnabled = false;

    public static OverlayCorner overlayCorner = OverlayCorner.TOP_LEFT;
    public static int hudScale = 100;
    public static TextContrast textContrast = TextContrast.SHADOW;
    public static BackgroundOpacity backgroundOpacity = BackgroundOpacity.MEDIUM;
    public static CoordPrecision coordPrecision = CoordPrecision.TWO_DECIMALS;

    public static HudColor fpsColor = HudColor.DYNAMIC;
    public static HudColor coordsColor = HudColor.WHITE;
    public static HudColor facingColor = HudColor.GRAY;
    public static HudColor biomeColor = HudColor.CYAN;
    public static HudColor memoryColor = HudColor.DYNAMIC;
    public static HudColor cpuColor = HudColor.DYNAMIC;
    public static HudColor gpuColor = HudColor.DYNAMIC;
    public static HudColor timeColor = HudColor.GOLD;
    public static HudColor pingColor = HudColor.DYNAMIC;

    public static boolean fastSbbEnabled = true;
    public static boolean backfaceCullingEnabled = true;
    public static boolean fastLightCacheEnabled = true;
    public static boolean cullBeltsEnabled = true;
    public static boolean kineticCullingEnabled = true;
    public static boolean skipStationaryKineticsEnabled = true;
    public static boolean kineticAudioThrottlingEnabled = true;
    public static boolean kineticLodEnabled = true;
    public static boolean cullBlockEntitiesEnabled = true;
    public static boolean cullParticlesEnabled = true;
    public static boolean reduceBlockEntityLagEnabled = true;
    public static boolean cullEntitiesEnabled = true;
    public static boolean cullShadowsEnabled = true;
    public static boolean createFxCullEnabled = true;
    public static boolean aggressiveTickSleepEnabled = true;

    public static boolean fixMemoryLeaksEnabled = true;
    public static boolean fixClientLevelLeakEnabled = true;
    public static boolean fixFakePlayerLeakEnabled = true;
    public static boolean fixDebugSubscriptionLeakEnabled = true;

    public static boolean optimizeContraptionsEnabled = true;
    public static boolean optimizeEntityRenderingEnabled = true;

    public static Runnable saveHandler = () -> {};

    public static void save() {
        try {
            saveHandler.run();
        } catch (Throwable ignored) {
        }
    }

    public static void sync() {
        optimizationsEnabled = ENABLE_OPTIMIZATIONS.get();
        showFpsEnabled = SHOW_FPS.get();
        extraHudEnabled = ENABLE_EXTRA_HUD.get();

        showFpsExtendedEnabled = SHOW_FPS_EXTENDED.get();
        showCoordsEnabled = SHOW_COORDS.get();
        showFacingEnabled = SHOW_FACING.get();
        showBiomeEnabled = SHOW_BIOME.get();
        showMemoryEnabled = SHOW_MEMORY.get();
        showCpuEnabled = SHOW_CPU.get();
        showGpuEnabled = SHOW_GPU.get();
        showGameTimeEnabled = SHOW_GAME_TIME.get();
        showPingEnabled = SHOW_PING.get();

        overlayCorner = OVERLAY_CORNER.get();
        hudScale = HUD_SCALE.get();
        textContrast = TEXT_CONTRAST.get();
        backgroundOpacity = BACKGROUND_OPACITY.get();
        coordPrecision = COORD_PRECISION.get();

        fpsColor = FPS_COLOR.get();
        coordsColor = COORDS_COLOR.get();
        facingColor = FACING_COLOR.get();
        biomeColor = BIOME_COLOR.get();
        memoryColor = MEMORY_COLOR.get();
        cpuColor = CPU_COLOR.get();
        gpuColor = GPU_COLOR.get();
        timeColor = TIME_COLOR.get();
        pingColor = PING_COLOR.get();

        ExtraSettings.enabled = extraHudEnabled;
        ExtraSettings.showFps = showFpsEnabled;
        ExtraSettings.showFpsExtended = showFpsExtendedEnabled;
        ExtraSettings.showCoords = showCoordsEnabled;
        ExtraSettings.showFacing = showFacingEnabled;
        ExtraSettings.showBiome = showBiomeEnabled;
        ExtraSettings.showMemory = showMemoryEnabled;
        ExtraSettings.showCpu = showCpuEnabled;
        ExtraSettings.showGpu = showGpuEnabled;
        ExtraSettings.showGameTime = showGameTimeEnabled;
        ExtraSettings.showPing = showPingEnabled;

        ExtraSettings.overlayCorner = overlayCorner;
        ExtraSettings.hudScale = hudScale;
        ExtraSettings.textContrast = textContrast;
        ExtraSettings.backgroundOpacity = backgroundOpacity;
        ExtraSettings.coordPrecision = coordPrecision;

        ExtraSettings.fpsColor = fpsColor;
        ExtraSettings.coordsColor = coordsColor;
        ExtraSettings.facingColor = facingColor;
        ExtraSettings.biomeColor = biomeColor;
        ExtraSettings.memoryColor = memoryColor;
        ExtraSettings.cpuColor = cpuColor;
        ExtraSettings.gpuColor = gpuColor;
        ExtraSettings.timeColor = timeColor;
        ExtraSettings.pingColor = pingColor;

        fastSbbEnabled = FAST_SUPER_BYTE_BUFFER.get();
        backfaceCullingEnabled = BACKFACE_CULLING.get();
        fastLightCacheEnabled = FAST_LIGHT_CACHE.get();
        cullBeltsEnabled = CULL_BELTS.get();
        kineticCullingEnabled = KINETIC_CULLING.get();
        skipStationaryKineticsEnabled = SKIP_STATIONARY_KINETICS.get();
        kineticAudioThrottlingEnabled = KINETIC_AUDIO_THROTTLING.get();
        kineticLodEnabled = KINETIC_LOD.get();
        cullBlockEntitiesEnabled = CULL_BLOCK_ENTITIES.get();
        cullParticlesEnabled = CULL_PARTICLES.get();
        reduceBlockEntityLagEnabled = REDUCE_BLOCK_ENTITY_LAG.get();
        cullEntitiesEnabled = CULL_ENTITIES.get();
        cullShadowsEnabled = CULL_SHADOWS.get();
        createFxCullEnabled = CREATE_FX_CULL.get();
        aggressiveTickSleepEnabled = AGGRESSIVE_TICK_SLEEP.get();

        fixMemoryLeaksEnabled = FIX_MEMORY_LEAKS.get();
        fixClientLevelLeakEnabled = FIX_CLIENT_LEVEL_LEAK.get();
        fixFakePlayerLeakEnabled = FIX_FAKE_PLAYER_LEAK.get();
        fixDebugSubscriptionLeakEnabled = FIX_DEBUG_SUBSCRIPTION_LEAK.get();

        optimizeContraptionsEnabled = OPTIMIZE_CONTRAPTIONS.get();
        optimizeEntityRenderingEnabled = OPTIMIZE_ENTITY_RENDERING.get();
    }
}
