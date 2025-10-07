package org.ppvon.ultimateCobblemonProgression.api;

import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.Set;

/**
 * Immutable definition of a trainer tier
 *
 * @param index the tier index (1-based)
 * @param levelCap the level cap for the tier
 * @param species the list of species identifiers in the tier
 * @since 1.0.0
 */
public record TierDefinition(int index, int levelCap, Set<ResourceLocation> species) {
    /**
     * Canonical constructor ensuring the species set is unmodifiable.
     *
     * @param index the tier index (1-based)
     * @param levelCap the level cap for the tier
     * @param species the list of species identifiers in the tier
     */
    public TierDefinition {
        species = Collections.unmodifiableSet(species);
    }
}
