package org.ppvon.ucp.common.mixin.player;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import org.ppvon.ucp.common.access.trainer.TrainerLevelHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class TrainerLevelPlayerMixin implements TrainerLevelHolder {

    @Unique
    private static final String UCP_TRAINER_LEVEL_KEY = "ucp:trainer_level";

    @Unique
    private static final String UCP_INITIALIZED_KEY = "ucp:initialized";

    @Unique
    private int ucp$trainerLevel = 0; // 0 means "needs initialization" for new and migrated players

    @Unique
    private boolean ucp$initialized = false;

    @Override
    public int ucp$getTrainerLevel() {
        return this.ucp$trainerLevel;
    }

    @Override
    public void ucp$setTrainerLevel(int level) {
        this.ucp$trainerLevel = level;
    }

    @Override
    public boolean ucp$isTrainerLevelInitialized() {
        return this.ucp$initialized;
    }

    @Override
    public void ucp$setTrainerLevelInitialized(boolean initialized) {
        this.ucp$initialized = initialized;
    }

    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    private void ucp$saveTrainerLevel(CompoundTag tag, CallbackInfo ci) {
        tag.putInt(UCP_TRAINER_LEVEL_KEY, this.ucp$trainerLevel);
        tag.putBoolean(UCP_INITIALIZED_KEY, this.ucp$initialized);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    private void ucp$loadTrainerLevel(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains(UCP_TRAINER_LEVEL_KEY, Tag.TAG_INT)) {
            this.ucp$trainerLevel = tag.getInt(UCP_TRAINER_LEVEL_KEY);
        }
        this.ucp$initialized = tag.contains(UCP_INITIALIZED_KEY, Tag.TAG_BYTE)
                && tag.getBoolean(UCP_INITIALIZED_KEY);
    }
}
