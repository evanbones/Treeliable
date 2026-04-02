package com.evandev.treeliable.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PlacedLogTracker {
    private static final Map<ResourceKey<Level>, Set<BlockPos>> PLACED_LOGS = new ConcurrentHashMap<>();

    public static void addPlacedLog(Level level, BlockPos pos) {
        PLACED_LOGS.computeIfAbsent(level.dimension(), k -> new HashSet<>()).add(pos.immutable());
    }

    public static void removePlacedLog(Level level, BlockPos pos) {
        Set<BlockPos> logs = PLACED_LOGS.get(level.dimension());
        if (logs != null) {
            logs.remove(pos);
        }
    }

    public static boolean isPlayerPlaced(Level level, BlockPos pos) {
        Set<BlockPos> logs = PLACED_LOGS.get(level.dimension());
        return logs != null && logs.contains(pos);
    }
}