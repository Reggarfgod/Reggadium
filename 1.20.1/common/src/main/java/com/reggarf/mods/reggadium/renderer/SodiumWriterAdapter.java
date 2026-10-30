package com.reggarf.mods.reggadium.renderer;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import org.lwjgl.system.MemoryStack;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

public class SodiumWriterAdapter implements NativeVertexWriter {
    private static final boolean AVAILABLE;
    private static MethodHandle TRY_OF;
    private static MethodHandle PUSH;

    static {
        boolean avail = false;
        try {
            Class<?> writerClass = null;
            try {
                // Sodium 0.6+ (MC 1.21+)
                writerClass = Class.forName("net.caffeinemc.mods.sodium.api.vertex.buffer.VertexBufferWriter");
            } catch (ClassNotFoundException e) {
                // Legacy Sodium
                try {
                    writerClass = Class.forName("me.jellysquid.mods.sodium.client.render.vertex.VertexBufferWriter");
                } catch (ClassNotFoundException ignored) {
                }
            }

            if (writerClass != null) {
                MethodHandles.Lookup lookup = MethodHandles.lookup();
                Method tryOfMethod = writerClass.getMethod("tryOf", VertexConsumer.class);
                TRY_OF = lookup.unreflect(tryOfMethod);
                Method pushMethod = writerClass.getMethod("push", MemoryStack.class, long.class, int.class, VertexFormat.class);
                PUSH = lookup.unreflect(pushMethod);
                avail = true;
            }
        } catch (Throwable ignored) {
        }
        AVAILABLE = avail;
    }

    private final Object writer;

    public SodiumWriterAdapter(Object writer) {
        this.writer = writer;
    }

    public static NativeVertexWriter tryOf(VertexConsumer consumer) {
        if (!AVAILABLE || TRY_OF == null) {
            return null;
        }
        try {
            Object writer = TRY_OF.invoke(consumer);
            if (writer != null) {
                return new SodiumWriterAdapter(writer);
            }
        } catch (Throwable ignored) {
        }
        return null;
    }

    @Override
    public void push(MemoryStack stack, long ptr, int count, VertexFormat format) {
        if (PUSH == null || writer == null) {
            return;
        }
        try {
            PUSH.invoke(writer, stack, ptr, count, format);
        } catch (Throwable ignored) {
        }
    }
}
