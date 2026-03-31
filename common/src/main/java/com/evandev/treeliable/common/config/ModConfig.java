package com.evandev.treeliable.common.config;

import com.evandev.treeliable.Treeliable;
import com.evandev.treeliable.common.chop.ChopUtil;
import com.evandev.treeliable.common.config.resource.ResourceIdentifier;
import com.evandev.treeliable.common.settings.Permissions;
import com.evandev.treeliable.common.settings.Setting;
import com.evandev.treeliable.common.settings.SettingsField;
import com.evandev.treeliable.common.settings.SneakBehavior;
import com.evandev.treeliable.platform.Services;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = Services.PLATFORM.getConfigDirectory().resolve("treeliable.json").toFile();
    private static ModConfig INSTANCE;

    @SerializedName("enabled")
    public boolean enabled = true;
    @SerializedName("drop_loot_for_chopped_blocks")
    public boolean dropLootForChoppedBlocks = true;
    @SerializedName("chopping_enabled")
    public boolean choppingEnabled = true;
    @SerializedName("sneak_behavior")
    public SneakBehavior sneakBehavior = SneakBehavior.INVERT_CHOPPING;
    @SerializedName("trees_must_have_leaves")
    public boolean treesMustHaveLeaves = true;
    @SerializedName("chop_in_creative_mode")
    public boolean chopInCreativeMode = false;
    @SerializedName("delay_felling_layers")
    public boolean delayFellingLayers = true;
    @SerializedName("felling_layer_delay_ticks")
    public int fellingLayerDelayTicks = 2;

    @SerializedName("max_tree_blocks")
    public int maxTreeBlocks = 1024;
    @SerializedName("max_leaves_blocks")
    public int maxLeavesBlocks = 1024;
    @SerializedName("max_break_leaves_distance")
    public int maxBreakLeavesDistance = 7;
    @SerializedName("ignore_persistent_leaves")
    public boolean ignorePersistentLeaves = true;
    @SerializedName("fell_leaves_strategy")
    public FellLeavesStrategy fellLeavesStrategy = FellLeavesStrategy.DECAY;
    @SerializedName("fell_credit_strategy")
    public FellCreditStrategy fellCreditStrategy = FellCreditStrategy.NONE;
    @SerializedName("damage_tool_per_log")
    public boolean damageToolPerLog = true;
    @SerializedName("exhaustion_per_log")
    public boolean exhaustionPerLog = true;

    @SerializedName("chop_counting_algorithm")
    public ChopCountingAlgorithm chopCountingAlgorithm = ChopCountingAlgorithm.LOGARITHMIC;
    @SerializedName("chop_count_rounding")
    public Rounder chopCountRounding = Rounder.NEAREST;
    @SerializedName("can_require_more_chops_than_blocks")
    public boolean canRequireMoreChopsThanBlocks = false;
    @SerializedName("logarithmic_a")
    public double logarithmicA = 10.0;
    @SerializedName("linear_m")
    public double linearM = 1.0;
    @SerializedName("linear_b")
    public double linearB = 0.0;

    @SerializedName("must_use_correct_tool_for_drops")
    public boolean mustUseCorrectToolForDrops = true;
    @SerializedName("must_use_fast_breaking_tool")
    public boolean mustUseFastBreakingTool = true;
    @SerializedName("prevent_chopping_on_right_click")
    public boolean preventChoppingOnRightClick = false;
    @SerializedName("prevent_chop_recursion")
    public boolean preventChopRecursion = true;
    @SerializedName("items_blacklist_or_whitelist")
    public ListType itemsBlacklistOrWhitelist = ListType.BLACKLIST;
    @SerializedName("compat_for_apotheosis")
    public boolean compatForApotheosis = true;
    @SerializedName("compat_for_silentgear")
    public boolean compatForSilentGear = true;
    @SerializedName("silentgear_saw_chops")
    public int silentGearSawChops = 5;

    @SerializedName("show_chopping_indicators")
    public boolean showChoppingIndicators = true;
    @SerializedName("indicator_x_offset")
    public int indicatorXOffset = 16;
    @SerializedName("indicator_y_offset")
    public int indicatorYOffset = 0;
    @SerializedName("show_feedback_messages")
    public boolean showFeedbackMessages = true;
    @SerializedName("verbose_api")
    public boolean verboseAPI = false;
    @SerializedName("suppress_vanilla_leaf_sounds_on_fell")
    public boolean suppressVanillaLeafSoundsOnFell = true;

    @SerializedName("choppable_blocks")
    public List<String> choppableBlocks = Arrays.asList("#treeliable:choppables", "#minecraft:logs");
    @SerializedName("choppable_blocks_exceptions")
    public List<String> choppableBlocksExceptions = Arrays.asList("minecraft:bamboo", "#dynamictrees:branches", "dynamictrees:trunk_shell");
    @SerializedName("leaves_blocks")
    public List<String> leavesBlocks = Arrays.asList("#treeliable:leaves_like", "#minecraft:leaves");
    @SerializedName("leaves_blocks_exceptions")
    public List<String> leavesBlocksExceptions = new ArrayList<>();
    @SerializedName("chopping_items")
    public List<String> choppingItems = Arrays.asList("botania:terra_axe", "mekanism:atomic_disassembler", "twilightforest:giant_pickaxe");

    @SerializedName("huge_mushroom_logs")
    public List<String> hugeMushroomLogs = List.of("#c:mushroom_stems");
    @SerializedName("huge_mushroom_leaves")
    public List<String> hugeMushroomLeaves = List.of("#c:mushroom_caps");
    @SerializedName("huge_fungus_logs")
    public List<String> hugeFungusLogs = Arrays.asList("#minecraft:crimson_stems", "#minecraft:warped_stems");
    @SerializedName("huge_fungus_leaves")
    public List<String> hugeFungusLeaves = Arrays.asList("#minecraft:wart_blocks", "minecraft:shroomlight");
    @SerializedName("problematic_leaves_trees_logs")
    public List<String> problematicLeavesTreesLogs = Arrays.asList("tropicraft:.*_log(_.*)?", "alexscaves:.*_log", "alexscaves:pewen_wood");
    @SerializedName("problematic_leaves_trees_leaves")
    public List<String> problematicLeavesTreesLeaves = Arrays.asList("tropicraft:.*_leaves(_.*)?", "alexscaves:.*_branch", "alexscaves:pewen_pines");
    @SerializedName("leaf_decay_exceptions")
    public List<String> leafDecayExceptions = List.of("#spectrum:colored_leaves");

    public transient Lazy<Set<Block>> choppableBlocksCache = new Lazy<>(() -> {
        Set<Block> blocks = getIdentifiedBlocks(choppableBlocks).collect(Collectors.toSet());
        Set<Block> exceptions = getIdentifiedBlocks(choppableBlocksExceptions).collect(Collectors.toSet());
        blocks.removeAll(exceptions);
        Treeliable.api.getChoppableBlockOverrides().forEach(pair -> {
            if (pair.getValue()) blocks.add(pair.getKey());
            else blocks.remove(pair.getKey());
        });
        return blocks;
    });

    public transient Lazy<Map<Block, TreeLeavesBehavior>> leavesBlocksCache = new Lazy<>(() -> {
        Set<Block> exceptions = getIdentifiedBlocks(leavesBlocksExceptions).collect(Collectors.toSet());
        Map<Block, TreeLeavesBehavior> blocks = getIdentifiedBlocks(leavesBlocks)
                .filter(b -> !exceptions.contains(b))
                .collect(Collectors.toConcurrentMap(k -> k, v -> TreeLeavesBehavior.DEFAULT, (a, b) -> b));
        Treeliable.api.getLeavesBlockOverrides().forEach(pair -> {
            if (pair.getValue()) blocks.put(pair.getKey(), TreeLeavesBehavior.PROBLEMATIC);
            else blocks.remove(pair.getKey());
        });
        return blocks;
    });

    public transient Lazy<Set<Item>> choppingItemsCache = new Lazy<>(() -> {
        Set<Item> items = getIdentifiedItems(choppingItems).collect(Collectors.toSet());
        Treeliable.api.getChoppingItemOverrides().forEach(pair -> {
            if (itemsBlacklistOrWhitelist.accepts(pair.getValue())) items.add(pair.getKey());
            else items.remove(pair.getKey());
        });
        return items;
    });

    public static ModConfig get() {
        if (INSTANCE == null) load();
        return INSTANCE;
    }

    public static void load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                INSTANCE = GSON.fromJson(reader, ModConfig.class);
            } catch (Exception e) {
                Treeliable.LOGGER.error("Failed to load treeliable.json", e);
                INSTANCE = new ModConfig();
                save();
            }
        } else {
            INSTANCE = new ModConfig();
            save();
        }
        INSTANCE.invalidateCaches();
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(INSTANCE, writer);
        } catch (IOException e) {
            Treeliable.LOGGER.error("Failed to save treeliable.json", e);
        }
        if (INSTANCE != null) INSTANCE.invalidateCaches();
    }

    public static Stream<Block> getIdentifiedBlocks(List<String> strings) {
        return strings.stream().map(ResourceIdentifier::from).flatMap(id -> id.resolve(BuiltInRegistries.BLOCK));
    }

    public static Stream<Item> getIdentifiedItems(List<String> strings) {
        return strings.stream().map(ResourceIdentifier::from).flatMap(id -> id.resolve(BuiltInRegistries.ITEM));
    }

    public static Permissions getServerPermissions() {
        Permissions permissions = new Permissions();
        for (SettingsField field : SettingsField.values()) {
            for (Object value : field.getValues()) {
                permissions.permit(new Setting(field, value));
            }
        }
        return permissions;
    }

    public void invalidateCaches() {
        choppableBlocksCache.reset();
        leavesBlocksCache.reset();
        choppingItemsCache.reset();
        ChopUtil.defaultDetector.reset();
    }
}