package org.ppvon.ucp.common.api.tiers.requirements;

public final class TierRequirements {
    public final TierRequirementsDex dex;

    public static final TierRequirements NONE = new TierRequirements(TierRequirementsDex.NONE);

    public TierRequirements(TierRequirementsDex dex) {
        this.dex = dex != null ? dex : TierRequirementsDex.NONE;
    }

    public boolean hasAny() {
        return dex.hasAny();
    }
}
