package com.reggarf.mods.reggadium.mixin.create.render;

import com.reggarf.mods.reggadium.culling.ReggadiumCuller;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = FilteringRenderer.class, remap = false)
public class FilteringRendererMixin {

    @Redirect(
        method = "renderOnBlockEntity",
        at = @At(value = "INVOKE", target = "Lcom/simibubi/create/foundation/blockEntity/SmartBlockEntity;isVirtual()Z"),
        require = 0
    )
    private static boolean reggadium$bypassDistanceCheckOnContraption(SmartBlockEntity be) {
        return be.isVirtual() || ReggadiumCuller.isVirtualOrOnShip(be);
    }
}
