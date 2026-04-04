package org.ppvon.ucp.common.config;

import net.minecraft.client.Minecraft;

public class ClientConfig {
    public boolean debug = false;

    // Widget and variation overlay coords are calculated from center of the screen
    // Reasoning is to preserve widget position regardless of current GUI scale

    public int widgetX = 1;
    public int widgetY = 1;
    public int widgetWidth = 1;
    public int widgetHeight = 1;

    // Variation bg layer overlays on top of widget bg but stays below the screen
    public boolean enableVariationBg = true;
    public int variationLayerBgX = 1;
    public int variationLayerBgY = 1;
    public int variationLayerBgWidth = 1;
    public int variationLayerBgHeight = 1;


    // All widgets are positioned relative to top left corner of widget
    public int trainerLevelX = 1;
    public int trainerLevelY = 1;
    public int trainerLevelWidth = 1;
    public int trainerLevelHeight = 1;

    public int levelCapX = 1;
    public int levelCapY = 1;
    public int levelCapWidth = 1;
    public int levelCapHeight = 1;

    public int seenRequirementX = 1;
    public int seenRequirementY = 1;
    public int seenRequirementWidth = 1;
    public int seenRequirementHeight = 1;

    public int caughtRequirementX = 1;
    public int caughtRequirementY = 1;
    public int caughtRequirementWidth = 1;
    public int caughtRequirementHeight = 1;


    public static class WidgetParams {
        // Needed these to be a function to be able to do hot reload whenever i change values
        // because mixin doesn't like hot reloading plain values
        // Doing it this way in class effectively changes return value in a function
        // and that return value is consumed by mixin injection

        public static boolean debug() {
            return UcpConfigs.client().debug;
        }

        public static int width() {
            return UcpConfigs.client().widgetWidth;
        }

        public static int height() {
            return UcpConfigs.client().widgetHeight;
        }

        public static int offsetX() {
            return (Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2) + UcpConfigs.client().widgetX;
        }

        public static int offsetY() {
            return (Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2) + UcpConfigs.client().widgetY;
        }

        // Variant background piece

        public static boolean bgVariantEnabled() {
            return UcpConfigs.client().enableVariationBg;
        }

        public static int bgVariantWidth() {
            return UcpConfigs.client().variationLayerBgWidth;
        }

        public static int bgVariantHeight() {
            return UcpConfigs.client().variationLayerBgHeight;
        }


        public static int bgVariantOffsetX() {
            return (Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2) + UcpConfigs.client().variationLayerBgX;
        }

        public static int bgVariantOffsetY() {
            return (Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2) + UcpConfigs.client().variationLayerBgY;
        }

        // Trainer level

        public static int trainerLevelWidth() {
            return UcpConfigs.client().trainerLevelWidth;
        }

        public static int trainerLevelHeight() {
            return UcpConfigs.client().trainerLevelHeight;
        }

        public static int trainerLevelOffsetX() {
            return (offsetX() + UcpConfigs.client().trainerLevelX);
        }

        public static int trainerLevelOffsetY() {
            return (offsetY() + UcpConfigs.client().trainerLevelY);
        }

        // Spawn level cap

        public static int levelCapWidth() {
            return UcpConfigs.client().levelCapWidth;
        }

        public static int levelCapHeight() {
            return UcpConfigs.client().levelCapHeight;
        }

        public static int levelCapOffsetX() {
            return offsetX() + UcpConfigs.client().levelCapX;
        }

        public static int levelCapOffsetY() {
            return offsetY() + UcpConfigs.client().levelCapY;
        }

        // Seen requirement

        public static int seenRequirementWidth() {
            return UcpConfigs.client().seenRequirementWidth;
        }

        public static int seenRequirementHeight() {
            return UcpConfigs.client().seenRequirementHeight;
        }

        public static int seenRequirementOffsetX() {
            return offsetX() + UcpConfigs.client().seenRequirementX;
        }

        public static int seenRequirementOffsetY() {
            return offsetY() + UcpConfigs.client().seenRequirementY;
        }

        // Caught requirement

        public static int caughtRequirementWidth() {
            return UcpConfigs.client().caughtRequirementWidth;
        }

        public static int caughtRequirementHeight() {
            return UcpConfigs.client().caughtRequirementHeight;
        }

        public static int caughtRequirementOffsetX() {
            return offsetX() + UcpConfigs.client().caughtRequirementX;
        }

        public static int caughtRequirementOffsetY() {
            return offsetY() + UcpConfigs.client().caughtRequirementY;
        }

    }
}
