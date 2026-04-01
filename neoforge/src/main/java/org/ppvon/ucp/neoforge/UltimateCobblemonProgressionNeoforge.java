package org.ppvon.ucp.neoforge;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import org.ppvon.ucp.common.UltimateCobblemonProgression;
import org.ppvon.ucp.common.internal.command.TrainerLevelCommands;
import org.ppvon.ucp.common.internal.levelcap.CandyRefundHandler;
import org.ppvon.ucp.common.internal.levelcap.ExpCapHandler;
import org.ppvon.ucp.common.internal.trainer.TrainerLevelInitializer;

@Mod(UltimateCobblemonProgression.MOD_ID)
public class UltimateCobblemonProgressionNeoforge {
    public UltimateCobblemonProgressionNeoforge(IEventBus modBus) {
        UltimateCobblemonProgressionNeoforgePlatform platform = new UltimateCobblemonProgressionNeoforgePlatform();
        UltimateCobblemonProgression.init(platform);
        modBus.addListener((RegisterPayloadHandlersEvent event) -> platform.networkManager().registerMessages(event));
    }

    @SubscribeEvent
    private void onRegisterCommands(RegisterCommandsEvent event) {
        TrainerLevelCommands.register(event.getDispatcher());
    }

    @SubscribeEvent
    private void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            TrainerLevelInitializer.initializeOnJoin(player);
        }
    }

    @SubscribeEvent
    private void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ExpCapHandler.onPlayerDisconnect(player.getUUID());
            CandyRefundHandler.onPlayerDisconnect(player.getUUID());
        }
    }
}
