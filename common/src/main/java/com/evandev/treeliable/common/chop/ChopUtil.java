package com.evandev.treeliable.common.chop;

import com.evandev.treeliable.Treeliable;
import com.evandev.treeliable.TreeliableException;
import com.evandev.treeliable.api.*;
import com.evandev.treeliable.common.config.*;
import com.evandev.treeliable.common.settings.ChopSettings;
import com.evandev.treeliable.common.util.ClassUtil;
import com.evandev.treeliable.common.util.PlacedLogTracker;
import com.evandev.treeliable.platform.Services;
import com.evandev.treeliable.platform.server.Server;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;
import java.util.stream.Stream;

public class ChopUtil {

    public static final Lazy<ITreeBlock> defaultDetector = new Lazy<>(
            () -> new TreeDetectorBuilder().build()
    );

    public static boolean isBlockALog(Level level, BlockPos pos) {
        return isBlockALog(level, pos, level.getBlockState(pos));
    }

    public static boolean isBlockALog(Level level, BlockPos pos, BlockState blockState) {
        return isBlockChoppable(level, pos, blockState) && !PlacedLogTracker.isPlayerPlaced(level, pos);
    }

    public static boolean isBlockChoppable(Level level, BlockPos pos) {
        return isBlockChoppable(level, pos, level.getBlockState(pos));
    }

    public static boolean isBlockChoppable(BlockGetter level, BlockPos pos, BlockState blockState) {
        return ClassUtil.getChoppableBlock(level, pos, blockState) != null;
    }

    public static boolean isBlockLeaves(Level level, BlockPos pos, BlockState state) {
        return isBlockLeaves(state);
    }

    public static boolean isBlockLeaves(Level level, BlockPos pos) {
        return isBlockLeaves(level.getBlockState(pos));
    }

    public static boolean isBlockLeaves(BlockState blockState) {
        TreeLeavesBehavior behavior = ModConfig.get().leavesBlocksCache.get().get(blockState.getBlock());
        return behavior != null && behavior.isLeaves(blockState);
    }

    public static int numChopsToFell(double support) {
        return ChopCounting.calculate(Math.max(1, Double.valueOf(support).intValue()));
    }

    public static int numChopsToFell(Level level, Stream<BlockPos> logs) {
        double support = logs.map(pos -> getSupportFactor(level, pos))
                .reduce(Double::sum)
                .orElse(1.0);

        return numChopsToFell(support);
    }

    public static Optional<Double> getSupportFactor(Level level, Stream<BlockPos> blocks) {
        return blocks.map(pos -> ChopUtil.getSupportFactor(level, pos)).reduce(Double::sum);
    }

    public static double getSupportFactor(Level level, BlockPos pos) {
        return getSupportFactor(level, pos, level.getBlockState(pos));
    }

    private static double getSupportFactor(Level level, BlockPos pos, BlockState state) {
        return (level.getBlockState(pos).getBlock() instanceof IFellableBlock block)
                ? block.getSupportFactor(level, pos, state)
                : 1.0;
    }

    private static ChopResult getChopResult(Level level, BlockPos origin, TreeData tree, boolean breakLeaves) {
        if (tree.streamLogs().findFirst().isEmpty()) {
            return ChopResult.IGNORED;
        }

        if (ModConfig.get().hytaleLikeFelling) {
            boolean hasOtherLogsInLayer = tree.getLogBlocks().stream()
                    .anyMatch(p -> p.getY() == origin.getY() && !p.equals(origin));

            if (hasOtherLogsInLayer) {
                return ChopResult.IGNORED;
            }
        }

        return new FellTreeResult(level, tree, breakLeaves);
    }

    public static TreeData getTree(Level level, BlockPos origin) {
        ITreeBlock detector = ClassUtil.getTreeBlock(getLogBlock(level, origin));
        if (detector == null) {
            detector = defaultDetector.get();
        }

        TreeData tree = detector.getTree(level, origin);

        return Services.PLATFORM.detectTreeEvent(level, null, origin, level.getBlockState(origin), tree);
    }

    public static int blockDistance(BlockPos a, BlockPos b) {
        return a.distManhattan(b);
    }

    public static int horizontalBlockDistance(BlockPos a, BlockPos b) {
        return new Vec3i(a.getX(), 0, a.getZ()).distManhattan(new Vec3i(b.getX(), 0, b.getZ()));
    }

