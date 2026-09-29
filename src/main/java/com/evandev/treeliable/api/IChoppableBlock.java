package com.evandev.treeliable.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public interface IChoppableBlock extends IFellableBlock {

    default boolean isChoppable(BlockGetter level, BlockPos blockPos, BlockState blockState) {
        return true;
    }

    @Override
    default double getSupportFactor(BlockGetter level, BlockPos pos, BlockState blockState) {
        return 1.0;
    }
}