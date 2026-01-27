package org.ppvon.ucp.common.internal.tiers;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.server.MinecraftServer;
import org.ppvon.ucp.common.api.tiers.Tier;

import java.util.concurrent.atomic.AtomicReference;

public final class TierLoadCoordinator {

    private static final AtomicReference<Int2ObjectMap<Tier>> PENDING = new AtomicReference<>(null);
    private static volatile MinecraftServer SERVER;

    private TierLoadCoordinator() {}

    public static void onServerStarted(MinecraftServer server) {
        SERVER = server;
        tryApply();
    }

    public static void onServerStopped() {
        SERVER = null;
        PENDING.set(null);
    }

    public static void stage(Int2ObjectMap<Tier> parsedTiers) {
        PENDING.set(parsedTiers);
        tryApply();
    }

    private static void tryApply() {
        MinecraftServer server = SERVER;
        if (server == null) return;

        Int2ObjectMap<Tier> pending = PENDING.getAndSet(null);
        if (pending == null) return;

        server.execute(() -> TierManager.replaceAll(pending));
    }
}
