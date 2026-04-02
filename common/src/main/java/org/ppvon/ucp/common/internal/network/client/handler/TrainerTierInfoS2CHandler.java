package org.ppvon.ucp.common.internal.network.client.handler;

import com.cobblemon.mod.common.api.net.ClientNetworkPacketHandler;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;
import org.ppvon.ucp.common.client.UltimateCobblemonProgressionClient;
import org.ppvon.ucp.common.internal.network.server.payload.TrainerTierInfoS2C;
import org.ppvon.ucp.common.internal.trainer.TrainerLevelProgression;

import static org.ppvon.ucp.common.UltimateCobblemonProgression.LOGGER;

public class TrainerTierInfoS2CHandler implements ClientNetworkPacketHandler<TrainerTierInfoS2C> {
    @Override
    public void handle(@NotNull TrainerTierInfoS2C payload, @NotNull Minecraft client) {
        LOGGER.info("got trainer tier");
        UltimateCobblemonProgressionClient.trainerProgression = new TrainerLevelProgression.ProgressionInfoHolder(
                payload.tier(),
                payload.totalTiers(),
                payload.levelCap(),
                payload.requirements()
        );
    }
}
