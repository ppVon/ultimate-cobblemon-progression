package org.ppvon.ucp.common.internal.network.server.handler;

import com.cobblemon.mod.common.api.net.ServerNetworkPacketHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.ppvon.ucp.common.api.tiers.Tier;
import org.ppvon.ucp.common.api.tiers.TierRegistry;
import org.ppvon.ucp.common.api.trainer.TrainerLevels;
import org.ppvon.ucp.common.config.UcpConfigs;
import org.ppvon.ucp.common.internal.network.UCPNetwork;
import org.ppvon.ucp.common.internal.network.client.payload.PokedexOpenC2S;
import org.ppvon.ucp.common.internal.network.server.payload.TrainerTierInfoS2C;
import org.ppvon.ucp.common.internal.trainer.TrainerLevelProgression;

import java.util.Optional;

import static org.ppvon.ucp.common.UltimateCobblemonProgression.LOGGER;

public class PokedexOpenC2SHandler implements ServerNetworkPacketHandler<PokedexOpenC2S> {
    @Override
    public void handle(@NotNull PokedexOpenC2S payload, @NotNull MinecraftServer server, @NotNull ServerPlayer player) {
        LOGGER.info("opened dex!");

        int currentTier = TrainerLevels.get(player);
        int levelCap = UcpConfigs.common().doLevelCap ? Math.max(TierRegistry.getLevelCap(currentTier), 1) : -1;

        Optional<Tier> nextTier = TierRegistry.next(currentTier);
        TrainerLevelProgression.DexCounts countsForNextTier;
        if (UcpConfigs.common().doDexProgression && nextTier.isPresent()) {
            countsForNextTier = new TrainerLevelProgression.DexCounts(
                    nextTier.get().requirements.dex.seen,
                    nextTier.get().requirements.dex.caught
            );
        } else {
            countsForNextTier = TrainerLevelProgression.DexCounts.ZERO;
        }

        UCPNetwork.sendPacketToPlayer(player, new TrainerTierInfoS2C(
                TrainerLevels.get(player),
                TierRegistry.maxIndex(),
                levelCap,
                countsForNextTier
        ));
    }
}
