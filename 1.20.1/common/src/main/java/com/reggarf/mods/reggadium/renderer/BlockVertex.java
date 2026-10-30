package com.reggarf.mods.reggadium.renderer;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import org.lwjgl.system.MemoryUtil;

public class BlockVertex {
    public static final VertexFormat FORMAT = DefaultVertexFormat.BLOCK;
    public static final int STRIDE = 32;

    public static void write(long ptr, float x, float y, float z, int color, float u, float v, int light, int normal) {
        MemoryUtil.memPutFloat(ptr + 0L, x);
        MemoryUtil.memPutFloat(ptr + 4L, y);
        MemoryUtil.memPutFloat(ptr + 8L, z);
        MemoryUtil.memPutInt(ptr + 12L, color);
        MemoryUtil.memPutFloat(ptr + 16L, u);
        MemoryUtil.memPutFloat(ptr + 20L, v);
        MemoryUtil.memPutInt(ptr + 24L, light);
        MemoryUtil.memPutInt(ptr + 28L, normal);
    }
}