    public static boolean canChopWithTool(Player player, Level level, BlockPos pos) {
        return canChopWithTool(player, player.getMainHandItem(), level, pos, level.getBlockState(pos));

    }

    public static boolean canChopWithTool(Player player, ItemStack tool, Level level, BlockPos pos, BlockState blockState) {
        IChoppingItem choppingItem = Treeliable.api.getRegisteredChoppingItemBehavior(tool.getItem());

        boolean isBlacklisted = ModConfig.get().itemsBlacklistOrWhitelist.accepts(
                ModConfig.get().choppingItemsCache.get().contains(tool.getItem())
        );

        boolean canChop = (choppingItem != null) ? choppingItem.canChop(player, tool, level, pos, blockState) : isBlacklisted;

        return (!ModConfig.get().mustUseCorrectToolForDrops || !blockState.requiresCorrectToolForDrops() || tool.isCorrectToolForDrops(blockState))
                && (!ModConfig.get().mustUseFastBreakingTool || tool.getDestroySpeed(blockState) > 1f)
                && canChop;
    }

    public static int getNumChopsByTool(ItemStack tool, BlockState blockState) {
        IChoppingItem choppingItem = ClassUtil.getChoppingItem(tool.getItem());
        if (choppingItem != null) {
            return choppingItem.getNumChops(tool, blockState);
        } else {
            return 1;
        }
    }

    public static boolean playerWantsToChop(Player player, ChopSettings chopSettings) {
        if (ModConfig.get().enabled && (player != null && !player.isCreative() || chopSettings.getChopInCreativeMode())) {
            return chopSettings.getChoppingEnabled() ^ chopSettings.getSneakBehavior().shouldChangeChopBehavior(player);
        } else {
            return false;
        }
    }

    public static boolean chop(ServerPlayer agent, ServerLevel level, BlockPos pos, BlockState blockState, ItemStack tool, Object trigger) throws TreeliableException {
        ChopSettings chopSettings = Server.instance().getPlayerChopData(agent).getSettings();
        if (ChopUtil.playerWantsToChop(agent, chopSettings)) {
            int numChops = ChopUtil.getNumChopsByTool(tool, blockState);
            boolean treesMustHaveLeaves = chopSettings.getTreesMustHaveLeaves();
            return chop(agent, level, pos, blockState, tool, trigger, numChops, treesMustHaveLeaves);
        }

        return false;
    }

    public static boolean chop(ServerPlayer agent, ServerLevel level, BlockPos pos, BlockState blockState, ItemStack tool, Object trigger, int numChops, boolean treesMustHaveLeaves) throws TreeliableException {
        try {
            return chopUnsafe(agent, level, pos, blockState, tool, trigger, numChops, treesMustHaveLeaves);
        } catch (Exception e) {
            throw new TreeliableException(String.format("Parameters: %s, %s, %s, %s, %s, %s, %s, %s", agent, level, pos, blockState, tool, trigger, numChops, treesMustHaveLeaves), e);
        }
    }

    public static boolean chopUnsafe(ServerPlayer agent, ServerLevel level, BlockPos pos, BlockState blockState, ItemStack tool, Object trigger, int numChops, boolean treesMustHaveLeaves) {

        if (!isBlockChoppable(level, pos, blockState)
                || !ChopUtil.canChopWithTool(agent, tool, level, pos, blockState)) {
            return false;
        }

        TreeData tree = getTree(level, pos);
        ChopData chopData = new ChopDataImpl(numChops, tree);

        boolean doChop = Services.PLATFORM.startChopEvent(agent, level, pos, blockState, chopData, trigger);
        if (!doChop) {
            return false;
        }

        ChopResult chopResult = ChopUtil.getChopResult(
                level,
                pos,
                tree,
                ModConfig.get().fellLeavesStrategy != FellLeavesStrategy.IGNORE
        );

        if (chopResult != ChopResult.IGNORED) {
            chopResult.apply(pos, agent, tool);
            Services.PLATFORM.finishChopEvent(agent, level, pos, blockState, chopData, chopResult);
            tool.mineBlock(level, blockState, pos, agent);
        }

        return false;
    }

    public static BlockState getLogState(Level level, BlockPos pos) {
        return level.getBlockState(pos);
    }

    public static Block getLogBlock(Level level, BlockPos pos) {
        return getLogBlock(level, pos, level.getBlockState(pos));
    }

    public static Block getLogBlock(Level level, BlockPos pos, BlockState state) {
        return state.getBlock();
    }
}