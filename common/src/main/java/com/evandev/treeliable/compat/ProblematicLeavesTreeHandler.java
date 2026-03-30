package com.evandev.treeliable.compat;

import com.evandev.treeliable.api.ITreeBlock;
import com.evandev.treeliable.api.TreeChopAPI;
import com.evandev.treeliable.api.TreeData;
import com.evandev.treeliable.api.TreeDetectorBuilder;
import com.evandev.treeliable.common.config.Lazy;
import com.evandev.treeliable.common.config.ModConfig;
import com.evandev.treeliable.common.util.BlockNeighbors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;
import java.util.stream.Collectors;

public class ProblematicLeavesTreeHandler implements ITreeBlock {
    private static final Lazy<Set<Block>> logs = new Lazy<>(() -> ModConfig.getIdentifiedBlocks(ModConfig.get().problematicLeavesTreesLogs).collect(Collectors.toSet()));
    private static final Lazy<Set<Block>> leaves = new Lazy<>(() -> ModConfig.getIdentifiedBlocks(ModConfig.get().problematicLeavesTreesLeaves).collect(Collectors.toSet()));
    private static ITreeBlock detectionHandler;

    public static void register(TreeChopAPI api) {
        detectionHandler = new TreeDetectorBuilder()
                .logs(ProblematicLeavesTreeHandler::isLog)
                .leaves(ProblematicLeavesTreeHandler::isLeaves)
                .maxLeavesDistance(7)
                .leavesScanner((level, pos) -> BlockNeighbors.ADJACENTS_AND_DIAGONALS.asStream(pos))
                .leavesStrategy(TreeDetectorBuilder.LeavesStrategy.SHORTEST_PATH)
                .build();

        ProblematicLeavesTreeHandler handler = new ProblematicLeavesTreeHandler();
        logs.get().forEach(block -> {
            api.overrideChoppableBlock(block, true);
            api.registerBlockBehavior(block, handler);
        });
        leaves.get().forEach(block -> api.overrideLeavesBlock(block, true));
    }

    public static boolean isLog(Level level, BlockPos pos, BlockState state) {
        return logs.get().contains(state.getBlock());
    }

    public static boolean isLeaves(Level level, BlockPos pos, BlockState state) {
        return leaves.get().contains(state.getBlock());
    }

    @Override
    public TreeData getTree(Level level, BlockPos origin) {
        return detectionHandler.getTree(level, origin);
    }


}
