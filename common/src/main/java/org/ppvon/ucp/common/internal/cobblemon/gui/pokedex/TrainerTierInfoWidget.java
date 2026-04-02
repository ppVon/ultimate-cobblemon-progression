package org.ppvon.ucp.common.internal.cobblemon.gui.pokedex;

import com.cobblemon.mod.common.client.gui.summary.widgets.SoundlessWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.ppvon.ucp.common.client.UltimateCobblemonProgressionClient;
import org.ppvon.ucp.common.internal.trainer.TrainerLevelProgression;

import static org.ppvon.ucp.common.UltimateCobblemonProgression.modId;

public class TrainerTierInfoWidget extends SoundlessWidget {

    public static class WidgetData {
        public static ResourceLocation TRAINER_TIER_ICON = modId("textures/icon/trainer_tier.png");
        public static ResourceLocation LEVEL_CAP_ICON = modId("textures/icon/level_cap.png");
        public static ResourceLocation SEEN_REQUIRED_ICON = modId("textures/icon/seen_required.png");
        public static ResourceLocation CAUGHT_REQUIRED_ICON = modId("textures/icon/caught_required.png");

        public static int width = 171;
        public static int height = 20;

        /**
         * Needed these to be a function to be able to do hot reload whenever i change values
         * Returns widget bg offset from center of screen at x coordinate
         *
         * @return int
         */
        public static int bgOffsetX() {
            return 174;
        }

        public static int bgVariantOffsetX() {
            return 328;
        }

        public static int bgVariantSizeX() {
            return 16;
        }

        /**
         * Returns widget bg offset from center of screen at y coordinate
         *
         * @return int
         */
        public static int bgOffsetY() {
            return 8;
        }
        public static int bgVariantOffsetY() {
            return -5;
        }
        public static int bgVariantSizeY() {
            return 12;
        }

        public static int ICON_LABEL_WIDGET_WIDTH() {
            return 40;
        }

        public static int ICON_LABEL_WIDGET_HEIGHT() {
            return 10;
        }

        public static int ICON_LABEL_WIDGET_OFFSET() {
            return 34;
        }
    }

    /**
     * we always have that one regardless of config (for now)
     * the rest are determined by config
     */
    private final IconLabelComboWidget trainerTier;
    private IconLabelComboWidget levelCap;
    private IconLabelComboWidget seenRequirement;
    private IconLabelComboWidget caughtRequirement;

    public TrainerTierInfoWidget(int pX, int pY, @NotNull Component component) {
        super(pX, pY, WidgetData.width, WidgetData.height, component);

        int screenMiddleX = Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2;
        int screenMiddleY = Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2;
        int widgetLeftX = screenMiddleX - (WidgetData.width / 2) + 122;
        int widgetTopY = screenMiddleY - (WidgetData.height / 2) - 98;
        int nextWidgetX = 0;

        TrainerLevelProgression.ProgressionInfoHolder progressionInfo = UltimateCobblemonProgressionClient.trainerProgression;

        this.trainerTier = new IconLabelComboWidget(
                WidgetData.TRAINER_TIER_ICON,
                Component.translatable("ucp.gui.pokedex.trainer_tier", progressionInfo.tier(), progressionInfo.totalTiers()),
                widgetLeftX + nextWidgetX,
                widgetTopY,
                WidgetData.ICON_LABEL_WIDGET_WIDTH(),
                WidgetData.ICON_LABEL_WIDGET_HEIGHT()
        );

        this.addWidget(this.trainerTier);
        nextWidgetX += (WidgetData.ICON_LABEL_WIDGET_OFFSET() - 4);

        if (progressionInfo.levelCap() > -1) {
            this.levelCap = new IconLabelComboWidget(
                    WidgetData.LEVEL_CAP_ICON,
                    Component.translatable(
                            "ucp.gui.pokedex.level_cap",
                            StringUtils.leftPad(String.valueOf(progressionInfo.levelCap()), 3, "0")
                    ),
                    widgetLeftX + nextWidgetX,
                    widgetTopY,
                    WidgetData.ICON_LABEL_WIDGET_WIDTH(),
                    WidgetData.ICON_LABEL_WIDGET_HEIGHT()
            );

            this.addWidget(levelCap);
            nextWidgetX += WidgetData.ICON_LABEL_WIDGET_OFFSET();
        }

        if (progressionInfo.requirements().seen() > 0) {
            this.seenRequirement = new IconLabelComboWidget(
                    WidgetData.SEEN_REQUIRED_ICON,
                    Component.translatable(
                            "ucp.gui.pokedex.seen_required",
                            StringUtils.leftPad(String.valueOf(progressionInfo.requirements().seen()), 4, "0")
                    ),
                    widgetLeftX + nextWidgetX,
                    widgetTopY,
                    WidgetData.ICON_LABEL_WIDGET_WIDTH(),
                    WidgetData.ICON_LABEL_WIDGET_HEIGHT()
            );

            this.addWidget(seenRequirement);
            nextWidgetX += WidgetData.ICON_LABEL_WIDGET_OFFSET();
        }

        if (progressionInfo.requirements().caught() > 0) {
            this.caughtRequirement = new IconLabelComboWidget(
                    WidgetData.CAUGHT_REQUIRED_ICON,
                    Component.translatable(
                            "ucp.gui.pokedex.caught_required",
                            StringUtils.leftPad(String.valueOf(progressionInfo.requirements().caught()), 4, "0")
                    ),
                    widgetLeftX + nextWidgetX,
                    widgetTopY,
                    WidgetData.ICON_LABEL_WIDGET_WIDTH(),
                    WidgetData.ICON_LABEL_WIDGET_HEIGHT()
            );

            this.addWidget(caughtRequirement);
        }
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        trainerTier.render(guiGraphics, mouseX, mouseY, delta);
        if (levelCap != null) {
            levelCap.render(guiGraphics, mouseX, mouseY, delta);
        }
        if (seenRequirement != null) {
            seenRequirement.render(guiGraphics, mouseX, mouseY, delta);
        }
        if (caughtRequirement != null) {
            caughtRequirement.render(guiGraphics, mouseX, mouseY, delta);
        }
    }
}
