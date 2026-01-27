package org.ppvon.ucp.common.access.cobblemon.species;

import com.cobblemon.mod.common.pokemon.Species; // adjust import if your Species is elsewhere

public final class SpeciesTierAccess {
    private SpeciesTierAccess() {}

    public static int getTier(Species species) {
        return ((SpeciesTierHolder) (Object) species).ucp$getTier();
    }

    public static void setTier(Species species, int tier) {
        ((SpeciesTierHolder) (Object) species).ucp$setTier(tier);
    }
}
