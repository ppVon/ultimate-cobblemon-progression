package org.ppvon.ucp.common.api.tiers;

import net.minecraft.resources.ResourceLocation;
import org.ppvon.ucp.common.api.tiers.requirements.TierRequirements;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class Tier {
    public final int index;
    public final int levelCap;
    public final Set<ResourceLocation> species;
    public final TierRequirements requirements;

    public Tier(int index, int levelCap, Set<ResourceLocation> species, TierRequirements requirements) {
        this.index = index;
        this.levelCap = levelCap;
        this.species = Collections.unmodifiableSet(new LinkedHashSet<>(species));
        this.requirements = requirements != null ? requirements : TierRequirements.NONE;
    }

    public Tier withIndex(int idx) {
        return new Tier(idx, this.levelCap, this.species, this.requirements);
    }
}
