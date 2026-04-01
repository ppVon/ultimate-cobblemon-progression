package org.ppvon.ucp.common.mixin.client.cobblemon.gui;


import com.cobblemon.mod.common.client.gui.pokedex.PokedexGUI;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import org.ppvon.ucp.common.internal.cobblemon.gui.pokedex.TrainerTierInfoWidget;
import org.ppvon.ucp.common.mixin.client.minecraft.gui.ScreenAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.blitk;
import static com.cobblemon.mod.common.client.gui.pokedex.PokedexGUIConstants.BASE_HEIGHT;
import static com.cobblemon.mod.common.client.gui.pokedex.PokedexGUIConstants.BASE_WIDTH;
import static com.cobblemon.mod.common.util.LocalizationUtilsKt.lang;
import static org.ppvon.ucp.common.UltimateCobblemonProgression.modId;

@Mixin(PokedexGUI.class)
public class PokedexTrainerTierMixin {
    @Unique
    private static final ResourceLocation ucp$trainerTierWidgetBg = modId("textures/gui/pokedex_trainer_tier.png");

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    shift = At.Shift.AFTER,
                    ordinal = 0,
                    target = "Lcom/cobblemon/mod/common/api/gui/GuiUtilsKt;blitk$default(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/resources/ResourceLocation;Ljava/lang/Number;Ljava/lang/Number;Ljava/lang/Number;Ljava/lang/Number;Ljava/lang/Number;Ljava/lang/Number;Ljava/lang/Number;Ljava/lang/Number;Ljava/lang/Number;Ljava/lang/Number;Ljava/lang/Number;Ljava/lang/Number;Ljava/lang/Number;ZFILjava/lang/Object;)V"
            )
    )
    public void ucp$renderTrainerTierBg(
            CallbackInfo ci,
            @Local(name = "matrices") PoseStack matrices,
            @Local(name = "x") int x,
            @Local(name = "y") int y
    ) {
        blitk(
                matrices,
                ucp$trainerTierWidgetBg,
                x + TrainerTierInfoWidget.WidgetData.bgOffsetX(),
                y - TrainerTierInfoWidget.WidgetData.bgOffsetY(),
                20,
                171,
                0,
                0,
                171,
                20
        );
    }

    @Inject(at = @At("TAIL"), method = "init")
    public void ucp$renderTrainerTier(CallbackInfo ci) {
        int width = ((ScreenAccessor) this).ucp$width();
        int height = ((ScreenAccessor) this).ucp$height();

        int x = (width - BASE_WIDTH) / 2;
        int y = (height - BASE_HEIGHT) / 2;

        TrainerTierInfoWidget trainerTierInfo = new TrainerTierInfoWidget(x, y, lang("ui.pokedex.pokemon_info"));

        ((ScreenAccessor) this).ucp$addRenderableWidget(trainerTierInfo);
    }
}
