package org.ppvon.ultimateCobblemonProgression.common.component;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ppvon.ultimateCobblemonProgression.common.tiers.TierRegistry;

public class TrainerLevelComponentImpl implements TrainerLevelComponent, AutoSyncedComponent {
    private int level = 1;
    private final Player player;
    private boolean loadedFromNbt = false;

    public TrainerLevelComponentImpl(Player p) {
        this.player = p;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void setLevel(int level) {
        int max = TierRegistry.maxTier();
        int actual = Math.clamp(level, 1, max);
        this.level = actual;
        TrainerLevelComponents.KEY.sync(this.player);
    }

    @Override
    public void readFromNbt(CompoundTag tag, HolderLookup.Provider registryLookup) {
        if (tag.contains("Level")) {
            this.level = tag.getInt("Level");
            this.loadedFromNbt = true;
        }
    }

    @Override
    public void writeToNbt(CompoundTag tag, HolderLookup.Provider registryLookup) {
        tag.putInt("Level", this.level);
    }

    public boolean loadedFromNbt() {
        return loadedFromNbt;
    }
}
