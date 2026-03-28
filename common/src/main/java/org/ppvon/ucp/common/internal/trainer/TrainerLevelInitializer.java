package org.ppvon.ucp.common.internal.trainer;

import net.minecraft.server.level.ServerPlayer;
import org.ppvon.ucp.common.UltimateCobblemonProgression;
import org.ppvon.ucp.common.access.trainer.TrainerLevelAccess;
import org.ppvon.ucp.common.api.trainer.TrainerLevels;
import org.ppvon.ucp.common.api.tiers.TierRegistry;

public final class TrainerLevelInitializer {
    private TrainerLevelInitializer() {}

    public static void initializeOnJoin(ServerPlayer player) {
        if (player == null || TrainerLevelAccess.isInitialized(player)) {
            return;
        }

        if (TierRegistry.count() <= 0) {
            UltimateCobblemonProgression.LOGGER.warn(
                    "Skipping trainer level initialization for {} because no tiers are loaded yet.",
                    player.getGameProfile().getName()
            );
            return;
        }

        TrainerLevelProgression.DexCounts counts = TrainerLevelProgression.getDexCounts(player);
        int resolvedLevel = TrainerLevelProgression.resolveHighestQualifyingTier(counts.seen(), counts.caught());
        if (resolvedLevel > 0) {
            TrainerLevels.set(player, resolvedLevel);
        }
    }
}
