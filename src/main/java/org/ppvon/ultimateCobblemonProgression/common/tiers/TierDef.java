package org.ppvon.ultimateCobblemonProgression.common.tiers;

import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.Set;

public final class TierDef {
    public final int index;
    public final int levelCap;
    public final Set<ResourceLocation> species;

    public TierDef(int index, int levelCap, Set<ResourceLocation> species) {
        this.index = index;
        this.levelCap = levelCap;
        this.species = Collections.unmodifiableSet(species);
    }
}