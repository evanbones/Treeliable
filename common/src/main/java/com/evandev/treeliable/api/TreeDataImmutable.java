package com.evandev.treeliable.api;

import com.evandev.treeliable.common.chop.Chop;
import net.minecraft.core.BlockPos;

import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

public interface TreeDataImmutable {

    Set<BlockPos> getLogBlocks();

    int getChops();

    Stream<BlockPos> streamLogs();

    Stream<BlockPos> streamLeaves();

    boolean hasLeaves();

    boolean isAProperTree(boolean mustHaveLeaves);

    boolean readyToFell(int numChops);

    /**
     * Use with caution, as this requires mapping out the whole tree.
     * Use {@link TreeDataImmutable#readyToFell(int)} instead whenever possible to avoid unnecessary computations.
     */
    int numChopsNeededToFell();

    Collection<Chop> chop(BlockPos target, int numChops);

    void forEachLeaves(Consumer<BlockPos> consumer);
}