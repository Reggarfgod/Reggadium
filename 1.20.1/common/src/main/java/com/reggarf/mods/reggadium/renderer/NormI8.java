package com.reggarf.mods.reggadium.renderer;

import net.minecraft.util.Mth;
import org.joml.Vector3f;

public class NormI8 {

    public static int pack(Vector3f normal) {
        return pack(normal.x(), normal.y(), normal.z());
    }

    public static int pack(float x, float y, float z) {
        int ix = (int) (Mth.clamp(x, -1.0f, 1.0f) * 127.0f) & 255;
        int iy = (int) (Mth.clamp(y, -1.0f, 1.0f) * 127.0f) & 255;
        int iz = (int) (Mth.clamp(z, -1.0f, 1.0f) * 127.0f) & 255;
        return (iz << 16) | (iy << 8) | ix;
    }

    public static float unpackX(int packed) {
        return ((byte) (packed & 255)) * 0.007874016f;
    }

    public static float unpackY(int packed) {
        return ((byte) ((packed >>> 8) & 255)) * 0.007874016f;
    }

    public static float unpackZ(int packed) {
        return ((byte) ((packed >>> 16) & 255)) * 0.007874016f;
    }
}
