package org.ppvon.ucp.fabric;

import com.cobblemon.mod.common.ModAPI;
import net.fabricmc.loader.api.FabricLoader;
import org.ppvon.ucp.common.UltimateCobblemonProgressionPlatform;

import java.nio.file.Path;

public class UltimateCobblemonProgressionFabricPlatform implements UltimateCobblemonProgressionPlatform {
    @Override
    public ModAPI platform() {
        return ModAPI.FABRIC;
    }

    @Override
    public Path configDir() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
