package com.evandev.treeliable.client.integration;

import com.evandev.treeliable.common.config.*;
import com.evandev.treeliable.common.settings.SneakBehavior;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.List;

public class ClothConfigIntegration {

    public static Screen createScreen(Screen parent) {
        ModConfig config = ModConfig.get();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("treeliable.config.title"));

        builder.setSavingRunnable(ModConfig::save);

        ConfigEntryBuilder eb = builder.entryBuilder();

        ConfigCategory general = builder.getOrCreateCategory(Component.translatable("treeliable.config.category.general"));

        general.addEntry(eb.startBooleanToggle(Component.translatable("treeliable.config.enabled"), config.enabled)
                .setTooltip(Component.translatable("treeliable.config.enabled.tooltip"))
                .setDefaultValue(true).setSaveConsumer(v -> config.enabled = v).build());
        general.addEntry(eb.startBooleanToggle(Component.translatable("treeliable.config.chopping_enabled"), config.choppingEnabled)
                .setTooltip(Component.translatable("treeliable.config.chopping_enabled.tooltip"))
                .setDefaultValue(true).setSaveConsumer(v -> config.choppingEnabled = v).build());
        general.addEntry(eb.startEnumSelector(Component.translatable("treeliable.config.sneak_behavior"), SneakBehavior.class, config.sneakBehavior)
                .setTooltip(Component.translatable("treeliable.config.sneak_behavior.tooltip"))
                .setDefaultValue(SneakBehavior.INVERT_CHOPPING).setSaveConsumer(v -> config.sneakBehavior = v).build());
        general.addEntry(eb.startBooleanToggle(Component.translatable("treeliable.config.chop_in_creative_mode"), config.chopInCreativeMode)
                .setTooltip(Component.translatable("treeliable.config.chop_in_creative_mode.tooltip"))
                .setDefaultValue(false).setSaveConsumer(v -> config.chopInCreativeMode = v).build());
        general.addEntry(eb.startBooleanToggle(Component.translatable("treeliable.config.drop_loot_for_chopped_blocks"), config.dropLootForChoppedBlocks)
                .setTooltip(Component.translatable("treeliable.config.drop_loot_for_chopped_blocks.tooltip"))
                .setDefaultValue(true).setSaveConsumer(v -> config.dropLootForChoppedBlocks = v).build());
        general.addEntry(eb.startBooleanToggle(Component.translatable("treeliable.config.must_use_correct_tool_for_drops"), config.mustUseCorrectToolForDrops)
                .setTooltip(Component.translatable("treeliable.config.must_use_correct_tool_for_drops.tooltip"))
                .setDefaultValue(true).setSaveConsumer(v -> config.mustUseCorrectToolForDrops = v).build());
        general.addEntry(eb.startBooleanToggle(Component.translatable("treeliable.config.must_use_fast_breaking_tool"), config.mustUseFastBreakingTool)
                .setTooltip(Component.translatable("treeliable.config.must_use_fast_breaking_tool.tooltip"))
                .setDefaultValue(true).setSaveConsumer(v -> config.mustUseFastBreakingTool = v).build());
        general.addEntry(eb.startBooleanToggle(Component.translatable("treeliable.config.prevent_chopping_on_right_click"), config.preventChoppingOnRightClick)
                .setTooltip(Component.translatable("treeliable.config.prevent_chopping_on_right_click.tooltip"))
                .setDefaultValue(false).setSaveConsumer(v -> config.preventChoppingOnRightClick = v).build());
        general.addEntry(eb.startBooleanToggle(Component.translatable("treeliable.config.prevent_chop_recursion"), config.preventChopRecursion)
                .setTooltip(Component.translatable("treeliable.config.prevent_chop_recursion.tooltip"))
                .setDefaultValue(true).setSaveConsumer(v -> config.preventChopRecursion = v).build());

        ConfigCategory limits = builder.getOrCreateCategory(Component.translatable("treeliable.config.category.limits"));

