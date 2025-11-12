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

        Map<Integer, Set<ResourceLocation>> tierToSpecies = TierRegistry.exportSpeciesByTier();
        for (var e : tierToSpecies.entrySet()) {
            int tier = e.getKey();
            for (ResourceLocation id : e.getValue()) {
                Species sp = PokemonSpecies.INSTANCE.getByIdentifier(id);
                if (sp != null) {
                    UCPApi.setTier(sp, tier);
                }
            }
        }
    }
}