package com.reggarf.mods.reggadium.mixin.create.render;

import com.reggarf.mods.reggadium.Config;
import com.reggarf.mods.reggadium.renderer.ReggadiumByteBuffer;
import com.mojang.blaze3d.vertex.BufferBuilder;
import net.createmod.catnip.render.MutableTemplateMesh;
import net.createmod.catnip.render.SuperBufferFactory;
import net.createmod.catnip.render.SuperByteBuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SuperBufferFactory.class, remap = false)
public abstract class SuperBufferFactoryMixin {
    @Inject(method = "create", at = @At("HEAD"), cancellable = true, require = 0)
    public void onCreate(BufferBuilder.RenderedBuffer data, CallbackInfoReturnable<SuperByteBuffer> cir) {
        if (!Config.optimizationsEnabled || !Config.fastSbbEnabled) {
            return;
        }

        cir.setReturnValue(new ReggadiumByteBuffer(new MutableTemplateMesh(data).toImmutable()));
    }
}
