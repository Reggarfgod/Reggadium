package com.reggarf.mods.reggadium.culling;

import net.minecraft.world.level.block.state.BlockState;

public interface IReggadiumCullableBlockEntity {
    byte reggadium$getBoxKind();
    void reggadium$setBoxKind(byte kind);
    BlockState reggadium$getBoxState();
    double[] reggadium$getRelativeBox();
    void reggadium$setRelativeBox(BlockState state, double[] box);
}
