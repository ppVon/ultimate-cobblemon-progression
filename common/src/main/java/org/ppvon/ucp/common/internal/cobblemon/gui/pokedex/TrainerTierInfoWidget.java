package org.ppvon.ucp.common.internal.cobblemon.gui.pokedex;

import com.cobblemon.mod.common.client.CobblemonResources;
import com.cobblemon.mod.common.client.gui.summary.widgets.SoundlessWidget;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.ppvon.ucp.common.internal.trainer.TrainerLevelProgression;

import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.blitk;
import static com.cobblemon.mod.common.client.render.RenderHelperKt.drawScaledText;
import static org.ppvon.ucp.common.UltimateCobblemonProgression.modId;

public class TrainerTierInfoWidget extends SoundlessWidget {
    private record WidgetData() {
        public static Integer width = 171;
        public static Integer height = 20;
        public static ResourceLocation bg = modId("textures/gui/pokedex_trainer_tier.png");
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

        blitk(
                matrices,
                WidgetData.bg,
                this.widgetLeftX + 86,
                this.widgetTopY - 102,
                20,
                171,
                0,
                0,
                171,
                20
        );

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
