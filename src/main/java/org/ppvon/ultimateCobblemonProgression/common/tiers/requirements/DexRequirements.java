package org.ppvon.ultimateCobblemonProgression.common.tiers.requirements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public final class DexRequirements {
    public final int seen;
    public final int caught;
    public static final DexRequirements NONE = new DexRequirements(0, 0);

    public DexRequirements(int seen, int caught) {
        this.seen = seen;
        this.caught = caught;
    }

    public boolean hasAny() {
        return seen > 0 || caught > 0;
    }

    public static final Codec<DexRequirements> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.INT.optionalFieldOf("seen", 0).forGetter(d -> d.seen),
            Codec.INT.optionalFieldOf("caught", 0).forGetter(d -> d.caught)
    ).apply(i, DexRequirements::new));
}
