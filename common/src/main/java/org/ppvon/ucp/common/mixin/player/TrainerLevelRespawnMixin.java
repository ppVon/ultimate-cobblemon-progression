package org.ppvon.ucp.common.mixin.player;

import net.minecraft.server.level.ServerPlayer;
import org.ppvon.ucp.common.access.trainer.TrainerLevelHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Carries the trainer level across a respawn.
 *
 * <p>{@code PlayerList#respawn} discards the dying {@link ServerPlayer} and builds a fresh one
 * rather than re-reading it from disk, so the fields added by
 * {@link TrainerLevelPlayerMixin} come back at their defaults (level {@code 0}, uninitialized).
 * Only what {@code restoreFrom} copies survives, so the trainer level has to be copied here.
 *
 * <p>The copy is unconditional: {@code keepEverything} distinguishes an end-portal return from a
 * death, and the trainer level is progression, not inventory — it is never lost on death.
 */
@Mixin(ServerPlayer.class)
public abstract class TrainerLevelRespawnMixin {

    @Inject(method = "restoreFrom", at = @At("RETURN"))
    private void ucp$copyTrainerLevel(ServerPlayer oldPlayer, boolean keepEverything, CallbackInfo ci) {
        if (!(oldPlayer instanceof TrainerLevelHolder from) || !(((Object) this) instanceof TrainerLevelHolder to)) {
            return;
        }

        to.ucp$setTrainerLevel(from.ucp$getTrainerLevel());
        to.ucp$setTrainerLevelInitialized(from.ucp$isTrainerLevelInitialized());
    }
}