        limits.addEntry(eb.startBooleanToggle(Component.translatable("treeliable.config.trees_must_have_leaves"), config.treesMustHaveLeaves)
                .setTooltip(Component.translatable("treeliable.config.trees_must_have_leaves.tooltip"))
                .setDefaultValue(true).setSaveConsumer(v -> config.treesMustHaveLeaves = v).build());
        limits.addEntry(eb.startIntField(Component.translatable("treeliable.config.max_tree_blocks"), config.maxTreeBlocks)
                .setTooltip(Component.translatable("treeliable.config.max_tree_blocks.tooltip"))
                .setDefaultValue(1024).setSaveConsumer(v -> config.maxTreeBlocks = v).build());
        limits.addEntry(eb.startIntField(Component.translatable("treeliable.config.max_leaves_blocks"), config.maxLeavesBlocks)
                .setTooltip(Component.translatable("treeliable.config.max_leaves_blocks.tooltip"))
                .setDefaultValue(1024).setSaveConsumer(v -> config.maxLeavesBlocks = v).build());
        limits.addEntry(eb.startIntField(Component.translatable("treeliable.config.max_break_leaves_distance"), config.maxBreakLeavesDistance)
                .setTooltip(Component.translatable("treeliable.config.max_break_leaves_distance.tooltip"))
                .setDefaultValue(7).setSaveConsumer(v -> config.maxBreakLeavesDistance = v).build());
        limits.addEntry(eb.startBooleanToggle(Component.translatable("treeliable.config.ignore_persistent_leaves"), config.ignorePersistentLeaves)
                .setTooltip(Component.translatable("treeliable.config.ignore_persistent_leaves.tooltip"))
                .setDefaultValue(true).setSaveConsumer(v -> config.ignorePersistentLeaves = v).build());
        limits.addEntry(eb.startEnumSelector(Component.translatable("treeliable.config.fell_leaves_strategy"), FellLeavesStrategy.class, config.fellLeavesStrategy)
                .setTooltip(Component.translatable("treeliable.config.fell_leaves_strategy.tooltip"))
                .setDefaultValue(FellLeavesStrategy.DECAY).setSaveConsumer(v -> config.fellLeavesStrategy = v).build());
        limits.addEntry(eb.startEnumSelector(Component.translatable("treeliable.config.fell_credit_strategy"), FellCreditStrategy.class, config.fellCreditStrategy)
                .setTooltip(Component.translatable("treeliable.config.fell_credit_strategy.tooltip"))
                .setDefaultValue(FellCreditStrategy.NONE).setSaveConsumer(v -> config.fellCreditStrategy = v).build());
        limits.addEntry(eb.startBooleanToggle(Component.translatable("treeliable.config.damage_tool_per_log"), config.damageToolPerLog)
                .setTooltip(Component.translatable("treeliable.config.damage_tool_per_log.tooltip"))
                .setDefaultValue(true).setSaveConsumer(v -> config.damageToolPerLog = v).build());
        limits.addEntry(eb.startBooleanToggle(Component.translatable("treeliable.config.exhaustion_per_log"), config.exhaustionPerLog)
                .setTooltip(Component.translatable("treeliable.config.exhaustion_per_log.tooltip"))
                .setDefaultValue(true).setSaveConsumer(v -> config.exhaustionPerLog = v).build());

        ConfigCategory algorithm = builder.getOrCreateCategory(Component.translatable("treeliable.config.category.algorithm"));

        algorithm.addEntry(eb.startEnumSelector(Component.translatable("treeliable.config.chop_counting_algorithm"), ChopCountingAlgorithm.class, config.chopCountingAlgorithm)
                .setTooltip(Component.translatable("treeliable.config.chop_counting_algorithm.tooltip"))
                .setDefaultValue(ChopCountingAlgorithm.LOGARITHMIC).setSaveConsumer(v -> config.chopCountingAlgorithm = v).build());
        algorithm.addEntry(eb.startEnumSelector(Component.translatable("treeliable.config.chop_count_rounding"), Rounder.class, config.chopCountRounding)
                .setTooltip(Component.translatable("treeliable.config.chop_count_rounding.tooltip"))
                .setDefaultValue(Rounder.NEAREST).setSaveConsumer(v -> config.chopCountRounding = v).build());
        algorithm.addEntry(eb.startBooleanToggle(Component.translatable("treeliable.config.can_require_more_chops_than_blocks"), config.canRequireMoreChopsThanBlocks)
                .setTooltip(Component.translatable("treeliable.config.can_require_more_chops_than_blocks.tooltip"))
                .setDefaultValue(false).setSaveConsumer(v -> config.canRequireMoreChopsThanBlocks = v).build());
        algorithm.addEntry(eb.startDoubleField(Component.translatable("treeliable.config.logarithmic_a"), config.logarithmicA)
                .setTooltip(Component.translatable("treeliable.config.logarithmic_a.tooltip"))
                .setDefaultValue(10.0).setSaveConsumer(v -> config.logarithmicA = v).build());
        algorithm.addEntry(eb.startDoubleField(Component.translatable("treeliable.config.linear_m"), config.linearM)
                .setTooltip(Component.translatable("treeliable.config.linear_m.tooltip"))
                .setDefaultValue(1.0).setSaveConsumer(v -> config.linearM = v).build());
        algorithm.addEntry(eb.startDoubleField(Component.translatable("treeliable.config.linear_b"), config.linearB)
                .setTooltip(Component.translatable("treeliable.config.linear_b.tooltip"))
                .setDefaultValue(0.0).setSaveConsumer(v -> config.linearB = v).build());

        ConfigCategory visuals = builder.getOrCreateCategory(Component.translatable("treeliable.config.category.visuals"));

