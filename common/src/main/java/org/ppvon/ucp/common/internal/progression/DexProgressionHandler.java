package org.ppvon.ucp.common.internal.progression;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.pokemon.PokedexDataChangedEvent;
import com.cobblemon.mod.common.util.PlayerExtensionsKt;
import net.minecraft.server.level.ServerPlayer;
import org.ppvon.ucp.common.api.tiers.Tier;
import org.ppvon.ucp.common.api.tiers.TierRegistry;
import org.ppvon.ucp.common.api.trainer.TrainerLevels;
import org.ppvon.ucp.common.config.UcpConfigs;
import org.ppvon.ucp.common.internal.network.UCPNetwork;
import org.ppvon.ucp.common.internal.network.server.payload.TrainerTierInfoS2C;
import org.ppvon.ucp.common.internal.trainer.TrainerLevelProgression;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class DexProgressionHandler {
    private static boolean initialized;

    public static List<UUID> eligibleClients = new ArrayList<>();

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

        if (eligibleClients.contains(player.getUUID())) {
            notifyClient(player);
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

    public static void notifyClient(ServerPlayer player) {
        int currentTier = TrainerLevels.get(player);

        // if we send -1 to client - it will disable level cap widget for them
        int levelCap = UcpConfigs.common().doLevelCap ? Math.max(TierRegistry.getLevelCap(currentTier), 1) : -1;

        Optional<Tier> nextTier = TierRegistry.next(currentTier);
        TrainerLevelProgression.DexCounts countsForNextTier;

        // same with sending DexCounts.ZERO - it will disable widgets on client
        if (UcpConfigs.common().doDexProgression && nextTier.isPresent()) {
            countsForNextTier = new TrainerLevelProgression.DexCounts(
                    nextTier.get().requirements.dex.seen,
                    nextTier.get().requirements.dex.caught
            );
        } else {
            countsForNextTier = TrainerLevelProgression.DexCounts.ZERO;
        }

        UCPNetwork.sendPacketToPlayer(player, new TrainerTierInfoS2C(
                TrainerLevels.get(player),
                TierRegistry.maxIndex(),
                levelCap,
                countsForNextTier
        ));
    }
}
