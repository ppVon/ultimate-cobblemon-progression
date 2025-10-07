package org.ppvon.ultimateCobblemonProgression.common.levelcap;

import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.pokemon.ExperienceGainedPreEvent;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.ppvon.ultimateCobblemonProgression.common.component.TrainerLevelComponents;
import org.ppvon.ultimateCobblemonProgression.common.tiers.TierRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ExpCapListener {
    private static final Map<UUID, Integer> lastMsgTick = new HashMap<>();
    private static final int MSG_COOLDOWN = 80;

    public static void register() {
        CobblemonEvents.EXPERIENCE_GAINED_EVENT_PRE.subscribe(Priority.HIGHEST, ExpCapListener::onPreExp);
    }

    private static kotlin.Unit onPreExp(ExperienceGainedPreEvent e) {
        Pokemon mon = e.getPokemon();
        ServerPlayer owner = mon.getOwnerPlayer();
        if (owner == null) return kotlin.Unit.INSTANCE;

        int trainerLevel = TrainerLevelComponents.KEY.get(owner).getLevel();
        int cap = TierRegistry.getCapForTier(trainerLevel);
        int lvl = mon.getLevel();
        int req = e.getExperience();

        if (lvl >= cap) {
            e.setExperience(0);
            notify(owner, mon, cap, "XP blocked: at Trainer cap (");
            return kotlin.Unit.INSTANCE;
        }

        int expToCap = mon.getExperienceToLevel(cap);
        if (req > expToCap) {
            e.setExperience(expToCap);
            notify(owner, mon, cap, "XP capped: can’t exceed Trainer cap (");
        }
        return kotlin.Unit.INSTANCE;
    }

    private static void notify(ServerPlayer sp, Pokemon mon, int cap, String msg) {
        int now = sp.server.getTickCount();
        Integer last = lastMsgTick.get(sp.getUUID());
        if (last == null || now - last >= MSG_COOLDOWN) {
            sp.displayClientMessage(
                    Component.literal(mon.getDisplayName().getString() + " — " + msg + cap + ")."),
                    true
            );
            lastMsgTick.put(sp.getUUID(), now);
        }
    }
}
