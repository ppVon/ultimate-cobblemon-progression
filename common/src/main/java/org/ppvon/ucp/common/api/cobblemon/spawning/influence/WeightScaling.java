package org.ppvon.ucp.common.api.cobblemon.spawning.influence;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.spawning.detail.PokemonSpawnDetail;
import com.cobblemon.mod.common.api.spawning.detail.SpawnDetail;
import com.cobblemon.mod.common.api.spawning.position.SpawnablePosition;
import com.cobblemon.mod.common.pokemon.Species;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.ppvon.ucp.common.api.cobblemon.spawning.PlayerResolver;
import org.ppvon.ucp.common.api.cobblemon.species.SpeciesTierRegistry;
import org.ppvon.ucp.common.api.tiers.TierRegistry;
import org.ppvon.ucp.common.api.trainer.TrainerLevels;
import org.ppvon.ucp.common.config.UcpConfigs;

public final class WeightScaling {
    public static double weightFactor(int speciesTier, int trainerTier) {
        int diff = Math.abs(speciesTier - trainerTier);

        double decay = UcpConfigs.spawn().weightDecayPerTier;

        double f = UcpConfigs.spawn().weightCurrentTierBuff - decay;

        if(f < UcpConfigs.spawn().weightMinFactor) {
            return UcpConfigs.spawn().weightMinFactor;
        }
        return f;
    }

    public static float affectWeight(@NotNull SpawnDetail spawnDetail, @NotNull SpawnablePosition ctx, float currentWeight) {
        if(UcpConfigs.spawn().doWeightScaling) {
            return currentWeight;
        }
        if(!(spawnDetail instanceof PokemonSpawnDetail psd)) {
            return currentWeight;
        }

        String speciesStr = psd.getPokemon().getSpecies();
        if (speciesStr == null || "random".equalsIgnoreCase(speciesStr)) {
            return currentWeight;
        }

        if (TierRegistry.maxIndex() == 0) {
            return currentWeight;
        }

        Species sp = PokemonSpecies.getByName(speciesStr);
        int speciesTier = SpeciesTierRegistry.getTier(sp);

        if(speciesTier == -1) {
            return currentWeight;
        }

        ServerPlayer player = PlayerResolver.resolvePlayer(ctx);

        if(player == null) {
            return currentWeight;
        }

        int trainerTier = TrainerLevels.get(player);

        double factor = weightFactor(speciesTier, trainerTier);
        float adjusted = (float)(currentWeight * factor);

        return adjusted;
    }
}
