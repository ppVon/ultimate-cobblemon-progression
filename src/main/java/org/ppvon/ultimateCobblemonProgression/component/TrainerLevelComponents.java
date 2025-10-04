package org.ppvon.ultimateCobblemonProgression.component;

import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistryV3;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.ppvon.ultimateCobblemonProgression.UltimateCobblemonProgression;

public final class TrainerLevelComponents implements EntityComponentInitializer {
    public static final ComponentKey<TrainerLevelComponent> KEY =
            ComponentRegistryV3.INSTANCE.getOrCreate(
                    ResourceLocation.fromNamespaceAndPath(UltimateCobblemonProgression.MOD_ID, "trainer_level"),
                    TrainerLevelComponent.class
            );

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        try {
            registry.registerForPlayers(
                    KEY,
                    (Player p) -> new TrainerLevelComponentImpl(p),
                    RespawnCopyStrategy.CHARACTER
            );
        } catch (Throwable t) {
            throw t;
        }
    }
}