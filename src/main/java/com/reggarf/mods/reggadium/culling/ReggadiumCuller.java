package com.reggarf.mods.reggadium.culling;

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

    public static boolean isBlockEntityVisible(BlockEntity be) {
        if (be == null) return true;
        BlockPos pos = be.getBlockPos();
        return isPosVisible(pos);
    }

    public static boolean isPosVisible(BlockPos pos) {
        Frustum frustum = getFrustum();
        if (frustum == null) return true;

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        return frustum.isVisible(new AABB(x, y, z, x + 1, y + 1, z + 1));
    }

    public static boolean isAABBVisible(AABB box) {
        if (box == null) return true;
        Frustum frustum = getFrustum();
        if (frustum == null) return true;
        return frustum.isVisible(box);
    }

    public static boolean isParticleVisible(Particle p, float partialTicks) {
        if (p == null) return true;
        Frustum frustum = getFrustum();
        if (frustum == null) return true;
        AABB box = p.getRenderBoundingBox(partialTicks);
        if (box == null) return true;
        return frustum.isVisible(box);
    }

    public static boolean isBeyondDistance(BlockPos pos, double maxDistanceSq) {
        Minecraft mc = Minecraft.getInstance();
        Camera camera = mc.gameRenderer != null ? mc.gameRenderer.getMainCamera() : null;
        if (camera == null) return false;

        Vec3 camPos = camera.getPosition();
        double dx = pos.getX() + 0.5 - camPos.x;
        double dy = pos.getY() + 0.5 - camPos.y;
        double dz = pos.getZ() + 0.5 - camPos.z;

        return (dx * dx + dy * dy + dz * dz) > maxDistanceSq;
    }

    public static boolean isBeyondDistance(Vec3 pos, double maxDistanceSq) {
        Minecraft mc = Minecraft.getInstance();
        Camera camera = mc.gameRenderer != null ? mc.gameRenderer.getMainCamera() : null;
        if (camera == null) return false;

        Vec3 camPos = camera.getPosition();
        double dx = pos.x - camPos.x;
        double dy = pos.y - camPos.y;
        double dz = pos.z - camPos.z;

        return (dx * dx + dy * dy + dz * dz) > maxDistanceSq;
    }
}
