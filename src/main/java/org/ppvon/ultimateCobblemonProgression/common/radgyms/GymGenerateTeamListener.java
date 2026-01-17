package org.ppvon.ultimateCobblemonProgression.common.radgyms;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import lol.gito.radgyms.api.event.GymEvents;
import org.ppvon.ultimateCobblemonProgression.common.component.TrainerLevelComponents;
import org.ppvon.ultimateCobblemonProgression.common.tiers.TierRegistry;

import java.util.List;

public final class GymGenerateTeamListener {
    private GymGenerateTeamListener() {}

    public static void register() {
        GymEvents.GENERATE_TEAM.subscribe(Priority.LOWEST, onGenerateTeam);
    }

    private static final Function1<GymEvents.GenerateTeamEvent, Unit> onGenerateTeam = event -> {
        // TODO Gym trainer team generation logic
        int playerLevel = TrainerLevelComponents.KEY.get(event.getPlayer()).getLevel();
        int playerCap = TierRegistry.getCapForTier(playerLevel);

        List<PokemonProperties> team = event.getTeam();
        int numPokemon = team.toArray().length;
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