package org.ppvon.ucp.fabric;

import net.fabricmc.api.ModInitializer;
import org.ppvon.ucp.common.UltimateCobblemonProgression;
import org.ppvon.ucp.common.UltimateCobblemonProgressionPlatform;

public class UltimateCobblemonProgressionFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        UltimateCobblemonProgressionPlatform platform = new UltimateCobblemonProgressionFabricPlatform();
        UltimateCobblemonProgression.init(platform);
    }
}
