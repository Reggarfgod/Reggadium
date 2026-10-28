package com.reggarf.mods.reggadium.mixin;

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

        if (!ReggadiumCuller.isParticleVisible((SingleQuadParticle) (Object) this, partialTicks)) {
            ci.cancel();
        }
    }
}
