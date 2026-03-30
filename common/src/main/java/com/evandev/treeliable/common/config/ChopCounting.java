package com.evandev.treeliable.common.config;

public class ChopCounting {

    private static final int CACHE_SIZE = 1024;
    private static final int[] cache = new int[CACHE_SIZE];
    private static int numCached = 1;

    public static int calculate(int support) {
        if (support < CACHE_SIZE) {
            if (numCached <= support) {
                for (; numCached <= support; ++numCached) {
                    cache[numCached] = recalculate(numCached);
                }
            }
            return cache[support];
        } else {
            return recalculate(support);
        }
    }

    private static int recalculate(int support) {
        Rounder rounder = ModConfig.get().chopCountRounding;
        boolean canRequireMore = ModConfig.get().canRequireMoreChopsThanBlocks;

        int count = Math.max(1, rounder.round(ModConfig.get().chopCountingAlgorithm.calculate(support, rounder, canRequireMore)));
        return canRequireMore ? count : Math.min(support, count);
    }
}
