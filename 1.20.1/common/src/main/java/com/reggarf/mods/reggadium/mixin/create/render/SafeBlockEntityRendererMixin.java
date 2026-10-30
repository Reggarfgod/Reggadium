package com.reggarf.mods.reggadium.mixin.create.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.reggarf.mods.reggadium.Config;
import com.reggarf.mods.reggadium.culling.IReggadiumCullableBlockEntity;
import com.reggarf.mods.reggadium.culling.ReggadiumCuller;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Method;

@Mixin(value = SafeBlockEntityRenderer.class, remap = false)
public abstract class SafeBlockEntityRendererMixin {

    @Inject(method = "render(Lnet/minecraft/world/level/block/entity/BlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V", at = @At("HEAD"), cancellable = true, require = 0)
    public void onRender(BlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay, CallbackInfo ci) {
        if (!Config.optimizationsEnabled) {
            return;
        }

        BlockPos pos = be.getBlockPos();
        double distSq = ReggadiumCuller.distanceSq(pos);

        if (Config.kineticCullingEnabled) {
            if (distSq > 2304.0) {
                ci.cancel();
                return;
            }

            if (be instanceof IReggadiumCullableBlockEntity cullable) {
                BlockState state = be.getBlockState();
                double[] box = cullable.reggadium$getRelativeBox();
                if (box == null || cullable.reggadium$getBoxState() != state) {
                    try {
                        Method m = be.getClass().getMethod("getRenderBoundingBox");
                        AABB boundingBox = (AABB) m.invoke(be);
                        if (boundingBox != null) {
                            int x = pos.getX(), y = pos.getY(), z = pos.getZ();
                            box = new double[] {
                                    boundingBox.minX - x, boundingBox.minY - y, boundingBox.minZ - z,
                                    boundingBox.maxX - x, boundingBox.maxY - y, boundingBox.maxZ - z
                            };
                        }
                    } catch (Throwable ignored) {
                    }
                    if (box == null) {
                        box = new double[] { 0.0, 0.0, 0.0, 1.0, 1.0, 1.0 };
                    }
                    cullable.reggadium$setBoxKind(ReggadiumCuller.KIND_CUSTOM);
                    cullable.reggadium$setRelativeBox(state, box);
                }

                int x = pos.getX(), y = pos.getY(), z = pos.getZ();
                if (!ReggadiumCuller.isCubeVisible(x + box[0], y + box[1], z + box[2],
                                                   x + box[3], y + box[4], z + box[5])) {
                    ci.cancel();
                    return;
                }
            } else if (!ReggadiumCuller.isBlockEntityVisible(be)) {
                ci.cancel();
                return;
            }
        }

        if (Config.skipStationaryKineticsEnabled && be instanceof KineticBlockEntity kbe) {
            if (kbe.getSpeed() == 0 && distSq > 256.0) {
                ci.cancel();
            }
        }
    }
}
