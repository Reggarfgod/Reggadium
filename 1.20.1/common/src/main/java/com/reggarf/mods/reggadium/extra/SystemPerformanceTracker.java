package com.reggarf.mods.reggadium.extra;

import net.minecraft.client.Minecraft;

import java.lang.management.ManagementFactory;

public class SystemPerformanceTracker {

    private static final com.sun.management.OperatingSystemMXBean osBean;

    static {
        java.lang.management.OperatingSystemMXBean bean = ManagementFactory.getOperatingSystemMXBean();
        if (bean instanceof com.sun.management.OperatingSystemMXBean sunBean) {
            osBean = sunBean;
        } else {
            osBean = null;
        }
    }

    private static long lastSampleTime = 0;
    private static int currentCpuProcess = 0;
    private static double currentGpuPercent = 0.0;

    public static void update() {
        long now = System.currentTimeMillis();
        // Sample every 500 milliseconds (2 times per second) for smooth and stable readings
        if (now - lastSampleTime < 500) {
            return;
        }
        lastSampleTime = now;

        // 1. Process CPU Utilization
        if (osBean != null) {
            try {
                double load = osBean.getProcessCpuLoad();
                if (load >= 0.0) {
                    currentCpuProcess = (int) Math.round(load * 100.0);
                } else {
                    double sysLoad = osBean.getCpuLoad();
                    if (sysLoad >= 0.0) {
                        currentCpuProcess = (int) Math.round(sysLoad * 100.0);
                    }
                }
            } catch (Throwable ignored) {
            }
        }

        // 2. GPU Utilization
        try {
            Minecraft mc = Minecraft.getInstance();
            double gpu = mc.getGpuUtilization();
            if (gpu >= 0.0) {
                currentGpuPercent = Math.min(100.0, Math.max(0.0, gpu));
            }
        } catch (Throwable ignored) {
        }
    }

    public static int getCpuProcess() {
        return currentCpuProcess;
    }

    public static int getGpuPercent() {
        return (int) Math.round(currentGpuPercent);
    }

    public static double getCpuUsagePercent() {
        return (double) currentCpuProcess;
    }

    public static double getGpuUsagePercent() {
        return currentGpuPercent;
    }
}
