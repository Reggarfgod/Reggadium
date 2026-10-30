package com.reggarf.mods.reggadium.mixin.render;

import com.reggarf.mods.reggadium.culling.IReggadiumCullableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BlockEntity.class)
public class BlockEntityMixin implements IReggadiumCullableBlockEntity {
    @Unique
    private byte reggadium$boxKind = 0;

    @Unique
    private BlockState reggadium$boxState = null;

    @Unique
    private double[] reggadium$relativeBox = null;

    @Override
    public byte reggadium$getBoxKind() {
        return this.reggadium$boxKind;
    }

    @Override
    public void reggadium$setBoxKind(byte kind) {
        this.reggadium$boxKind = kind;
    }

    @Override
    public BlockState reggadium$getBoxState() {
        return this.reggadium$boxState;
    }

    @Override
    public double[] reggadium$getRelativeBox() {
        return this.reggadium$relativeBox;
    }

    @Override
    public void reggadium$setRelativeBox(BlockState state, double[] box) {
        this.reggadium$boxState = state;
        this.reggadium$relativeBox = box;
    }
}
