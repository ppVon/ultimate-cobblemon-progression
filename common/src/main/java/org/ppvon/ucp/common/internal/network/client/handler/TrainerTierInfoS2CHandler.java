package org.ppvon.ucp.common.internal.network.client.handler;

import com.cobblemon.mod.common.api.net.ClientNetworkPacketHandler;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;
import org.ppvon.ucp.common.internal.network.server.payload.TrainerTierInfoS2C;

public class TrainerTierInfoS2CHandler implements ClientNetworkPacketHandler<TrainerTierInfoS2C> {
    @Override
    public void handle(@NotNull TrainerTierInfoS2C payload, @NotNull Minecraft client) {

    }
}
