package com.reggarf.mods.reggadium.extra;

import com.reggarf.mods.reggadium.Config;
import com.reggarf.mods.reggadium.Reggadium;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
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

public class ReggadiumExtraHud {

    public static final ResourceLocation LAYER_ID = new ResourceLocation(Reggadium.MODID, "extra_hud");

    public static class HudLine {
        public final Component text;
        public final int color;

        public HudLine(Component text, int color) {
            this.text = text;
            this.color = color;
        }
    }

    public static void render(GuiGraphics guiGraphics) {
        if (!ExtraSettings.enabled) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.options.renderDebug || mc.options.hideGui) {
            return;
        }

        ClientTickTracker.update();
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
                String axisName = dir.getAxis().getName().toUpperCase();
                Component facingText = Component.translatable(
                        "reggadium.extra.overlay.facing",
                        dir.getName().toUpperCase(),
                        axisSign + axisName
                );
                int facingColor = ExtraSettings.facingColor.getColor(0xAAAAAA);
                lines.add(new HudLine(facingText, facingColor));
            }

            // 4. Biome Line
            if (ExtraSettings.showBiome && mc.level != null) {
                BlockPos pos = player.blockPosition();
                Holder<Biome> biomeHolder = mc.level.getBiome(pos);
                String biomeName = biomeHolder.unwrapKey()
                        .map(k -> k.location().getPath().replace('_', ' '))
                        .orElse("Unknown");
                Component biomeText = Component.translatable("reggadium.extra.overlay.biome", capitalizeWords(biomeName));
                int biomeColor = ExtraSettings.biomeColor.getColor(0x55FFFF);
                lines.add(new HudLine(biomeText, biomeColor));
            }
        }

        // 5. Memory / RAM Usage Line
        if (ExtraSettings.showMemory) {
            long maxMem = Runtime.getRuntime().maxMemory();
            long totalMem = Runtime.getRuntime().totalMemory();
            long freeMem = Runtime.getRuntime().freeMemory();
            long usedMem = totalMem - freeMem;
            long usedMb = usedMem / (1024L * 1024L);
            long maxMb = maxMem / (1024L * 1024L);
            int percent = (int) ((usedMem * 100L) / maxMem);

            int dynamicMemColor;
            if (percent >= 90) {
                dynamicMemColor = 0xFF5555; // Red
            } else if (percent >= 75) {
                dynamicMemColor = 0xFFFF55; // Yellow
            } else {
                dynamicMemColor = 0x55FF55; // Green
            }
            int memColor = ExtraSettings.memoryColor.getColor(dynamicMemColor);

            Component memText = Component.translatable("reggadium.extra.overlay.memory", usedMb, maxMb, percent);
            lines.add(new HudLine(memText, memColor));
        }

        // 6. CPU Usage Line
        if (ExtraSettings.showCpu) {
            double cpuPercent = SystemPerformanceTracker.getCpuUsagePercent();
            int dynamicCpuColor;
            if (cpuPercent >= 85.0) {
                dynamicCpuColor = 0xFF5555;
            } else if (cpuPercent >= 50.0) {
                dynamicCpuColor = 0xFFFF55;
            } else {
                dynamicCpuColor = 0x55FF55;
            }
            int cpuColor = ExtraSettings.cpuColor.getColor(dynamicCpuColor);
            Component cpuText = Component.translatable("reggadium.extra.overlay.cpu", String.format("%.1f", cpuPercent));
            lines.add(new HudLine(cpuText, cpuColor));
        }

        // 7. GPU Usage Line
        if (ExtraSettings.showGpu) {
            double gpuPercent = SystemPerformanceTracker.getGpuUsagePercent();
            int dynamicGpuColor;
            if (gpuPercent >= 90.0) {
                dynamicGpuColor = 0xFF5555;
            } else if (gpuPercent >= 60.0) {
                dynamicGpuColor = 0xFFFF55;
            } else {
                dynamicGpuColor = 0x55FF55;
            }
            int gpuColor = ExtraSettings.gpuColor.getColor(dynamicGpuColor);
            Component gpuText = Component.translatable("reggadium.extra.overlay.gpu", String.format("%.1f", gpuPercent));
            lines.add(new HudLine(gpuText, gpuColor));
        }

        // 8. In-Game Time Line
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
        if (ExtraSettings.showPing && mc.getConnection() != null && player != null) {
            PlayerInfo info = mc.getConnection().getPlayerInfo(player.getUUID());
            if (info != null) {
                int latency = info.getLatency();
                int dynamicPingColor;
                if (latency <= 50) {
                    dynamicPingColor = 0x55FF55; // Low ping
                } else if (latency <= 150) {
                    dynamicPingColor = 0xFFFF55; // Moderate
                } else {
                    dynamicPingColor = 0xFF5555; // High
                }
                int pingColor = ExtraSettings.pingColor.getColor(dynamicPingColor);
                Component pingText = Component.translatable("reggadium.extra.overlay.ping", latency);
                lines.add(new HudLine(pingText, pingColor));
            }
        }

        if (lines.isEmpty()) {
            return;
        }

        renderOverlay(guiGraphics, mc.font, lines);
    }

    private static void renderOverlay(GuiGraphics guiGraphics, Font font, List<HudLine> lines) {
        float scale = ExtraSettings.hudScale / 100.0f;
        int screenWidth = guiGraphics.guiWidth();
        int screenHeight = guiGraphics.guiHeight();

        // Calculate maximum line width
        int maxWidth = 0;
        for (HudLine line : lines) {
            int w = font.width(line.text);
            if (w > maxWidth) maxWidth = w;
        }

        int lineHeight = font.lineHeight + 2;
        int totalHeight = lines.size() * lineHeight + 4;
        int totalWidth = maxWidth + 6;

        float scaledWidth = totalWidth * scale;
        float scaledHeight = totalHeight * scale;

        int margin = 4;
        float posX;
        float posY;

        OverlayCorner corner = ExtraSettings.overlayCorner;
        switch (corner) {
            case TOP_RIGHT:
                posX = screenWidth - scaledWidth - margin;
                posY = margin;
                break;
            case BOTTOM_LEFT:
                posX = margin;
                posY = screenHeight - scaledHeight - margin;
                break;
            case BOTTOM_RIGHT:
                posX = screenWidth - scaledWidth - margin;
                posY = screenHeight - scaledHeight - margin;
                break;
            case TOP_LEFT:
            default:
                posX = margin;
                posY = margin;
                break;
        }

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(posX, posY, 0.0f);
        guiGraphics.pose().scale(scale, scale, 1.0f);

        // Draw background if contrast is BACKGROUND
        if (ExtraSettings.textContrast == TextContrast.BACKGROUND) {
            int bgAlpha = ExtraSettings.backgroundOpacity.getAlpha();
            int bgColor = (bgAlpha << 24) | 0x000000;
            guiGraphics.fill(0, 0, totalWidth, totalHeight, bgColor);
        }

        boolean dropShadow = (ExtraSettings.textContrast == TextContrast.SHADOW);
        int curY = 3;
        for (HudLine line : lines) {
            int curX = 3;
            // For right-aligned corners, align text to the right
            if (corner == OverlayCorner.TOP_RIGHT || corner == OverlayCorner.BOTTOM_RIGHT) {
                curX = totalWidth - 3 - font.width(line.text);
            }
            guiGraphics.drawString(font, line.text, curX, curY, line.color, dropShadow);
            curY += lineHeight;
        }

        guiGraphics.pose().popPose();
    }

    private static String capitalizeWords(String input) {
        if (input == null || input.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        boolean nextUpper = true;
        for (char c : input.toCharArray()) {
            if (Character.isWhitespace(c)) {
                nextUpper = true;
                sb.append(c);
            } else if (nextUpper) {
                sb.append(Character.toUpperCase(c));
                nextUpper = false;
            } else {
                sb.append(Character.toLowerCase(c));
            }
        }
        return sb.toString();
    }
}
