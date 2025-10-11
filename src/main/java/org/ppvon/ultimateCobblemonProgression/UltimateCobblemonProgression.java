package org.ppvon.ultimateCobblemonProgression;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.ppvon.ultimateCobblemonProgression.common.component.TrainerLevelComponentImpl;
import org.ppvon.ultimateCobblemonProgression.common.component.TrainerLevelComponents;

import org.ppvon.ultimateCobblemonProgression.common.gym.RadGymsGymGenerateTeamListener;
import org.ppvon.ultimateCobblemonProgression.common.gym.RadGymsGymRewardsListener;
import org.ppvon.ultimateCobblemonProgression.common.tiers.TierRegistry;
import org.ppvon.ultimateCobblemonProgression.config.CommonConfig;
import org.ppvon.ultimateCobblemonProgression.common.influence.TrainerLevelInfluenceRegistrar;
import org.ppvon.ultimateCobblemonProgression.common.levelcap.CandyEntityBlock;
import org.ppvon.ultimateCobblemonProgression.common.levelcap.CandyRefund;
import org.ppvon.ultimateCobblemonProgression.common.levelcap.ExpCapListener;
import org.ppvon.ultimateCobblemonProgression.common.tiers.TierDataLoader;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UltimateCobblemonProgression implements ModInitializer {
    public static final String MOD_ID = "ultimate-cobblemon-progression";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        CommonConfig.init();

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            var player = handler.player;
            var comp = TrainerLevelComponents.KEY.get(player);

            if(comp instanceof TrainerLevelComponentImpl impl && !impl.loadedFromNbt()) {
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
                                int v = TrainerLevelComponents.KEY.get(p).getLevel();
                                ctx.getSource().sendSuccess(() -> Component.literal("Trainer Level: " + v), false);
                                return 1;
                            })
                    )
                    .then(Commands.literal("set")
                            .then(Commands.argument("value", IntegerArgumentType.integer(0, 999))
                                    .executes(ctx -> {
                                        ServerPlayer p = ctx.getSource().getPlayerOrException();
                                        int v = IntegerArgumentType.getInteger(ctx, "value");
                                        TrainerLevelComponents.KEY.get(p).setLevel(v);
                                        int newLevel = TrainerLevelComponents.KEY.get(p).getLevel();
                                        ctx.getSource().sendSuccess(() -> Component.literal("Set Trainer Level to " + newLevel), true);
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

        CandyEntityBlock.register();
        CandyRefund.register();
        ExpCapListener.register();


        RadGymsGymRewardsListener.register();
        RadGymsGymGenerateTeamListener.register();
        LOG.info("Done Loading");
    }
}