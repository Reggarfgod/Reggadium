package com.reggarf.mods.reggadium.renderer;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import org.lwjgl.system.MemoryStack;

public interface NativeVertexWriter {

    void push(MemoryStack stack, long ptr, int count, VertexFormat format);

    static NativeVertexWriter tryOf(VertexConsumer consumer) {
        if (consumer == null) {
            return null;
        }

        // 1. Try Embeddium if present
        if (Holder.EMBEDDIUM_PRESENT) {
            try {
                NativeVertexWriter writer = EmbeddiumWriterAdapter.tryOf(consumer);
                if (writer != null) {
                    return writer;
                }
            } catch (Throwable ignored) {
            }
        }

        // 2. Try Sodium if present
        if (Holder.SODIUM_PRESENT) {
            try {
                NativeVertexWriter writer = SodiumWriterAdapter.tryOf(consumer);
                if (writer != null) {
                    return writer;
                }
            } catch (Throwable ignored) {
            }
        }

        return null;
    }

    class Holder {
        static final boolean EMBEDDIUM_PRESENT;
        static final boolean SODIUM_PRESENT;

        static {
            boolean emb = false;
            try {
                Class.forName("org.embeddedt.embeddium.api.vertex.buffer.VertexBufferWriter");
                emb = true;
            } catch (Throwable ignored) {
            }
            EMBEDDIUM_PRESENT = emb;

            boolean sod = false;
            try {
                Class.forName("net.caffeinemc.mods.sodium.api.vertex.buffer.VertexBufferWriter");
                sod = true;
            } catch (Throwable ignored) {
                try {
                    Class.forName("me.jellysquid.mods.sodium.client.render.vertex.VertexBufferWriter");
                    sod = true;
                } catch (Throwable ignored2) {
                }
            }
            SODIUM_PRESENT = sod;
        }
    }
}
