package com.evandev.treeliable;

import com.evandev.treeliable.api.IChoppingItem;
import com.evandev.treeliable.api.ITreeliableBlockBehavior;
import com.evandev.treeliable.api.TreeData;
import com.evandev.treeliable.api.TreeliableAPI;
import com.evandev.treeliable.common.chop.ChopUtil;
import com.evandev.treeliable.common.config.ModConfig;
import com.evandev.treeliable.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class TreeliableInternalAPI implements TreeliableAPI {
    private static final Map<Block, ITreeliableBlockBehavior> blockBehaviors = new HashMap<>();

    private static final Map<Block, Boolean> choppableBlockOverrides = new HashMap<>() {
        @Override
        public Boolean put(Block block, Boolean isChoppable) {
            ModConfig.get().invalidateCaches();
            return super.put(block, isChoppable);
        }
    };

    private static final Map<Block, Boolean> leavesBlockOverrides = new HashMap<>() {
        @Override
        public Boolean put(Block block, Boolean isLeaves) {
            ModConfig.get().invalidateCaches();
            return super.put(block, isLeaves);
        }
    };

    private static final Map<Item, IChoppingItem> choppingItemBehaviors = new HashMap<>();
    private static final Map<Item, Boolean> choppingItemOverrides = new HashMap<>();

    private static final Marker API_MARKER = MarkerManager.getMarker("API");

    private final String modId;

    TreeliableInternalAPI(String modId) {
        this.modId = modId;
    }

    private void print(String message) {
        if (ModConfig.get().verboseAPI) {
            Treeliable.LOGGER.info(API_MARKER, "API [{}] {}", modId, message);
        }
    }

    @Override
    public void overrideChoppableBlock(Block block, boolean isChoppable) {
        choppableBlockOverrides.put(block, isChoppable);
        print(String.format("Set isChoppable=%s for block %s",
                isChoppable,
                Services.PLATFORM.getResourceLocationForBlock(block)));
    }

    @Override
    public void overrideLeavesBlock(Block block, boolean isLeaves) {
        leavesBlockOverrides.put(block, isLeaves);
        print(String.format("Set isLeaves=%s for block %s",
                isLeaves,
                Services.PLATFORM.getResourceLocationForBlock(block)));
    }

    @Override
    public void overrideChoppingItem(Item item, boolean canChop) {
        choppingItemOverrides.put(item, canChop);
        print(String.format("Set canChop=%s for item %s",
                canChop,
                Services.PLATFORM.getResourceLocationForItem(item)));
    }

    @Override
    public void registerBlockBehavior(Block block, ITreeliableBlockBehavior handler) {
        blockBehaviors.put(block, handler);
        print(String.format("Registered new behavior for block %s",
                Services.PLATFORM.getResourceLocationForBlock(block)));
    }

    @Override
    public boolean deregisterBlockBehavior(Block block) {
        ITreeliableBlockBehavior existing = blockBehaviors.remove(block);
        print(String.format("Deregistered behavior for block %s",
                Services.PLATFORM.getResourceLocationForBlock(block)));
        return existing == null;
    }

    @Override
    public ITreeliableBlockBehavior getRegisteredBlockBehavior(Block block) {
        return blockBehaviors.get(block);
    }

    @Override
    public void registerChoppingItemBehavior(Item item, IChoppingItem handler) {
        choppingItemBehaviors.put(item, handler);
        print(String.format("Registered new behavior for item %s",
                Services.PLATFORM.getResourceLocationForItem(item)));
    }

    @Override
    public boolean deregisterChoppingItemBehavior(Item item) {
        IChoppingItem existing = choppingItemBehaviors.remove(item);
        print(String.format("Deregistered behavior for item %s",
                Services.PLATFORM.getResourceLocationForItem(item)));
        return existing == null;
    }

    @Override
    public IChoppingItem getRegisteredChoppingItemBehavior(Item item) {
        return choppingItemBehaviors.get(item);
    }

    @Override
    public boolean isBlockChoppable(Level level, BlockPos pos, BlockState blockState) {
        return ChopUtil.isBlockALog(level, pos, blockState);
    }

    @Override
    public boolean isBlockLeaves(Level level, BlockPos pos, BlockState blockState) {
        return ChopUtil.isBlockLeaves(blockState);
    }

    @Override
    public boolean canChopWithItem(Player player, ItemStack stack, Level level, BlockPos pos, BlockState blockState) {
        return ChopUtil.canChopWithTool(player, stack, level, pos, blockState);
    }

    @Override
    public TreeData getTree(Level level, BlockPos pos) {
        return ChopUtil.getTree(level, pos);
    }

    public Stream<Pair<Block, Boolean>> getChoppableBlockOverrides() {
        return choppableBlockOverrides.entrySet().stream().map(entry -> Pair.of(entry.getKey(), entry.getValue()));
    }

    public Stream<Pair<Block, Boolean>> getLeavesBlockOverrides() {
        return leavesBlockOverrides.entrySet().stream().map(entry -> Pair.of(entry.getKey(), entry.getValue()));
    }

    public Stream<Pair<Item, Boolean>> getChoppingItemOverrides() {
        return choppingItemOverrides.entrySet().stream().map(entry -> Pair.of(entry.getKey(), entry.getValue()));
    }
}
