package org.ppvon.ultimateCobblemonProgression.tiers;

import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TierRegistry {
    private static final Logger LOG = LogManager.getLogger("UCP/Tiers");
    private static final Pattern TIER_NAME = Pattern.compile("^tier_(\\d+)$");

    public static final class TierDef {
        public final int index;
        public final int levelCap;
        public final Set<ResourceLocation> species;

        public TierDef(int index, int levelCap, Set<ResourceLocation> species) {
            this.index = index;
            this.levelCap = levelCap;
            this.species = Collections.unmodifiableSet(species);
        }
    }

    private static final Map<Integer, TierDef> BY_TIER = new HashMap<>();
    private static final Map<ResourceLocation, Integer> SPECIES_MIN_TIER = new HashMap<>();
    private static int maxTier = 0;

    private TierRegistry() {}

    public static void clear() {
        BY_TIER.clear();
        SPECIES_MIN_TIER.clear();
        maxTier = 0;
    }

    public static void put(TierDef def) {
        BY_TIER.put(def.index, def);
        maxTier = Math.max(maxTier, def.index);
        for (var s : def.species) {
            SPECIES_MIN_TIER.merge(s, def.index, Math::min);
        }
    }

    public static int maxTier() { return maxTier; }
    public static Optional<TierDef> get(int idx) { return Optional.ofNullable(BY_TIER.get(idx)); }
    public static int getMinTierFor(ResourceLocation speciesId) {
        return SPECIES_MIN_TIER.getOrDefault(speciesId, Integer.MAX_VALUE);
    }

    public static OptionalInt tryParseTierIndex(String pathNoFolders) {
        Matcher m = TIER_NAME.matcher(pathNoFolders);
        if (!m.matches()) return OptionalInt.empty();
        return OptionalInt.of(Integer.parseInt(m.group(1)));
    }

    public static String summary() {
        return "tiers=" + BY_TIER.size() + " max=" + maxTier + " species=" + SPECIES_MIN_TIER.size();
    }
}
