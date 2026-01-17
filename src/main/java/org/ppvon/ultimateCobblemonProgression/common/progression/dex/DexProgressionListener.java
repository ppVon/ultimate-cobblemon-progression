package org.ppvon.ultimateCobblemonProgression.common.progression.dex;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.pokemon.PokedexDataChangedEvent;
import com.cobblemon.mod.common.api.pokedex.AbstractPokedexManager;
import com.cobblemon.mod.common.api.pokedex.CaughtCount;
import com.cobblemon.mod.common.api.pokedex.SeenCount;
import kotlin.Unit;
import net.minecraft.server.level.ServerPlayer;
import org.ppvon.ultimateCobblemonProgression.common.progression.ProgressionManager;
import org.ppvon.ultimateCobblemonProgression.config.ConfigLoader;

import java.util.UUID;

import static com.cobblemon.mod.common.util.PlayerExtensionsKt.getPlayer;

public final class DexProgressionListener {

    private DexProgressionListener() {}

    public static void register() {
        CobblemonEvents.POKEDEX_DATA_CHANGED_POST.subscribe(Priority.NORMAL, DexProgressionListener::onDexChanged);
    }

    private static Unit onDexChanged(PokedexDataChangedEvent event) {
        if (!ConfigLoader.DO_DEX_PROGRESSION.get()) {
            return Unit.INSTANCE;
        }

        AbstractPokedexManager manager = event.getPokedexManager();

        int caughtCount = ConfigLoader.REQUIRE_DEX_CAUGHT.get()
            ? manager.getGlobalCalculatedValue(CaughtCount.INSTANCE)
            : 0;

        int seenCount = ConfigLoader.REQUIRE_DEX_SEEN.get()
            ? manager.getGlobalCalculatedValue(SeenCount.INSTANCE)
            : 0;

        UUID playerUUID = event.getPlayerUUID();
        ServerPlayer player = getPlayer(playerUUID);
        if (player == null) return Unit.INSTANCE;

        ProgressionManager.attemptLevelUp(player, seenCount, caughtCount);

        return Unit.INSTANCE;
}

}
