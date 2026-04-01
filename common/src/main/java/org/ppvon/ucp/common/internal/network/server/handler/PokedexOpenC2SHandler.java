package org.ppvon.ucp.common.internal.network.server.handler;

import com.cobblemon.mod.common.api.net.ServerNetworkPacketHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.ppvon.ucp.common.internal.network.client.payload.PokedexOpenC2S;

import static org.ppvon.ucp.common.UltimateCobblemonProgression.LOGGER;

public class PokedexOpenC2SHandler implements ServerNetworkPacketHandler<PokedexOpenC2S> {
    @Override
    public void handle(@NotNull PokedexOpenC2S payload, @NotNull MinecraftServer server, @NotNull ServerPlayer player) {
        LOGGER.info("opened dex!");
    }
}
