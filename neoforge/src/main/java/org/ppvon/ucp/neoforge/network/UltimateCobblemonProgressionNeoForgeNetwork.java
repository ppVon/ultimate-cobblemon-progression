package org.ppvon.ucp.neoforge.network;

import com.cobblemon.mod.common.NetworkManager;
import com.cobblemon.mod.common.api.net.NetworkPacket;
import com.cobblemon.mod.common.client.net.data.DataRegistrySyncPacketHandler;
import com.cobblemon.mod.neoforge.net.NeoForgePacketInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.jetbrains.annotations.NotNull;
import org.ppvon.ucp.common.internal.network.UCPNetwork;

import java.util.HashSet;
import java.util.Objects;

import static org.ppvon.ucp.common.UltimateCobblemonProgression.MOD_ID;

public class UltimateCobblemonProgressionNeoForgeNetwork implements NetworkManager {
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    public void registerMessages(RegisterPayloadHandlersEvent event) {
        String PROTOCOL_VERSION = "1.0";

        PayloadRegistrar registrar = event.registrar(MOD_ID).versioned(PROTOCOL_VERSION);
        PayloadRegistrar netRegistrar = event.registrar(MOD_ID).versioned(PROTOCOL_VERSION).executesOn(HandlerThread.MAIN);

        HashSet<ResourceLocation> syncPackets = new HashSet<>();
        HashSet<ResourceLocation> asyncPackets = new HashSet<>();

        UCPNetwork.s2cPayloads.stream().map(NeoForgePacketInfo::new).forEach((packet) -> {
            boolean handleAsync = packet.getInfo().getHandler() instanceof DataRegistrySyncPacketHandler<?,?>;

            if (handleAsync) {
                asyncPackets.add(packet.getInfo().getId());
            } else {
                syncPackets.add(packet.getInfo().getId());
            }

            packet.registerToClient(handleAsync ? netRegistrar : registrar);
        });

        UCPNetwork.c2sPayloads.stream().map(NeoForgePacketInfo::new).forEach((packet) -> {
            packet.registerToServer(registrar);
        });
    }

    @Override
    public void sendPacketToPlayer(@NotNull ServerPlayer serverPlayer, @NotNull NetworkPacket<?> networkPacket) {
        serverPlayer.connection.send(networkPacket);
    }

    @Override
    public void sendToServer(@NotNull NetworkPacket<?> networkPacket) {
        Objects.requireNonNull(Minecraft.getInstance().getConnection()).send(networkPacket);
    }
}
