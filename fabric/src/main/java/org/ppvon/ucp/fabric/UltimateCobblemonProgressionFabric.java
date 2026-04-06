package org.ppvon.ucp.fabric;

import com.cobblemon.mod.common.ModAPI;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import org.ppvon.ucp.common.UltimateCobblemonProgression;
import org.ppvon.ucp.common.UltimateCobblemonProgressionPlatform;
import org.ppvon.ucp.common.internal.command.TrainerLevelCommands;
import org.ppvon.ucp.common.internal.levelcap.CandyRefundHandler;
import org.ppvon.ucp.common.internal.levelcap.ExpCapHandler;
import org.ppvon.ucp.common.internal.trainer.TrainerLevelInitializer;
import org.ppvon.ucp.fabric.network.UltimateCobblemonProgressionFabricNetwork;

import java.nio.file.Path;

public class UltimateCobblemonProgressionFabric implements UltimateCobblemonProgressionPlatform {
    private static final UltimateCobblemonProgressionFabricNetwork network = new UltimateCobblemonProgressionFabricNetwork();

    @Override
    public ModAPI platform() {
        return ModAPI.FABRIC;
    }

    public UltimateCobblemonProgressionFabricNetwork networkManager() {
        return network;
    }

    @Override
    public Path configDir() {
        return FabricLoader.getInstance().getConfigDir();
    }

    @Override
    public void initialize() {
        UltimateCobblemonProgression.init(this);

        networkManager().registerMessages();
        networkManager().registerServerHandlers();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                TrainerLevelCommands.register(dispatcher));
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
                TrainerLevelInitializer.initializeOnJoin(handler.player));
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            ExpCapHandler.onPlayerDisconnect(handler.player.getUUID());
            CandyRefundHandler.onPlayerDisconnect(handler.player.getUUID());
        });
        FabricCandyEvents.register();
    }
}
