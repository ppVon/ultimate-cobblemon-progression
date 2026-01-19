package org.ppvon.ucp.neoforge;

import com.cobblemon.mod.common.ModAPI;
import org.ppvon.ucp.common.UltimateCobblemonProgressionPlatform;

public class UltimateCobblemonProgressionNeoforgePlatform implements UltimateCobblemonProgressionPlatform {
    @Override
    public ModAPI platform() {
        return ModAPI.NEOFORGE;
    }
}
