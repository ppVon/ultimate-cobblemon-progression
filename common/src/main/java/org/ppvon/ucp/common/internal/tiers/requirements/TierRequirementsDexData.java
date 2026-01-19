package org.ppvon.ucp.common.internal.tiers.requirements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.ppvon.ucp.common.api.tiers.requirements.TierRequirementsDex;

public final class TierRequirementsDexData {
    private TierRequirementsDexData() {}

    public static final Codec<TierRequirementsDex> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.INT.optionalFieldOf("seen", 0).forGetter(d -> d.seen),
            Codec.INT.optionalFieldOf("caught", 0).forGetter(d -> d.caught)
    ).apply(i, TierRequirementsDex::new));
}
