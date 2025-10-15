package org.ppvon.ultimateCobblemonProgression.common.tiers.requirements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public final class TierRequirements {
    public final DexRequirements dex;
    public static final TierRequirements NONE = new TierRequirements(DexRequirements.NONE);


    public TierRequirements(DexRequirements dex) {
        this.dex = dex != null ? dex : DexRequirements.NONE;
    }

    public boolean hasAny() {
        return dex.hasAny();
    }

    public static final Codec<TierRequirements> CODEC = RecordCodecBuilder.create(i -> i.group(
            DexRequirements.CODEC.optionalFieldOf("dex", DexRequirements.NONE).forGetter(r -> r.dex)
    ).apply(i, TierRequirements::new));
}
