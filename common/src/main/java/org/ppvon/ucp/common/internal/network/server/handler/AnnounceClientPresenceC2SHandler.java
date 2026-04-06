package org.ppvon.ucp.common.internal.network.server.handler;

import com.cobblemon.mod.common.api.net.ServerNetworkPacketHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.ppvon.ucp.common.internal.network.client.payload.AnnounceClientPresenceC2S;
import org.ppvon.ucp.common.internal.progression.DexProgressionHandler;

import static org.ppvon.ucp.common.UltimateCobblemonProgression.LOGGER;

public class AnnounceClientPresenceC2SHandler implements ServerNetworkPacketHandler<AnnounceClientPresenceC2S> {
    @Override
    public void handle(@NotNull AnnounceClientPresenceC2S payload, @NotNull MinecraftServer server, @NotNull ServerPlayer player) {
        if (!DexProgressionHandler.eligibleClients.contains(player.getUUID())) {
            LOGGER.info("player announced presence of client-side installation, added to notification list");
            DexProgressionHandler.eligibleClients.add(player.getUUID());
        }
    }
}
