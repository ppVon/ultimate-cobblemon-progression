package org.ppvon.ucp.common.internal.trainer;

import net.minecraft.server.level.ServerPlayer;
import org.ppvon.ucp.common.access.trainer.TrainerLevelAccess;
import org.ppvon.ucp.common.api.trainer.TrainerLevels;

public final class TrainerLevelInitializer {
    private TrainerLevelInitializer() {}

    public static void initializeOnJoin(ServerPlayer player) {
        if (player == null || TrainerLevelAccess.getRaw(player) > 0) {
            return;
        }

        TrainerLevelProgression.DexCounts counts = TrainerLevelProgression.getDexCounts(player);
        int resolvedLevel = TrainerLevelProgression.resolveHighestQualifyingTier(counts.seen(), counts.caught());
        TrainerLevels.set(player, resolvedLevel);
    }
}
