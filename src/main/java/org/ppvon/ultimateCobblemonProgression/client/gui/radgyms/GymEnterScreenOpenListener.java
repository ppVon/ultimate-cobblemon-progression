package org.ppvon.ultimateCobblemonProgression.client.gui.radgyms;


import com.cobblemon.mod.common.api.Priority;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import lol.gito.radgyms.api.event.GymEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.ppvon.ultimateCobblemonProgression.common.component.TrainerLevelComponents;
import org.ppvon.ultimateCobblemonProgression.common.tiers.TierRegistry;

public final class GymEnterScreenOpenListener {
    private GymEnterScreenOpenListener() {}

    public static void register() {
        GymEvents.ENTER_SCREEN_OPEN.subscribe(Priority.LOW, onEnterScreenOpen);
    }

    private static final Function1<GymEvents.GymEnterScreenOpenEvent, Unit> onEnterScreenOpen = event -> {
        LocalPlayer p = Minecraft.getInstance().player;
        if (p == null) {
            return Unit.INSTANCE;
        }

        int playerLevel = TrainerLevelComponents.KEY.get(p).getLevel();
        int levelCap = TierRegistry.getCapForTier(playerLevel);
        int minLevel = 10;
        if(playerLevel != 1) {
            minLevel = TierRegistry.getCapForTier(playerLevel-1);
        }

        event.setMaxLevel(levelCap);
        event.setMinLevel(minLevel);
        return Unit.INSTANCE;
    };
}