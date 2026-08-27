package org.ppvon.ucp.common.internal.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.ppvon.ucp.common.api.trainer.TrainerLevels;
import org.ppvon.ucp.common.api.tiers.Tier;
import org.ppvon.ucp.common.internal.progression.DexProgressionHandler;
import org.ppvon.ucp.common.internal.trainer.TrainerLevelProgression;

public final class TrainerLevelCommands {
    private TrainerLevelCommands() {}

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("trainerlevel")
                .then(Commands.literal("get")
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            context.getSource().sendSuccess(() -> TrainerLevelProgression.buildStatusMessage(player), false);
                            return 1;
                        }))
                .then(Commands.literal("set")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.argument("level", IntegerArgumentType.integer(1, 999))
                                        .executes(context -> {
                                            ServerPlayer target = EntityArgument.getPlayer(context, "player");
                                            int requestedLevel = IntegerArgumentType.getInteger(context, "level");
                                            TrainerLevels.set(target, requestedLevel);
                                            int newLevel = TrainerLevels.get(target);
                                            DexProgressionHandler.notifyClient(target);
                                            String clampNote = newLevel != requestedLevel
                                                    ? " (clamped from " + requestedLevel + "; max tier is " + newLevel + ")"
                                                    : "";

                                            context.getSource().sendSuccess(
                                                    () -> Component.literal("Set " + target.getGameProfile().getName()
                                                            + "'s Trainer Level to " + newLevel + clampNote),
                                                    true
                                            );
                                            target.sendSystemMessage(Component.literal("Your Trainer Level was set to " + newLevel));
                                            return 1;
                                        }))))
                .then(Commands.literal("promote")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes(context -> {
                                    CommandSourceStack source = context.getSource();
                                    ServerPlayer target = EntityArgument.getPlayer(context, "player");
                                    int currentLevel = TrainerLevels.get(target);
                                    Tier nextTier = TrainerLevelProgression.nextTier(currentLevel).orElse(null);

                                    if (nextTier == null) {
                                        source.sendSuccess(
                                                () -> Component.literal(target.getGameProfile().getName()
                                                        + " is already at the maximum Trainer Level (" + currentLevel + ")"),
                                                false
                                        );
                                        return 1;
                                    }

                                    TrainerLevels.set(target, nextTier.index);
                                    int newLevel = TrainerLevels.get(target);
                                    DexProgressionHandler.notifyClient(target);

                                    source.sendSuccess(
                                            () -> Component.literal("Promoted " + target.getGameProfile().getName()
                                                    + " to Trainer Level " + newLevel),
                                            true
                                    );
                                    target.sendSystemMessage(TrainerLevelProgression.buildPromotionMessage(newLevel));
                                    return 1;
                                }))));
    }
}