        visuals.addEntry(eb.startBooleanToggle(Component.translatable("treeliable.config.show_chopping_indicators"), config.showChoppingIndicators)
                .setTooltip(Component.translatable("treeliable.config.show_chopping_indicators.tooltip"))
                .setDefaultValue(true).setSaveConsumer(v -> config.showChoppingIndicators = v).build());
        visuals.addEntry(eb.startIntField(Component.translatable("treeliable.config.indicator_x_offset"), config.indicatorXOffset)
                .setTooltip(Component.translatable("treeliable.config.indicator_x_offset.tooltip"))
                .setDefaultValue(16).setSaveConsumer(v -> config.indicatorXOffset = v).build());
        visuals.addEntry(eb.startIntField(Component.translatable("treeliable.config.indicator_y_offset"), config.indicatorYOffset)
                .setTooltip(Component.translatable("treeliable.config.indicator_y_offset.tooltip"))
                .setDefaultValue(0).setSaveConsumer(v -> config.indicatorYOffset = v).build());
        visuals.addEntry(eb.startBooleanToggle(Component.translatable("treeliable.config.show_feedback_messages"), config.showFeedbackMessages)
                .setTooltip(Component.translatable("treeliable.config.show_feedback_messages.tooltip"))
                .setDefaultValue(true).setSaveConsumer(v -> config.showFeedbackMessages = v).build());
        visuals.addEntry(eb.startBooleanToggle(Component.translatable("treeliable.config.verbose_api"), config.verboseAPI)
                .setTooltip(Component.translatable("treeliable.config.verbose_api.tooltip"))
                .setDefaultValue(false).setSaveConsumer(v -> config.verboseAPI = v).build());

        ConfigCategory compat = builder.getOrCreateCategory(Component.translatable("treeliable.config.category.compat"));

        compat.addEntry(eb.startBooleanToggle(Component.translatable("treeliable.config.compat_for_apotheosis"), config.compatForApotheosis)
                .setTooltip(Component.translatable("treeliable.config.compat_for_apotheosis.tooltip"))
                .setDefaultValue(true).setSaveConsumer(v -> config.compatForApotheosis = v).build());
        compat.addEntry(eb.startBooleanToggle(Component.translatable("treeliable.config.compat_for_silentgear"), config.compatForSilentGear)
                .setTooltip(Component.translatable("treeliable.config.compat_for_silentgear.tooltip"))
                .setDefaultValue(true).setSaveConsumer(v -> config.compatForSilentGear = v).build());
        compat.addEntry(eb.startIntField(Component.translatable("treeliable.config.silentgear_saw_chops"), config.silentGearSawChops)
                .setTooltip(Component.translatable("treeliable.config.silentgear_saw_chops.tooltip"))
                .setDefaultValue(5).setSaveConsumer(v -> config.silentGearSawChops = v).build());

        ConfigCategory lists = builder.getOrCreateCategory(Component.translatable("treeliable.config.category.lists"));

        lists.addEntry(eb.startEnumSelector(Component.translatable("treeliable.config.items_filter_type"), ListType.class, config.itemsBlacklistOrWhitelist)
                .setTooltip(Component.translatable("treeliable.config.items_filter_type.tooltip"))
                .setDefaultValue(ListType.BLACKLIST).setSaveConsumer(v -> config.itemsBlacklistOrWhitelist = v).build());
        lists.addEntry(eb.startStrList(Component.translatable("treeliable.config.choppable_blocks"), config.choppableBlocks)
                .setTooltip(Component.translatable("treeliable.config.choppable_blocks.tooltip"))
                .setDefaultValue(Arrays.asList("#treeliable:choppables", "#minecraft:logs")).setSaveConsumer(v -> config.choppableBlocks = v).build());
        lists.addEntry(eb.startStrList(Component.translatable("treeliable.config.choppable_blocks_exceptions"), config.choppableBlocksExceptions)
                .setTooltip(Component.translatable("treeliable.config.choppable_blocks_exceptions.tooltip"))
                .setDefaultValue(Arrays.asList("minecraft:bamboo", "#dynamictrees:branches", "dynamictrees:trunk_shell")).setSaveConsumer(v -> config.choppableBlocksExceptions = v).build());
        lists.addEntry(eb.startStrList(Component.translatable("treeliable.config.leaves_blocks"), config.leavesBlocks)
                .setTooltip(Component.translatable("treeliable.config.leaves_blocks.tooltip"))
                .setDefaultValue(Arrays.asList("#treeliable:leaves_like", "#minecraft:leaves")).setSaveConsumer(v -> config.leavesBlocks = v).build());
        lists.addEntry(eb.startStrList(Component.translatable("treeliable.config.leaves_blocks_exceptions"), config.leavesBlocksExceptions)
                .setTooltip(Component.translatable("treeliable.config.leaves_blocks_exceptions.tooltip"))
                .setDefaultValue(List.of()).setSaveConsumer(v -> config.leavesBlocksExceptions = v).build());
        lists.addEntry(eb.startStrList(Component.translatable("treeliable.config.chopping_items"), config.choppingItems)
                .setTooltip(Component.translatable("treeliable.config.chopping_items.tooltip"))
                .setDefaultValue(Arrays.asList("botania:terra_axe", "mekanism:atomic_disassembler", "twilightforest:giant_pickaxe")).setSaveConsumer(v -> config.choppingItems = v).build());

        return builder.build();
    }
}