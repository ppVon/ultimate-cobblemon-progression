package org.ppvon.ultimateCobblemonProgression.influence;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import com.cobblemon.mod.common.api.spawning.SpawnBucket;
import com.cobblemon.mod.common.api.spawning.context.SpawningContext;
import com.cobblemon.mod.common.api.spawning.context.calculators.SpawningContextCalculator;
import com.cobblemon.mod.common.api.spawning.detail.PokemonSpawnDetail;
import com.cobblemon.mod.common.api.spawning.detail.SpawnAction;
import com.cobblemon.mod.common.api.spawning.detail.SpawnDetail;
import com.cobblemon.mod.common.api.spawning.influence.SpawningInfluence;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.ppvon.ultimateCobblemonProgression.component.TrainerLevelComponents;
import org.ppvon.ultimateCobblemonProgression.config.CommonConfig;
import org.ppvon.ultimateCobblemonProgression.tiers.TierRegistry;

import java.util.function.Supplier;

/**
 * Influence that affects spawnability, weight, and level of spawned pokemon
 *
 * Spawnability:
 *  This mod has a "trainer level" component saved to a player that maps to
 *  a "tier" defined in data.  This tier defines a list of pokemon that are
 *  allowed to spawn if a trainer is at least that tier.  If a species is not
 *  found in a tier, by default the spawn is still blocked, but can be configured
 *  with BLOCK_UNKNOWN_SPECIES
 *
 * Weight:
 *  Pokemon in the same tier as the player get a buff to their weight via
 *  WEIGHT_CURRENT_TIER_BUFF.  Pokemon below the player tier have a decay
 *  applied to their weight WEIGHT_DECAY_PER_TIER but can't go below the minimum
 *  WEIGHT_MIN_FACTOR.
 *  For example
 *      Tier 1 mon with weight 300
 *      Tier 5 player
 *      Diff in tier is 4
 *      4 * WEIGHT_DECAY_PER_TIER(0.20) = 0.80
 *      Weight multiplier = 0.20 (not lower than min so no clamping)
 *
 *      New tier 1 mon weight = 60
 *
 *  Level:
 *   The level of mons is scaled to a triangle distribution around the level cap
 *   for the tier they're in.  The distribution is scaled around MIN_LEVEL_SCALING,
 *   AVG_LEVEL_SCALING, and MAX_LEVEL_SCALING.  If a pokemon is below the
 *   player's tier, their cap is buffed by the difference in cap between the tiers.
 *   For example
 *      Tier 1 mon, level cap 15 (default data)
 *      Tier 5 player, level cap 69 (default data)
 *      69 - 15 = 54
 *      54 * TIER_CAP_SCALING (0.25) =~ 14
 *
 *      New tier 1 cap is player is level 5 = 29
 *
 *      With default min/avg/max, the Tier 1pokemon will spawn between
 *      level 13 and 32, with an average of 22.
 *
 *    This means lower tier pokemon will still stay lower level than higher tier
 *    pokemon, but will not be level 5-16 forever.
 */
public final class TrainerLevelInfluence implements SpawningInfluence {
    private static final Supplier<Boolean> BLOCK_UNKNOWN_SPECIES = CommonConfig.BLOCK_UNKNOWN_SPECIES;
    private static final Supplier<Double> TIER_CAP_SCALING = CommonConfig.TIER_CAP_SCALING;

    private static final Supplier<Double> MIN_LEVEL_SCALING = CommonConfig.MIN_LEVEL_SCALING;
    private static final Supplier<Double> AVG_LEVEL_SCALING = CommonConfig.AVG_LEVEL_SCALING;
    private static final Supplier<Double> MAX_LEVEL_SCALING = CommonConfig.MAX_LEVEL_SCALING;

    private static final Supplier<Double> WEIGHT_DECAY_PER_TIER = CommonConfig.WEIGHT_DECAY_PER_TIER;
    private static final Supplier<Double> WEIGHT_MIN_FACTOR = CommonConfig.WEIGHT_MIN_FACTOR;
    private static final Supplier<Double> WEIGHT_CURRENT_TIER_BUFF = CommonConfig.WEIGHT_CURRENT_TIER_BUFF;

