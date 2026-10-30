package com.reggarf.mods.reggadium.renderer;

import it.unimi.dsi.fastutil.longs.Long2IntMap;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;

public final class ReggadiumLightCache {
    private static final Long2IntMap WORLD_LIGHT_CACHE = new Long2IntOpenHashMap();

    public static void clear() {
        if (!WORLD_LIGHT_CACHE.isEmpty()) {
            WORLD_LIGHT_CACHE.clear();
        }
    }

    public static int getLight(BlockAndTintGetter world, BlockPos pos) {
        return WORLD_LIGHT_CACHE.computeIfAbsent(pos.asLong(), $ -> LevelRenderer.getLightColor(world, pos));
    }

    private ReggadiumLightCache() {}
}
