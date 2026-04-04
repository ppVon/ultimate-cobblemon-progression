package org.ppvon.ucp.common.client;

import com.cobblemon.mod.common.platform.events.PlatformEvents;
import org.ppvon.ucp.common.config.UcpConfigs;
import org.ppvon.ucp.common.internal.network.UCPNetwork;
import org.ppvon.ucp.common.internal.network.client.payload.AnnounceClientPresenceC2S;
import org.ppvon.ucp.common.internal.network.client.payload.RequestTierDataC2S;
import org.ppvon.ucp.common.internal.trainer.TrainerLevelProgression;

public class UltimateCobblemonProgressionClient {
    private static UltimateCobblemonProgressionClient INSTANCE;
    public static TrainerLevelProgression.ProgressionInfoHolder trainerProgression;

    private UltimateCobblemonProgressionClientPlatform platform;

    public static UltimateCobblemonProgressionClient getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UltimateCobblemonProgressionClient();
        }

        return INSTANCE;
    }

    public void init(UltimateCobblemonProgressionClientPlatform platform) {
        UcpConfigs.loadClient();

        this.platform = platform;

        // Make server know that client have mod installed clientside and that we can notify it when dex updates
        // Sync initial trainer tier state just in case on player login
        // After this it will be synced only when dex progression gets updated
        PlatformEvents.CLIENT_PLAYER_LOGIN.subscribe((event) -> {
            UCPNetwork.sendToServer(new AnnounceClientPresenceC2S());
            UCPNetwork.sendToServer(new RequestTierDataC2S());
        });
    }
}
