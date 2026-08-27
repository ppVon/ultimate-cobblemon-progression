package org.ppvon.ucp.fabric.network;

import com.cobblemon.mod.common.NetworkManager;
import com.cobblemon.mod.common.api.net.NetworkPacket;
import com.cobblemon.mod.fabric.net.FabricPacketInfo;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.ppvon.ucp.common.internal.network.UCPNetwork;

public class UltimateCobblemonProgressionFabricNetwork implements NetworkManager {
    public void registerMessages() {
        UCPNetwork.s2cPayloads
                .stream()
                .map(FabricPacketInfo::new)
                .forEach((packet) -> packet.registerPacket(true));
        UCPNetwork.c2sPayloads
                .stream()
                .map(FabricPacketInfo::new)
                .forEach((packet) -> packet.registerPacket(false));
    }

    /**
     * Registers handlers for C2S payloads. Must run on both the dedicated server and the
     * integrated server, so it may only touch {@code ServerPlayNetworking}.
     */
    public void registerServerHandlers() {
        UCPNetwork.c2sPayloads
                .stream()
                .map(FabricPacketInfo::new)
                .forEach(FabricPacketInfo::registerServerHandler);
    }

    /**
     * Registers handlers for S2C payloads. Touches {@code ClientPlayNetworking}, which does not
     * exist in the SERVER environment, so this may only be called from the client entrypoint.
     */
    public void registerClientHandlers() {
        UCPNetwork.s2cPayloads
                .stream()
                .map(FabricPacketInfo::new)
                .forEach(FabricPacketInfo::registerClientHandler);
    }

    @Override
    public void sendPacketToPlayer(@NotNull ServerPlayer serverPlayer, @NotNull NetworkPacket<?> networkPacket) {
        ServerPlayNetworking.send(serverPlayer, networkPacket);
    }

    @Override
    public void sendToServer(@NotNull NetworkPacket<?> networkPacket) {
        ClientPlayNetworking.send(networkPacket);
    }
}
