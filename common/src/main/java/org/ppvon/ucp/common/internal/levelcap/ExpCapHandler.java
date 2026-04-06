package org.ppvon.ucp.common.internal.levelcap;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.pokemon.ExperienceGainedEvent;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.ppvon.ucp.common.api.trainer.TrainerLevels;
import org.ppvon.ucp.common.api.tiers.TierRegistry;
import org.ppvon.ucp.common.config.UcpConfigs;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class ExpCapHandler {
    private static final int MESSAGE_COOLDOWN_TICKS = 80;
    private static final Map<UUID, Integer> LAST_MESSAGE_TICK = new ConcurrentHashMap<>();

    private static boolean initialized;

    private ExpCapHandler() {}

    public static void init() {
        if (initialized) {
            return;
        }
        initialized = true;
        CobblemonEvents.EXPERIENCE_GAINED_EVENT_PRE.subscribe(Priority.HIGHEST, ExpCapHandler::onPreExperience);
    }

    private static void onPreExperience(ExperienceGainedEvent.Pre event) {
        if (!UcpConfigs.common().doLevelCap) {
            return;
        }

        Pokemon pokemon = event.getPokemon();
        ServerPlayer owner = pokemon.getOwnerPlayer();
        if (owner == null) {
            return;
        }

        int trainerLevel = TrainerLevels.get(owner);
        int cap = TierRegistry.getLevelCap(trainerLevel);
        if (cap <= 0) {
            return;
        }

        int expToForbiddenLevel = Math.max(0, pokemon.getExperienceToLevel(cap + 1));
        int maxAllowed = Math.max(0, expToForbiddenLevel - 1);
        if (event.getExperience() > maxAllowed) {
            event.setExperience(maxAllowed);
            notify(owner, pokemon, cap, maxAllowed == 0
                    ? "XP blocked: at Trainer cap ("
                    : "XP capped: approaching Trainer cap (");
        }
    }

    public static void onPlayerDisconnect(UUID uuid) {
        LAST_MESSAGE_TICK.remove(uuid);
    }

    private static void notify(ServerPlayer player, Pokemon pokemon, int cap, String prefix) {
        int now = player.server.getTickCount();
        Integer last = LAST_MESSAGE_TICK.get(player.getUUID());
        if (last != null && now - last < MESSAGE_COOLDOWN_TICKS) {
            return;
        }

        player.displayClientMessage(
                Component.literal(pokemon.getDisplayName(false).getString() + " - " + prefix + cap + ")."),
                true
        );
        LAST_MESSAGE_TICK.put(player.getUUID(), now);
    }
}
