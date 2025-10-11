package org.ppvon.ultimateCobblemonProgression.common.gym;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import lol.gito.radgyms.api.events.GymEvents;
import lol.gito.radgyms.api.events.gym.GenerateRewardEvent;
import lol.gito.radgyms.api.events.gym.GenerateTeamEvent;
import org.ppvon.ultimateCobblemonProgression.common.component.TrainerLevelComponents;
import org.ppvon.ultimateCobblemonProgression.common.tiers.TierRegistry;

import java.util.List;

public final class RadGymsGymGenerateTeamListener {
    private RadGymsGymGenerateTeamListener() {}

    public static void register() {
        GymEvents.GENERATE_TEAM.subscribe(Priority.LOWEST, onGenerateTeam);

    }

    private static final Function1<GenerateTeamEvent, Unit> onGenerateTeam = event -> {
        int playerLevel = TrainerLevelComponents.KEY.get(event.getPlayer()).getLevel();
        int playerCap = TierRegistry.getCapForTier(playerLevel);

        List<PokemonProperties> team = event.getTeam();
        team.clear();
        PokemonProperties p = PokemonProperties.Companion.parse("zigzagoon level=10");
        team.add(p);
        PokemonProperties p2 = PokemonProperties.Companion.parse("charmander level=10");
        if(event.isLeader()) {
            team.add(p2);
        }
        return Unit.INSTANCE;
    };
}
