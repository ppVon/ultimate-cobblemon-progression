package org.ppvon.ultimateCobblemonProgression.common.progression.dex;

import com.cobblemon.mod.common.api.pokedex.AbstractPokedexManager;
import com.cobblemon.mod.common.api.pokedex.CaughtCount;
import com.cobblemon.mod.common.api.pokedex.SeenCount;
import com.cobblemon.mod.common.util.PlayerExtensionsKt;
import net.minecraft.server.level.ServerPlayer;

public class DexProgressionApi {
    private DexProgressionApi() {}

    public static DexCounts get(ServerPlayer player) {
        if( player == null ) return DexCounts.ZERO;

        AbstractPokedexManager dex = PlayerExtensionsKt.pokedex(player);
        if( dex == null ) return DexCounts.ZERO;

        int caught = dex.getGlobalCalculatedValue(CaughtCount.INSTANCE);
        int seen = dex.getGlobalCalculatedValue(SeenCount.INSTANCE);

        return new DexCounts(seen, caught);
    }


    public record DexCounts(int seen, int caught) {
        public static final DexCounts ZERO = new DexCounts(0, 0);
    }
}
