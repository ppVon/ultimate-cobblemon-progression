package org.ppvon.ucp.common.mixin.cobblemon.species;

import com.cobblemon.mod.common.pokemon.Species; // adjust import if your Species is elsewhere
import org.ppvon.ucp.common.access.cobblemon.species.SpeciesTierHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Species.class)
public class SpeciesTierMixin implements SpeciesTierHolder {

    @Unique
    private volatile int ucp$tier = -1;

    @Override
    public int ucp$getTier() {
        return ucp$tier;
    }

    @Override
    public void ucp$setTier(int tier) {
        this.ucp$tier = tier;
    }
}
