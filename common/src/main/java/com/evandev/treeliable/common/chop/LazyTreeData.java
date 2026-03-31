package com.evandev.treeliable.common.chop;

import com.evandev.treeliable.api.AbstractTreeData;
import com.evandev.treeliable.common.config.ModConfig;
import com.evandev.treeliable.common.math.graph.DirectedGraph;
import com.evandev.treeliable.common.math.graph.FloodFill;
import com.evandev.treeliable.common.math.graph.FloodFillImpl;
import com.evandev.treeliable.common.math.graph.GraphUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class LazyTreeData extends AbstractTreeData {

    private final Level level;
    private final boolean smartDetection;
    private final int maxLeavesDistance;
    private final Set<BlockPos> leaves = new HashSet<>();
    private final DirectedGraph<BlockPos> logsWorld;
    private final DirectedGraph<BlockPos> leavesWorld;
    private final Set<BlockPos> logs = new HashSet<>();
    private double mass = 0;
    private LogFinder logFinder;
    private Set<BlockPos> base;

    public LazyTreeData(Level level, BlockPos origin, DirectedGraph<BlockPos> logGraph, DirectedGraph<BlockPos> leavesGraph, Predicate<BlockPos> logFilter, Predicate<BlockPos> leavesFilter, int maxNumLogs, int maxLeavesDistance, boolean smartDetection) {
        this.level = level;
        this.smartDetection = smartDetection;
        this.maxLeavesDistance = maxLeavesDistance;

        logsWorld = GraphUtil.filter(
                logGraph,
                this::gatherLog,
                pos -> check(pos, logFilter, leavesFilter)
        );

        leavesWorld = GraphUtil.filterNeighbors(
                leavesGraph,
                leavesFilter
        );

        makeTreeBase(level, origin);

        // Populate base logs and calculate initial mass
        for (BlockPos pos : base) {
            gatherLog(pos);
        }

        logFinder = new LogFinder(logsWorld, base, maxNumLogs);
    }

    private boolean gatherLog(BlockPos pos) {
        if (logs.add(pos)) {
            mass += ChopUtil.getSupportFactor(level, pos);
        }
        return true;
    }

    private boolean check(BlockPos pos, Predicate<BlockPos> logFilter, Predicate<BlockPos> leavesFilter) {
        if (leavesFilter.test(pos)) {
            leaves.add(pos);
        }
        return logFilter.test(pos);
    }

    @Override
    public boolean hasLeaves() {
        if (!leaves.isEmpty()) {
            return true;
        }
        return logFinder.find().anyMatch(p -> !leaves.isEmpty());
    }

    @Override
    public Stream<BlockPos> streamLogs() {
        return Stream.concat(logs.stream(), logFinder.find());
    }

    @Override
    public Stream<BlockPos> streamLeaves() {
        List<BlockPos> allLeaves = new LinkedList<>();
        forEachLeaves(leaves, allLeaves::add);
        return allLeaves.stream();
    }

    private void completeTree() {
        logFinder.find().count();
    }

    @Override
    public void forEachLeaves(Consumer<BlockPos> consumer) {
        forEachLeaves(leaves, consumer);
    }

    private void forEachLeaves(Collection<BlockPos> firstLeaves, Consumer<BlockPos> forEach) {
        completeTree(); // Make sure all log-adjacent leaves are discovered

        AtomicInteger leavesCount = new AtomicInteger(0);
        int maxLeaves = ModConfig.get().maxLeavesBlocks;

        Consumer<BlockPos> limitingConsumer = pos -> {
            if (leavesCount.getAndIncrement() < maxLeaves) {
                forEach.accept(pos);
            }
        };

        if (smartDetection) {
            forEachLeavesSmart(firstLeaves, limitingConsumer);
        } else {
            forEachLeavesDumb(firstLeaves, limitingConsumer);
        }
    }

    private void forEachLeavesSmart(Collection<BlockPos> firstLeaves, Consumer<BlockPos> forEach) {
        leaves.stream().filter(pos -> leavesHasExactDistance(level.getBlockState(pos), 1)).forEach(forEach);

        AtomicInteger highestDistance = new AtomicInteger(maxLeavesDistance);
        AtomicInteger distance = new AtomicInteger();
        DirectedGraph<BlockPos> distancedLeavesGraph = GraphUtil.filterNeighbors(
                leavesWorld,
                pos -> {
                    BlockState state = level.getBlockState(pos);
                    state.getOptionalValue(LeavesBlock.DISTANCE).ifPresent(d -> {
                        if (d > highestDistance.get()) {
                            highestDistance.set(d);
                        }
                    });

                    return leavesHasAtLeastDistance(state, distance.get());
                }
        );

        FloodFillImpl<BlockPos> flood = new FloodFillImpl<>(firstLeaves, distancedLeavesGraph, a -> 0);

        for (int i = 2; i <= highestDistance.get(); ++i) {
            distance.set(i);
            flood.fillOnce(forEach);
        }
    }

    private void forEachLeavesDumb(Collection<BlockPos> firstLeaves, Consumer<BlockPos> forEach) {
        FloodFillImpl<BlockPos> flood = new FloodFillImpl<>(firstLeaves, leavesWorld, a -> 0);

        for (int i = 0; i < maxLeavesDistance; ++i) {
            flood.fillOnce(forEach);
        }
    }

    @Override
    public int numChopsNeededToFell() {
        completeTree();
        return ChopUtil.numChopsToFell(mass);
    }

    public Level getLevel() {
        return level;
    }

    private boolean leavesHasExactDistance(BlockState state, int distance) {
        return state.getOptionalValue(LeavesBlock.DISTANCE).orElse(distance) == distance || state.getOptionalValue(LeavesBlock.PERSISTENT).orElse(false);
    }

    private boolean leavesHasAtLeastDistance(BlockState state, int distance) {
        return state.getOptionalValue(LeavesBlock.DISTANCE).orElse(distance) >= distance || state.getOptionalValue(LeavesBlock.PERSISTENT).orElse(false);
    }

    private void makeTreeBase(Level level, BlockPos origin) {
        base = new HashSet<>();
        if (ChopUtil.isBlockChoppable(level, origin)) {
            base.add(origin);
        }
    }

    private static class LogFinder {
        private final int maxSize;
        FloodFill<BlockPos> flood;
        private int size = 0;

        public LogFinder(DirectedGraph<BlockPos> logsWorld, Set<BlockPos> base, int maxSize) {
            flood = GraphUtil.flood(logsWorld, base, v -> -v.getY());
            this.maxSize = maxSize;
        }

        public Stream<BlockPos> find() {
            int n = size;
            return flood.fill().peek(a -> ++size).limit(maxSize - n);
        }
    }
}