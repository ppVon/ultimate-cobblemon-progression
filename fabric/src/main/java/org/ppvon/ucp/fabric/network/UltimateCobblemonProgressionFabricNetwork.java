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

    public void registerServerHandlers() {
        UCPNetwork.s2cPayloads
                .stream()
                .map(FabricPacketInfo::new)
                .forEach(FabricPacketInfo::registerClientHandler);
    }

    public void registerClientHandlers() {
        UCPNetwork.c2sPayloads
                .stream()
                .map(FabricPacketInfo::new)
                .forEach(FabricPacketInfo::registerServerHandler);
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
