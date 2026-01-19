package org.ppvon.ucp.common.internal.tiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import org.ppvon.ucp.common.api.tiers.Tier;
import org.ppvon.ucp.common.api.tiers.requirements.TierRequirements;
import org.ppvon.ucp.common.internal.tiers.requirements.TierRequirementsData;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

final class TierData {
    private TierData() {}

    private static final Codec<Set<ResourceLocation>> SPECIES_SET_CODEC =
            ResourceLocation.CODEC.listOf().xmap(
                    LinkedHashSet::new,
                    ArrayList::new
            );

    static final Codec<Tier> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.INT.fieldOf("levelCap").forGetter(t -> t.levelCap),

            SPECIES_SET_CODEC.optionalFieldOf("species", Set.of()).forGetter(t -> t.species),

            TierRequirementsData.CODEC
                    .optionalFieldOf("requirements", TierRequirements.NONE)
                    .forGetter(t -> t.requirements)
    ).apply(i, (cap, species, reqs) -> new Tier(-1, cap, species, reqs)));
}
