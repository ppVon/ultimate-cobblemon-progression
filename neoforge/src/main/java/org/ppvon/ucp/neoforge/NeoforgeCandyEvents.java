package org.ppvon.ucp.neoforge;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.ppvon.ucp.common.internal.levelcap.CandyBlockHandler;
import org.ppvon.ucp.common.internal.levelcap.CandyRefundHandler;

public final class NeoforgeCandyEvents {
    private NeoforgeCandyEvents() {}

    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        ItemStack heldStack = player.getItemInHand(event.getHand());
        if (!CandyBlockHandler.isCandy(heldStack) || !(event.getTarget() instanceof PokemonEntity pokemonEntity)) {
            return;
        }

        Pokemon pokemon = pokemonEntity.getPokemon();
        if (!CandyBlockHandler.shouldBlock(player, pokemon)) {
            return;
        }

        CandyRefundHandler.markBlockedInteraction(player, event.getHand());
        player.displayClientMessage(
                CandyBlockHandler.blockedMessage(pokemon, CandyBlockHandler.trainerCap(player)),
                true
        );
        event.setCancellationResult(InteractionResult.FAIL);
        event.setCanceled(true);
    }

    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        if (!CandyBlockHandler.isCandy(player.getItemInHand(event.getHand()))) {
            return;
        }

        CandyRefundHandler.scheduleRefundCheck(player, event.getHand());
    }
}
