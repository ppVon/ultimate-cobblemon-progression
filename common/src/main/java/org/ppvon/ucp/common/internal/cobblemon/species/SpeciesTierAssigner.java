package org.ppvon.ucp.common.internal.cobblemon.species;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Species;
import net.minecraft.resources.ResourceLocation;
import org.ppvon.ucp.common.UltimateCobblemonProgression;
import org.ppvon.ucp.common.access.cobblemon.species.SpeciesTierAccess;
import org.ppvon.ucp.common.api.event.TierEvents;
import org.slf4j.Logger;

import java.util.*;

public final class SpeciesTierAssigner {
    private static final Logger LOGGER = UltimateCobblemonProgression.LOGGER;

    private static final Set<Species> TOUCHED_LAST_APPLY =
            Collections.newSetFromMap(new IdentityHashMap<>());

    private SpeciesTierAssigner() {}

    public static void onTiersUpdated(TierEvents.TiersUpdatedEvent event) {

        if (!TOUCHED_LAST_APPLY.isEmpty()) {
            for (Species sp : TOUCHED_LAST_APPLY) {
                SpeciesTierAccess.setTier(sp, -1);
            }
            TOUCHED_LAST_APPLY.clear();
        }

        Map<ResourceLocation, Integer> idToTier = new HashMap<>();

        for (var tier : event.tiers()) {
            int tierNum = tier.index;
            for (ResourceLocation speciesId : tier.species) {
                idToTier.put(speciesId, tierNum);
            }
        }

        LOGGER.info("{} species found in tier data", idToTier.size());

        for (var entry : idToTier.entrySet()) {
            Species sp = resolveSpecies(entry.getKey());
            if (sp == null) {
                LOGGER.warn("Tier datapack referenced unknown species: {}", entry.getKey());
                continue;
            }
            SpeciesTierAccess.setTier(sp, entry.getValue());
            TOUCHED_LAST_APPLY.add(sp);
        }

        TierEvents.SPECIES_TIERS_APPLIED.emit(new TierEvents.SpeciesTiersAppliedEvent());
    }

    private static Species resolveSpecies(ResourceLocation id) {
        return PokemonSpecies.getByIdentifier(id);
    }
}
