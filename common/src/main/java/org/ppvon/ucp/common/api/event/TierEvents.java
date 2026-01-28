package org.ppvon.ucp.common.api.event;

import org.ppvon.ucp.common.util.EventObservableEx;
import org.ppvon.ucp.common.api.tiers.Tier;
import java.util.List;

public final class TierEvents {
    /**
     * Fired when UCP tier definitions are loaded or reloaded.
     *
     * @param tiers ordered, immutable list of tiers
     */
    public record TiersUpdatedEvent(List<Tier> tiers) {}

    /**
     * Fired when tier data is loaded or reloaded.
     */
    public static final EventObservableEx<TiersUpdatedEvent> TIERS_UPDATED = new EventObservableEx<>();

    /**
     * Fired after species tier data has been applied to Cobblemon species.
     */
    public record SpeciesTiersAppliedEvent() {}

    /**
     * Observable fired once species tier data has been fully applied.
     */
    public static final EventObservableEx<SpeciesTiersAppliedEvent> SPECIES_TIERS_APPLIED =
            new EventObservableEx<>();

}
