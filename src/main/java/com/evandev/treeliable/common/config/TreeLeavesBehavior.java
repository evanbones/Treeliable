package com.evandev.treeliable.common.config;

import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

public interface TreeLeavesBehavior {
    TreeLeavesBehavior DEFAULT = state -> !ModConfig.get().ignorePersistentLeaves || !state.hasProperty(LeavesBlock.PERSISTENT) || !state.getValue(LeavesBlock.PERSISTENT);
    TreeLeavesBehavior PROBLEMATIC = state -> true;

    boolean isLeaves(BlockState blockState);
}
