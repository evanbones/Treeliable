package com.evandev.treeliable.common.registry;

import com.evandev.treeliable.Treeliable;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class FabricModBlocks {
    public static final TagKey<Block> CHOPPABLES = TagKey.create(Registries.BLOCK, Treeliable.resource("choppables"));
}
