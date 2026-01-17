package org.ppvon.ultimateCobblemonProgression.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.ppvon.ultimateCobblemonProgression.client.gui.radgyms.RadGymsClientCompat;

public class UltimateCobblemonProgressionClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        boolean isLoaded = FabricLoader.getInstance().isModLoaded("rad-gyms");
        /*
        if(FabricLoader.getInstance().isModLoaded("rad-gyms")){
            RadGymsClientCompat.register();
        }
         */
    }
}
