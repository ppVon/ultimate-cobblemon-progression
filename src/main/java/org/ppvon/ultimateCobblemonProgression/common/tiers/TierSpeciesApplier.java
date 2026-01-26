package org.ppvon.ultimateCobblemonProgression.common.tiers;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Species;
import net.minecraft.resources.ResourceLocation;
import org.ppvon.ultimateCobblemonProgression.api.UCPApi;

import java.util.Map;
import java.util.Set;

public final class TierSpeciesApplier {
    private TierSpeciesApplier() {}

    public static void applyFromRegistry() {
        for (Species sp : PokemonSpecies.INSTANCE.getSpecies()) {
            UCPApi.setTier(sp, -1);
        }

        Map<Integer, TierDef> tierToSpecies = TierRegistry.exportSpeciesByTier();
        for (var e : tierToSpecies.entrySet()) {
            int tier = e.getKey();
            int realSpecies = 0;
            for (ResourceLocation id : e.getValue().species) {
                Species sp = PokemonSpecies.getByIdentifier(id);
                if (sp != null) {
                    realSpecies += 1;
                    UCPApi.setTier(sp, tier);
                }
            }
            TierRegistry.putRealSpecies(tier, realSpecies);
        }
    }
}