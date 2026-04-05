package org.ppvon.ucp.common.internal.network.server.handler;

import com.cobblemon.mod.common.api.net.ServerNetworkPacketHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.ppvon.ucp.common.internal.network.client.payload.RequestTierDataC2S;
import org.ppvon.ucp.common.internal.progression.DexProgressionHandler;

import static org.ppvon.ucp.common.UltimateCobblemonProgression.LOGGER;

public class RequestTierDataC2SHandler implements ServerNetworkPacketHandler<RequestTierDataC2S> {
    @Override
    public void handle(@NotNull RequestTierDataC2S payload, @NotNull MinecraftServer server, @NotNull ServerPlayer player) {
        if (DexProgressionHandler.eligibleClients.contains(player.getUUID())) {
            LOGGER.info("player client requested tier update");
            DexProgressionHandler.notifyClient(player);
        }
    }
}
