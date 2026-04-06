package org.ppvon.ucp.neoforge;

import com.cobblemon.mod.common.ModAPI;
import net.neoforged.fml.loading.FMLPaths;
import org.ppvon.ucp.common.UltimateCobblemonProgressionPlatform;
import org.ppvon.ucp.neoforge.network.UltimateCobblemonProgressionNeoForgeNetwork;

import java.nio.file.Path;

public class UltimateCobblemonProgressionNeoforgePlatform implements UltimateCobblemonProgressionPlatform {
    private final UltimateCobblemonProgressionNeoForgeNetwork network;

    public UltimateCobblemonProgressionNeoforgePlatform() {
        this.network = new UltimateCobblemonProgressionNeoForgeNetwork();
    }

    @Override
    public ModAPI platform() {
        return ModAPI.NEOFORGE;
    }

    @Override
    public Path configDir() {
        return FMLPaths.CONFIGDIR.get();
    }

    public UltimateCobblemonProgressionNeoForgeNetwork networkManager() {
        return network;
    }

    @Override
    public void initialize() {
    }
}
