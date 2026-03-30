package com.evandev.treeliable.common.loot;

import com.evandev.treeliable.Treeliable;
import com.evandev.treeliable.mixin.LootContextParamSetsAccess;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.Set;
import java.util.stream.Collectors;

public class TreeliableLootContextParams {
    public static void init() {

    }

    public static final LootContextParam<Integer> BLOCK_CHOP_COUNT = new LootContextParam<>(Treeliable.resource("count_block_chops"));
    public static final LootContextParam<Boolean> DESTROY_BLOCK = new LootContextParam<>(Treeliable.resource("tree_felled"));

    public static final LootContextParamSet SET = LootContextParamSetsAccess.callRegister("treeliable", set -> {
        Set<LootContextParam<?>> required = LootContextParamSets.BLOCK.getRequired();
        Set<LootContextParam<?>> optional = LootContextParamSets.BLOCK.getAllowed().stream().filter(p -> !required.contains(p)).collect(Collectors.toSet());

        required.forEach(set::required);
        optional.forEach(set::optional);

        set.required(BLOCK_CHOP_COUNT).required(DESTROY_BLOCK);

        set.build();
    });
}
