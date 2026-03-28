package org.ppvon.ucp.fabric;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ItemStack;
import org.ppvon.ucp.common.internal.levelcap.CandyBlockHandler;
import org.ppvon.ucp.common.internal.levelcap.CandyRefundHandler;

public final class FabricCandyEvents {
    private FabricCandyEvents() {}

    public static void register() {
        UseEntityCallback.EVENT.register((player, level, hand, entity, hitResult) -> {
            if (!(player instanceof ServerPlayer serverPlayer)) {
                return InteractionResult.PASS;
            }

            ItemStack heldStack = serverPlayer.getItemInHand(hand);
            if (!CandyBlockHandler.isCandy(heldStack) || !(entity instanceof PokemonEntity pokemonEntity)) {
                return InteractionResult.PASS;
            }

            Pokemon pokemon = pokemonEntity.getPokemon();
            if (!CandyBlockHandler.shouldBlock(serverPlayer, pokemon)) {
                return InteractionResult.PASS;
            }

            CandyRefundHandler.markBlockedInteraction(serverPlayer, hand);
            serverPlayer.displayClientMessage(
                    CandyBlockHandler.blockedMessage(pokemon, CandyBlockHandler.trainerCap(serverPlayer)),
                    true
            );
            return InteractionResult.FAIL;
        });

        UseItemCallback.EVENT.register((player, level, hand) -> {
            ItemStack stack = player.getItemInHand(hand);
            if (!(player instanceof ServerPlayer serverPlayer) || !CandyBlockHandler.isCandy(stack)) {
                return InteractionResultHolder.pass(stack);
            }

            CandyRefundHandler.scheduleRefundCheck(serverPlayer, hand);
            return InteractionResultHolder.pass(stack);
        });
    }
}
