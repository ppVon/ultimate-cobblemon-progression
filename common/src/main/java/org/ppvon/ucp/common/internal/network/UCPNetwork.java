package org.ppvon.ucp.common.internal.network;

import com.cobblemon.mod.common.api.net.NetworkPacket;
import com.cobblemon.mod.common.net.PacketRegisterInfo;
import net.minecraft.server.level.ServerPlayer;
import org.ppvon.ucp.common.UltimateCobblemonProgression;
import org.ppvon.ucp.common.internal.network.client.handler.TrainerTierInfoS2CHandler;
import org.ppvon.ucp.common.internal.network.client.payload.AnnounceClientPresenceC2S;
import org.ppvon.ucp.common.internal.network.client.payload.RequestTierDataC2S;
import org.ppvon.ucp.common.internal.network.server.handler.AnnounceClientPresenceC2SHandler;
import org.ppvon.ucp.common.internal.network.server.handler.RequestTierDataC2SHandler;
import org.ppvon.ucp.common.internal.network.server.payload.TrainerTierInfoS2C;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.ppvon.ucp.common.UltimateCobblemonProgression.LOGGER;

public class UCPNetwork {
    public static List<PacketRegisterInfo<?>> s2cPayloads = generateS2CPacketInfoList();
    public static List<PacketRegisterInfo<?>> c2sPayloads = generateC2SPacketInfoList();

    public static void sendToServer(NetworkPacket<?> packet) {
        Objects.requireNonNull(UltimateCobblemonProgression.platform.networkManager()).sendToServer(packet);
    }

    public static void sendPacketToPlayer(ServerPlayer player, NetworkPacket<?> packet) {
        Objects.requireNonNull(UltimateCobblemonProgression.platform.networkManager()).sendPacketToPlayer(player, packet);
    }

    private static List<PacketRegisterInfo<?>> generateS2CPacketInfoList() {
        List<PacketRegisterInfo<?>> packets = new ArrayList<>();

        packets.add(new PacketRegisterInfo<>(
                TrainerTierInfoS2C.ID,
                TrainerTierInfoS2C::decode,
                new TrainerTierInfoS2CHandler(),
                null
        ));

        LOGGER.info("added s2c!");
        return packets;
    }

    private static List<PacketRegisterInfo<?>> generateC2SPacketInfoList() {
        List<PacketRegisterInfo<?>> packets = new ArrayList<>();
        packets.add(new PacketRegisterInfo<>(
                AnnounceClientPresenceC2S.ID,
                AnnounceClientPresenceC2S::decode,
                new AnnounceClientPresenceC2SHandler(),
                null
        ));

        packets.add(new PacketRegisterInfo<>(
                RequestTierDataC2S.ID,
                RequestTierDataC2S::decode,
                new RequestTierDataC2SHandler(),
                null
        ));
        LOGGER.info("added c2s!");

        return packets;
    }
}
