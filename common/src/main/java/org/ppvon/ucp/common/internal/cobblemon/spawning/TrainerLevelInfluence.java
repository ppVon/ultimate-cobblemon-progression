package org.ppvon.ucp.common.internal.cobblemon.spawning;

import com.cobblemon.mod.common.api.spawning.detail.SpawnAction;
import com.cobblemon.mod.common.api.spawning.detail.SpawnDetail;
import com.cobblemon.mod.common.api.spawning.influence.SpawningInfluence;
import com.cobblemon.mod.common.api.spawning.position.SpawnablePosition;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.ppvon.ucp.common.api.cobblemon.spawning.influence.LevelScaling;
import org.ppvon.ucp.common.api.cobblemon.spawning.influence.SpeciesBlocking;
import org.ppvon.ucp.common.api.cobblemon.spawning.influence.WeightScaling;

public class TrainerLevelInfluence implements SpawningInfluence {
    @Override
    public boolean affectSpawnable(@NotNull SpawnDetail spawnDetail, @NotNull SpawnablePosition spawningContext) {
        return SpeciesBlocking.allowSpecies(spawnDetail, spawningContext);
    }

    @Override
    public float affectWeight(@NotNull SpawnDetail spawnDetail,
                              @NotNull SpawnablePosition ctx,
                              float currentWeight) {
        return WeightScaling.affectWeight(spawnDetail, ctx, currentWeight);
    }

    @Override
    public void affectSpawn(@NotNull SpawnAction<?> action, @NotNull Entity entity) {
        LevelScaling.affectSpawn(action, entity);
    }
}
