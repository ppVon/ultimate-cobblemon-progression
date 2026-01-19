package org.ppvon.ucp.common.api.trainer;

import net.minecraft.server.level.ServerPlayer;
import org.ppvon.ucp.common.internal.trainer.TrainerLevelAccess;

/**
 * Public, stable API for interacting with a player's trainer level.
 *
 * <p>This API enforces all invariants for trainer levels:
 * <ul>
 *   <li>Trainer level is always {@code >= 1}</li>
 *   <li>Trainer level is clamped to a runtime-defined maximum</li>
 *   <li>Invalid or legacy values are automatically clamped</li>
 * </ul>
 *
 * <p>The maximum trainer level is not known at static initialization time and
 * must be supplied at runtime (typically after datapacks are loaded or reloaded).
 *
 */
public final class TrainerLevels {

    /**
     * The minimum valid trainer level.
     *
     * <p>This is fixed and invariant for the lifetime of the mod.
     */
    private static final int MIN_LEVEL = 1;

    /**
     * The maximum valid trainer level.
     *
     * <p>This value is defined at runtime (e.g. from datapacks) and may change
     * when datapacks are reloaded. Until explicitly set, it defaults to {@code 1}.
     */
    private static volatile int maxLevel = MIN_LEVEL;

    private TrainerLevels() {}

    /**
     * Sets the current maximum valid trainer level.
     *
     * <p>This should be called once datapack-driven trainer level data has been
     * fully loaded or reloaded.
     *
     * <p>The provided value is clamped to {@link #MIN_LEVEL} to prevent invalid
     * configurations.
     *
     * @param newMaxLevel the maximum trainer level allowed at runtime
     */
    public static void setMaxLevel(int newMaxLevel) {
        maxLevel = Math.max(MIN_LEVEL, newMaxLevel);
    }

    /**
     * Returns the current maximum valid trainer level.
     *
     * <p>This reflects the most recently loaded datapack configuration.
     *
     * @return the current maximum trainer level
     */
    public static int getMaxLevel() {
        return maxLevel;
    }

    /**
     * Returns the player's trainer level.
     *
     * <p>If the stored value is invalid (less than {@link #MIN_LEVEL} or greater
     * than the current maximum), it will be automatically clamped and repaired
     * in storage before being returned.
     *
     * @param player the server-side player
     * @return a valid trainer level within the allowed range
     */
    public static int get(ServerPlayer player) {
        int raw = TrainerLevelAccess.getRaw(player);
        int clamped = clamp(raw);

        if (clamped != raw) {
            TrainerLevelAccess.setRaw(player, clamped);
        }

        return clamped;
    }

    /**
     * Sets the player's trainer level.
     *
     * <p>The provided value is clamped to the valid range defined by
     * {@link #MIN_LEVEL} and {@link #getMaxLevel()}.
     *
     * @param player the server-side player
     * @param level  the desired trainer level
     */
    public static void set(ServerPlayer player, int level) {
        TrainerLevelAccess.setRaw(player, clamp(level));
    }

    /**
     * Ensures that the player has a valid trainer level stored.
     *
     * <p>This method should be called when a player is first observed by the
     * system (e.g. on login, respawn copy, or world join).
     *
     * <p>If the stored value is invalid, it will be repaired to a valid value
     * within the current bounds.
     *
     * @param player the server-side player
     */
    public static void ensureInitialized(ServerPlayer player) {
        int raw = TrainerLevelAccess.getRaw(player);

        if (raw < MIN_LEVEL) {
            TrainerLevelAccess.setRaw(player, MIN_LEVEL);
        } else if (raw > maxLevel) {
            TrainerLevelAccess.setRaw(player, maxLevel);
        }
    }

    /**
     * Clamps the given level to the currently valid range.
     *
     * @param level a raw trainer level value
     * @return a level between {@link #MIN_LEVEL} and {@link #getMaxLevel()}
     */
    private static int clamp(int level) {
        if (level < MIN_LEVEL) return MIN_LEVEL;

        int max = maxLevel;
        if (level > max) return max;

        return level;
    }
}
