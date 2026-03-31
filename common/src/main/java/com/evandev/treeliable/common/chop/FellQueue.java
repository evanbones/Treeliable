package com.evandev.treeliable.common.chop;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FellQueue {
    private static final List<FellTask> TASKS = new ArrayList<>();

    public static void addTask(Map<Integer, List<Runnable>> layersMap, int delayTicks) {
        TASKS.add(new FellTask(layersMap, delayTicks));
    }

    public static void tick() {
        if (TASKS.isEmpty()) return;
        TASKS.removeIf(FellTask::tick);
    }

    private static class FellTask {
        private final List<List<Runnable>> layers;
        private final int delayTicks;
        private int currentDelay = 0;
        private int currentLayerIndex = 0;

        public FellTask(Map<Integer, List<Runnable>> layersMap, int delayTicks) {
            this.layers = new ArrayList<>(new TreeMap<>(layersMap).values());
            this.delayTicks = delayTicks;
        }

        public boolean tick() {
            if (currentDelay > 0) {
                currentDelay--;
                return false;
            }

            if (currentLayerIndex < layers.size()) {
                for (Runnable action : layers.get(currentLayerIndex)) {
                    action.run();
                }
                currentLayerIndex++;
                currentDelay = delayTicks;
            }

            return currentLayerIndex >= layers.size();
        }
    }
}