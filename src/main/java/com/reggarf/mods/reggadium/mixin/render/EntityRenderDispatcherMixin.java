package com.reggarf.mods.reggadium.mixin.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.reggarf.mods.reggadium.Config;
import com.reggarf.mods.reggadium.culling.ReggadiumCuller;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {

    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    private void reggadium$cullEntity(Entity entity, Frustum frustum, double camX, double camY, double camZ, CallbackInfoReturnable<Boolean> cir) {
        if (!Config.optimizationsEnabled || !Config.cullEntitiesEnabled) {
            return;
        }
        if (entity instanceof Player) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (entity == mc.getCameraEntity() || (mc.player != null && entity == mc.player.getVehicle())) {
            return;
        }

        double dx = entity.getX() - camX;
        double dy = entity.getY() - camY;
        double dz = entity.getZ() - camZ;
        double distSq = dx * dx + dy * dy + dz * dz;

        double maxSq = 4096.0;
        if (entity instanceof ItemEntity) {
            maxSq = 1024.0;
        } else if (entity instanceof ExperienceOrb) {
            maxSq = 576.0;
        }
        if (distSq > maxSq) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "renderShadow", at = @At("HEAD"), cancellable = true, require = 0, remap = true)
    private static void reggadium$cullShadow(PoseStack poseStack, MultiBufferSource bufferSource, Entity entity, float weight, float partialTicks, LevelReader level, float size, CallbackInfo ci) {
        if (!Config.optimizationsEnabled || !Config.cullShadowsEnabled) {
            return;
        }
        if (ReggadiumCuller.isBeyondDistance(entity.getX(), entity.getY(), entity.getZ(), 256.0)) {
            ci.cancel();
        }
    }
}
