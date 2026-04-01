package org.ppvon.ucp.common.internal.network;

import com.cobblemon.mod.common.api.net.NetworkPacket;
import com.cobblemon.mod.common.net.PacketRegisterInfo;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import org.ppvon.ucp.common.UltimateCobblemonProgression;
import org.ppvon.ucp.common.UltimateCobblemonProgressionPlatform;
import org.ppvon.ucp.common.internal.network.client.handler.TrainerTierInfoS2CHandler;
import org.ppvon.ucp.common.internal.network.client.payload.PokedexOpenC2S;
import org.ppvon.ucp.common.internal.network.server.handler.PokedexOpenC2SHandler;
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
                PokedexOpenC2S.ID,
                PokedexOpenC2S::decode,
                new PokedexOpenC2SHandler(),
                null
        ));
        LOGGER.info("added c2s!");

        return packets;
    }
}
