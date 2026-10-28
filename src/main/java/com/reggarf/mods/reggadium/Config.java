package com.reggarf.mods.reggadium;

import com.reggarf.mods.reggadium.extra.BackgroundOpacity;
import com.reggarf.mods.reggadium.extra.CoordPrecision;
import com.reggarf.mods.reggadium.extra.ExtraSettings;
import com.reggarf.mods.reggadium.extra.HudColor;
import com.reggarf.mods.reggadium.extra.OverlayCorner;
import com.reggarf.mods.reggadium.extra.TextContrast;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = Reggadium.MODID)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // General Category
    public static final ModConfigSpec.BooleanValue ENABLE_OPTIMIZATIONS;
    public static final ModConfigSpec.BooleanValue SHOW_FPS;

    // Extra / HUD Category
    public static final ModConfigSpec.BooleanValue SHOW_FPS_EXTENDED;
    public static final ModConfigSpec.BooleanValue SHOW_COORDS;
    public static final ModConfigSpec.BooleanValue SHOW_FACING;
    public static final ModConfigSpec.BooleanValue SHOW_BIOME;
    public static final ModConfigSpec.BooleanValue SHOW_MEMORY;
    public static final ModConfigSpec.BooleanValue SHOW_CPU;
    public static final ModConfigSpec.BooleanValue SHOW_GPU;
    public static final ModConfigSpec.BooleanValue SHOW_GAME_TIME;
    public static final ModConfigSpec.BooleanValue SHOW_PING;

    public static final ModConfigSpec.EnumValue<OverlayCorner> OVERLAY_CORNER;
    public static final ModConfigSpec.IntValue HUD_SCALE;
    public static final ModConfigSpec.EnumValue<TextContrast> TEXT_CONTRAST;
    public static final ModConfigSpec.EnumValue<BackgroundOpacity> BACKGROUND_OPACITY;
    public static final ModConfigSpec.EnumValue<CoordPrecision> COORD_PRECISION;

    public static final ModConfigSpec.EnumValue<HudColor> FPS_COLOR;
    public static final ModConfigSpec.EnumValue<HudColor> COORDS_COLOR;
    public static final ModConfigSpec.EnumValue<HudColor> FACING_COLOR;
    public static final ModConfigSpec.EnumValue<HudColor> BIOME_COLOR;
    public static final ModConfigSpec.EnumValue<HudColor> MEMORY_COLOR;
    public static final ModConfigSpec.EnumValue<HudColor> CPU_COLOR;
    public static final ModConfigSpec.EnumValue<HudColor> GPU_COLOR;
    public static final ModConfigSpec.EnumValue<HudColor> TIME_COLOR;
    public static final ModConfigSpec.EnumValue<HudColor> PING_COLOR;

    // Performance Category
    public static final ModConfigSpec.BooleanValue FAST_SUPER_BYTE_BUFFER;
    public static final ModConfigSpec.BooleanValue BACKFACE_CULLING;
    public static final ModConfigSpec.BooleanValue FAST_LIGHT_CACHE;
    public static final ModConfigSpec.BooleanValue CULL_BELTS;
    public static final ModConfigSpec.BooleanValue KINETIC_CULLING;
    public static final ModConfigSpec.BooleanValue SKIP_STATIONARY_KINETICS;
    public static final ModConfigSpec.BooleanValue KINETIC_AUDIO_THROTTLING;
    public static final ModConfigSpec.BooleanValue KINETIC_LOD;
    public static final ModConfigSpec.BooleanValue CULL_BLOCK_ENTITIES;
    public static final ModConfigSpec.BooleanValue CULL_PARTICLES;
    public static final ModConfigSpec.BooleanValue REDUCE_BLOCK_ENTITY_LAG;

    // Quality Category
    public static final ModConfigSpec.BooleanValue OPTIMIZE_CONTRAPTIONS;

    // Advanced Category
    public static final ModConfigSpec.BooleanValue OPTIMIZE_ENTITY_RENDERING;

    static {
        BUILDER.push("general");
        ENABLE_OPTIMIZATIONS = BUILDER
                .comment("Enable global Reggadium performance optimizations")
                .define("enableOptimizations", true);

        SHOW_FPS = BUILDER
                .comment("Display a real-time in-game FPS counter on the screen")
                .define("showFps", true);
        BUILDER.pop();

        BUILDER.push("extra");
        SHOW_FPS_EXTENDED = BUILDER
                .comment("Display extended FPS metrics (lowest, average, highest framerate)")
                .define("showFpsExtended", false);

        SHOW_COORDS = BUILDER
                .comment("Display player XYZ coordinates on screen")
                .define("showCoords", false);

        SHOW_FACING = BUILDER
                .comment("Display player looking direction and axis")
                .define("showFacing", false);

        SHOW_BIOME = BUILDER
                .comment("Display current biome name on screen")
                .define("showBiome", false);

        SHOW_MEMORY = BUILDER
                .comment("Display current memory / RAM allocation on screen")
                .define("showMemory", false);

        SHOW_CPU = BUILDER
                .comment("Display real-time CPU utilization percentage on screen")
                .define("showCpu", false);

        SHOW_GPU = BUILDER
                .comment("Display real-time graphics card GPU utilization percentage on screen")
                .define("showGpu", false);

        SHOW_GAME_TIME = BUILDER
                .comment("Display current in-game time and day counter on screen")
                .define("showGameTime", false);

        SHOW_PING = BUILDER
                .comment("Display network latency / ping on screen")
                .define("showPing", false);

        OVERLAY_CORNER = BUILDER
                .comment("Screen corner to anchor the HUD overlay")
                .defineEnum("overlayCorner", OverlayCorner.TOP_LEFT);

        HUD_SCALE = BUILDER
                .comment("Scale percentage of the HUD overlay (50% to 200%)")
                .defineInRange("hudScale", 100, 50, 200);

        TEXT_CONTRAST = BUILDER
                .comment("Text contrast mode for the HUD overlay (None, Shadow, Background)")
                .defineEnum("textContrast", TextContrast.SHADOW);

        BACKGROUND_OPACITY = BUILDER
                .comment("Opacity of the HUD background box when contrast mode is Background")
                .defineEnum("backgroundOpacity", BackgroundOpacity.MEDIUM);

        COORD_PRECISION = BUILDER
                .comment("Decimal precision for XYZ coordinates display")
                .defineEnum("coordPrecision", CoordPrecision.TWO_DECIMALS);

        FPS_COLOR = BUILDER
                .comment("Color for FPS overlay text")
                .defineEnum("fpsColor", HudColor.DYNAMIC);

        COORDS_COLOR = BUILDER
                .comment("Color for Coordinates overlay text")
                .defineEnum("coordsColor", HudColor.WHITE);

        FACING_COLOR = BUILDER
                .comment("Color for Facing Direction overlay text")
                .defineEnum("facingColor", HudColor.GRAY);

        BIOME_COLOR = BUILDER
                .comment("Color for Biome overlay text")
                .defineEnum("biomeColor", HudColor.CYAN);

        MEMORY_COLOR = BUILDER
                .comment("Color for Memory / RAM overlay text")
                .defineEnum("memoryColor", HudColor.DYNAMIC);

        CPU_COLOR = BUILDER
                .comment("Color for CPU usage overlay text")
                .defineEnum("cpuColor", HudColor.DYNAMIC);

        GPU_COLOR = BUILDER
                .comment("Color for GPU usage overlay text")
                .defineEnum("gpuColor", HudColor.DYNAMIC);

        TIME_COLOR = BUILDER
                .comment("Color for In-Game Time overlay text")
                .defineEnum("timeColor", HudColor.GOLD);

        PING_COLOR = BUILDER
                .comment("Color for Latency / Ping overlay text")
                .defineEnum("pingColor", HudColor.DYNAMIC);
        BUILDER.pop();

        BUILDER.push("performance");
        FAST_SUPER_BYTE_BUFFER = BUILDER
                .comment("Accelerate Create SuperByteBuffer vertex operations and memory writes using native format buffers")
                .define("fastSuperByteBuffer", true);

        BACKFACE_CULLING = BUILDER
                .comment("Enable perspective frustum backface culling for Create moving contraptions and kinetic blocks")
                .define("backfaceCulling", true);

        FAST_LIGHT_CACHE = BUILDER
                .comment("Cache light queries for rotating kinetic blocks and level lighting calculations to reduce draw call overhead")
                .define("fastLightCache", true);

        CULL_BELTS = BUILDER
                .comment("Cull off-screen belts and transported items to drastically increase factory FPS")
                .define("cullBelts", true);

        KINETIC_CULLING = BUILDER
                .comment("Cull off-screen and distant rotating kinetic block entities")
                .define("kineticCulling", true);

        SKIP_STATIONARY_KINETICS = BUILDER
                .comment("Skip rotational transforms for stationary or stopped kinetic machines")
                .define("skipStationaryKinetics", true);

        KINETIC_AUDIO_THROTTLING = BUILDER
                .comment("Distance-cull and rate-throttle soundscape audio for running kinetic machinery")
                .define("kineticAudioThrottling", true);

        KINETIC_LOD = BUILDER
                .comment("Dynamic Level-of-Detail for rotating components at a distance to eliminate vertex transformation jitter")
                .define("kineticLod", true);

        CULL_BLOCK_ENTITIES = BUILDER
                .comment("Frustum cull off-screen block entities (chests, signs, machines, banners)")
                .define("cullBlockEntities", true);

        CULL_PARTICLES = BUILDER
                .comment("Frustum cull off-screen particles to reduce fill rate and vertex load")
                .define("cullParticles", true);

        REDUCE_BLOCK_ENTITY_LAG = BUILDER
                .comment("Reduce kinetic block entity overhead when outside visible view or frustum")
                .define("reduceBlockEntityLag", true);
        BUILDER.pop();

        BUILDER.push("quality");
        OPTIMIZE_CONTRAPTIONS = BUILDER
                .comment("Optimize and LOD cull distant moving contraption actors and detailed parts")
                .define("optimizeContraptions", true);
        BUILDER.pop();

        BUILDER.push("advanced");
        OPTIMIZE_ENTITY_RENDERING = BUILDER
                .comment("Optimize entity and block entity vertex formatting pipelines")
                .define("optimizeEntityRendering", true);
        BUILDER.pop();
    }

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean optimizationsEnabled = true;
    public static boolean showFpsEnabled = true;

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

    public static boolean optimizeContraptionsEnabled = true;

    public static boolean optimizeEntityRenderingEnabled = true;

    public static void save() {
        try {
            if (SPEC.isLoaded()) {
                SPEC.save();
            }
        } catch (Throwable ignored) {
        }
    }

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event) {
        optimizationsEnabled = ENABLE_OPTIMIZATIONS.get();
        showFpsEnabled = SHOW_FPS.get();

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

        // Sync with ExtraSettings
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

        optimizeContraptionsEnabled = OPTIMIZE_CONTRAPTIONS.get();

        optimizeEntityRenderingEnabled = OPTIMIZE_ENTITY_RENDERING.get();
    }
}
