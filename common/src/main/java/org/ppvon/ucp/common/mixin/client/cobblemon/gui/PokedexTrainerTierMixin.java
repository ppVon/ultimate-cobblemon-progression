package org.ppvon.ucp.common.mixin.client.cobblemon.gui;


import com.cobblemon.mod.common.client.gui.pokedex.PokedexGUI;
import org.ppvon.ucp.common.internal.cobblemon.gui.pokedex.TrainerTierInfoWidget;
import org.ppvon.ucp.common.mixin.client.minecraft.gui.ScreenAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.cobblemon.mod.common.client.gui.pokedex.PokedexGUIConstants.BASE_HEIGHT;
import static com.cobblemon.mod.common.client.gui.pokedex.PokedexGUIConstants.BASE_WIDTH;
import static com.cobblemon.mod.common.util.LocalizationUtilsKt.lang;

@Mixin(PokedexGUI.class)
public class PokedexTrainerTierMixin {
    @Inject(at = @At("TAIL"), method = "init")
    public void ucp$renderTrainerTier(CallbackInfo ci) {
        int width = ((ScreenAccessor) this).ucp$width();
        int height = ((ScreenAccessor) this).ucp$height();

        int x = (width - BASE_WIDTH) / 2;
        int y = (height - BASE_HEIGHT) / 2;

        TrainerTierInfoWidget trainerTierInfo = new TrainerTierInfoWidget(
                x, y,
                lang("ui.pokedex.pokemon_info")
        );

        ((ScreenAccessor) this).ucp$addRenderableWidget(trainerTierInfo);
    }
}
