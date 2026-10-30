package com.reggarf.mods.reggadium.memory;

import com.google.common.collect.Maps;
import com.mojang.logging.LogUtils;
import com.reggarf.mods.reggadium.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.storage.LevelResource;
import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Reggadium Memory & Leak Prevention Subsystem (1.20.1 Common).
 *
 * Implements advanced memory leak mitigation across Forge and Fabric:
 * 1. ClientLevel Unload event propagation and crosshair / pick entity clearance.
 * 2. FakePlayer advancement listener detached lifecycle (prevents worlds & server references from leaking).
 * 3. Flywheel LevelAttached / InstancedRenderDispatcher world-cache invalidation.
 */
public class ReggadiumMemoryManager {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final AtomicInteger LAST_UNLOADED_LEVEL_HASH = new AtomicInteger();
    private static final Map<UUID, PlayerAdvancements> FAKE_PLAYER_ADVANCEMENTS = Maps.newHashMap();

    public static boolean isFakePlayer(ServerPlayer player) {
        if (player == null) return false;
        Class<?> clazz = player.getClass();
        while (clazz != null && clazz != Object.class) {
            String name = clazz.getSimpleName();
            if (name.contains("FakePlayer")) {
                return true;
            }
            clazz = clazz.getSuperclass();
        }
        return false;
    }

    public static PlayerAdvancements getOrCreateFakePlayerAdvancements(ServerPlayer fakePlayer, PlayerList playerList) {
        UUID uuid = fakePlayer.getUUID();
        PlayerAdvancements existing = FAKE_PLAYER_ADVANCEMENTS.get(uuid);
        if (existing != null) {
            return existing;
        }

        try {
            var server = fakePlayer.getServer();
            if (server == null) return null;
            Path path = server.getWorldPath(LevelResource.PLAYER_ADVANCEMENTS_DIR).resolve(uuid + ".json");
            PlayerAdvancements adv = new PlayerAdvancements(
                    server.getFixerUpper(),
                    playerList,
                    server.getAdvancements(),
                    path,
                    fakePlayer
            );
            adv.stopListening();
            FAKE_PLAYER_ADVANCEMENTS.put(uuid, adv);
            return adv;
        } catch (Throwable t) {
            LOGGER.debug("[Reggadium] Failed to create fake player advancements: {}", t.getMessage());
            return null;
        }
    }

    public static boolean isUnloaded(LevelAccessor level) {
        return level != null && LAST_UNLOADED_LEVEL_HASH.get() == System.identityHashCode(level);
    }

    public static void onClientLevelUnload(LevelAccessor level) {
        if (level != null) {
            LAST_UNLOADED_LEVEL_HASH.set(System.identityHashCode(level));
            invalidateFlywheelLevel(level);
            postLevelUnloadEventIfAvailable(level);
        }
    }

    public static void onServerLevelUnload(LevelAccessor level) {
        if (!Config.fixMemoryLeaksEnabled) return;

        if (level != null) {
            LAST_UNLOADED_LEVEL_HASH.set(System.identityHashCode(level));
        }

        FAKE_PLAYER_ADVANCEMENTS.entrySet().removeIf(entry -> {
            PlayerAdvancements adv = entry.getValue();
            if (adv != null) {
                adv.stopListening();
                return true;
            }
            return false;
        });

        invalidateFlywheelLevel(level);
    }

    public static void onServerStopped() {
        for (PlayerAdvancements adv : FAKE_PLAYER_ADVANCEMENTS.values()) {
            if (adv != null) {
                adv.stopListening();
            }
        }
        FAKE_PLAYER_ADVANCEMENTS.clear();
    }

    public static void clearClientPickEntities() {
        try {
            Minecraft mc = Minecraft.getInstance();
            mc.crosshairPickEntity = null;
            mc.hitResult = null;
        } catch (Throwable ignored) {
        }
    }

    private static void postLevelUnloadEventIfAvailable(LevelAccessor level) {
        // Try Forge
        try {
            Class<?> forge = Class.forName("net.minecraftforge.common.MinecraftForge");
            Object eventBus = forge.getField("EVENT_BUS").get(null);
            Class<?> eventClass = Class.forName("net.minecraftforge.event.level.LevelEvent$Unload");
            Object event = eventClass.getConstructor(LevelAccessor.class).newInstance(level);
            Method post = eventBus.getClass().getMethod("post", Object.class);
            post.invoke(eventBus, event);
        } catch (Throwable ignored) {
        }
    }

    private static void invalidateFlywheelLevel(LevelAccessor level) {
        if (level == null) return;
        // Flywheel 1.0+
        try {
            Class<?> levelAttached = Class.forName("dev.engine_room.flywheel.lib.util.LevelAttached");
            Method invalidate = levelAttached.getMethod("invalidateLevel", LevelAccessor.class);
            invalidate.invoke(null, level);
        } catch (Throwable ignored) {
        }
        // Flywheel 0.6.x (1.20.1)
        try {
            Class<?> dispatcher = Class.forName("com.jozufozu.flywheel.backend.instancing.InstancedRenderDispatcher");
            Method invalidate = dispatcher.getMethod("resetWorld", LevelAccessor.class);
            invalidate.invoke(null, level);
        } catch (Throwable ignored) {
        }
    }
}
