package org.ppvon.ucp.neoforge;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.ppvon.ucp.common.UltimateCobblemonProgression;
import org.ppvon.ucp.common.internal.command.TrainerLevelCommands;
import org.ppvon.ucp.common.internal.trainer.TrainerLevelInitializer;

@Mod(UltimateCobblemonProgression.MOD_ID)
public class UltimateCobblemonProgressionNeoforge {
    public UltimateCobblemonProgressionNeoforge() {
        UltimateCobblemonProgression.init(new UltimateCobblemonProgressionNeoforgePlatform());
        NeoForge.EVENT_BUS.addListener(this::onRegisterCommands);
        NeoForge.EVENT_BUS.addListener(this::onPlayerLoggedIn);
        NeoForge.EVENT_BUS.addListener(NeoforgeCandyEvents::onEntityInteract);
        NeoForge.EVENT_BUS.addListener(NeoforgeCandyEvents::onRightClickItem);
    }

    private void onRegisterCommands(RegisterCommandsEvent event) {
        TrainerLevelCommands.register(event.getDispatcher());
    }

    private void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            TrainerLevelInitializer.initializeOnJoin(player);
        }
    }
}
