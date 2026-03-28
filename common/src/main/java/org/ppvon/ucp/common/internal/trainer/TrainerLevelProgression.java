package org.ppvon.ucp.common.internal.trainer;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.pokedex.AbstractPokedexManager;
import com.cobblemon.mod.common.api.pokedex.CaughtCount;
import com.cobblemon.mod.common.api.pokedex.SeenCount;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import org.ppvon.ucp.common.api.trainer.TrainerLevels;
import org.ppvon.ucp.common.api.tiers.Tier;
import org.ppvon.ucp.common.api.tiers.TierRegistry;
import org.ppvon.ucp.common.api.tiers.requirements.TierRequirementsDex;
import org.ppvon.ucp.common.config.UcpConfigs;

import java.util.List;
import java.util.Optional;

public final class TrainerLevelProgression {
    private TrainerLevelProgression() {}

    public static DexCounts getDexCounts(ServerPlayer player) {
        if (player == null) {
            return DexCounts.ZERO;
        }
        return getDexCounts(Cobblemon.INSTANCE.getPlayerDataManager().getPokedexData(player));
    }

    public static DexCounts getDexCounts(AbstractPokedexManager pokedexManager) {
        if (pokedexManager == null) {
            return DexCounts.ZERO;
        }

        int seen = pokedexManager.getGlobalCalculatedValue(SeenCount.INSTANCE);
        int caught = pokedexManager.getGlobalCalculatedValue(CaughtCount.INSTANCE);
        return new DexCounts(seen, caught);
    }

    public static boolean meetsRequirements(TierRequirementsDex requirements, int seen, int caught) {
        if (requirements == null) {
            return true;
        }

        boolean seenOk = requirements.seen <= 0 || seen >= requirements.seen;
        boolean caughtOk = requirements.caught <= 0 || caught >= requirements.caught;
        return seenOk && caughtOk;
    }

    public static int resolveHighestQualifyingTier(int seen, int caught) {
        List<Tier> tiers = TierRegistry.ordered();
        if (tiers.isEmpty()) {
            return 0;
        }

        for (int i = tiers.size() - 1; i >= 0; i--) {
            Tier tier = tiers.get(i);
            if (meetsRequirements(tier.requirements.dex, seen, caught)) {
                return tier.index;
            }
        }
        return 0;
    }

    public static Optional<Tier> nextTier(int currentLevel) {
        for (Tier tier : TierRegistry.ordered()) {
            if (tier.index > currentLevel) {
                return Optional.of(tier);
            }
        }
        return Optional.empty();
    }

    public static Component buildStatusMessage(ServerPlayer player) {
        int currentLevel = TrainerLevels.get(player);
        Optional<Tier> nextTier = nextTier(currentLevel);
        if (nextTier.isEmpty()) {
            return Component.literal("Trainer Level: " + currentLevel + " (MAX)")
                    .withStyle(ChatFormatting.GREEN);
        }

        Tier next = nextTier.get();
        TierRequirementsDex requirements = next.requirements.dex;

        String nextLevelLabel = UcpConfigs.common().doLevelCap
                ? "Next Level: " + next.index + " (Level cap " + next.levelCap + ")"
                : "Next Level: " + next.index;
        MutableComponent message = Component.literal("Trainer Level: " + currentLevel)
                .append(CommonComponents.NEW_LINE)
                .append(Component.literal(nextLevelLabel));

        if (!UcpConfigs.common().doDexProgression) {
            return message;
        }

        DexCounts counts = getDexCounts(player);

        if (requirements == null || !requirements.hasAny()) {
            return message.append(CommonComponents.NEW_LINE)
                    .append(Component.literal("Pokedex Progress: None").withStyle(ChatFormatting.GRAY));
        }

        message.append(CommonComponents.NEW_LINE)
                .append(Component.literal("Pokedex Progress:"));

        if (requirements.seen > 0) {
            message.append(CommonComponents.NEW_LINE)
                    .append(Component.literal(" - Seen: " + counts.seen() + " / " + requirements.seen));
        }
        if (requirements.caught > 0) {
            message.append(CommonComponents.NEW_LINE)
                    .append(Component.literal(" - Caught: " + counts.caught() + " / " + requirements.caught));
        }

        return message;
    }

    public static Component buildPromotionMessage(int newLevel) {
        Tier currentTier = TierRegistry.get(newLevel);
        if (currentTier == null) {
            return Component.literal("Trainer Level: " + newLevel).withStyle(ChatFormatting.GREEN);
        }

        MutableComponent header = Component.literal("Congrats! Your trainer level is now ")
                .withStyle(ChatFormatting.GREEN)
                .append(Component.literal(String.valueOf(newLevel)).withStyle(ChatFormatting.BOLD));

        MutableComponent message = header;

        if (UcpConfigs.common().doLevelCap) {
            message.append(CommonComponents.NEW_LINE)
                    .append(Component.literal(" - New level cap: "))
                    .append(number(currentTier.levelCap));
        }

        if (UcpConfigs.spawn().doSpeciesBlocking) {
            message.append(CommonComponents.NEW_LINE)
                    .append(Component.literal(" - " + currentTier.species.size() + " new species unlocked"));
        }

        Optional<Tier> nextTier = nextTier(newLevel);
        if (!UcpConfigs.common().doDexProgression || nextTier.isEmpty()) {
            return message;
        }

        TierRequirementsDex requirements = nextTier.get().requirements.dex;
        message.append(CommonComponents.NEW_LINE)
                .append(Component.literal("Requirements for next level:").withStyle(ChatFormatting.YELLOW));

        if (requirements == null || !requirements.hasAny()) {
            return message.append(CommonComponents.NEW_LINE)
                    .append(Component.literal(" - None").withStyle(ChatFormatting.GRAY));
        }

        if (requirements.seen > 0) {
            message.append(CommonComponents.NEW_LINE)
                    .append(Component.literal(" - Seen "))
                    .append(number(requirements.seen));
        }
        if (requirements.caught > 0) {
            message.append(CommonComponents.NEW_LINE)
                    .append(Component.literal(" - Caught "))
                    .append(number(requirements.caught));
        }

        return message;
    }

    private static Component number(int value) {
        return Component.literal(String.valueOf(value)).withStyle(ChatFormatting.AQUA);
    }

    public record DexCounts(int seen, int caught) {
        public static final DexCounts ZERO = new DexCounts(0, 0);
    }
}
