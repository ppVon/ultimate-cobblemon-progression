package org.ppvon.ucp.fabric;

import com.cobblemon.mod.common.ModAPI;
import org.ppvon.ucp.common.UltimateCobblemonProgressionPlatform;

public class UltimateCobblemonProgressionFabricPlatform implements UltimateCobblemonProgressionPlatform {
    @Override
    public ModAPI platform() {
        return ModAPI.FABRIC;
    }
}
