package org.ppvon.ucp.neoforge;

import net.neoforged.fml.common.Mod;
import org.ppvon.ucp.common.UltimateCobblemonProgression;

@Mod(UltimateCobblemonProgression.MOD_ID)
public class UltimateCobblemonProgressionNeoforge {
    public UltimateCobblemonProgressionNeoforge() {
        UltimateCobblemonProgression.init(new UltimateCobblemonProgressionNeoforgePlatform());
    }
}
