package org.ppvon.ucp.common;

import com.cobblemon.mod.common.ModAPI;

import java.nio.file.Path;

public interface UltimateCobblemonProgressionPlatform {
    ModAPI platform();

    default Path configDir() {
        throw new AssertionError();
    }
}
