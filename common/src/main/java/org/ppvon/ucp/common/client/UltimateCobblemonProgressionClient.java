package org.ppvon.ucp.common.client;

import com.cobblemon.mod.common.platform.events.PlatformEvents;
import org.ppvon.ucp.common.internal.network.UCPNetwork;
import org.ppvon.ucp.common.internal.network.client.payload.PokedexOpenC2S;
import org.ppvon.ucp.common.internal.trainer.TrainerLevelProgression;

public class UltimateCobblemonProgressionClient {
    private static UltimateCobblemonProgressionClient INSTANCE;
    public static TrainerLevelProgression.ProgressionInfoHolder trainerProgression;

    private UltimateCobblemonProgressionClientPlatform platform;

    public void init(UltimateCobblemonProgressionClientPlatform platform) {
        this.platform = platform;

        // Sync initial trainer tier state just in case on player login
        PlatformEvents.CLIENT_PLAYER_LOGIN.subscribe((event) -> UCPNetwork.sendToServer(new PokedexOpenC2S()));
    }

    public static UltimateCobblemonProgressionClient getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UltimateCobblemonProgressionClient();
        }

        return INSTANCE;
    }
}
