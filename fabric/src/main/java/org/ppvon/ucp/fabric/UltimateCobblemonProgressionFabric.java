package org.ppvon.ucp.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import org.ppvon.ucp.common.UltimateCobblemonProgression;
import org.ppvon.ucp.common.UltimateCobblemonProgressionPlatform;
import org.ppvon.ucp.common.internal.command.TrainerLevelCommands;
import org.ppvon.ucp.common.internal.trainer.TrainerLevelInitializer;

public class UltimateCobblemonProgressionFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        UltimateCobblemonProgressionPlatform platform = new UltimateCobblemonProgressionFabricPlatform();
        UltimateCobblemonProgression.init(platform);
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                TrainerLevelCommands.register(dispatcher));
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
                TrainerLevelInitializer.initializeOnJoin(handler.player));
        FabricCandyEvents.register();
    }
}
