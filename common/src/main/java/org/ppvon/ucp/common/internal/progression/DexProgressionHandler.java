package org.ppvon.ucp.common.internal.progression;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.pokemon.PokedexDataChangedEvent;
import com.cobblemon.mod.common.util.PlayerExtensionsKt;
import net.minecraft.server.level.ServerPlayer;
import org.ppvon.ucp.common.api.trainer.TrainerLevels;
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

        TrainerLevelProgression.DexCounts counts = TrainerLevelProgression.getDexCounts(event.getPokedexManager());
        int currentLevel = TrainerLevels.get(player);
        int resolvedLevel = TrainerLevelProgression.resolveHighestQualifyingTier(counts.seen(), counts.caught());
        if (resolvedLevel <= currentLevel) {
            return;
        }

        TrainerLevels.set(player, resolvedLevel);
        int updatedLevel = TrainerLevels.get(player);
        if (updatedLevel <= currentLevel) {
            return;
        }

        player.displayClientMessage(TrainerLevelProgression.buildPromotionMessage(updatedLevel), false);
    }
}
