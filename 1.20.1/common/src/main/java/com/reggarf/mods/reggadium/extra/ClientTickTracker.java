package com.reggarf.mods.reggadium.extra;

import com.google.common.collect.EvictingQueue;
import net.minecraft.client.Minecraft;

import java.util.Queue;

public class ClientTickTracker {

    private static final Queue<Integer> FPS_QUEUE = EvictingQueue.create(100);
    private static int currentFps = 0;
    private static int averageFps = 0;
    private static int lowestFps = 0;
    private static int highestFps = 0;

    public static void update() {
        Minecraft mc = Minecraft.getInstance();
        currentFps = mc.getFps();
        FPS_QUEUE.add(currentFps);

        int sum = 0;
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        for (int fps : FPS_QUEUE) {
            sum += fps;
            if (fps < min) min = fps;
            if (fps > max) max = fps;
        }

        int count = FPS_QUEUE.size();
        averageFps = count > 0 ? (sum / count) : currentFps;
        lowestFps = min != Integer.MAX_VALUE ? min : currentFps;
        highestFps = max != Integer.MIN_VALUE ? max : currentFps;
    }

    public static int getCurrentFps() {
        return currentFps;
    }

    public static int getAverageFps() {
        return averageFps;
    }

    public static int getLowestFps() {
        return lowestFps;
    }

    public static int getHighestFps() {
        return highestFps;
    }
}
