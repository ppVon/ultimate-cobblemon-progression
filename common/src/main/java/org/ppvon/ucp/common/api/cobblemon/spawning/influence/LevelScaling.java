package org.ppvon.ucp.common.api.cobblemon.spawning.influence;

import com.cobblemon.mod.common.api.spawning.detail.SpawnAction;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.ppvon.ucp.common.api.cobblemon.species.SpeciesTierRegistry;
import org.ppvon.ucp.common.api.tiers.TierRegistry;
import org.ppvon.ucp.common.api.trainer.TrainerLevels;
import org.ppvon.ucp.common.config.UcpConfigs;

public final class LevelScaling {
    public static int sampleTriangularInt(int min, int mode, int max, RandomSource rand) {
        if (min >= max) return min;
        if (max > 100) max = 100;
        mode = Mth.clamp(mode, min, max);
        double u = rand.nextDouble();
        double F = (double)(mode - min) / (max - min);

        double x;
        if (u < F) {
            x = min + Math.sqrt(u * (max - min) * (mode - min));
        } else {
            x = max - Math.sqrt((1 - u) * (max - min) * (max - mode));
        }
        return Mth.clamp((int)Math.round(x), min, max);
    }

    public static void affectSpawn(@NotNull SpawnAction<?> action, @NotNull Entity entity) {
        if(!UcpConfigs.spawn().doLevelScaling) {
            return;
        }

        if(!(entity instanceof PokemonEntity pe)) {
            return;
        }

        if(!(pe.level() instanceof ServerLevel server)) {
            return;
        }

        ServerPlayer player = (ServerPlayer) server.getNearestPlayer(pe, 128.0);
        if (player == null) return;

        Pokemon pokemon = pe.getPokemon();
        Species species = pokemon.getSpecies();

        int speciesTier = SpeciesTierRegistry.getTier(species);
        if(speciesTier == -1) {
            return;
        }
        int trainerTier = TrainerLevels.get(player);

        int speciesLevelCap = TierRegistry.getLevelCap(speciesTier);
        int trainerLevelCap = TierRegistry.getLevelCap(trainerTier);

        int capDelta = Math.max(0, trainerLevelCap - speciesLevelCap);
        double capScaling = UcpConfigs.spawn().tierCapScaling;
        int buffedCap = (int)Math.round(speciesLevelCap + (capDelta*capScaling));

        double minScaling = UcpConfigs.spawn().minLevelScaling;
        double avgScaling = UcpConfigs.spawn().avgLevelScaling;
        double maxScaling = UcpConfigs.spawn().maxLevelScaling;

        int min = Math.max(1, (int)Math.floor(minScaling * buffedCap));
        int mode = Math.max(1, (int)Math.round(avgScaling * buffedCap));
        int max = Math.max(1, (int)Math.floor(maxScaling * buffedCap));

        if (max > 100) {
            max = 100;
        }

        int newLevel = sampleTriangularInt(min, mode, max, pe.getRandom());

        pokemon.setLevel(newLevel);
    }
}
