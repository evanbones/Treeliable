package com.evandev.treeliable.client.integration;

import com.evandev.treeliable.common.config.*;
import com.evandev.treeliable.common.settings.SneakBehavior;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.List;

public class YaclConfigIntegration {

    public static Screen createScreen(Screen parent) {
        ModConfig config = ModConfig.get();

        YetAnotherConfigLib.Builder builder = YetAnotherConfigLib.createBuilder()
                .title(Component.translatable("treeliable.config.title"))
                .save(ModConfig::save);

        ConfigCategory.Builder general = ConfigCategory.createBuilder()
                .name(Component.translatable("treeliable.config.category.general"));

        general.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("treeliable.config.enabled"))
                .description(OptionDescription.of(Component.translatable("treeliable.config.enabled.tooltip")))
                .binding(true, () -> config.enabled, v -> config.enabled = v)
                .controller(TickBoxControllerBuilder::create)
                .build());
        general.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("treeliable.config.chopping_enabled"))
                .description(OptionDescription.of(Component.translatable("treeliable.config.chopping_enabled.tooltip")))
                .binding(true, () -> config.choppingEnabled, v -> config.choppingEnabled = v)
                .controller(TickBoxControllerBuilder::create)
                .build());
        general.option(Option.<SneakBehavior>createBuilder()
                .name(Component.translatable("treeliable.config.sneak_behavior"))
                .description(OptionDescription.of(Component.translatable("treeliable.config.sneak_behavior.tooltip")))
                .binding(SneakBehavior.INVERT_CHOPPING, () -> config.sneakBehavior, v -> config.sneakBehavior = v)
                .controller(opt -> EnumControllerBuilder.create(opt).enumClass(SneakBehavior.class))
                .build());
        general.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("treeliable.config.chop_in_creative_mode"))
                .description(OptionDescription.of(Component.translatable("treeliable.config.chop_in_creative_mode.tooltip")))
                .binding(false, () -> config.chopInCreativeMode, v -> config.chopInCreativeMode = v)
                .controller(TickBoxControllerBuilder::create)
                .build());
        general.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("treeliable.config.hytale_like_felling"))
                .description(OptionDescription.of(Component.translatable("treeliable.config.hytale_like_felling.tooltip")))
                .binding(false, () -> config.hytaleLikeFelling, v -> config.hytaleLikeFelling = v)
                .controller(TickBoxControllerBuilder::create)
                .build());
        general.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("treeliable.config.drop_loot_for_chopped_blocks"))
                .description(OptionDescription.of(Component.translatable("treeliable.config.drop_loot_for_chopped_blocks.tooltip")))
                .binding(true, () -> config.dropLootForChoppedBlocks, v -> config.dropLootForChoppedBlocks = v)
                .controller(TickBoxControllerBuilder::create)
                .build());
        general.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("treeliable.config.must_use_correct_tool_for_drops"))
                .description(OptionDescription.of(Component.translatable("treeliable.config.must_use_correct_tool_for_drops.tooltip")))
                .binding(true, () -> config.mustUseCorrectToolForDrops, v -> config.mustUseCorrectToolForDrops = v)
                .controller(TickBoxControllerBuilder::create)
                .build());
        general.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("treeliable.config.must_use_fast_breaking_tool"))
                .description(OptionDescription.of(Component.translatable("treeliable.config.must_use_fast_breaking_tool.tooltip")))
                .binding(true, () -> config.mustUseFastBreakingTool, v -> config.mustUseFastBreakingTool = v)
                .controller(TickBoxControllerBuilder::create)
                .build());
        general.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("treeliable.config.prevent_chopping_on_right_click"))
                .description(OptionDescription.of(Component.translatable("treeliable.config.prevent_chopping_on_right_click.tooltip")))
                .binding(false, () -> config.preventChoppingOnRightClick, v -> config.preventChoppingOnRightClick = v)
                .controller(TickBoxControllerBuilder::create)
                .build());
        general.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("treeliable.config.prevent_chop_recursion"))
                .description(OptionDescription.of(Component.translatable("treeliable.config.prevent_chop_recursion.tooltip")))
                .binding(true, () -> config.preventChopRecursion, v -> config.preventChopRecursion = v)
                .controller(TickBoxControllerBuilder::create)
                .build());

        ConfigCategory.Builder limits = ConfigCategory.createBuilder()
                .name(Component.translatable("treeliable.config.category.limits"));

        limits.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("treeliable.config.trees_must_have_leaves"))
                .description(OptionDescription.of(Component.translatable("treeliable.config.trees_must_have_leaves.tooltip")))
                .binding(true, () -> config.treesMustHaveLeaves, v -> config.treesMustHaveLeaves = v)
                .controller(TickBoxControllerBuilder::create)
                .build());
        limits.option(Option.<Integer>createBuilder()
                .name(Component.translatable("treeliable.config.max_tree_blocks"))
                .description(OptionDescription.of(Component.translatable("treeliable.config.max_tree_blocks.tooltip")))
                .binding(1024, () -> config.maxTreeBlocks, v -> config.maxTreeBlocks = v)
                .controller(IntegerFieldControllerBuilder::create)
                .build());
        limits.option(Option.<Integer>createBuilder()
                .name(Component.translatable("treeliable.config.max_leaves_blocks"))
                .description(OptionDescription.of(Component.translatable("treeliable.config.max_leaves_blocks.tooltip")))
                .binding(1024, () -> config.maxLeavesBlocks, v -> config.maxLeavesBlocks = v)
                .controller(IntegerFieldControllerBuilder::create)
                .build());
        limits.option(Option.<Integer>createBuilder()
                .name(Component.translatable("treeliable.config.max_break_leaves_distance"))
                .description(OptionDescription.of(Component.translatable("treeliable.config.max_break_leaves_distance.tooltip")))
                .binding(7, () -> config.maxBreakLeavesDistance, v -> config.maxBreakLeavesDistance = v)
                .controller(IntegerFieldControllerBuilder::create)
                .build());
        limits.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("treeliable.config.ignore_persistent_leaves"))
                .description(OptionDescription.of(Component.translatable("treeliable.config.ignore_persistent_leaves.tooltip")))
                .binding(true, () -> config.ignorePersistentLeaves, v -> config.ignorePersistentLeaves = v)
                .controller(TickBoxControllerBuilder::create)
                .build());
        limits.option(Option.<FellLeavesStrategy>createBuilder()
                .name(Component.translatable("treeliable.config.fell_leaves_strategy"))
                .description(OptionDescription.of(Component.translatable("treeliable.config.fell_leaves_strategy.tooltip")))
                .binding(FellLeavesStrategy.DECAY, () -> config.fellLeavesStrategy, v -> config.fellLeavesStrategy = v)
                .controller(opt -> EnumControllerBuilder.create(opt).enumClass(FellLeavesStrategy.class))
                .build());
        limits.option(Option.<FellCreditStrategy>createBuilder()
                .name(Component.translatable("treeliable.config.fell_credit_strategy"))
                .description(OptionDescription.of(Component.translatable("treeliable.config.fell_credit_strategy.tooltip")))
                .binding(FellCreditStrategy.NONE, () -> config.fellCreditStrategy, v -> config.fellCreditStrategy = v)
                .controller(opt -> EnumControllerBuilder.create(opt).enumClass(FellCreditStrategy.class))
                .build());
        limits.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("treeliable.config.damage_tool_per_log"))
                .description(OptionDescription.of(Component.translatable("treeliable.config.damage_tool_per_log.tooltip")))
                .binding(true, () -> config.damageToolPerLog, v -> config.damageToolPerLog = v)
                .controller(TickBoxControllerBuilder::create)
                .build());
        limits.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("treeliable.config.exhaustion_per_log"))
                .description(OptionDescription.of(Component.translatable("treeliable.config.exhaustion_per_log.tooltip")))
                .binding(true, () -> config.exhaustionPerLog, v -> config.exhaustionPerLog = v)
                .controller(TickBoxControllerBuilder::create)
                .build());
        limits.option(Option.<Double>createBuilder()
                .name(Component.translatable("treeliable.config.log_drop_chance"))
                .description(OptionDescription.of(Component.translatable("treeliable.config.log_drop_chance.tooltip")))
                .binding(1.0, () -> config.logDropChance, v -> config.logDropChance = v)
                .controller(opt -> DoubleFieldControllerBuilder.create(opt).range(0.0, 1.0))
                .build());

        ConfigCategory.Builder algorithm = ConfigCategory.createBuilder()
                .name(Component.translatable("treeliable.config.category.algorithm"));

        algorithm.option(Option.<ChopCountingAlgorithm>createBuilder()
                .name(Component.translatable("treeliable.config.chop_counting_algorithm"))
                .description(OptionDescription.of(Component.translatable("treeliable.config.chop_counting_algorithm.tooltip")))
                .binding(ChopCountingAlgorithm.LINEAR, () -> config.chopCountingAlgorithm, v -> config.chopCountingAlgorithm = v)
                .controller(opt -> EnumControllerBuilder.create(opt).enumClass(ChopCountingAlgorithm.class))
                .build());
        algorithm.option(Option.<Rounder>createBuilder()
                .name(Component.translatable("treeliable.config.chop_count_rounding"))
                .description(OptionDescription.of(Component.translatable("treeliable.config.chop_count_rounding.tooltip")))
                .binding(Rounder.NEAREST, () -> config.chopCountRounding, v -> config.chopCountRounding = v)
                .controller(opt -> EnumControllerBuilder.create(opt).enumClass(Rounder.class))
                .build());
        algorithm.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("treeliable.config.can_require_more_chops_than_blocks"))
                .description(OptionDescription.of(Component.translatable("treeliable.config.can_require_more_chops_than_blocks.tooltip")))
                .binding(true, () -> config.canRequireMoreChopsThanBlocks, v -> config.canRequireMoreChopsThanBlocks = v)
                .controller(TickBoxControllerBuilder::create)
                .build());
        algorithm.option(Option.<Double>createBuilder()
                .name(Component.translatable("treeliable.config.logarithmic_a"))
                .description(OptionDescription.of(Component.translatable("treeliable.config.logarithmic_a.tooltip")))
                .binding(10.0, () -> config.logarithmicA, v -> config.logarithmicA = v)
                .controller(DoubleFieldControllerBuilder::create)
                .build());
        algorithm.option(Option.<Double>createBuilder()
                .name(Component.translatable("treeliable.config.linear_m"))
                .description(OptionDescription.of(Component.translatable("treeliable.config.linear_m.tooltip")))
                .binding(1.0, () -> config.linearM, v -> config.linearM = v)
                .controller(DoubleFieldControllerBuilder::create)
                .build());
        algorithm.option(Option.<Double>createBuilder()
                .name(Component.translatable("treeliable.config.linear_b"))
                .description(OptionDescription.of(Component.translatable("treeliable.config.linear_b.tooltip")))
                .binding(0.0, () -> config.linearB, v -> config.linearB = v)
                .controller(DoubleFieldControllerBuilder::create)
                .build());

        ConfigCategory.Builder visuals = ConfigCategory.createBuilder()
                .name(Component.translatable("treeliable.config.category.visuals"));

        visuals.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("treeliable.config.show_chopping_indicator"))
                .description(OptionDescription.of(Component.translatable("treeliable.config.show_chopping_indicator.tooltip")))
                .binding(true, () -> config.showChoppingIndicator, v -> config.showChoppingIndicator = v)
                .controller(TickBoxControllerBuilder::create)
                .build());
        visuals.option(Option.<Integer>createBuilder()
                .name(Component.translatable("treeliable.config.chopping_indicator_x_offset"))
                .description(OptionDescription.of(Component.translatable("treeliable.config.chopping_indicator_x_offset.tooltip")))
                .binding(8, () -> config.choppingIndicatorXOffset, v -> config.choppingIndicatorXOffset = v)
                .controller(IntegerFieldControllerBuilder::create)
                .build());
        visuals.option(Option.<Integer>createBuilder()
                .name(Component.translatable("treeliable.config.chopping_indicator_y_offset"))
                .description(OptionDescription.of(Component.translatable("treeliable.config.chopping_indicator_y_offset.tooltip")))
                .binding(-8, () -> config.choppingIndicatorYOffset, v -> config.choppingIndicatorYOffset = v)
                .controller(IntegerFieldControllerBuilder::create)
                .build());
        visuals.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("treeliable.config.delay_felling_layers"))
                .description(OptionDescription.of(Component.translatable("treeliable.config.delay_felling_layers.tooltip")))
                .binding(true, () -> config.delayFellingLayers, v -> config.delayFellingLayers = v)
                .controller(TickBoxControllerBuilder::create)
                .build());
        visuals.option(Option.<Integer>createBuilder()
                .name(Component.translatable("treeliable.config.felling_layer_delay_ticks"))
                .description(OptionDescription.of(Component.translatable("treeliable.config.felling_layer_delay_ticks.tooltip")))
                .binding(2, () -> config.fellingLayerDelayTicks, v -> config.fellingLayerDelayTicks = v)
                .controller(IntegerFieldControllerBuilder::create)
                .build());
        visuals.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("treeliable.config.exponential_felling_speedup"))
                .description(OptionDescription.of(Component.translatable("treeliable.config.exponential_felling_speedup.tooltip")))
                .binding(false, () -> config.exponentialFellingSpeedup, v -> config.exponentialFellingSpeedup = v)
                .controller(TickBoxControllerBuilder::create)
                .build());
        visuals.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("treeliable.config.show_feedback_messages"))
                .description(OptionDescription.of(Component.translatable("treeliable.config.show_feedback_messages.tooltip")))
                .binding(true, () -> config.showFeedbackMessages, v -> config.showFeedbackMessages = v)
                .controller(TickBoxControllerBuilder::create)
                .build());
        visuals.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("treeliable.config.verbose_api"))
                .description(OptionDescription.of(Component.translatable("treeliable.config.verbose_api.tooltip")))
                .binding(false, () -> config.verboseAPI, v -> config.verboseAPI = v)
                .controller(TickBoxControllerBuilder::create)
                .build());
        visuals.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("treeliable.config.suppress_vanilla_leaf_sounds_on_fell"))
                .description(OptionDescription.of(Component.translatable("treeliable.config.suppress_vanilla_leaf_sounds_on_fell.tooltip")))
                .binding(false, () -> config.suppressVanillaLeafSoundsOnFell, v -> config.suppressVanillaLeafSoundsOnFell = v)
                .controller(TickBoxControllerBuilder::create)
                .build());

        ConfigCategory.Builder compat = ConfigCategory.createBuilder()
                .name(Component.translatable("treeliable.config.category.compat"));

        compat.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("treeliable.config.compat_for_apotheosis"))
                .description(OptionDescription.of(Component.translatable("treeliable.config.compat_for_apotheosis.tooltip")))
                .binding(true, () -> config.compatForApotheosis, v -> config.compatForApotheosis = v)
                .controller(TickBoxControllerBuilder::create)
                .build());
        compat.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("treeliable.config.compat_for_silentgear"))
                .description(OptionDescription.of(Component.translatable("treeliable.config.compat_for_silentgear.tooltip")))
                .binding(true, () -> config.compatForSilentGear, v -> config.compatForSilentGear = v)
                .controller(TickBoxControllerBuilder::create)
                .build());
        compat.option(Option.<Integer>createBuilder()
                .name(Component.translatable("treeliable.config.silentgear_saw_chops"))
                .description(OptionDescription.of(Component.translatable("treeliable.config.silentgear_saw_chops.tooltip")))
                .binding(5, () -> config.silentGearSawChops, v -> config.silentGearSawChops = v)
                .controller(IntegerFieldControllerBuilder::create)
                .build());

        ConfigCategory.Builder lists = ConfigCategory.createBuilder()
                .name(Component.translatable("treeliable.config.category.lists"));

        lists.option(Option.<ListType>createBuilder()
                .name(Component.translatable("treeliable.config.items_filter_type"))
                .description(OptionDescription.of(Component.translatable("treeliable.config.items_filter_type.tooltip")))
                .binding(ListType.BLACKLIST, () -> config.itemsBlacklistOrWhitelist, v -> config.itemsBlacklistOrWhitelist = v)
                .controller(opt -> EnumControllerBuilder.create(opt).enumClass(ListType.class))
                .build());
        lists.option(ListOption.<String>createBuilder()
                .name(Component.translatable("treeliable.config.choppable_blocks"))
                .description(OptionDescription.of(Component.translatable("treeliable.config.choppable_blocks.tooltip")))
                .binding(Arrays.asList("#treeliable:choppables", "#minecraft:logs"), () -> config.choppableBlocks, v -> config.choppableBlocks = v)
                .controller(StringControllerBuilder::create)
                .initial("")
                .build());
        lists.option(ListOption.<String>createBuilder()
                .name(Component.translatable("treeliable.config.choppable_blocks_exceptions"))
                .description(OptionDescription.of(Component.translatable("treeliable.config.choppable_blocks_exceptions.tooltip")))
                .binding(Arrays.asList("minecraft:bamboo", "#dynamictrees:branches", "dynamictrees:trunk_shell"), () -> config.choppableBlocksExceptions, v -> config.choppableBlocksExceptions = v)
                .controller(StringControllerBuilder::create)
                .initial("")
                .build());
        lists.option(ListOption.<String>createBuilder()
                .name(Component.translatable("treeliable.config.leaves_blocks"))
                .description(OptionDescription.of(Component.translatable("treeliable.config.leaves_blocks.tooltip")))
                .binding(Arrays.asList("#treeliable:leaves_like", "#minecraft:leaves"), () -> config.leavesBlocks, v -> config.leavesBlocks = v)
                .controller(StringControllerBuilder::create)
                .initial("")
                .build());
        lists.option(ListOption.<String>createBuilder()
                .name(Component.translatable("treeliable.config.leaves_blocks_exceptions"))
                .description(OptionDescription.of(Component.translatable("treeliable.config.leaves_blocks_exceptions.tooltip")))
                .binding(List.of(), () -> config.leavesBlocksExceptions, v -> config.leavesBlocksExceptions = v)
                .controller(StringControllerBuilder::create)
                .initial("")
                .build());
        lists.option(ListOption.<String>createBuilder()
                .name(Component.translatable("treeliable.config.chopping_items"))
                .description(OptionDescription.of(Component.translatable("treeliable.config.chopping_items.tooltip")))
                .binding(Arrays.asList("botania:terra_axe", "mekanism:atomic_disassembler", "twilightforest:giant_pickaxe"), () -> config.choppingItems, v -> config.choppingItems = v)
                .controller(StringControllerBuilder::create)
                .initial("")
                .build());

        return builder
                .category(general.build())
                .category(limits.build())
                .category(algorithm.build())
                .category(visuals.build())
                .category(compat.build())
                .category(lists.build())
                .build()
                .generateScreen(parent);
    }
}
