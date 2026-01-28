package org.ppvon.ucp.common.api.cobblemon.spawning;

import com.cobblemon.mod.common.api.spawning.position.SpawnablePosition;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public final class PlayerResolver {
    public static ServerPlayer resolvePlayer(SpawnablePosition ctx) {
        var cause = ctx.getCause();

        Entity e = cause.getEntity();
        if (e instanceof ServerPlayer sp) return sp;
        if (e instanceof net.minecraft.world.entity.projectile.Projectile p && p.getOwner() instanceof ServerPlayer sp2) return sp2;
        if (e instanceof net.minecraft.world.entity.TamableAnimal t && t.getOwner() instanceof ServerPlayer sp3) return sp3;

        ServerLevel level = ctx.getWorld();
        BlockPos origin = ctx.getPosition();
        return (ServerPlayer) level.getNearestPlayer(origin.getX() + 0.5, origin.getY() + 0.5, origin.getZ() + 0.5, 128.0, false);
    }
}
