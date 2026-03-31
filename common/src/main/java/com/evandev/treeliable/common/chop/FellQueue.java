package com.evandev.treeliable.common.chop;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FellQueue {
    private static final List<FellTask> TASKS = new ArrayList<>();

    public static void addTask(Map<Integer, List<Runnable>> layersMap, int delayTicks, boolean exponentialSpeedup) {
        TASKS.add(new FellTask(layersMap, delayTicks, exponentialSpeedup));
    }

    public static void tick() {
        if (TASKS.isEmpty()) return;
        TASKS.removeIf(FellTask::tick);
    }

    private static class FellTask {
        private final List<List<Runnable>> layers;
        private final int baseDelayTicks;
        private final boolean exponentialSpeedup;
        private int currentDelay = 0;
        private int currentLayerIndex = 0;

        public FellTask(Map<Integer, List<Runnable>> layersMap, int delayTicks, boolean exponentialSpeedup) {
            this.layers = new ArrayList<>(new TreeMap<>(layersMap).values());
            this.baseDelayTicks = delayTicks;
            this.exponentialSpeedup = exponentialSpeedup;
        }

        public boolean tick() {
            if (currentDelay > 0) {
                currentDelay--;
                return false;
            }

            if (baseDelayTicks <= 0) {
                currentDelay = 0;
            } else if (exponentialSpeedup) {
                double speed = Math.pow(1.2, currentLayerIndex);
                double delayForNext = baseDelayTicks / speed;

                currentDelay = (int) Math.max(0, Math.round(delayForNext));
            } else {
                currentDelay = baseDelayTicks;
            }

            if (currentLayerIndex < layers.size()) {
                for (Runnable action : layers.get(currentLayerIndex)) {
                    action.run();
                }
                currentLayerIndex++;
            }

            return currentLayerIndex >= layers.size();
        }
    }
}