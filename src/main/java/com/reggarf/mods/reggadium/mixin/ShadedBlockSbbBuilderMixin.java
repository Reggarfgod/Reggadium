package com.reggarf.mods.reggadium.mixin;

import com.reggarf.mods.reggadium.Config;
import com.reggarf.mods.reggadium.renderer.ReggadiumByteBuffer;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.MeshData;
import it.unimi.dsi.fastutil.ints.IntList;
import net.createmod.catnip.render.MutableTemplateMesh;
import net.createmod.catnip.render.ShadedBlockSbbBuilder;
import net.createmod.catnip.render.SuperByteBuffer;
import net.createmod.catnip.render.TemplateMesh;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ShadedBlockSbbBuilder.class, remap = false)
public abstract class ShadedBlockSbbBuilderMixin {
    @Shadow
    protected BufferBuilder bufferBuilder;

    @Shadow
    @Final
    protected IntList shadeSwapVertices;

    @Inject(method = "end", at = @At("HEAD"), cancellable = true, require = 0)
    public void onEnd(CallbackInfoReturnable<SuperByteBuffer> cir) {
        if (!Config.optimizationsEnabled || !Config.fastSbbEnabled) {
            return;
        }

        MeshData data = bufferBuilder.build();
        TemplateMesh mesh;

        if (data != null) {
            mesh = new MutableTemplateMesh(data).toImmutable();
            data.close();
        } else {
            mesh = new TemplateMesh(0);
        }

        cir.setReturnValue(new ReggadiumByteBuffer(mesh, shadeSwapVertices.toIntArray()));
    }
}
