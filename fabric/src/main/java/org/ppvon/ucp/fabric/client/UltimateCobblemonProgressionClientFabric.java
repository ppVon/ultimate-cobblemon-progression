package org.ppvon.ucp.fabric.client;

import org.ppvon.ucp.common.UltimateCobblemonProgression;
import org.ppvon.ucp.common.client.UltimateCobblemonProgressionClient;
import org.ppvon.ucp.common.client.UltimateCobblemonProgressionClientPlatform;
import org.ppvon.ucp.fabric.network.UltimateCobblemonProgressionFabricNetwork;

public class UltimateCobblemonProgressionClientFabric implements UltimateCobblemonProgressionClientPlatform {
    @Override
    public void initialize() {
        UltimateCobblemonProgressionClient.getInstance().init(this);
        ((UltimateCobblemonProgressionFabricNetwork) UltimateCobblemonProgression.platform.networkManager()).registerClientHandlers();
    }
}
