package com.evandev.treeliable.compat;

import com.evandev.treeliable.TreeChopException;
import com.evandev.treeliable.Treeliable;
import com.evandev.treeliable.api.TreeData;
import com.evandev.treeliable.client.Client;
import com.evandev.treeliable.common.chop.ChopUtil;
import com.evandev.treeliable.common.settings.ChopSettings;
import com.evandev.treeliable.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.stream.Collectors;

public class WailaUtil {
    @NotNull
    public static MutableComponent getPrefixedBlockName(BlockState state) {
        String originalBlockName = net.minecraft.locale.Language.getInstance().getOrDefault(
                state.getBlock().getDescriptionId()
        );
        return net.minecraft.network.chat.Component.translatable("treechop.waila.chopped_x", originalBlockName);
    }

    public static BlockState getLogState(Level level, BlockPos pos, BlockState state) {
        return state;
    }

    public static boolean playerWantsTreeInfo(Level level, BlockPos pos, boolean showTreeBlocks, boolean showNumChops) {
        if (!Services.PLATFORM.isDedicatedServer()) {
            return playerWantsTreeInfo(level, pos, Client.getPlayer(), Client.getChopSettings(), showTreeBlocks, showNumChops);
        } else {
            return false;
        }
    }

    public static boolean playerWantsTreeInfo(Level level, BlockPos pos, Player player, ChopSettings chopSettings, boolean showTreeBlocks, boolean showNumChops) {
        return ChopUtil.playerWantsToChop(player, chopSettings)
                && ChopUtil.isBlockChoppable(level, pos, level.getBlockState(pos))
                && (showTreeBlocks || showNumChops);
    }

    public static void addTreeInfo(Level level,
                                   BlockPos pos,
                                   boolean showTreeBlocks,
                                   boolean showNumChops,
                                   Consumer<Component> addNumChops,
                                   Consumer<ItemStack> addTreeBlockStack) {
        try {
            addTreeInfoOrCrash(level, pos, showTreeBlocks, showNumChops, addNumChops, addTreeBlockStack);
        } catch (TreeChopException e) {
            Treeliable.cry(e);
        }
    }

    public static void addTreeInfoOrCrash(Level level,
                                          BlockPos pos,
                                          boolean showTreeBlocks,
                                          boolean showNumChops,
                                          Consumer<Component> addNumChops,
                                          Consumer<ItemStack> addTreeBlockStack) throws TreeChopException {

        try {
            TreeData tree = Client.treeCache.getTree(level, pos);

            if (tree.isAProperTree(Client.getChopSettings().getTreesMustHaveLeaves())) {
                if (showNumChops) {
                    addNumChops.accept(Component.translatable("treechop.waila.x_out_of_y_chops", tree.getChops(), ChopUtil.numChopsToFell(level, tree.streamLogs())));
                }

                if (showTreeBlocks) {
                    tree.streamLogs()
                            .collect(Collectors.groupingBy((BlockPos pos2) -> {
                                BlockState state = level.getBlockState(pos2);
                                return state.getBlock().getCloneItemStack(level, pos2, state).getItem();
                            }, Collectors.counting()))
                            .forEach((item, count) -> {
                                ItemStack stack = item.getDefaultInstance();
                                stack.setCount(count.intValue());
                                addTreeBlockStack.accept(stack);
                            });
                }
            }
        } catch (Exception e) {
            throw new TreeChopException(String.format("Parameters: %s, %s, %s, %s", level, pos, showTreeBlocks, showNumChops), e);
        }
    }
}
