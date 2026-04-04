package org.ppvon.ucp.common.mixin.client.cobblemon.gui;


import com.cobblemon.mod.common.api.scheduling.ClientTaskTracker;
import com.cobblemon.mod.common.client.gui.pokedex.PokedexGUI;
import com.cobblemon.mod.common.client.pokedex.PokedexType;
import com.llamalad7.mixinextras.sugar.Local;
import kotlin.Unit;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import org.ppvon.ucp.common.config.ClientConfig.WidgetParams;
import org.ppvon.ucp.common.config.UcpConfigs;
import org.ppvon.ucp.common.internal.cobblemon.gui.pokedex.TrainerTierInfoWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.blitk;
import static org.ppvon.ucp.common.UltimateCobblemonProgression.LOGGER;
import static org.ppvon.ucp.common.UltimateCobblemonProgression.modId;

@Mixin(PokedexGUI.class)
public class PokedexTrainerTierMixin {
    @Unique
    private TrainerTierInfoWidget ucp$trainerTierInfo = new TrainerTierInfoWidget(
            WidgetParams.offsetX(),
            WidgetParams.offsetY()
    );

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

        if (WidgetParams.debug()) {
            // resource-intensive
            ClientTaskTracker.INSTANCE.addTask(
                    ClientTaskTracker.INSTANCE.taskBuilder()
                            .infiniteIterations()
                            .interval(1f) // reload every 1 second
                            .execute((task) -> {
                                try {
                                    UcpConfigs.reloadClient();
                                    this.ucp$trainerTierInfo = new TrainerTierInfoWidget(
                                            WidgetParams.offsetX(),
                                            WidgetParams.offsetY()
                                    );
                                } catch (Throwable e) {
                                    LOGGER.info("Config failed to reload: {}", e.getMessage());
                                }
                                return Unit.INSTANCE;
                            })
                            .build()
            );
        }
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
            @Local(name = "context") GuiGraphics context,
            @Local(name = "mouseX") int mouseX,
            @Local(name = "mouseY") int mouseY,
            @Local(name = "delta") float delta
    ) {
        blitk(
                context.pose(),
                ucp$trainerTierWidgetBg,
                WidgetParams.offsetX(),
                WidgetParams.offsetY(),
                WidgetParams.height(),
                WidgetParams.width(),
                0,
                0,
                WidgetParams.width(),
                WidgetParams.height()
        );
        if (WidgetParams.bgVariantEnabled()) {
            blitk(
                    context.pose(),
                    ucp$trainerTierWidgetVariantBg(ucp$type),
                    WidgetParams.bgVariantOffsetX(),
                    WidgetParams.bgVariantOffsetY(),
                    WidgetParams.bgVariantHeight(),
                    WidgetParams.bgVariantWidth(),
                    0,
                    0,
                    WidgetParams.bgVariantWidth(),
                    WidgetParams.bgVariantHeight()
            );
        }

        ucp$trainerTierInfo.render(context, mouseX, mouseY, delta);
    }

    @Unique
    private static ResourceLocation ucp$trainerTierWidgetVariantBg(PokedexType type) {
        return modId(String.format("textures/gui/trainer_tier_widget_variant_%s.png", type.name().toLowerCase()));
    }
}
