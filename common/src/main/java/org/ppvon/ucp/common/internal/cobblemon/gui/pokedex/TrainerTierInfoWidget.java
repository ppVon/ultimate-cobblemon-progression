package org.ppvon.ucp.common.internal.cobblemon.gui.pokedex;

import com.cobblemon.mod.common.client.gui.summary.widgets.SoundlessWidget;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.blitk;
import static org.ppvon.ucp.common.UltimateCobblemonProgression.modId;

public class TrainerTierInfoWidget extends SoundlessWidget {
    private record WidgetData() {
        public static Integer width = 300;
        public static Integer height = 40;
        public static ResourceLocation bg = modId("textures/gui/pokedex_trainer_tier.png");
    }

    public TrainerTierInfoWidget(int pX, int pY, @NotNull Component component) {
        super(pX, pY, WidgetData.width, WidgetData.height, component);
    }

    @Override
    protected void renderWidget(GuiGraphics context, int mouseX, int mouseY, float delta) {
        PoseStack matrices = context.pose();
        Player player = Minecraft.getInstance().player;
        player.
        // base dimensions
        int baseWidth = 171;
        int baseHeight = 20;
        // center of screen
        int middleX = Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2;
        int middleY = Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2;
        // left x and top y of widget relative to the center of screen
        int leftX = middleX - (baseWidth / 2);
        int topY = middleY - (baseHeight / 2);

        int x = width / 2;
        int y = height / 2;

        blitk(
                matrices,
                WidgetData.bg,
                leftX + 86,
                topY - 103,
                20,
                171,
                0,
                0,
                171,
                20
        );
    }
}
