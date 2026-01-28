package org.ppvon.ucp.common.api.cobblemon.spawning.influence;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.spawning.detail.PokemonSpawnDetail;
import com.cobblemon.mod.common.api.spawning.detail.SpawnDetail;
import com.cobblemon.mod.common.api.spawning.position.SpawnablePosition;
import com.cobblemon.mod.common.pokemon.Species;
import net.minecraft.server.level.ServerPlayer;
import org.ppvon.ucp.common.UltimateCobblemonProgression;
import org.ppvon.ucp.common.api.cobblemon.spawning.PlayerResolver;
import org.ppvon.ucp.common.api.cobblemon.species.SpeciesTierRegistry;
import org.ppvon.ucp.common.api.tiers.TierRegistry;
import org.ppvon.ucp.common.api.trainer.TrainerLevels;
import org.ppvon.ucp.common.config.UcpConfigs;
import org.slf4j.Logger;

public final class SpeciesBlocking {

    private static final Logger LOGGER = UltimateCobblemonProgression.LOGGER;

    public static boolean allowSpecies(SpawnDetail detail, SpawnablePosition ctx) {
        if(!UcpConfigs.spawn().doSpeciesBlocking) {
            return true;
        }

        if(!(detail instanceof PokemonSpawnDetail psd)) return true;

        String speciesStr = psd.getPokemon().getSpecies();
        if(speciesStr == null || "random".equalsIgnoreCase(speciesStr)) {
            return true;
        }

        if (TierRegistry.maxIndex() == 0) {
            return true;
        }

        Species species = PokemonSpecies.getByName(speciesStr);
        int speciesTier = SpeciesTierRegistry.getTier(species);

        if(speciesTier == -1) {
            return !UcpConfigs.spawn().blockUnknownSpecies;
        }

        ServerPlayer player = PlayerResolver.resolvePlayer(ctx);

        if (player == null) {
            return false;
        }

        int trainerTier = TrainerLevels.get(player);

        return speciesTier <= trainerTier;
    }
}
