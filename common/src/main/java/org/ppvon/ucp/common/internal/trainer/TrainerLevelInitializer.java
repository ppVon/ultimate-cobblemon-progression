package org.ppvon.ucp.common.internal.trainer;

import net.minecraft.server.level.ServerPlayer;
import org.ppvon.ucp.common.UltimateCobblemonProgression;
import org.ppvon.ucp.common.access.trainer.TrainerLevelAccess;
import org.ppvon.ucp.common.api.trainer.TrainerLevels;
import org.ppvon.ucp.common.api.tiers.TierRegistry;
import org.ppvon.ucp.common.config.UcpConfigs;

public final class TrainerLevelInitializer {
    private TrainerLevelInitializer() {}

    public static void initializeOnJoin(ServerPlayer player) {
        if (player == null) {
            return;
        }

        // Repair out-of-range values (including the 0 stored by new and pre-mod players) so the
        // player always holds a valid level, whatever the progression config says.
        TrainerLevels.ensureInitialized(player);

        if (TrainerLevelAccess.isInitialized(player)) {
            return;
        }

        // Seeding a level from existing Pokedex progress is dex progression. With it disabled,
        // levels are driven by commands/datapacks only, so never derive one here — doing so would
        // overwrite a manually assigned level on the next login.
        if (!UcpConfigs.common().doDexProgression) {
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
