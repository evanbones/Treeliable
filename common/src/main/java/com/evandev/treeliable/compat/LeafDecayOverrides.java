package com.evandev.treeliable.compat;

import com.evandev.treeliable.api.ILeaveslikeBlock;
import com.evandev.treeliable.api.ITreeChopBlockBehavior;
import com.evandev.treeliable.api.TreeChopAPI;
import com.evandev.treeliable.common.config.Lazy;
import com.evandev.treeliable.common.config.ModConfig;
import com.evandev.treeliable.common.util.LevelUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.Set;
import java.util.stream.Collectors;

public class LeafDecayOverrides {

    private static final Lazy<Set<Block>> nondecayableLeaves = new Lazy<>(() -> ModConfig.getIdentifiedBlocks(ModConfig.get().leafDecayExceptions).collect(Collectors.toSet()));

    public static void register(TreeChopAPI api) {
        ITreeChopBlockBehavior handler = (ILeaveslikeBlock) (player, level, pos, blockState) ->
                LevelUtil.harvestBlock(player, level, pos, ItemStack.EMPTY, false);

        nondecayableLeaves.get().forEach(block -> {
            api.overrideLeavesBlock(block, true);
            api.registerBlockBehavior(block, handler);
        });
    }
}
