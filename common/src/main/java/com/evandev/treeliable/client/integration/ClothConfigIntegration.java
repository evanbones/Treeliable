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
                .setTitle(Component.literal("Treeliable Configuration"));

        builder.setSavingRunnable(ModConfig::save);

        ConfigEntryBuilder eb = builder.entryBuilder();

        ConfigCategory general = builder.getOrCreateCategory(Component.literal("General"));

        general.addEntry(eb.startBooleanToggle(Component.literal("Mod Enabled"), config.enabled)
                .setDefaultValue(true).setSaveConsumer(v -> config.enabled = v).build());
        general.addEntry(eb.startBooleanToggle(Component.literal("Chopping Enabled"), config.choppingEnabled)
                .setDefaultValue(true).setSaveConsumer(v -> config.choppingEnabled = v).build());
        general.addEntry(eb.startEnumSelector(Component.literal("Sneak Behavior"), SneakBehavior.class, config.sneakBehavior)
                .setDefaultValue(SneakBehavior.INVERT_CHOPPING).setSaveConsumer(v -> config.sneakBehavior = v).build());
        general.addEntry(eb.startBooleanToggle(Component.literal("Chop in Creative Mode"), config.chopInCreativeMode)
                .setDefaultValue(false).setSaveConsumer(v -> config.chopInCreativeMode = v).build());
        general.addEntry(eb.startBooleanToggle(Component.literal("Drop Loot for Chopped Blocks"), config.dropLootForChoppedBlocks)
                .setDefaultValue(true).setSaveConsumer(v -> config.dropLootForChoppedBlocks = v).build());
        general.addEntry(eb.startBooleanToggle(Component.literal("Drop Loot on First Chop"), config.dropLootOnFirstChop)
                .setDefaultValue(false).setSaveConsumer(v -> config.dropLootOnFirstChop = v).build());
        general.addEntry(eb.startBooleanToggle(Component.literal("Must Use Correct Tool for Drops"), config.mustUseCorrectToolForDrops)
                .setDefaultValue(true).setSaveConsumer(v -> config.mustUseCorrectToolForDrops = v).build());
        general.addEntry(eb.startBooleanToggle(Component.literal("Must Use Fast Breaking Tool"), config.mustUseFastBreakingTool)
                .setDefaultValue(false).setSaveConsumer(v -> config.mustUseFastBreakingTool = v).build());
        general.addEntry(eb.startBooleanToggle(Component.literal("Prevent Chopping on Right Click"), config.preventChoppingOnRightClick)
                .setDefaultValue(false).setSaveConsumer(v -> config.preventChoppingOnRightClick = v).build());
        general.addEntry(eb.startBooleanToggle(Component.literal("Prevent Chop Recursion"), config.preventChopRecursion)
                .setDefaultValue(true).setSaveConsumer(v -> config.preventChopRecursion = v).build());

        ConfigCategory limits = builder.getOrCreateCategory(Component.literal("Limits & Strategy"));

        limits.addEntry(eb.startBooleanToggle(Component.literal("Trees Must Have Leaves"), config.treesMustHaveLeaves)
                .setDefaultValue(true).setSaveConsumer(v -> config.treesMustHaveLeaves = v).build());
        limits.addEntry(eb.startIntField(Component.literal("Max Tree Blocks"), config.maxTreeBlocks)
                .setDefaultValue(1024).setSaveConsumer(v -> config.maxTreeBlocks = v).build());
        limits.addEntry(eb.startIntField(Component.literal("Max Leaves Blocks"), config.maxLeavesBlocks)
                .setDefaultValue(1024).setSaveConsumer(v -> config.maxLeavesBlocks = v).build());
        limits.addEntry(eb.startIntField(Component.literal("Max Break Leaves Distance"), config.maxBreakLeavesDistance)
                .setDefaultValue(7).setSaveConsumer(v -> config.maxBreakLeavesDistance = v).build());
        limits.addEntry(eb.startBooleanToggle(Component.literal("Ignore Persistent Leaves"), config.ignorePersistentLeaves)
                .setDefaultValue(true).setSaveConsumer(v -> config.ignorePersistentLeaves = v).build());
        limits.addEntry(eb.startEnumSelector(Component.literal("Fell Leaves Strategy"), FellLeavesStrategy.class, config.fellLeavesStrategy)
                .setDefaultValue(FellLeavesStrategy.DECAY).setSaveConsumer(v -> config.fellLeavesStrategy = v).build());
        limits.addEntry(eb.startEnumSelector(Component.literal("Fell Credit Strategy"), FellCreditStrategy.class, config.fellCreditStrategy)
                .setDefaultValue(FellCreditStrategy.NONE).setSaveConsumer(v -> config.fellCreditStrategy = v).build());

        ConfigCategory algorithm = builder.getOrCreateCategory(Component.literal("Algorithm"));

        algorithm.addEntry(eb.startEnumSelector(Component.literal("Chop Counting Algorithm"), ChopCountingAlgorithm.class, config.chopCountingAlgorithm)
                .setDefaultValue(ChopCountingAlgorithm.LOGARITHMIC).setSaveConsumer(v -> config.chopCountingAlgorithm = v).build());
        algorithm.addEntry(eb.startEnumSelector(Component.literal("Chop Count Rounding"), Rounder.class, config.chopCountRounding)
                .setDefaultValue(Rounder.NEAREST).setSaveConsumer(v -> config.chopCountRounding = v).build());
        algorithm.addEntry(eb.startBooleanToggle(Component.literal("Can Require More Chops Than Blocks"), config.canRequireMoreChopsThanBlocks)
                .setDefaultValue(false).setSaveConsumer(v -> config.canRequireMoreChopsThanBlocks = v).build());
        algorithm.addEntry(eb.startDoubleField(Component.literal("Logarithmic A Factor"), config.logarithmicA)
                .setDefaultValue(10.0).setSaveConsumer(v -> config.logarithmicA = v).build());
        algorithm.addEntry(eb.startDoubleField(Component.literal("Linear M Factor"), config.linearM)
                .setDefaultValue(1.0).setSaveConsumer(v -> config.linearM = v).build());
        algorithm.addEntry(eb.startDoubleField(Component.literal("Linear B Factor"), config.linearB)
                .setDefaultValue(0.0).setSaveConsumer(v -> config.linearB = v).build());

        ConfigCategory visuals = builder.getOrCreateCategory(Component.literal("Visuals"));

        visuals.addEntry(eb.startBooleanToggle(Component.literal("Show Chopping Indicators"), config.showChoppingIndicators)
                .setDefaultValue(true).setSaveConsumer(v -> config.showChoppingIndicators = v).build());
        visuals.addEntry(eb.startIntField(Component.literal("Indicator X Offset"), config.indicatorXOffset)
                .setDefaultValue(16).setSaveConsumer(v -> config.indicatorXOffset = v).build());
        visuals.addEntry(eb.startIntField(Component.literal("Indicator Y Offset"), config.indicatorYOffset)
                .setDefaultValue(0).setSaveConsumer(v -> config.indicatorYOffset = v).build());
        visuals.addEntry(eb.startBooleanToggle(Component.literal("Show Feedback Messages"), config.showFeedbackMessages)
                .setDefaultValue(true).setSaveConsumer(v -> config.showFeedbackMessages = v).build());
        visuals.addEntry(eb.startBooleanToggle(Component.literal("Verbose API Logging"), config.verboseAPI)
                .setDefaultValue(false).setSaveConsumer(v -> config.verboseAPI = v).build());

        ConfigCategory compat = builder.getOrCreateCategory(Component.literal("Compatibility"));

        compat.addEntry(eb.startBooleanToggle(Component.literal("Compat for Apotheosis"), config.compatForApotheosis)
                .setDefaultValue(true).setSaveConsumer(v -> config.compatForApotheosis = v).build());
        compat.addEntry(eb.startBooleanToggle(Component.literal("Compat for Silent Gear"), config.compatForSilentGear)
                .setDefaultValue(true).setSaveConsumer(v -> config.compatForSilentGear = v).build());
        compat.addEntry(eb.startIntField(Component.literal("Silent Gear Saw Chops"), config.silentGearSawChops)
                .setDefaultValue(5).setSaveConsumer(v -> config.silentGearSawChops = v).build());

        ConfigCategory lists = builder.getOrCreateCategory(Component.literal("Lists & Blocks"));

        lists.addEntry(eb.startEnumSelector(Component.literal("Items Filter Type"), ListType.class, config.itemsBlacklistOrWhitelist)
                .setDefaultValue(ListType.BLACKLIST).setSaveConsumer(v -> config.itemsBlacklistOrWhitelist = v).build());
        lists.addEntry(eb.startStrList(Component.literal("Choppable Blocks"), config.choppableBlocks)
                .setDefaultValue(Arrays.asList("#treeliable:choppables", "#minecraft:logs")).setSaveConsumer(v -> config.choppableBlocks = v).build());
        lists.addEntry(eb.startStrList(Component.literal("Choppable Blocks Exceptions"), config.choppableBlocksExceptions)
                .setDefaultValue(Arrays.asList("minecraft:bamboo", "#dynamictrees:branches", "dynamictrees:trunk_shell")).setSaveConsumer(v -> config.choppableBlocksExceptions = v).build());
        lists.addEntry(eb.startStrList(Component.literal("Leaves Blocks"), config.leavesBlocks)
                .setDefaultValue(Arrays.asList("#treeliable:leaves_like", "#minecraft:leaves")).setSaveConsumer(v -> config.leavesBlocks = v).build());
        lists.addEntry(eb.startStrList(Component.literal("Leaves Blocks Exceptions"), config.leavesBlocksExceptions)
                .setDefaultValue(List.of()).setSaveConsumer(v -> config.leavesBlocksExceptions = v).build());
        lists.addEntry(eb.startStrList(Component.literal("Chopping Items"), config.choppingItems)
                .setDefaultValue(Arrays.asList("botania:terra_axe", "mekanism:atomic_disassembler", "twilightforest:giant_pickaxe")).setSaveConsumer(v -> config.choppingItems = v).build());

        return builder.build();
    }
}