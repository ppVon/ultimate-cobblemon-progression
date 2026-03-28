package org.ppvon.ucp.common.internal.levelcap;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.pokemon.interaction.ExperienceCandyUseEvent;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.ppvon.ucp.common.api.trainer.TrainerLevels;
import org.ppvon.ucp.common.api.tiers.TierRegistry;
import org.ppvon.ucp.common.config.UcpConfigs;

import java.util.Set;

public final class CandyBlockHandler {
    private static final Set<ResourceLocation> CANDIES = Set.of(
            id("cobblemon", "exp_candy_xs"),
            id("cobblemon", "exp_candy_s"),
            id("cobblemon", "exp_candy_m"),
            id("cobblemon", "exp_candy_l"),
            id("cobblemon", "exp_candy_xl"),
            id("cobblemon", "rare_candy")
    );

    private static boolean initialized;

    private CandyBlockHandler() {}

    public static void init() {
        if (initialized) {
            return;
        }
        initialized = true;
        CobblemonEvents.EXPERIENCE_CANDY_USE_PRE.subscribe(Priority.HIGHEST, CandyBlockHandler::onCandyUsePre);
    }

    public static boolean isCandy(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return false;
        }
        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return itemId != null && CANDIES.contains(itemId);
    }

    public static boolean isAtOrAboveCap(Pokemon pokemon, int trainerLevel) {
        int cap = TierRegistry.getLevelCap(trainerLevel);
        return cap > 0 && pokemon.getLevel() >= cap;
    }

    public static boolean shouldBlock(ServerPlayer player, Pokemon pokemon) {
        if (!UcpConfigs.common().doLevelCap || player == null || pokemon == null) {
            return false;
        }
        return isAtOrAboveCap(pokemon, TrainerLevels.get(player));
    }

    public static int trainerCap(ServerPlayer player) {
        return TierRegistry.getLevelCap(TrainerLevels.get(player));
    }

    public static Component blockedMessage(Pokemon pokemon, int cap) {
        return Component.literal(pokemon.getDisplayName(false).getString()
                + " is at your Trainer cap (" + cap + "). Candy blocked.");
    }

    private static void onCandyUsePre(ExperienceCandyUseEvent.Pre event) {
        ServerPlayer player = event.getPlayer();
        Pokemon pokemon = event.getPokemon();

        if (!shouldBlock(player, pokemon)) {
            return;
        }

        event.cancel();
        player.displayClientMessage(blockedMessage(pokemon, trainerCap(player)), true);
    }

    private static ResourceLocation id(String namespace, String path) {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }
}
