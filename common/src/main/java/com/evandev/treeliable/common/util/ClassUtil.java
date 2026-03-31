package com.evandev.treeliable.common.util;

import com.evandev.treeliable.Treeliable;
import com.evandev.treeliable.api.*;
import com.evandev.treeliable.common.config.ModConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ClassUtil {

    /**
     * Helper method to reduce type-checking and registry lookups.
     */
    @Nullable
    private static <T> T getBehavior(Block block, Class<T> clazz) {
        if (clazz.isInstance(block)) {
            return clazz.cast(block);
        }
        ITreeliableBlockBehavior behavior = Treeliable.api.getRegisteredBlockBehavior(block);
        if (clazz.isInstance(behavior)) {
            return clazz.cast(behavior);
        }
        return null;
    }

    @Nullable
    public static IChoppableBlock getChoppableBlock(BlockGetter level, BlockPos blockPos, BlockState blockState) {
        IChoppableBlock choppableBlock = getChoppableBlockUnchecked(blockState.getBlock());
        return (choppableBlock != null && choppableBlock.isChoppable(level, blockPos, blockState))
                ? choppableBlock
                : null;
    }

    @Nullable
    public static IChoppableBlock getChoppableBlockUnchecked(Block block) {
        IChoppableBlock behavior = getBehavior(block, IChoppableBlock.class);
        if (behavior != null) {
            return behavior;
        }

        if (ModConfig.get().choppableBlocksCache.get().contains(block)) {
            return new IChoppableBlock() {
                @Override
                public void chop(Player player, ItemStack tool, Level level, BlockPos pos, BlockState blockState, int numChops, boolean felling) {
                    if (!felling) {
                        LevelUtil.harvestBlock(player, level, pos, tool, true);
                    }
                }

                @Override
                public int getNumChops(BlockGetter level, BlockPos pos, BlockState blockState) {
                    return 0;
                }

                @Override
                public int getMaxNumChops(BlockGetter level, BlockPos blockPos, BlockState blockState) {
                    return 1;
                }
            };
        }

        return null;
    }

    @Nullable
    public static IFellableBlock getFellableBlock(Block block) {
        return getBehavior(block, IFellableBlock.class);
    }

    @Nullable
    public static ITreeBlock getTreeBlock(Block block) {
        return getBehavior(block, ITreeBlock.class);
    }

    @Nullable
    public static ILeaveslikeBlock getLeaveslikeBlock(Block block) {
        return getBehavior(block, ILeaveslikeBlock.class);
    }

    @Nullable
    public static IChoppingItem getChoppingItem(Item item) {
        if (item instanceof IChoppingItem choppingItem) {
            return choppingItem;
        }
        return Treeliable.api.getRegisteredChoppingItemBehavior(item);
    }
}