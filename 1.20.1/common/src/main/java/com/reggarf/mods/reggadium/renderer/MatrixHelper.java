package com.reggarf.mods.reggadium.renderer;

import org.joml.Matrix3f;

public class MatrixHelper {

    public static float transformNormalX(Matrix3f mat, float x, float y, float z) {
        return mat.m00 * x + mat.m10 * y + mat.m20 * z;
    }

    public static float transformNormalY(Matrix3f mat, float x, float y, float z) {
        return mat.m01 * x + mat.m11 * y + mat.m21 * z;
    }

    public static float transformNormalZ(Matrix3f mat, float x, float y, float z) {
        return mat.m02 * x + mat.m12 * y + mat.m22 * z;
    }
}
