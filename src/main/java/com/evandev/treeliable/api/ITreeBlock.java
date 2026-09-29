package com.evandev.treeliable.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface ITreeBlock extends ITreeliableBlockBehavior {
    TreeData getTree(Level level, BlockPos origin);
}
