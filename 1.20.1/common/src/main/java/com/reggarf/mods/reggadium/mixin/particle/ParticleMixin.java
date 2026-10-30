package com.reggarf.mods.reggadium.mixin.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.reggarf.mods.reggadium.Config;
import com.reggarf.mods.reggadium.culling.ReggadiumCuller;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.SingleQuadParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SingleQuadParticle.class)
public class ParticleMixin {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void onRender(VertexConsumer buffer, Camera camera, float partialTicks, CallbackInfo ci) {
        if (!Config.optimizationsEnabled || !Config.cullParticlesEnabled) {
            return;
        }

        ParticleAccessor accessor = (ParticleAccessor) this;
        double x = accessor.reggadium$getX();
        double y = accessor.reggadium$getY();
        double z = accessor.reggadium$getZ();
        if (ReggadiumCuller.isBeyondDistance(x, y, z, 2304.0) || !ReggadiumCuller.isPointVisible(x, y, z, 0.75)) {
            ci.cancel();
        }
    }
}
