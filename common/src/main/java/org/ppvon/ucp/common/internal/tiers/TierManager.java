package org.ppvon.ucp.common.internal.tiers;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.ppvon.ucp.common.api.tiers.Tier;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public final class TierManager {
    private TierManager() {}

    public record Snapshot(
            Int2ObjectMap<Tier> byIndex,
            List<Tier> ordered,
            int overallMaxLevelCap
    ) {}

    private static final AtomicReference<Snapshot> SNAPSHOT =
            new AtomicReference<>(new Snapshot(Int2ObjectMaps.emptyMap(), List.of(), 0));

    public static Snapshot snapshot() {
        return SNAPSHOT.get();
    }

    public static Tier getTier(int index) {
        return SNAPSHOT.get().byIndex().get(index);
    }

    public static List<Tier> getOrdered() {
        return SNAPSHOT.get().ordered();
    }

    public static int getOverallMaxLevelCap() {
        return SNAPSHOT.get().overallMaxLevelCap();
    }

    public static void replaceAll(Int2ObjectMap<Tier> byIndex) {
        // Order by index
        List<Tier> ordered = new ArrayList<>(byIndex.values());
        ordered.sort(Comparator.comparingInt(t -> t.index));

        // Compute overall max
        int overallMax = 0;
        for (Tier t : ordered) {
            if (t.levelCap > overallMax) overallMax = t.levelCap;
        }

        // Defensive copy + immutable view
        Int2ObjectOpenHashMap<Tier> copy = new Int2ObjectOpenHashMap<>(byIndex);
        Int2ObjectMap<Tier> unmodifiable = Int2ObjectMaps.unmodifiable(copy);

        SNAPSHOT.set(new Snapshot(unmodifiable, List.copyOf(ordered), overallMax));
    }
}
