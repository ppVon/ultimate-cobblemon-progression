package org.ppvon.ucp.common.internal.cobblemon.gui.pokedex;

import com.cobblemon.mod.common.client.gui.summary.widgets.SoundlessWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.StringUtils;
import org.ppvon.ucp.common.client.UltimateCobblemonProgressionClient;
import org.ppvon.ucp.common.internal.trainer.TrainerLevelProgression;

import static com.cobblemon.mod.common.client.gui.pokedex.PokedexTooltipKt.renderTooltip;
import static com.cobblemon.mod.common.util.LocalizationUtilsKt.lang;
import static org.ppvon.ucp.common.UltimateCobblemonProgression.modId;
import static org.ppvon.ucp.common.config.ClientConfig.WidgetParams;

public class TrainerTierInfoWidget extends SoundlessWidget {
    // Icons
    private static final ResourceLocation TRAINER_TIER_ICON = modId("textures/icon/trainer_tier.png");
    private static final ResourceLocation LEVEL_CAP_ICON = modId("textures/icon/level_cap.png");
    private static final ResourceLocation SEEN_REQUIRED_ICON = modId("textures/icon/seen_required.png");
    private static final ResourceLocation CAUGHT_REQUIRED_ICON = modId("textures/icon/caught_required.png");

    // Tooltips
    private static final MutableComponent TRAINER_TIER_TOOLTIP = Component.translatable("ucp.gui.pokedex.trainer_tier.tooltip").copy();
    private static final MutableComponent LEVEL_CAP_TOOLTIP = Component.translatable("ucp.gui.pokedex.level_cap.tooltip").copy();
    private static final MutableComponent SEEN_REQUIRED_TOOLTIP = Component.translatable("ucp.gui.pokedex.seen_required.tooltip").copy();
    private static final MutableComponent CAUGHT_REQUIRED_TOOLTIP = Component.translatable("ucp.gui.pokedex.caught_required.tooltip").copy();


    private TrainerLevelProgression.ProgressionInfoHolder progressionInfo;

    private IconLabelComboWidget trainerTier;
    private IconLabelComboWidget levelCap;
    private IconLabelComboWidget seenRequirement;
    private IconLabelComboWidget caughtRequirement;

    public TrainerTierInfoWidget(int pX, int pY) {
        super(pX, pY, WidgetParams.width(), WidgetParams.height(), lang("ui.pokedex.pokemon_info"));

        if (UltimateCobblemonProgressionClient.trainerProgression == null) return;
        this.progressionInfo = UltimateCobblemonProgressionClient.trainerProgression;

        this.trainerTier = new IconLabelComboWidget(
                TRAINER_TIER_ICON,
                Component.translatable("ucp.gui.pokedex.trainer_tier", progressionInfo.tier(), progressionInfo.totalTiers()),
                WidgetParams.trainerLevelOffsetX(),
                WidgetParams.trainerLevelOffsetY(),
                WidgetParams.trainerLevelWidth(),
                WidgetParams.trainerLevelHeight()
        );
        this.addWidget(this.trainerTier);

        if (progressionInfo.levelCap() > -1) {
            this.levelCap = new IconLabelComboWidget(
                    LEVEL_CAP_ICON,
                    Component.translatable(
                            "ucp.gui.pokedex.level_cap",
                            StringUtils.leftPad(String.valueOf(progressionInfo.levelCap()), 3, "0")
                    ),
                    WidgetParams.levelCapOffsetX(),
                    WidgetParams.levelCapOffsetY(),
                    WidgetParams.levelCapWidth(),
                    WidgetParams.levelCapHeight()
            );
            this.addWidget(levelCap);
        }

        if (progressionInfo.requirements().seen() > 0) {
            this.seenRequirement = new IconLabelComboWidget(
                    SEEN_REQUIRED_ICON,
                    Component.translatable(
                            "ucp.gui.pokedex.seen_required",
                            StringUtils.leftPad(String.valueOf(progressionInfo.requirements().seen()), 4, "0")
                    ),
                    WidgetParams.seenRequirementOffsetX(),
                    WidgetParams.seenRequirementOffsetY(),
                    WidgetParams.seenRequirementWidth(),
                    WidgetParams.seenRequirementHeight()
            );
            this.addWidget(seenRequirement);
        }

        if (progressionInfo.requirements().caught() > 0) {
            this.caughtRequirement = new IconLabelComboWidget(
                    CAUGHT_REQUIRED_ICON,
                    Component.translatable(
                            "ucp.gui.pokedex.caught_required",
                            StringUtils.leftPad(String.valueOf(progressionInfo.requirements().caught()), 4, "0")
                    ),
                    WidgetParams.caughtRequirementOffsetX(),
                    WidgetParams.caughtRequirementOffsetY(),
                    WidgetParams.caughtRequirementWidth(),
                    WidgetParams.caughtRequirementHeight()
            );

            this.addWidget(caughtRequirement);
        }
    }

    private void renderComboWidgetTooltip(
            GuiGraphics guiGraphics,
            IconLabelComboWidget widget,
            MutableComponent text,
            int mouseX,
            int mouseY,
            float delta
    ) {
        if (widget.isHovered()) {
            renderTooltip(
                    guiGraphics,
                    text,
                    mouseX,
                    mouseY,
                    delta,
                    -14
            );
        }
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        if (progressionInfo == null) return;

        trainerTier.render(guiGraphics, mouseX, mouseY, delta);
        this.renderComboWidgetTooltip(guiGraphics, trainerTier, TRAINER_TIER_TOOLTIP, mouseX, mouseY, delta);
        if (levelCap != null) {
            levelCap.render(guiGraphics, mouseX, mouseY, delta);
            this.renderComboWidgetTooltip(guiGraphics, levelCap, LEVEL_CAP_TOOLTIP, mouseX, mouseY, delta);
        }
        if (seenRequirement != null) {
            seenRequirement.render(guiGraphics, mouseX, mouseY, delta);
            this.renderComboWidgetTooltip(guiGraphics, seenRequirement, SEEN_REQUIRED_TOOLTIP, mouseX, mouseY, delta);
        }
        if (caughtRequirement != null) {
            caughtRequirement.render(guiGraphics, mouseX, mouseY, delta);
            this.renderComboWidgetTooltip(guiGraphics, caughtRequirement, CAUGHT_REQUIRED_TOOLTIP, mouseX, mouseY, delta);
        }
    }
}
