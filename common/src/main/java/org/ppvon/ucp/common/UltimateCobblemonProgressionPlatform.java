package org.ppvon.ucp.common;

import com.cobblemon.mod.common.ModAPI;
import com.cobblemon.mod.common.NetworkManager;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public interface UltimateCobblemonProgressionPlatform {
    default NetworkManager networkManager() {
        throw new AssertionError();
    }

    ModAPI platform();

    default Path configDir() {
        throw new AssertionError();
    }

    void initialize();
}
