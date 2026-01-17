package org.ppvon.ultimateCobblemonProgression;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.ppvon.ultimateCobblemonProgression.common.component.TrainerLevelComponentImpl;
import org.ppvon.ultimateCobblemonProgression.common.component.TrainerLevelComponents;
import org.ppvon.ultimateCobblemonProgression.common.progression.ProgressionManager;
import org.ppvon.ultimateCobblemonProgression.common.progression.dex.DexProgressionApi;
import org.ppvon.ultimateCobblemonProgression.common.tiers.TierRegistry;
import org.ppvon.ultimateCobblemonProgression.common.tiers.TierSpeciesApplier;
import org.ppvon.ultimateCobblemonProgression.common.influence.TrainerLevelInfluenceRegistrar;
import org.ppvon.ultimateCobblemonProgression.common.levelcap.CandyEntityBlock;
import org.ppvon.ultimateCobblemonProgression.common.levelcap.CandyRefund;
import org.ppvon.ultimateCobblemonProgression.common.levelcap.ExpCapListener;
import org.ppvon.ultimateCobblemonProgression.common.tiers.TierDataLoader;
import org.ppvon.ultimateCobblemonProgression.config.ConfigLoader;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UltimateCobblemonProgression implements ModInitializer {
    public static final String MOD_ID = "ultimate-cobblemon-progression";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {

        ConfigLoader.init();

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            var player = handler.player;
            var comp = TrainerLevelComponents.KEY.get(player);

            if (comp instanceof TrainerLevelComponentImpl impl && !impl.loadedFromNbt()) {
                impl.setLevel(1);
            }
            TrainerLevelComponents.KEY.sync(player);
        });

        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            TrainerLevelComponents.KEY.sync(newPlayer);
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, env) -> {
            dispatcher.register(Commands.literal("trainerlevel")
                    .then(Commands.literal("get")
                            .executes(ctx -> {
                                ServerPlayer p = ctx.getSource().getPlayerOrException();

                                int currentLevel = TrainerLevelComponents.KEY.get(p).getLevel();
                                var nextTierOpt = TierRegistry.get(currentLevel + 1);

                                if (nextTierOpt.isEmpty()) {
                                    ctx.getSource().sendSuccess(
                                            () -> Component.literal("Trainer Level: " + currentLevel + " (MAX)"),
                                            false
                                    );
                                    return 1;
                                }

                                var nextTier = nextTierOpt.get();
                                var dexReq = nextTier.requirements.dex;

                                DexProgressionApi.DexCounts counts = DexProgressionApi.get(p);
                                int seenCount = counts.seen();
                                int caughtCount = counts.caught();

                                boolean requireSeen = ConfigLoader.REQUIRE_DEX_SEEN.get();
                                boolean requireCaught = ConfigLoader.REQUIRE_DEX_CAUGHT.get();

                                StringBuilder message = new StringBuilder();

                                message.append("Trainer Level: ")
                                        .append(currentLevel)
                                        .append("\n\n");

                                message.append("Next Level: ")
                                        .append(currentLevel + 1)
                                        .append(" (Level cap ")
                                        .append(nextTier.levelCap)
                                        .append(")\n");

                                if (requireSeen || requireCaught) {
                                    message.append("Pokédex Progress:\n");

                                    if (requireSeen) {
                                        message.append("• Seen:   ")
                                                .append(seenCount)
                                                .append(" / ")
                                                .append(dexReq.seen)
                                                .append("\n");
                                    }

                                    if (requireCaught) {
                                        message.append("• Caught: ")
                                                .append(caughtCount)
                                                .append(" / ")
                                                .append(dexReq.caught)
                                                .append("\n");
                                    }
                                }

                                ctx.getSource().sendSuccess(
                                        () -> Component.literal(message.toString()),
                                        false
                                );
                                return 1;
                            })
                    )
                    .then(Commands.literal("set")
                            .requires(src -> src.hasPermission(2))
                            .then(Commands.argument("player", EntityArgument.player())
                                    .then(Commands.argument("value", IntegerArgumentType.integer(0, 999))
                                            .executes(ctx -> {
                                                ServerPlayer target = EntityArgument.getPlayer(ctx, "player");
                                                int v = IntegerArgumentType.getInteger(ctx, "value");

                                                TrainerLevelComponents.KEY.get(target).setLevel(v);
                                                int newLevel = TrainerLevelComponents.KEY.get(target).getLevel();

                                                ctx.getSource().sendSuccess(
                                                        () -> Component.literal(
                                                                "Set " + target.getGameProfile().getName()
                                                                        + "'s Trainer Level to " + newLevel
                                                        ),
                                                        true
                                                );

                                                target.sendSystemMessage(
                                                        Component.literal("Your Trainer Level was set to " + newLevel)
                                                );

                                                return 1;
                                            })
                                    )
                            )
                    )
                    .then(Commands.literal("promote")
                            .requires(src -> src.hasPermission(2))
                            .then(Commands.argument("player", EntityArgument.player())
                                    .executes(ctx -> {
                                        CommandSourceStack src = ctx.getSource();
                                        ServerPlayer target = EntityArgument.getPlayer(ctx, "player");

                                        int current = TrainerLevelComponents.KEY.get(target).getLevel();
                                        int next = current + 1;

                                        if (TierRegistry.get(next).isEmpty()) {
                                            src.sendSuccess(
                                                    () -> Component.literal(
                                                            target.getGameProfile().getName()
                                                                    + " is already at the maximum Trainer Level ("
                                                                    + current + ")"
                                                    ),
                                                    false
                                            );
                                            return 1;
                                        }

                                        TrainerLevelComponents.KEY.get(target).setLevel(next);

                                        src.sendSuccess(
                                                () -> Component.literal(
                                                        "Promoted " + target.getGameProfile().getName()
                                                                + " to Trainer Level " + next
                                                ),
                                                true
                                        );

                                        target.sendSystemMessage(
                                                ProgressionManager.buildChatMessage(target, next)
                                        );

                                        return 1;
                                    })
                            )
                    )
            );
        });

        LOG.info("Trainer level commands registered");
        TrainerLevelInfluenceRegistrar.registerOnce();
        LOG.info("Trainer level influence registered");

        TierDataLoader.register();

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            TierSpeciesApplier.applyFromRegistry();
        });

        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, rm, success) -> {
            if (success) {
                TierSpeciesApplier.applyFromRegistry();
            }
        });

        CandyEntityBlock.register();
        CandyRefund.register();
        ExpCapListener.register();
        ProgressionManager.register();

        LOG.info("Done Loading");
    }
}