package org.ppvon.ucp.neoforge.client;

import org.ppvon.ucp.common.client.UltimateCobblemonProgressionClient;
import org.ppvon.ucp.common.client.UltimateCobblemonProgressionClientPlatform;

public class UltimateCobblemonProgressionClientNeoforge implements UltimateCobblemonProgressionClientPlatform {
    @Override
    public void initialize() {
        UltimateCobblemonProgressionClient.getInstance().init(this);
    }
}
