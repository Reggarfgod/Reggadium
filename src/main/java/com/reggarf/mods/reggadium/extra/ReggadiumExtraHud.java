package com.reggarf.mods.reggadium.extra;

import com.reggarf.mods.reggadium.Config;
import com.reggarf.mods.reggadium.Reggadium;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class ReggadiumExtraHud implements LayeredDraw.Layer {

    public static final ResourceLocation LAYER_ID = ResourceLocation.fromNamespaceAndPath(Reggadium.MODID, "extra_hud");

    public static class HudLine {
        public final Component text;
        public final int color;

        public HudLine(Component text, int color) {
            this.text = text;
            this.color = color;
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (!ExtraSettings.enabled) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.getDebugOverlay().showDebugScreen() || mc.options.hideGui) {
            return;
        }

        // Update system performance tracker (CPU, GPU)
        SystemPerformanceTracker.update();

        List<HudLine> lines = new ArrayList<>();

        // 1. FPS Line
        if (ExtraSettings.showFps) {
            int fps = ClientTickTracker.getCurrentFps();
            int dynamicFpsColor;
            if (fps >= 60) {
                dynamicFpsColor = 0x55FF55; // Vibrant Green
            } else if (fps >= 30) {
                dynamicFpsColor = 0xFFFF55; // Yellow
            } else {
                dynamicFpsColor = 0xFF5555; // Red
            }
            int fpsColor = ExtraSettings.fpsColor.getColor(dynamicFpsColor);

            if (ExtraSettings.showFpsExtended) {
                Component fpsText = Component.translatable(
                        "reggadium.extra.overlay.fps_extended",
                        fps,
                        ClientTickTracker.getLowestFps(),
                        ClientTickTracker.getAverageFps(),
                        ClientTickTracker.getHighestFps()
                );
                lines.add(new HudLine(fpsText, fpsColor));
            } else {
                Component fpsText = Component.translatable("reggadium.extra.overlay.fps", fps);
                lines.add(new HudLine(fpsText, fpsColor));
            }
        }

        LocalPlayer player = mc.player;
        if (player != null && !player.isReducedDebugInfo()) {
            // 2. Coordinates Line
            if (ExtraSettings.showCoords) {
                Vec3 pos = player.position();
                String fmt = ExtraSettings.coordPrecision.getFormat();
                Component coordsText = Component.translatable(
                        "reggadium.extra.overlay.coordinates",
                        String.format(fmt, pos.x),
                        String.format(fmt, pos.y),
                        String.format(fmt, pos.z)
                );
                int coordsColor = ExtraSettings.coordsColor.getColor(0xE0E0E0);
                lines.add(new HudLine(coordsText, coordsColor));
            }

            // 3. Facing Direction Line
            if (ExtraSettings.showFacing) {
                Direction dir = player.getDirection();
                String axisSign = dir.getAxisDirection() == Direction.AxisDirection.POSITIVE ? "+" : "-";
                String axisName = axisSign + dir.getAxis().getName().toUpperCase();
                Component facingText = Component.translatable(
                        "reggadium.extra.overlay.facing",
                        dir.getName().toUpperCase(),
                        axisName
                );
                int facingColor = ExtraSettings.facingColor.getColor(0xCCCCCC);
                lines.add(new HudLine(facingText, facingColor));
            }

            // 4. Biome Line
            if (ExtraSettings.showBiome && mc.level != null) {
                BlockPos blockPos = player.blockPosition();
                Holder<Biome> biomeHolder = mc.level.getBiome(blockPos);
                String biomeName = biomeHolder.unwrapKey()
                        .map(k -> formatBiomeName(k.location().getPath()))
                        .orElse("Unknown");
                Component biomeText = Component.translatable("reggadium.extra.overlay.biome", biomeName);
                int biomeColor = ExtraSettings.biomeColor.getColor(0x88DDFF);
                lines.add(new HudLine(biomeText, biomeColor));
            }

            // 5. Memory / RAM Usage Line
            if (ExtraSettings.showMemory) {
                Runtime rt = Runtime.getRuntime();
                long total = rt.totalMemory();
                long free = rt.freeMemory();
                long max = rt.maxMemory();
                long used = total - free;
                int usedMb = (int) (used / 1024L / 1024L);
                int maxMb = (int) (max / 1024L / 1024L);
                int percent = max > 0 ? (int) ((used * 100L) / max) : 0;

                int dynamicMemColor;
                if (percent < 70) {
                    dynamicMemColor = 0x55FF55; // Green
                } else if (percent < 85) {
                    dynamicMemColor = 0xFFFF55; // Yellow
                } else {
                    dynamicMemColor = 0xFF5555; // Red
                }
                int memColor = ExtraSettings.memoryColor.getColor(dynamicMemColor);
                Component memText = Component.translatable("reggadium.extra.overlay.memory", usedMb, maxMb, percent);
                lines.add(new HudLine(memText, memColor));
            }

            // 6. CPU Usage Line
            if (ExtraSettings.showCpu) {
                int cpu = SystemPerformanceTracker.getCpuProcess();
                int dynamicCpuColor;
                if (cpu < 60) {
                    dynamicCpuColor = 0x55FF55; // Green
                } else if (cpu < 85) {
                    dynamicCpuColor = 0xFFFF55; // Yellow
                } else {
                    dynamicCpuColor = 0xFF5555; // Red
                }
                int cpuColor = ExtraSettings.cpuColor.getColor(dynamicCpuColor);
                Component cpuText = Component.translatable("reggadium.extra.overlay.cpu", cpu);
                lines.add(new HudLine(cpuText, cpuColor));
            }

            // 7. GPU Usage Line
            if (ExtraSettings.showGpu) {
                int gpu = SystemPerformanceTracker.getGpuPercent();
                int dynamicGpuColor;
                if (gpu < 70) {
                    dynamicGpuColor = 0x55FF55; // Green
                } else if (gpu < 90) {
                    dynamicGpuColor = 0xFFFF55; // Yellow
                } else {
                    dynamicGpuColor = 0xFF5555; // Red
                }
                int gpuColor = ExtraSettings.gpuColor.getColor(dynamicGpuColor);
                Component gpuText = Component.translatable("reggadium.extra.overlay.gpu", gpu);
                lines.add(new HudLine(gpuText, gpuColor));
            }

            // 8. In-Game Time & Day Line
            if (ExtraSettings.showGameTime && mc.level != null) {
                long dayTime = mc.level.getDayTime();
                long day = dayTime / 24000L;
                long timeOfDay = dayTime % 24000L;
                int hours = (int) ((timeOfDay / 1000 + 6) % 24);
                int minutes = (int) ((timeOfDay % 1000) * 60 / 1000);
                String timeStr = String.format("%02d:%02d", hours, minutes);

                int dynamicTimeColor;
                if (timeOfDay < 12000) {
                    dynamicTimeColor = 0xFFFF55; // Day Yellow
                } else if (timeOfDay < 13800) {
                    dynamicTimeColor = 0xFFAA00; // Sunset Gold
                } else if (timeOfDay < 22200) {
                    dynamicTimeColor = 0x55FFFF; // Night Cyan / Blue
                } else {
                    dynamicTimeColor = 0xFFAA00; // Sunrise Gold
                }
                int timeColor = ExtraSettings.timeColor.getColor(dynamicTimeColor);
                Component timeText = Component.translatable("reggadium.extra.overlay.gametime", timeStr, day);
                lines.add(new HudLine(timeText, timeColor));
            }

            // 9. Ping / Latency Line
            if (ExtraSettings.showPing && mc.getConnection() != null) {
                PlayerInfo info = mc.getConnection().getPlayerInfo(player.getUUID());
                if (info != null) {
                    int latency = info.getLatency();
                    int dynamicPingColor;
                    if (latency < 80) {
                        dynamicPingColor = 0x55FF55; // Green
                    } else if (latency < 180) {
                        dynamicPingColor = 0xFFFF55; // Yellow
                    } else {
                        dynamicPingColor = 0xFF5555; // Red
                    }
                    int pingColor = ExtraSettings.pingColor.getColor(dynamicPingColor);
                    Component pingText = Component.translatable("reggadium.extra.overlay.ping", latency);
                    lines.add(new HudLine(pingText, pingColor));
                }
            }
        }

        if (lines.isEmpty()) {
            return;
        }

        Font font = mc.font;
        float scale = Math.max(0.2f, ExtraSettings.hudScale / 100.0f);
        int screenWidth = (int) (guiGraphics.guiWidth() / scale);
        int screenHeight = (int) (guiGraphics.guiHeight() / scale);

        OverlayCorner corner = ExtraSettings.overlayCorner;
        TextContrast contrast = ExtraSettings.textContrast;
        int bgArgb = ExtraSettings.backgroundOpacity.getColorArgb();

        int y;
        if (corner == OverlayCorner.BOTTOM_LEFT || corner == OverlayCorner.BOTTOM_RIGHT) {
            y = screenHeight - font.lineHeight - 4;
        } else {
            y = 4;
        }

        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(scale, scale, 1.0f);

        for (HudLine line : lines) {
            int width = font.width(line.text);
            int x;
            if (corner == OverlayCorner.TOP_RIGHT || corner == OverlayCorner.BOTTOM_RIGHT) {
                x = screenWidth - width - 4;
            } else {
                x = 4;
            }

            if (contrast == TextContrast.BACKGROUND) {
                guiGraphics.fill(x - 2, y - 1, x + width + 2, y + font.lineHeight + 1, bgArgb);
            }

            boolean dropShadow = (contrast == TextContrast.SHADOW);
            guiGraphics.drawString(font, line.text, x, y, line.color, dropShadow);

            if (corner == OverlayCorner.BOTTOM_LEFT || corner == OverlayCorner.BOTTOM_RIGHT) {
                y -= font.lineHeight + 2;
            } else {
                y += font.lineHeight + 2;
            }
        }

        guiGraphics.pose().popPose();
    }

    private static String formatBiomeName(String raw) {
        String[] parts = raw.split("_");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            if (!parts[i].isEmpty()) {
                sb.append(Character.toUpperCase(parts[i].charAt(0)));
                if (parts[i].length() > 1) {
                    sb.append(parts[i].substring(1).toLowerCase());
                }
                if (i < parts.length - 1) {
                    sb.append(" ");
                }
            }
        }
        return sb.toString();
    }
}
