package com.evandev.treeliable.common.util;

import com.evandev.treeliable.api.TreeData;
import com.evandev.treeliable.common.chop.ChopUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;

public class TreeCache {
    private final SingleBlockCache<TreeData> singleBlockCache = new SingleBlockCache<>();

    public void invalidate() {
        singleBlockCache.invalidate();
    }

    public TreeData getTree(Level level, BlockPos pos) {
        TreeData tree = singleBlockCache.get(level, pos);
        if (tree == null) {
            tree = ChopUtil.getTree(level, pos);
            singleBlockCache.put(level, pos, tree);
        }
        return tree;
    }

    private static class SingleBlockCache<T> {
        private T value;
        private BlockGetter level;
        private BlockState blockState;
        private BlockPos pos;

        public T get(BlockGetter level, BlockPos pos) {
            if (level == this.level && pos.equals(this.pos) && Objects.equals(level.getBlockState(pos), blockState)) {
                return value;
            } else {
                return null;
            }
        }

        public void put(BlockGetter level, BlockPos pos, T value) {
            this.value = value;
            this.level = level;
            this.pos = pos;
            this.blockState = level.getBlockState(pos);
        }

        public void invalidate() {
            level = null;
            value = null;
            blockState = Blocks.AIR.defaultBlockState();
        }
    }
}