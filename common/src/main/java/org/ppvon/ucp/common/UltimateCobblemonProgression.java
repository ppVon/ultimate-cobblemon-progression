package org.ppvon.ucp.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import dev.architectury.registry.ReloadListenerRegistry;
import net.minecraft.server.packs.PackType;
import org.ppvon.ucp.common.internal.tiers.TierReloadListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UltimateCobblemonProgression {
    public static final String MOD_ID = "ultimate_cobblemon_progression";
    public static final Logger LOGGER = LoggerFactory.getLogger("UCP");

    private UltimateCobblemonProgression() {}


    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

    public static void init(UltimateCobblemonProgressionPlatform platform) {
        ReloadListenerRegistry.register(PackType.SERVER_DATA, new TierReloadListener(GSON));
    }
}
