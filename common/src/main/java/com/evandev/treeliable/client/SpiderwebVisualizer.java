package com.evandev.treeliable.client;

import com.evandev.treeliable.api.TreeData;
import com.evandev.treeliable.client.gui.screen.ChopIndicator;
import com.evandev.treeliable.common.chop.ChopUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.Set;

public class SpiderwebVisualizer {
    private static final Set<BlockPos> currentlyVisualized = new HashSet<>();
    private static BlockPos lastTarget = null;

    public static void update(Minecraft minecraft, boolean isDestroying, BlockPos targetPos, float progress) {
        Level level = minecraft.level;
        if (level == null || minecraft.player == null) return;

        if (!isDestroying || (targetPos != null && !targetPos.equals(lastTarget))) {
            clearVisuals(level);
            lastTarget = targetPos;
            if (!isDestroying) return;
        }

        if (targetPos == null) return;

        try {
            if (ChopUtil.playerWantsToChop(minecraft.player, Client.getChopSettings()) && ChopIndicator.blockCanBeChopped(targetPos)) {
                TreeData tree = Client.treeCache.getTree(level, targetPos);

                Set<BlockPos> layer = new HashSet<>();
                int maxDist = 0;

                for (BlockPos pos : tree.getLogBlocks()) {
                    if (pos.getY() == targetPos.getY()) {
                        layer.add(pos);
                        int dist = ChopUtil.horizontalBlockDistance(targetPos, pos);
                        if (dist > maxDist) {
                            maxDist = dist;
                        }
                    }
                }

                for (BlockPos pos : layer) {
                    if (pos.equals(targetPos)) continue;

                    int dist = ChopUtil.horizontalBlockDistance(targetPos, pos);

                    float startThreshold = (float) dist / (maxDist + 1);

                    int stage = -1;
                    if (progress >= startThreshold) {
                        float localProgress = (progress - startThreshold) / (1.0f - startThreshold);
                        stage = (int) (localProgress * 10.0f);
                    }

                    int fakeId = pos.hashCode();
                    level.destroyBlockProgress(fakeId, pos, stage);
                    currentlyVisualized.add(pos);
                }
            }
        } catch (Exception ignored) {

        }
    }

    public static void clearVisuals(Level level) {
        for (BlockPos pos : currentlyVisualized) {
            level.destroyBlockProgress(pos.hashCode(), pos, -1);
        }
        currentlyVisualized.clear();
    }
}