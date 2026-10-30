package com.reggarf.mods.reggadium.mixin.render;

import net.minecraft.client.renderer.culling.Frustum;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Frustum.class)
public interface FrustumInvoker {
    @Invoker("cubeInFrustum")
    boolean reggadium$cubeInFrustum(double minX, double minY, double minZ, double maxX, double maxY, double maxZ);
}
