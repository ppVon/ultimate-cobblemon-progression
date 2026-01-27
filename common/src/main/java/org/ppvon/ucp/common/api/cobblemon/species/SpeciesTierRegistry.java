package org.ppvon.ucp.common.api.cobblemon.species;

import com.cobblemon.mod.common.pokemon.Species; // adjust import if needed
import org.ppvon.ucp.common.access.cobblemon.species.SpeciesTierAccess;

/**
 * Public API for retrieving UCP tier assignments for Cobblemon {@link Species}.
 *
 * <h2>What this returns</h2>
 * <p>
 * UCP assigns each species a tier number based on UCP tier datapacks
 * (e.g. {@code data/ultimate_cobblemon_progression/tiers/*.json}).
 * This value is stored directly on the {@link Species} instance via mixin,
 * so reads are constant-time and allocation-free.
 * </p>
 *
 * <h2>Reload behavior</h2>
 * <p>
 * On datapack reload, UCP recomputes tier assignments from the latest tier snapshot and
 * re-applies them to all referenced species. If a species is not included by any tier
 * definition, it will remain unassigned ({@code -1}).
 * </p>
 *
 * <h2>Thread-safety</h2>
 * <p>
 * Reads are safe from any thread. During reload, assignments may change; callers should treat
 * the value as eventually consistent with the most recently-applied tier snapshot.
 * </p>
 */
public final class SpeciesTierRegistry {
    private SpeciesTierRegistry() {}

    /**
     * Returns the UCP tier number currently assigned to the given {@link Species}.
     *
     * <p>
     * This is an O(1) read from a field mixed into {@link Species}. It performs no registry lookups
     * and is safe to call from hot paths such as spawning logic.
     * </p>
     *
     * @param species the Cobblemon species instance (non-null)
     * @return the assigned tier number, or {@code -1} if the species has not been assigned a tier
     *         (e.g. not included in any tier definition, or assignment has not yet been applied).
     */
    public static int getTier(Species species) {
        return SpeciesTierAccess.getTier(species);
    }

    /**
     * Convenience helper for "is this species assigned to any tier?".
     *
     * @param species the Cobblemon species instance (non-null)
     * @return {@code true} if {@link #getTier(Species)} returns {@code >= 0}, otherwise {@code false}.
     */
    public static boolean hasTier(Species species) {
        return getTier(species) >= 0;
    }
}
