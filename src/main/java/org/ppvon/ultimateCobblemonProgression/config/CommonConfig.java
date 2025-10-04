package org.ppvon.ultimateCobblemonProgression.config;

import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigBuilder;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigType;
import net.mehvahdjukaar.moonlight.api.platform.configs.ModConfigHolder;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

import static org.ppvon.ultimateCobblemonProgression.UltimateCobblemonProgression.MOD_ID;

public final class CommonConfig {
    public static ModConfigHolder HOLDER;

    public static Supplier<Boolean> BLOCK_UNKNOWN_SPECIES;
    public static Supplier<Double> TIER_CAP_SCALING;
    public static Supplier<Double> MIN_LEVEL_SCALING;
    public static Supplier<Double> AVG_LEVEL_SCALING;
    public static Supplier<Double> MAX_LEVEL_SCALING;

    public static Supplier<Double> WEIGHT_DECAY_PER_TIER;
    public static Supplier<Double> WEIGHT_CURRENT_TIER_BUFF;
    public static Supplier<Double> WEIGHT_MIN_FACTOR;

    static {
        ResourceLocation location = ResourceLocation.fromNamespaceAndPath(MOD_ID, "common");
        ConfigBuilder b = ConfigBuilder.create(location, ConfigType.COMMON_SYNCED);

        b.push("Spawn Control").description("Related to spawning of Cobblemon");

        BLOCK_UNKNOWN_SPECIES = b.comment("If a species is not in a tier, it should never spawn").define("blockUnknownSpecies", true);
        TIER_CAP_SCALING = b.comment("How much a mons level is scaled based on how much higher player tier is than mon tier").define("tierCapScaling", 0.25, 0.0, 1.0);

        MIN_LEVEL_SCALING = b.comment("% Of level cap the minimum pokemon level can be").define("minLevelScaling", 0.45, 0, 1);
        AVG_LEVEL_SCALING = b.comment("% Of level cap the average pokemon level will be").define("avgLevelScaling", 0.75, 0, 1);
        MAX_LEVEL_SCALING = b.comment("% Of level cap the maximum pokemon level can be").define("maxLevelScaling", 1.1, 0, 2);

        WEIGHT_DECAY_PER_TIER = b.comment("% to reduce weight per tier a mon is below player").define("weightDecayPerTier", 0.20, 0.00, 1.0);
        WEIGHT_MIN_FACTOR = b.comment("Minimum % any mon weight will be scaled to").define("weightMinFactor", 0.15, 0, 1);

        WEIGHT_CURRENT_TIER_BUFF = b.comment("% to buff weight of mon in current player tier").define("weightCurrentTierBuff", 1.1, 0.0, 2.0);

        b.pop();

        HOLDER = b.build();

        HOLDER.forceLoad();
    }

    public static void init() {

    }
}
