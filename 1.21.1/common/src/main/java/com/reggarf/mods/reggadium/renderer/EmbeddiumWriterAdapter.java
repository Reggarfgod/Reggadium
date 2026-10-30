package com.reggarf.mods.reggadium.renderer;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import org.lwjgl.system.MemoryStack;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

public class EmbeddiumWriterAdapter implements NativeVertexWriter {
    private static final boolean AVAILABLE;
    private static Object BLOCK_DESC;
    private static Object ENTITY_DESC;
    private static MethodHandle TRY_OF;
    private static MethodHandle PUSH;

    static {
        boolean avail = false;
        try {
            Class<?> registryClass = Class.forName("org.embeddedt.embeddium.api.vertex.format.VertexFormatRegistry");
            Object registry = registryClass.getMethod("instance").invoke(null);
            Method getDesc = registryClass.getMethod("get", VertexFormat.class);
            BLOCK_DESC = getDesc.invoke(registry, DefaultVertexFormat.BLOCK);
            ENTITY_DESC = getDesc.invoke(registry, DefaultVertexFormat.NEW_ENTITY);

            Class<?> descClass = Class.forName("org.embeddedt.embeddium.api.vertex.format.VertexFormatDescription");
            Class<?> writerClass = Class.forName("org.embeddedt.embeddium.api.vertex.buffer.VertexBufferWriter");

            MethodHandles.Lookup lookup = MethodHandles.lookup();
            Method tryOfMethod = writerClass.getMethod("tryOf", VertexConsumer.class);
            TRY_OF = lookup.unreflect(tryOfMethod);

            Method pushMethod = writerClass.getMethod("push", MemoryStack.class, long.class, int.class, descClass);
            PUSH = lookup.unreflect(pushMethod);
            avail = true;
        } catch (Throwable ignored) {
        }
        AVAILABLE = avail;
    }

    private final Object writer;

    public EmbeddiumWriterAdapter(Object writer) {
        this.writer = writer;
    }

    public static NativeVertexWriter tryOf(VertexConsumer consumer) {
        if (!AVAILABLE || TRY_OF == null) {
            return null;
        }
        try {
            Object writer = TRY_OF.invoke(consumer);
            if (writer != null) {
                return new EmbeddiumWriterAdapter(writer);
            }
        } catch (Throwable ignored) {
        }
        return null;
    }

    @Override
    public void push(MemoryStack stack, long ptr, int count, VertexFormat format) {
        if (!AVAILABLE || PUSH == null || writer == null) {
            return;
        }
        try {
            Object desc = (format == DefaultVertexFormat.BLOCK) ? BLOCK_DESC : ENTITY_DESC;
            PUSH.invoke(writer, stack, ptr, count, desc);
        } catch (Throwable ignored) {
        }
    }
}
