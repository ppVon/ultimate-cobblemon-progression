package org.ppvon.ucp.common;

import com.cobblemon.mod.common.platform.events.PlatformEvents;
import dev.architectury.registry.ReloadListenerRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import org.ppvon.ucp.common.api.event.TierEvents;
import org.ppvon.ucp.common.config.UcpConfigs;
import org.ppvon.ucp.common.internal.cobblemon.species.SpeciesTierAssigner;
import org.ppvon.ucp.common.internal.levelcap.ExpCapHandler;
import org.ppvon.ucp.common.internal.progression.DexProgressionHandler;
import org.ppvon.ucp.common.internal.tiers.TierLoadCoordinator;
import org.ppvon.ucp.common.internal.tiers.TierReloadListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UltimateCobblemonProgression {
    public static final String MOD_ID = "ultimate_cobblemon_progression";
    public static final Logger LOGGER = LoggerFactory.getLogger("UCP");

    public static ResourceLocation modId(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    private UltimateCobblemonProgression() {
    }

    public static void init(UltimateCobblemonProgressionPlatform platform) {
        UcpConfigs.init(platform.configDir());
        UcpConfigs.load();
        ReloadListenerRegistry.register(PackType.SERVER_DATA, new TierReloadListener());

        TierEvents.TIERS_UPDATED.on(SpeciesTierAssigner::onTiersUpdated);
        ExpCapHandler.init();
        DexProgressionHandler.init();

        PlatformEvents.SERVER_STARTED.subscribe(TierLoadCoordinator::onServerStarted);
        PlatformEvents.SERVER_STOPPED.subscribe(event -> TierLoadCoordinator.onServerStopped());
    }
}
