package org.ppvon.ultimateCobblemonProgression.common.tiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import org.ppvon.ultimateCobblemonProgression.common.tiers.requirements.TierRequirements;

import java.util.*;

public final class TierDef {
    public final int index;
    public final int levelCap;
    public final Set<ResourceLocation> species;
    public final TierRequirements requirements;

    public TierDef(int index, int levelCap, Set<ResourceLocation> species, TierRequirements requirements) {
        this.index = index;
        this.levelCap = levelCap;
        this.species = Collections.unmodifiableSet(species);
        this.requirements = requirements != null ? requirements : TierRequirements.NONE;
    }

    private static final Codec<Set<ResourceLocation>> SPECIES_SET_CODEC =
            ResourceLocation.CODEC.listOf().xmap(
                    list -> new LinkedHashSet<>(list),
                    set  -> new ArrayList<>(set)
            );

    public static final Codec<TierDef> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.INT.fieldOf("levelCap").forGetter(t -> t.levelCap),

            SPECIES_SET_CODEC.optionalFieldOf("species", Set.of()).forGetter(t -> t.species),
            TierRequirements.CODEC.optionalFieldOf("requirements", TierRequirements.NONE).forGetter(t -> t.requirements)
    ).apply(i, (cap, species, reqs) -> new TierDef(-1, cap, species, reqs)));
}