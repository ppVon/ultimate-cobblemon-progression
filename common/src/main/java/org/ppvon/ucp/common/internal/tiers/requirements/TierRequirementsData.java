package org.ppvon.ucp.common.internal.tiers.requirements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.ppvon.ucp.common.api.tiers.requirements.TierRequirements;
import org.ppvon.ucp.common.api.tiers.requirements.TierRequirementsDex;

public final class TierRequirementsData {
    private TierRequirementsData() {}

    public static final Codec<TierRequirements> CODEC = RecordCodecBuilder.create(i -> i.group(
            TierRequirementsDexData.CODEC
                    .optionalFieldOf("dex", TierRequirementsDex.NONE)
                    .forGetter(r -> r.dex)
    ).apply(i, TierRequirements::new));
}
