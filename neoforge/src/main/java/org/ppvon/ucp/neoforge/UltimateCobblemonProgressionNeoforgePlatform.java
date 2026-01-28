package org.ppvon.ucp.neoforge;

import com.cobblemon.mod.common.ModAPI;
import net.neoforged.fml.loading.FMLPaths;
import org.ppvon.ucp.common.UltimateCobblemonProgressionPlatform;

import java.nio.file.Path;

public class UltimateCobblemonProgressionNeoforgePlatform implements UltimateCobblemonProgressionPlatform {
    @Override
    public ModAPI platform() {
        return ModAPI.NEOFORGE;
    }

    @Override
    public Path configDir() {
        return FMLPaths.CONFIGDIR.get();
    }
}