    private static int sampleTriangularInt(int min, int mode, int max, RandomSource rand) {
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

    private static ResourceLocation normId(String raw) {
        if (raw == null || raw.isBlank()) return null;
        String s = raw.toLowerCase();
        return ResourceLocation.tryParse(s.contains(":") ? s : "cobblemon:" + s);
    }

    private static int playerTier(ServerPlayer player) {
        return Math.max(0, TrainerLevelComponents.KEY.get(player).getLevel());
    }

    private static int speciesMinTier(ResourceLocation speciesId) {
        return TierRegistry.getMinTierFor(speciesId);
    }

    private static int levelCapForTier(int tier) {
        return TierRegistry.get(tier).map(t -> t.levelCap).orElse(Integer.MAX_VALUE);
    }

    private static double weightFactor(int speciesTier, int playerTier) {
        int diff = Math.abs(speciesTier - playerTier);
        double f = 1f - (WEIGHT_DECAY_PER_TIER.get() * diff);
        if(diff == 0) {
            f = WEIGHT_CURRENT_TIER_BUFF.get();
        }
        return (f < WEIGHT_MIN_FACTOR.get()) ? WEIGHT_MIN_FACTOR.get() : f;
    }

    private ServerPlayer resolvePlayer(SpawningContext ctx) {
        var cause = ctx.getCause();
        if (cause != null) {
            Entity e = cause.getEntity();
            if (e instanceof ServerPlayer sp) return sp;
            if (e instanceof net.minecraft.world.entity.projectile.Projectile p && p.getOwner() instanceof ServerPlayer sp2) return sp2;
            if (e instanceof net.minecraft.world.entity.TamableAnimal t && t.getOwner() instanceof ServerPlayer sp3) return sp3;
        }
        ServerLevel level = ctx.getWorld();
        BlockPos origin = ctx.getPosition();
        if (level != null && origin != null) {
            return (ServerPlayer) level.getNearestPlayer(origin.getX() + 0.5, origin.getY() + 0.5, origin.getZ() + 0.5, 128.0, false);
        }
        return null;
    }

    private boolean allowDetail(SpawnDetail detail, SpawningContext ctx) {
        if (!(detail instanceof PokemonSpawnDetail psd)) return true;

        String speciesStr = psd.getPokemon().getSpecies();
        if (speciesStr == null || "random".equalsIgnoreCase(speciesStr)) return true;

        ResourceLocation speciesId = normId(speciesStr);
        if (speciesId == null) return !BLOCK_UNKNOWN_SPECIES.get();

        if (TierRegistry.maxTier() == 0) return true;

        int monTier = speciesMinTier(speciesId);
        if (monTier == Integer.MAX_VALUE) {
            Log.warn(LogCategory.GENERAL, "No tier for {}", speciesId);
            return !BLOCK_UNKNOWN_SPECIES.get();
        }

        ServerPlayer player = resolvePlayer(ctx);
        if (player == null) return false;

        int trainerTier = playerTier(player);
        boolean allow = monTier <= trainerTier;

        return allow;
    }

    @Override
    public boolean affectSpawnable(@NotNull SpawnDetail spawnDetail, @NotNull SpawningContext spawningContext) {
        return allowDetail(spawnDetail, spawningContext);
    }

    @Override
    public float affectWeight(@NotNull SpawnDetail spawnDetail,
                              @NotNull SpawningContext ctx,
                              float currentWeight) {
        if (!(spawnDetail instanceof PokemonSpawnDetail psd)) return currentWeight;

        String speciesStr = psd.getPokemon().getSpecies();
        if (speciesStr == null || "random".equalsIgnoreCase(speciesStr)) return currentWeight;

        ResourceLocation speciesId = normId(speciesStr);
        if (speciesId == null) return BLOCK_UNKNOWN_SPECIES.get() ? 0.0f : currentWeight;

        if (TierRegistry.maxTier() == 0) return currentWeight;

        int monTier = speciesMinTier(speciesId);
        if (monTier == Integer.MAX_VALUE) {
            return BLOCK_UNKNOWN_SPECIES.get() ? 0.0f : currentWeight;
        }

        ServerPlayer player = resolvePlayer(ctx);
        if (player == null) return currentWeight;

        int trainerTier = playerTier(player);

        double factor = weightFactor(monTier, trainerTier);
        float adjusted = (float)(currentWeight * factor);

        return adjusted;
    }

    @Override
    public void affectSpawn(@NotNull Entity entity) {
        if (!(entity instanceof PokemonEntity pe)) return;
        if (!(pe.level() instanceof ServerLevel server)) return;

        ServerPlayer player = (ServerPlayer) server.getNearestPlayer(pe, 128.0);
        if (player == null) return;

        var poke = pe.getPokemon();
        String speciesStr = poke.getSpecies().getName();
        ResourceLocation speciesId = normId(speciesStr);
        if(speciesId == null) return;

        int speciesTier = TierRegistry.getMinTierFor((speciesId));
        int playerTier = playerTier(player);

        int speciesTierCap = levelCapForTier(speciesTier);
        if (speciesTierCap == Integer.MAX_VALUE) speciesTierCap = 100;

        int playerCap = levelCapForTier(playerTier);
        if (playerCap == Integer.MAX_VALUE) playerCap = 100;

        int capDelta = Math.max(0, playerCap - speciesTierCap);
        double capScaling = TIER_CAP_SCALING.get();
        int buffedCap = (int)Math.round(speciesTierCap + (capDelta*capScaling));

        double minScaling = MIN_LEVEL_SCALING.get();
        double avgScaling = AVG_LEVEL_SCALING.get();
        double maxScaling = MAX_LEVEL_SCALING.get();

        int min = Math.max(1, (int)Math.floor(minScaling * buffedCap));
        int mode = Math.max(1, (int)Math.round(avgScaling * buffedCap));
        int max = Math.max(1, (int)Math.floor(maxScaling * buffedCap));

        if (max > 100) {
            max = 100;
        }

        int newLevel = sampleTriangularInt(min, mode, max, pe.getRandom());

        int before = poke.getLevel();
        if (before != newLevel) {
            poke.setLevel(newLevel);
        }
    }

    @Override public void affectAction(@NotNull SpawnAction<?> spawnAction) { /* no-op */ }
    @Override public float affectBucketWeight(@NotNull SpawnBucket spawnBucket, float v) { return v; }

    @Override
    public boolean isAllowedPosition(@NotNull ServerLevel level,
                                     @NotNull BlockPos pos,
                                     @NotNull SpawningContextCalculator<?, ?> calc) {
        return true;
    }

    @Override public boolean isExpired() { return false; }
}
