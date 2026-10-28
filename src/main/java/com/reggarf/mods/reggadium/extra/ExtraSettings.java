package com.reggarf.mods.reggadium.extra;

import com.reggarf.mods.reggadium.Config;

public class ExtraSettings {

    // Metrics toggles
    public static boolean showFps = true;
    public static boolean showFpsExtended = false;
    public static boolean showCoords = false;
    public static boolean showFacing = false;
    public static boolean showBiome = false;
    public static boolean showMemory = false;
    public static boolean showCpu = false;
    public static boolean showGpu = false;
    public static boolean showGameTime = false;
    public static boolean showPing = false;

    // Positioning, Scaling & Styling
    public static OverlayCorner overlayCorner = OverlayCorner.TOP_LEFT;
    public static int hudScale = 100; // 50% to 200%, default 100%
    public static TextContrast textContrast = TextContrast.SHADOW;
    public static BackgroundOpacity backgroundOpacity = BackgroundOpacity.MEDIUM;
    public static CoordPrecision coordPrecision = CoordPrecision.TWO_DECIMALS;

    // Color customization
    public static HudColor fpsColor = HudColor.DYNAMIC;
    public static HudColor coordsColor = HudColor.WHITE;
    public static HudColor facingColor = HudColor.GRAY;
    public static HudColor biomeColor = HudColor.CYAN;
    public static HudColor memoryColor = HudColor.DYNAMIC;
    public static HudColor cpuColor = HudColor.DYNAMIC;
    public static HudColor gpuColor = HudColor.DYNAMIC;
    public static HudColor timeColor = HudColor.GOLD;
    public static HudColor pingColor = HudColor.DYNAMIC;

    public static void save() {
        Config.save();
    }
}
