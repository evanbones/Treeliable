package com.evandev.treeliable.common.chop;

import com.evandev.treeliable.Treeliable;
import com.evandev.treeliable.api.IChoppableBlock;
import com.evandev.treeliable.common.util.ClassUtil;
import com.evandev.treeliable.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public record Chop(BlockPos blockPos, int numChops) {

    public void apply(Level level, Player player, ItemStack tool, boolean felling) {
        BlockState blockState = level.getBlockState(blockPos);
        IChoppableBlock choppableBlock = ClassUtil.getChoppableBlock(level, blockPos, blockState);
        if (choppableBlock != null) {
            choppableBlock.chop(player, tool, level, blockPos, blockState, numChops, felling);
        } else {
            Treeliable.LOGGER.warn("Failed to chop block in level {} at position {} for player {}: {} is not choppable", level.dimension(), blockPos, player.getName(), Services.PLATFORM.getResourceLocationForBlock(blockState.getBlock()));
        }
    }
}
