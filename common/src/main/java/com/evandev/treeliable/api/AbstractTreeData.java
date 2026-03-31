package com.evandev.treeliable.api;

import com.evandev.treeliable.common.config.FellLeavesStrategy;
import com.evandev.treeliable.common.config.ModConfig;
import net.minecraft.core.BlockPos;

import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class AbstractTreeData implements TreeData {
    @Override
    public boolean isAProperTree(boolean mustHaveLeaves) {
        boolean canBeLil = hasLeaves() && ModConfig.get().fellLeavesStrategy != FellLeavesStrategy.IGNORE;
        long lowerboundSize = streamLogs().limit(2).count();
        return (hasLeaves() || !mustHaveLeaves) && lowerboundSize >= (canBeLil ? 1 : 2);
    }

    @Override
    public Set<BlockPos> getLogBlocks() {
        return streamLogs().collect(Collectors.toSet());
    }

    @Override
    public void forEachLeaves(Consumer<BlockPos> consumer) {
        streamLeaves().forEach(consumer);
    }
}