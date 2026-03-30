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
    // TODO: get rid of this vile hackery. Build block(state) lists instead of type checking.

    @Nullable
    public static IChoppableBlock getChoppableBlock(BlockGetter level, BlockPos blockPos, BlockState blockState) {
        IChoppableBlock choppableBlock = getChoppableBlockUnchecked(blockState.getBlock());
        return (choppableBlock != null && choppableBlock.isChoppable(level, blockPos, blockState))
                ? choppableBlock
                : null;
    }

    @Nullable
    public static IChoppableBlock getChoppableBlockUnchecked(Block block) {
        if (block instanceof IChoppableBlock choppableBlock) {
            return choppableBlock;
        } else if (Treeliable.api.getRegisteredChoppableBlockBehavior(block) instanceof IChoppableBlock choppableBlock) {
            return choppableBlock;
        } else if (ModConfig.get().choppableBlocksCache.get().contains(block)) {
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
        } else {
            return null;
        }
    }

    @Nullable
    public static IFellableBlock getFellableBlock(Block block) {
        if (block instanceof IFellableBlock fellableBlock) {
            return fellableBlock;
        } else if (Treeliable.api.getRegisteredChoppableBlockBehavior(block) instanceof IFellableBlock fellableBlock) {
            return fellableBlock;
        } else {
            return null;
        }
    }

    @Nullable
    public static IThwackableBlock getThwackableBlock(Block block) {
        if (block instanceof IThwackableBlock thwackableBlock) {
            return thwackableBlock;
        } else if (Treeliable.api.getRegisteredChoppableBlockBehavior(block) instanceof IThwackableBlock thwackableBlock) {
            return thwackableBlock;
        } else {
            return null;
        }
    }

    @Nullable
    public static ITreeBlock getTreeBlock(Block block) {
        if (block instanceof ITreeBlock treeBlock) {
            return treeBlock;
        } else if (Treeliable.api.getRegisteredChoppableBlockBehavior(block) instanceof ITreeBlock treeBlock) {
            return treeBlock;
        } else {
            return null;
        }
    }

    @Nullable
    public static ILeaveslikeBlock getLeaveslikeBlock(Block block) {
        if (block instanceof ILeaveslikeBlock leaveslikeBlock) {
            return leaveslikeBlock;
        } else if (Treeliable.api.getRegisteredBlockBehavior(block) instanceof ILeaveslikeBlock leaveslikeBlock) {
            return leaveslikeBlock;
        } else {
            return null;
        }
    }

    public static IChoppingItem getChoppingItem(Item item) {
        if (item instanceof IChoppingItem choppingItem) {
            return choppingItem;
        } else {
            return Treeliable.api.getRegisteredChoppingItemBehavior(item);
        }
    }
}
