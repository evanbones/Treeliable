package com.evandev.treeliable.api;

import net.minecraft.core.BlockPos;

import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

public interface TreeDataImmutable {

    Set<BlockPos> getLogBlocks();

    Stream<BlockPos> streamLogs();

    Stream<BlockPos> streamLeaves();

    boolean hasLeaves();

    boolean isAProperTree(boolean mustHaveLeaves);

    int numChopsNeededToFell();

    void forEachLeaves(Consumer<BlockPos> consumer);
}