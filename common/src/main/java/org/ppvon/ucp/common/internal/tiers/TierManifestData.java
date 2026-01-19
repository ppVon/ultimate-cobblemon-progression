package org.ppvon.ucp.common.internal.tiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

final class TierManifestData {
    private TierManifestData() {}

    static final class Manifest {
        final boolean includeDefaults;
        final List<Integer> tiers;

        Manifest(boolean includeDefaults, List<Integer> tiers) {
            this.includeDefaults = includeDefaults;
            this.tiers = tiers != null ? List.copyOf(tiers) : List.of();
        }
    }

    static final Manifest DEFAULT = new Manifest(true, List.of());

    static final Codec<Manifest> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.BOOL.optionalFieldOf("includeDefaults", true).forGetter(m -> m.includeDefaults),
            Codec.INT.listOf().optionalFieldOf("tiers", List.of()).forGetter(m -> m.tiers)
    ).apply(i, Manifest::new));
}
