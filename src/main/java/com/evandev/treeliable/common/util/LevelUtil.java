package com.evandev.treeliable.common.util;

import com.evandev.treeliable.common.config.ModConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class LevelUtil {
    public static void harvestBlock(Entity agent, Level level, BlockPos pos, ItemStack tool, boolean loud, boolean dropLoot) {
        if (level instanceof ServerLevel) {
            BlockState blockState = level.getBlockState(pos);

            // Plays particle and sound effects
            if (loud && agent instanceof Player player) {
                blockState.getBlock().playerWillDestroy(level, pos, blockState, player);
            }

            FluidState fluidStateOrAir = level.getFluidState(pos);
            blockState.getBlock().destroy(level, pos, blockState);

            // Check the config and the local random roll before dropping items
            if (ModConfig.get().dropLootForChoppedBlocks && dropLoot) {
                Block.dropResources(blockState, level, pos, level.getBlockEntity(pos), agent, tool);
            }

            level.setBlockAndUpdate(pos, fluidStateOrAir.createLegacyBlock());
        }
    }
}