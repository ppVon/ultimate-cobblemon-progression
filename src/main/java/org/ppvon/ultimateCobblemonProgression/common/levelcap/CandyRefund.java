package org.ppvon.ultimateCobblemonProgression.common.levelcap;

import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public final class CandyRefund {
    private static final Set<ResourceLocation> CANDIES = Set.of(
            ResourceLocation.fromNamespaceAndPath("cobblemon", "exp_candy_xs"),
            ResourceLocation.fromNamespaceAndPath("cobblemon", "exp_candy_s"),
            ResourceLocation.fromNamespaceAndPath("cobblemon", "exp_candy_m"),
            ResourceLocation.fromNamespaceAndPath("cobblemon", "exp_candy_l"),
            ResourceLocation.fromNamespaceAndPath("cobblemon", "exp_candy_xl"),
            ResourceLocation.fromNamespaceAndPath("cobblemon", "rare_candy")
    );

    static final class Attempt {
        final ResourceLocation itemId;
        final int startCount;
        final int startTick;
        Attempt(ResourceLocation id, int c, int t){ itemId=id; startCount=c; startTick=t; }
    }
    private static final Map<UUID, Attempt> recent = new HashMap<>();

    public static void register() {
        UseItemCallback.EVENT.register((player, level, hand) -> {
            ItemStack stack = player.getItemInHand(hand);

            if (!(player instanceof ServerPlayer sp) || hand != InteractionHand.MAIN_HAND) {
                return InteractionResultHolder.pass(stack);
            }

            ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
            if (id == null || !CANDIES.contains(id)) {
                return InteractionResultHolder.pass(stack);
            }

            recent.put(sp.getUUID(), new Attempt(id, stack.getCount(), level.getServer().getTickCount()));

            return InteractionResultHolder.pass(stack);
        });
    }

    public static void maybeRefund(ServerPlayer sp) {
        Attempt a = recent.get(sp.getUUID());
        if (a == null) return;

        int now = sp.server.getTickCount();
        if (now - a.startTick > 60) {
            recent.remove(sp.getUUID());
            return;
        }

        ItemStack cur = sp.getMainHandItem();
        ResourceLocation curId = BuiltInRegistries.ITEM.getKey(cur.getItem());
        boolean consumedOne = (curId != null && curId.equals(a.itemId) && cur.getCount() == Math.max(0, a.startCount - 1))
                || (curId == null && a.startCount == 1);

        if (consumedOne) {
            Item candy = BuiltInRegistries.ITEM.get(a.itemId);
            if (candy != null) {
                ItemStack refund = new ItemStack(candy, 1);
                if (!sp.addItem(refund)) sp.drop(refund, false);
                sp.displayClientMessage(Component.literal("Candy refunded: target is at your Trainer cap."), true);
            }
        }
        recent.remove(sp.getUUID());
    }
}
