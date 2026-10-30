package com.reggarf.mods.reggadium;

import com.reggarf.mods.reggadium.config.ConfigValue;
import com.reggarf.mods.reggadium.extra.BackgroundOpacity;
import com.reggarf.mods.reggadium.extra.CoordPrecision;
import com.reggarf.mods.reggadium.extra.HudColor;
import com.reggarf.mods.reggadium.extra.OverlayCorner;
import com.reggarf.mods.reggadium.extra.TextContrast;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = Reggadium.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConfigForge {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue ENABLE_OPTIMIZATIONS;
    public static final ForgeConfigSpec.BooleanValue SHOW_FPS;
    public static final ForgeConfigSpec.BooleanValue ENABLE_EXTRA_HUD;
    public static final ForgeConfigSpec.BooleanValue SHOW_FPS_EXTENDED;
    public static final ForgeConfigSpec.BooleanValue SHOW_COORDS;
    public static final ForgeConfigSpec.BooleanValue SHOW_FACING;
    public static final ForgeConfigSpec.BooleanValue SHOW_BIOME;
    public static final ForgeConfigSpec.BooleanValue SHOW_MEMORY;
    public static final ForgeConfigSpec.BooleanValue SHOW_CPU;
    public static final ForgeConfigSpec.BooleanValue SHOW_GPU;
    public static final ForgeConfigSpec.BooleanValue SHOW_GAME_TIME;
    public static final ForgeConfigSpec.BooleanValue SHOW_PING;

    public static final ForgeConfigSpec.EnumValue<OverlayCorner> OVERLAY_CORNER;
    public static final ForgeConfigSpec.IntValue HUD_SCALE;
    public static final ForgeConfigSpec.EnumValue<TextContrast> TEXT_CONTRAST;
    public static final ForgeConfigSpec.EnumValue<BackgroundOpacity> BACKGROUND_OPACITY;
    public static final ForgeConfigSpec.EnumValue<CoordPrecision> COORD_PRECISION;

    public static final ForgeConfigSpec.EnumValue<HudColor> FPS_COLOR;
    public static final ForgeConfigSpec.EnumValue<HudColor> COORDS_COLOR;
    public static final ForgeConfigSpec.EnumValue<HudColor> FACING_COLOR;
    public static final ForgeConfigSpec.EnumValue<HudColor> BIOME_COLOR;
    public static final ForgeConfigSpec.EnumValue<HudColor> MEMORY_COLOR;
    public static final ForgeConfigSpec.EnumValue<HudColor> CPU_COLOR;
    public static final ForgeConfigSpec.EnumValue<HudColor> GPU_COLOR;
    public static final ForgeConfigSpec.EnumValue<HudColor> TIME_COLOR;
    public static final ForgeConfigSpec.EnumValue<HudColor> PING_COLOR;

    public static final ForgeConfigSpec.BooleanValue FAST_SUPER_BYTE_BUFFER;
    public static final ForgeConfigSpec.BooleanValue BACKFACE_CULLING;
    public static final ForgeConfigSpec.BooleanValue FAST_LIGHT_CACHE;
    public static final ForgeConfigSpec.BooleanValue CULL_BELTS;
    public static final ForgeConfigSpec.BooleanValue KINETIC_CULLING;
    public static final ForgeConfigSpec.BooleanValue SKIP_STATIONARY_KINETICS;
    public static final ForgeConfigSpec.BooleanValue KINETIC_AUDIO_THROTTLING;
    public static final ForgeConfigSpec.BooleanValue KINETIC_LOD;
    public static final ForgeConfigSpec.BooleanValue CULL_BLOCK_ENTITIES;
    public static final ForgeConfigSpec.BooleanValue CULL_PARTICLES;
    public static final ForgeConfigSpec.BooleanValue REDUCE_BLOCK_ENTITY_LAG;
    public static final ForgeConfigSpec.BooleanValue CULL_ENTITIES;
    public static final ForgeConfigSpec.BooleanValue CULL_SHADOWS;
    public static final ForgeConfigSpec.BooleanValue CREATE_FX_CULL;
    public static final ForgeConfigSpec.BooleanValue AGGRESSIVE_TICK_SLEEP;

    public static final ForgeConfigSpec.BooleanValue FIX_MEMORY_LEAKS;
    public static final ForgeConfigSpec.BooleanValue FIX_CLIENT_LEVEL_LEAK;
    public static final ForgeConfigSpec.BooleanValue FIX_FAKE_PLAYER_LEAK;
    public static final ForgeConfigSpec.BooleanValue FIX_DEBUG_SUBSCRIPTION_LEAK;

    public static final ForgeConfigSpec.BooleanValue OPTIMIZE_CONTRAPTIONS;
    public static final ForgeConfigSpec.BooleanValue OPTIMIZE_ENTITY_RENDERING;

    static {
        BUILDER.push("general");
        ENABLE_OPTIMIZATIONS = BUILDER.define("enableOptimizations", true);
        SHOW_FPS = BUILDER.define("showFps", true);
        BUILDER.pop();

        BUILDER.push("extra");
        ENABLE_EXTRA_HUD = BUILDER.define("enableExtraHud", true);
        SHOW_FPS_EXTENDED = BUILDER.define("showFpsExtended", false);
        SHOW_COORDS = BUILDER.define("showCoords", false);
        SHOW_FACING = BUILDER.define("showFacing", false);
        SHOW_BIOME = BUILDER.define("showBiome", false);
        SHOW_MEMORY = BUILDER.define("showMemory", false);
        SHOW_CPU = BUILDER.define("showCpu", false);
        SHOW_GPU = BUILDER.define("showGpu", false);
        SHOW_GAME_TIME = BUILDER.define("showGameTime", false);
        SHOW_PING = BUILDER.define("showPing", false);

        OVERLAY_CORNER = BUILDER.defineEnum("overlayCorner", OverlayCorner.TOP_LEFT);
        HUD_SCALE = BUILDER.defineInRange("hudScale", 100, 50, 200);
        TEXT_CONTRAST = BUILDER.defineEnum("textContrast", TextContrast.SHADOW);
        BACKGROUND_OPACITY = BUILDER.defineEnum("backgroundOpacity", BackgroundOpacity.MEDIUM);
        COORD_PRECISION = BUILDER.defineEnum("coordPrecision", CoordPrecision.TWO_DECIMALS);

        FPS_COLOR = BUILDER.defineEnum("fpsColor", HudColor.DYNAMIC);
        COORDS_COLOR = BUILDER.defineEnum("coordsColor", HudColor.WHITE);
        FACING_COLOR = BUILDER.defineEnum("facingColor", HudColor.GRAY);
        BIOME_COLOR = BUILDER.defineEnum("biomeColor", HudColor.CYAN);
        MEMORY_COLOR = BUILDER.defineEnum("memoryColor", HudColor.DYNAMIC);
        CPU_COLOR = BUILDER.defineEnum("cpuColor", HudColor.DYNAMIC);
        GPU_COLOR = BUILDER.defineEnum("gpuColor", HudColor.DYNAMIC);
        TIME_COLOR = BUILDER.defineEnum("timeColor", HudColor.GOLD);
        PING_COLOR = BUILDER.defineEnum("pingColor", HudColor.DYNAMIC);
        BUILDER.pop();

        BUILDER.push("performance");
        FAST_SUPER_BYTE_BUFFER = BUILDER.define("fastSuperByteBuffer", true);
        BACKFACE_CULLING = BUILDER.define("backfaceCulling", true);
        FAST_LIGHT_CACHE = BUILDER.define("fastLightCache", true);
        CULL_BELTS = BUILDER.define("cullBelts", true);
        KINETIC_CULLING = BUILDER.define("kineticCulling", true);
        SKIP_STATIONARY_KINETICS = BUILDER.define("skipStationaryKinetics", true);
        KINETIC_AUDIO_THROTTLING = BUILDER.define("kineticAudioThrottling", true);
        KINETIC_LOD = BUILDER.define("kineticLod", true);
        CULL_BLOCK_ENTITIES = BUILDER.define("cullBlockEntities", true);
        CULL_PARTICLES = BUILDER.define("cullParticles", true);
        REDUCE_BLOCK_ENTITY_LAG = BUILDER.define("reduceBlockEntityLag", true);
        CULL_ENTITIES = BUILDER.define("cullEntities", true);
        CULL_SHADOWS = BUILDER.define("cullShadows", true);
        CREATE_FX_CULL = BUILDER.define("createFxCull", true);
        AGGRESSIVE_TICK_SLEEP = BUILDER.define("aggressiveTickSleep", true);
        BUILDER.pop();

        BUILDER.push("memory");
        FIX_MEMORY_LEAKS = BUILDER.define("fixMemoryLeaks", true);
        FIX_CLIENT_LEVEL_LEAK = BUILDER.define("fixClientLevelLeak", true);
        FIX_FAKE_PLAYER_LEAK = BUILDER.define("fixFakePlayerLeak", true);
        FIX_DEBUG_SUBSCRIPTION_LEAK = BUILDER.define("fixDebugSubscriptionLeak", true);
        BUILDER.pop();

        BUILDER.push("quality");
        OPTIMIZE_CONTRAPTIONS = BUILDER.define("optimizeContraptions", true);
        BUILDER.pop();

        BUILDER.push("advanced");
        OPTIMIZE_ENTITY_RENDERING = BUILDER.define("optimizeEntityRendering", true);
        BUILDER.pop();
    }

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static void init() {
        Config.ENABLE_OPTIMIZATIONS = wrap(ENABLE_OPTIMIZATIONS);
        Config.SHOW_FPS = wrap(SHOW_FPS);
        Config.ENABLE_EXTRA_HUD = wrap(ENABLE_EXTRA_HUD);
        Config.SHOW_FPS_EXTENDED = wrap(SHOW_FPS_EXTENDED);
        Config.SHOW_COORDS = wrap(SHOW_COORDS);
        Config.SHOW_FACING = wrap(SHOW_FACING);
        Config.SHOW_BIOME = wrap(SHOW_BIOME);
        Config.SHOW_MEMORY = wrap(SHOW_MEMORY);
        Config.SHOW_CPU = wrap(SHOW_CPU);
        Config.SHOW_GPU = wrap(SHOW_GPU);
        Config.SHOW_GAME_TIME = wrap(SHOW_GAME_TIME);
        Config.SHOW_PING = wrap(SHOW_PING);

        Config.OVERLAY_CORNER = wrap(OVERLAY_CORNER);
        Config.HUD_SCALE = wrap(HUD_SCALE);
        Config.TEXT_CONTRAST = wrap(TEXT_CONTRAST);
        Config.BACKGROUND_OPACITY = wrap(BACKGROUND_OPACITY);
        Config.COORD_PRECISION = wrap(COORD_PRECISION);

        Config.FPS_COLOR = wrap(FPS_COLOR);
        Config.COORDS_COLOR = wrap(COORDS_COLOR);
        Config.FACING_COLOR = wrap(FACING_COLOR);
        Config.BIOME_COLOR = wrap(BIOME_COLOR);
        Config.MEMORY_COLOR = wrap(MEMORY_COLOR);
        Config.CPU_COLOR = wrap(CPU_COLOR);
        Config.GPU_COLOR = wrap(GPU_COLOR);
        Config.TIME_COLOR = wrap(TIME_COLOR);
        Config.PING_COLOR = wrap(PING_COLOR);

        Config.FAST_SUPER_BYTE_BUFFER = wrap(FAST_SUPER_BYTE_BUFFER);
        Config.BACKFACE_CULLING = wrap(BACKFACE_CULLING);
        Config.FAST_LIGHT_CACHE = wrap(FAST_LIGHT_CACHE);
        Config.CULL_BELTS = wrap(CULL_BELTS);
        Config.KINETIC_CULLING = wrap(KINETIC_CULLING);
        Config.SKIP_STATIONARY_KINETICS = wrap(SKIP_STATIONARY_KINETICS);
        Config.KINETIC_AUDIO_THROTTLING = wrap(KINETIC_AUDIO_THROTTLING);
        Config.KINETIC_LOD = wrap(KINETIC_LOD);
        Config.CULL_BLOCK_ENTITIES = wrap(CULL_BLOCK_ENTITIES);
        Config.CULL_PARTICLES = wrap(CULL_PARTICLES);
        Config.REDUCE_BLOCK_ENTITY_LAG = wrap(REDUCE_BLOCK_ENTITY_LAG);
        Config.CULL_ENTITIES = wrap(CULL_ENTITIES);
        Config.CULL_SHADOWS = wrap(CULL_SHADOWS);
        Config.CREATE_FX_CULL = wrap(CREATE_FX_CULL);
        Config.AGGRESSIVE_TICK_SLEEP = wrap(AGGRESSIVE_TICK_SLEEP);

        Config.FIX_MEMORY_LEAKS = wrap(FIX_MEMORY_LEAKS);
        Config.FIX_CLIENT_LEVEL_LEAK = wrap(FIX_CLIENT_LEVEL_LEAK);
        Config.FIX_FAKE_PLAYER_LEAK = wrap(FIX_FAKE_PLAYER_LEAK);
        Config.FIX_DEBUG_SUBSCRIPTION_LEAK = wrap(FIX_DEBUG_SUBSCRIPTION_LEAK);

        Config.OPTIMIZE_CONTRAPTIONS = wrap(OPTIMIZE_CONTRAPTIONS);
        Config.OPTIMIZE_ENTITY_RENDERING = wrap(OPTIMIZE_ENTITY_RENDERING);

        Config.saveHandler = () -> {
            if (SPEC.isLoaded()) {
                SPEC.save();
            }
        };
    }

    private static <T> ConfigValue<T> wrap(ForgeConfigSpec.ConfigValue<T> forgeVal) {
        return new ConfigValue<T>() {
            @Override
            public T get() {
                return forgeVal.get();
            }

            @Override
            public void set(T value) {
                forgeVal.set(value);
            }
        };
    }

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event) {
        Config.sync();
    }
}
