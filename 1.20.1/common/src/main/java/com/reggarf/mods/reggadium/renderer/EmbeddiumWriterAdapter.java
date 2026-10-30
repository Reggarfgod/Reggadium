package com.reggarf.mods.reggadium.renderer;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.caffeinemc.mods.sodium.api.vertex.buffer.VertexBufferWriter;
import net.caffeinemc.mods.sodium.api.vertex.format.VertexFormatDescription;
import net.caffeinemc.mods.sodium.api.vertex.format.VertexFormatRegistry;
import org.lwjgl.system.MemoryStack;

public class EmbeddiumWriterAdapter implements NativeVertexWriter {
    private static VertexFormatDescription BLOCK_DESC;
    private static VertexFormatDescription ENTITY_DESC;

    static {
        try {
            BLOCK_DESC = VertexFormatRegistry.instance().get(DefaultVertexFormat.BLOCK);
            ENTITY_DESC = VertexFormatRegistry.instance().get(DefaultVertexFormat.NEW_ENTITY);
        } catch (Throwable ignored) {
        }
    }

    private final VertexBufferWriter writer;

    public EmbeddiumWriterAdapter(VertexBufferWriter writer) {
        this.writer = writer;
    }

    public static NativeVertexWriter tryOf(VertexConsumer consumer) {
        try {
            VertexBufferWriter writer = VertexBufferWriter.tryOf(consumer);
            if (writer != null) {
                return new EmbeddiumWriterAdapter(writer);
            }
        } catch (Throwable ignored) {
        }
        return null;
    }

    @Override
    public void push(MemoryStack stack, long ptr, int count, VertexFormat format) {
        VertexFormatDescription desc = (format == DefaultVertexFormat.BLOCK) ? BLOCK_DESC : ENTITY_DESC;
        writer.push(stack, ptr, count, desc);
    }
}
