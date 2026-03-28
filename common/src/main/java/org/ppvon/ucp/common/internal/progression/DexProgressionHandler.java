package org.ppvon.ucp.common.internal.progression;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.pokemon.PokedexDataChangedEvent;
import com.cobblemon.mod.common.util.PlayerExtensionsKt;
import net.minecraft.server.level.ServerPlayer;
import org.ppvon.ucp.common.api.trainer.TrainerLevels;
import org.ppvon.ucp.common.api.tiers.Tier;
import org.ppvon.ucp.common.config.UcpConfigs;
import org.ppvon.ucp.common.internal.trainer.TrainerLevelProgression;

public final class DexProgressionHandler {
    private static boolean initialized;

    private DexProgressionHandler() {}

    public static void init() {
        if (initialized) {
            return;
        }
        initialized = true;
        CobblemonEvents.POKEDEX_DATA_CHANGED_POST.subscribe(Priority.NORMAL, DexProgressionHandler::onDexChanged);
    }

    private static void onDexChanged(PokedexDataChangedEvent.Post event) {
        if (!UcpConfigs.common().doDexProgression) {
            return;
        }

        ServerPlayer player = PlayerExtensionsKt.getPlayer(event.getPlayerUUID());
        if (player == null) {
            return;
        }

        int currentLevel = TrainerLevels.get(player);
        Tier nextTier = TrainerLevelProgression.nextTier(currentLevel).orElse(null);
        if (nextTier == null) {
            return;
        }

        TrainerLevelProgression.DexCounts counts = TrainerLevelProgression.getDexCounts(event.getPokedexManager());
        if (!TrainerLevelProgression.meetsRequirements(nextTier.requirements.dex, counts.seen(), counts.caught())) {
            return;
        }

        TrainerLevels.set(player, nextTier.index);
        int updatedLevel = TrainerLevels.get(player);
        if (updatedLevel == currentLevel) {
            return;
        }

        player.displayClientMessage(TrainerLevelProgression.buildPromotionMessage(updatedLevel), false);
    }
}
