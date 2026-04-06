package org.ppvon.ucp.common.internal.cobblemon.gui.pokedex;

import com.cobblemon.mod.common.client.CobblemonResources;
import com.cobblemon.mod.common.client.gui.summary.widgets.SoundlessWidget;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.*;

public class IconLabelComboWidget extends SoundlessWidget {
    private final ResourceLocation icon;
    private final MutableComponent label;

    public IconLabelComboWidget(
            @NotNull ResourceLocation icon,
            @NotNull Component label,
            int x,
            int y,
            int width,
            int height
    ) {
        super(x, y, width, height, label);
        this.label = label.copy();
        this.icon = icon;
    }

    @Override
    protected void renderWidget(GuiGraphics context, int mouseX, int mouseY, float delta) {
        PoseStack matrices = context.pose();
        blitk(
                matrices,
                icon,
                this.getX(),
                this.getY(),
                9,
                9,
                0,
                0,
                9,
                9
        );

        drawText(
                context,
                CobblemonResources.INSTANCE.getDEFAULT_LARGE(),
                this.label.withStyle(Style.EMPTY.withBold(true)),
                this.getX() + 12,
                this.getY(),
                0x00FFFFFF
        );
    }
}
