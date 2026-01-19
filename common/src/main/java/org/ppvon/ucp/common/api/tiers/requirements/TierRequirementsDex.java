package org.ppvon.ucp.common.api.tiers.requirements;

public final class TierRequirementsDex {
    public final int seen;
    public final int caught;

    public static final TierRequirementsDex NONE = new TierRequirementsDex(0, 0);

    public TierRequirementsDex(int seen, int caught) {
        this.seen = seen;
        this.caught = caught;
    }

    public boolean hasAny() {
        return seen > 0 || caught > 0;
    }
}
