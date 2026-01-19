package org.ppvon.ucp.common.api.tiers;

import org.ppvon.ucp.common.api.tiers.requirements.TierRequirements;
import org.ppvon.ucp.common.internal.tiers.TierManager;

import java.util.List;
import java.util.Optional;

/**
 * Public, read-only access to UCP tier definitions loaded from datapacks.
 * <p>
 * Tier data is loaded from server datapacks (e.g. {@code /reload}) and is stored internally in an
 * immutable snapshot. All values returned by this class are derived from that snapshot.
 * <p>
 * <b>Important:</b> Tier indices are defined by datapack file names (for example {@code tier_1.json}
 * or {@code 1.json}). Indices may be non-contiguous if a datapack author skips a number.
 * You should not assume {@code 0..count()-1} exists unless you enforce that convention yourself.
 * <p>
 */
public final class TierRegistry {
    private TierRegistry() {}

    /**
     * Returns the {@link Tier} for a given tier index, or {@code null} if no tier exists at that index.
     * <p>
     * Prefer {@link #getOptional(int)} if you want to avoid null-handling.
     *
     * @param index the tier index (as derived from the tier datapack file name)
     * @return the tier definition, or {@code null} if not present
     */
    public static Tier get(int index) {
        return TierManager.getTier(index);
    }

    /**
     * Returns the {@link Tier} for a given tier index.
     *
     * @param index the tier index (as derived from the tier datapack file name)
     * @return an {@link Optional} containing the tier if present, otherwise {@link Optional#empty()}
     */
    public static Optional<Tier> getOptional(int index) {
        return Optional.ofNullable(get(index));
    }

    /**
     * Returns an immutable list of all currently-loaded tiers in ascending {@link Tier#index} order.
     * <p>
     * The returned list is safe to store and iterate. It will not change when datapacks reload;
     * instead, a new immutable list will replace the underlying snapshot.
     *
     * @return immutable ordered list of tiers
     */
    public static List<Tier> ordered() {
        return TierManager.getOrdered();
    }

    /**
     * Returns the number of tiers currently loaded.
     * <p>
     * This is the number of tier definitions present in the datapack set, and is not necessarily
     * {@code maxIndex + 1} if indices have gaps.
     *
     * @return number of loaded tiers
     */
    public static int count() {
        return TierManager.snapshot().ordered().size();
    }

    /**
     * Returns the highest tier index currently loaded, or {@code -1} if no tiers are loaded.
     * <p>
     * This is a convenience method for quickly determining the "top-most" defined tier index.
     * It does not guarantee contiguity.
     *
     * @return maximum loaded tier index, or {@code -1} if none exist
     */
    public static int maxIndex() {
        List<Tier> list = ordered();
        return list.isEmpty() ? -1 : list.get(list.size() - 1).index;
    }

    /**
     * Returns {@code true} if a tier exists for the given index.
     *
     * @param index the tier index
     * @return {@code true} if present, otherwise {@code false}
     */
    public static boolean has(int index) {
        return get(index) != null;
    }

    /**
     * Returns the next tier after {@code currentIndex}, if it exists.
     * <p>
     * This is purely {@code index + 1}. It does not skip gaps. If your datapacks define tiers
     * with missing indices, you may prefer to use {@link #ordered()} and walk the list yourself.
     *
     * @param currentIndex the current tier index
     * @return {@link Optional} containing the next tier if present
     */
    public static Optional<Tier> next(int currentIndex) {
        return getOptional(currentIndex + 1);
    }

    /**
     * Returns the previous tier before {@code currentIndex}, if it exists.
     * <p>
     * This is purely {@code index - 1}. It does not skip gaps. If your datapacks define tiers
     * with missing indices, you may prefer to use {@link #ordered()} and walk the list yourself.
     *
     * @param currentIndex the current tier index
     * @return {@link Optional} containing the previous tier if present
     */
    public static Optional<Tier> previous(int currentIndex) {
        return getOptional(currentIndex - 1);
    }

    /**
     * Returns the level cap for the given tier index.
     * <p>
     * If the tier does not exist, this returns {@code -1}.
     *
     * @param index the tier index
     * @return the tier's level cap, or {@code -1} if the tier is not present
     */
    public static int getLevelCap(int index) {
        Tier tier = get(index);
        return tier != null ? tier.levelCap : -1;
    }

    /**
     * Returns the requirements for the given tier index.
     * <p>
     * If the tier does not exist, this returns {@link TierRequirements#NONE}.
     *
     * @param index the tier index
     * @return the tier requirements, or {@link TierRequirements#NONE} if the tier is not present
     */
    public static TierRequirements getRequirements(int index) {
        Tier tier = get(index);
        return tier != null ? tier.requirements : TierRequirements.NONE;
    }
}
