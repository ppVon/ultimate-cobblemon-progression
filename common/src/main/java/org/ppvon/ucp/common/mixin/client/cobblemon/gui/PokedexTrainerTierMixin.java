package org.ppvon.ucp.common.mixin.client.cobblemon.gui;


import com.cobblemon.mod.common.client.gui.pokedex.PokedexGUI;
import com.cobblemon.mod.common.client.pokedex.PokedexType;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import org.ppvon.ucp.common.internal.cobblemon.gui.pokedex.TrainerTierInfoWidget;
import org.ppvon.ucp.common.internal.network.UCPNetwork;
import org.ppvon.ucp.common.internal.network.client.payload.PokedexOpenC2S;
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
    private PokedexType ucp$type;

    @Unique
    private static final ResourceLocation ucp$trainerTierWidgetBg = modId("textures/gui/trainer_tier_widget.png");

    @Inject(
            method = "<init>(Lcom/cobblemon/mod/common/client/pokedex/PokedexType;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/core/BlockPos;)V",
            at = @At(value = "TAIL")
    )
    public void ucp$capturePokedexType(PokedexType type, ResourceLocation initSpecies, BlockPos blockPos, CallbackInfo ci) {
        this.ucp$type = type;
    }

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
        blitk(
                matrices,
                ucp$trainerTierWidgetVariantBg(ucp$type),
                x + TrainerTierInfoWidget.WidgetData.bgVariantOffsetX(),
                y - TrainerTierInfoWidget.WidgetData.bgVariantOffsetY(),
                TrainerTierInfoWidget.WidgetData.bgVariantSizeY(),
                TrainerTierInfoWidget.WidgetData.bgVariantSizeX(),
                0,
                0,
                TrainerTierInfoWidget.WidgetData.bgVariantSizeX(),
                TrainerTierInfoWidget.WidgetData.bgVariantSizeY()
        );
    }

    @Inject(method = "init", at = @At("TAIL"))
    public void ucp$renderTrainerTier(CallbackInfo ci) {
        int width = ((ScreenAccessor) this).ucp$width();
        int height = ((ScreenAccessor) this).ucp$height();

        int x = (width - BASE_WIDTH) / 2;
        int y = (height - BASE_HEIGHT) / 2;
        UCPNetwork.sendToServer(new PokedexOpenC2S());
        TrainerTierInfoWidget trainerTierInfo = new TrainerTierInfoWidget(x, y, lang("ui.pokedex.pokemon_info"));

        ((ScreenAccessor) this).ucp$addRenderableWidget(trainerTierInfo);
    }

    @Unique
    private static ResourceLocation ucp$trainerTierWidgetVariantBg(PokedexType type) {
        return modId(String.format("textures/gui/trainer_tier_widget_variant_%s.png", type.name().toLowerCase()));
    }
}
