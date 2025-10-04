package org.ppvon.ultimateCobblemonProgression.influence;

import com.cobblemon.mod.common.api.spawning.influence.SpawningInfluence;
import com.cobblemon.mod.common.api.spawning.spawner.PlayerSpawnerFactory;
import kotlin.jvm.functions.Function1;
import net.minecraft.server.level.ServerPlayer;

public class TrainerLevelInfluenceRegistrar {
    private static final Function1<ServerPlayer, SpawningInfluence> TL_BUILDER =
            sp -> new TrainerLevelInfluence();  // Java lambda implements Function1's invoke(T)

    private static boolean registered = false;

    public static void registerOnce() {
        if (registered) return;
        var list = PlayerSpawnerFactory.INSTANCE.getInfluenceBuilders();
        if (!list.contains(TL_BUILDER)) {
            list.addFirst(TL_BUILDER);                  // run earliest
        }
        registered = true;
    }
}
