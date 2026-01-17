package org.ppvon.ultimateCobblemonProgression.common.radgyms;

import com.cobblemon.mod.common.api.Priority;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import lol.gito.radgyms.api.event.GymEvents;
import org.ppvon.ultimateCobblemonProgression.common.component.TrainerLevelComponents;
import org.ppvon.ultimateCobblemonProgression.common.tiers.TierRegistry;

public final class GymGenerateRewardListener {
    private GymGenerateRewardListener() {}

    public static void register() {
        GymEvents.GENERATE_REWARD.subscribe(Priority.NORMAL, onGenerateReward);

    }
    private static final Function1<GymEvents.GenerateRewardEvent, Unit> onGenerateReward = event -> {
        int playerLevel = TrainerLevelComponents.KEY.get(event.getPlayer()).getLevel();
        int playerCap = TierRegistry.getCapForTier(playerLevel);
        if(playerCap == event.getLevel()) {
            TrainerLevelComponents.KEY.get(event.getPlayer()).setLevel(playerLevel + 1);
        }
        return Unit.INSTANCE;
    };
}