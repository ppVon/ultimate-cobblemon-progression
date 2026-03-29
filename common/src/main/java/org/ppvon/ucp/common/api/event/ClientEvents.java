package org.ppvon.ucp.common.api.event;

import org.ppvon.ucp.common.util.EventObservableEx;

public final class ClientEvents {
    /**
     * Fired when client instance opens pokedex and TrainerTierInfoWidget constructor is called
     *
     * @see org.ppvon.ucp.common.mixin.client.cobblemon.gui.PokedexTrainerTierMixin
     */
    public record PokedexOpenEvent() {
    }

    /**
     * Observable fired once species tier data has been fully applied.
     */
    public static final EventObservableEx<PokedexOpenEvent> POKEDEX_OPEN = new EventObservableEx<>();
}
