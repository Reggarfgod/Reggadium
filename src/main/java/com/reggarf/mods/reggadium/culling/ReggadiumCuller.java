package com.reggarf.mods.reggadium.culling;

import com.reggarf.mods.reggadium.mixin.render.FrustumInvoker;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ReggadiumCuller {

    private static Frustum getFrustum() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.levelRenderer != null) {
            return mc.levelRenderer.getFrustum();
        }
        return null;
    }

    private static Vec3 cameraPos() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.gameRenderer == null) {
            return null;
        }
        Camera camera = mc.gameRenderer.getMainCamera();
        return camera == null ? null : camera.getPosition();
    }

    public static boolean isCubeVisible(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        Frustum frustum = getFrustum();
        if (frustum == null) {
            return true;
        }
        return ((FrustumInvoker) frustum).reggadium$cubeInFrustum(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public static boolean isBlockEntityVisible(BlockEntity be) {
        if (be == null) {
            return true;
        }
        return isPosVisible(be.getBlockPos());
    }

    public static boolean isPosVisible(BlockPos pos) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        return isCubeVisible(x, y, z, x + 1.0, y + 1.0, z + 1.0);
    }

    public static boolean isAABBVisible(AABB box) {
        if (box == null) {
            return true;
        }
        return isCubeVisible(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
    }

    public static boolean isPointVisible(double x, double y, double z, double radius) {
        return isCubeVisible(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius);
    }

    public static boolean isParticleVisible(Particle p, float partialTicks) {
        if (p == null) {
            return true;
        }
        AABB box = p.getRenderBoundingBox(partialTicks);
        if (box == null) {
            return true;
        }
        return isAABBVisible(box);
    }

    public static boolean isBeyondDistance(double x, double y, double z, double maxDistanceSq) {
        Vec3 camPos = cameraPos();
        if (camPos == null) {
            return false;
        }
        double dx = x - camPos.x;
        double dy = y - camPos.y;
        double dz = z - camPos.z;
        return (dx * dx + dy * dy + dz * dz) > maxDistanceSq;
    }

    public static boolean isBeyondDistance(BlockPos pos, double maxDistanceSq) {
        return isBeyondDistance(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, maxDistanceSq);
    }

    public static boolean isBeyondDistance(Vec3 pos, double maxDistanceSq) {
        return isBeyondDistance(pos.x, pos.y, pos.z, maxDistanceSq);
    }

    public static double distanceSq(BlockPos pos) {
        Vec3 camPos = cameraPos();
        if (camPos == null) {
            return 0.0;
        }
        double dx = pos.getX() + 0.5 - camPos.x;
        double dy = pos.getY() + 0.5 - camPos.y;
        double dz = pos.getZ() + 0.5 - camPos.z;
        return dx * dx + dy * dy + dz * dz;
    }

    public static double distanceSq(double x, double y, double z) {
        Vec3 camPos = cameraPos();
        if (camPos == null) {
            return 0.0;
        }
        double dx = x - camPos.x;
        double dy = y - camPos.y;
        double dz = z - camPos.z;
        return dx * dx + dy * dy + dz * dz;
    }
}
