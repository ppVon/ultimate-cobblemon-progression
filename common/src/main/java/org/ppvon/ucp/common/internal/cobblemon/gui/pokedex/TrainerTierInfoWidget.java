package org.ppvon.ucp.common.internal.cobblemon.gui.pokedex;

import com.cobblemon.mod.common.client.CobblemonResources;
import com.cobblemon.mod.common.client.gui.summary.widgets.SoundlessWidget;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;
import org.ppvon.ucp.common.internal.trainer.TrainerLevelProgression;

import static com.cobblemon.mod.common.client.render.RenderHelperKt.drawScaledText;

public class TrainerTierInfoWidget extends SoundlessWidget {
    public static class WidgetData {
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

        /**
         * Returns widget bg offset from center of screen at y coordinate
         *
         * @return int
         */
        public static int bgOffsetY() {
            return 8;
        }
    }

    private int screenMiddleX;
    private int screenMiddleY;
    private int widgetLeftX;
    private int widgetTopY;
    private TrainerLevelProgression.DexCounts dexCounts;

    public TrainerTierInfoWidget(int pX, int pY, @NotNull Component component) {
        super(pX, pY, WidgetData.width, WidgetData.height, component);
        this.setScreenMiddleX(Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2);
        this.setScreenMiddleY(Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2);
        this.setWidgetLeftX(this.screenMiddleX - (WidgetData.width / 2));
        this.setWidgetTopY(this.screenMiddleY - (WidgetData.height / 2));
    }

    @Override
    protected void renderWidget(GuiGraphics context, int mouseX, int mouseY, float delta) {
        PoseStack matrices = context.pose();

        drawScaledText(
                context,
                CobblemonResources.INSTANCE.getDEFAULT_LARGE(),
                Component.translatable("Tier: 1/7").withStyle(Style.EMPTY.withBold(true)),
                this.widgetLeftX + 128,
                this.widgetTopY - 97,
                1F,
                1F,
                Integer.MAX_VALUE,
                0x00FFFFFF,
                false,
                true,
                null,
                null
        );
    }

    public void setScreenMiddleX(int screenMiddleX) {
        this.screenMiddleX = screenMiddleX;
    }

    public void setScreenMiddleY(int screenMiddleY) {
        this.screenMiddleY = screenMiddleY;
    }

    public void setWidgetLeftX(int widgetLeftX) {
        this.widgetLeftX = widgetLeftX;
    }

    public void setWidgetTopY(int widgetTopY) {
        this.widgetTopY = widgetTopY;
    }

    public void setDexCounts(TrainerLevelProgression.DexCounts dexCounts) {
        this.dexCounts = dexCounts;
    }
}
