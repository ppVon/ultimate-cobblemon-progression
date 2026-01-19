package org.ppvon.ucp.common.access.trainer;

import net.minecraft.server.level.ServerPlayer;

public final class TrainerLevelAccess {
    private static final int DEFAULT_LEVEL = 1;

    private TrainerLevelAccess() {}

    private static TrainerLevelHolder holder(ServerPlayer player) {
        if (!(player instanceof TrainerLevelHolder h)) {
            throw new IllegalStateException("TrainerLevelHolder mixin not applied to ServerPlayer");
        }
        return h;
    }

    public static int getRaw(ServerPlayer player) {
        return holder(player).ucp$getTrainerLevel();
    }

    public static void setRaw(ServerPlayer player, int level) {
        holder(player).ucp$setTrainerLevel(Math.max(level, DEFAULT_LEVEL));
    }

    public static void ensureInitialized(ServerPlayer player) {
        TrainerLevelHolder h = holder(player);
        if (h.ucp$getTrainerLevel() <= 0) {
            h.ucp$setTrainerLevel(DEFAULT_LEVEL);
        }
    }
}
