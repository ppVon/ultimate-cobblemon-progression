package org.ppvon.ucp.common.internal.levelcap;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class CandyRefundHandler {
    private static final int REFUND_WINDOW_TICKS = 20;
    private static final Map<UUID, Attempt> PENDING = new ConcurrentHashMap<>();

    private CandyRefundHandler() {}

    public static void markBlockedInteraction(ServerPlayer player, InteractionHand hand) {
        if (player == null || hand == null) {
            return;
        }

        ItemStack stack = player.getItemInHand(hand);
        if (!CandyBlockHandler.isCandy(stack)) {
            return;
        }

        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (itemId == null) {
            return;
        }

        PENDING.put(player.getUUID(), new Attempt(itemId, stack.getCount(), hand, player.server.getTickCount()));
    }

    public static void scheduleRefundCheck(ServerPlayer player, InteractionHand hand) {
        Attempt attempt = PENDING.get(player.getUUID());
        if (attempt == null || attempt.hand() != hand) {
            return;
        }

        MinecraftServer server = player.server;
        server.execute(() -> refundIfConsumed(player, hand));
    }

    private static void refundIfConsumed(ServerPlayer player, InteractionHand hand) {
        Attempt attempt = PENDING.remove(player.getUUID());
        if (attempt == null || attempt.hand() != hand) {
            return;
        }

        if (player.server.getTickCount() - attempt.tick() > REFUND_WINDOW_TICKS) {
            return;
        }

        ItemStack current = player.getItemInHand(hand);
        ResourceLocation currentId = BuiltInRegistries.ITEM.getKey(current.getItem());
        boolean consumedOne = current.isEmpty()
                ? attempt.count() == 1
                : attempt.itemId().equals(currentId) && current.getCount() == attempt.count() - 1;

        if (!consumedOne) {
            return;
        }

        Item item = BuiltInRegistries.ITEM.get(attempt.itemId());
        if (item == null) {
            return;
        }

        ItemStack refund = new ItemStack(item);
        if (!player.addItem(refund)) {
            player.drop(refund, false);
        }
        player.displayClientMessage(Component.literal("Candy refunded: target is at your Trainer cap."), true);
    }

    public static void onPlayerDisconnect(UUID uuid) {
        PENDING.remove(uuid);
    }

    private record Attempt(ResourceLocation itemId, int count, InteractionHand hand, int tick) {}
}
