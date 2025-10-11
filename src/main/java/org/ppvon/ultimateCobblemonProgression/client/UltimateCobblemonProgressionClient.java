package org.ppvon.ultimateCobblemonProgression.client;

import net.fabricmc.api.ClientModInitializer;
import org.ppvon.ultimateCobblemonProgression.client.gui.RadGymsGuiListeners;

public class UltimateCobblemonProgressionClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        RadGymsGuiListeners.register();
    }
}
