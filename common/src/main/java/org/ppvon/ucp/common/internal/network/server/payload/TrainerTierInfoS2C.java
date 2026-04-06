package org.ppvon.ucp.common.internal.network.server.payload;

import com.cobblemon.mod.common.api.net.NetworkPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.ppvon.ucp.common.internal.trainer.TrainerLevelProgression;

import static org.ppvon.ucp.common.UltimateCobblemonProgression.modId;

public record TrainerTierInfoS2C(
        int tier,
        int totalTiers,
        int levelCap,
        TrainerLevelProgression.DexCounts requirements
) implements NetworkPacket<TrainerTierInfoS2C> {
    public static ResourceLocation ID = modId("net.trainer_tier_info");

    @Override
    public @NotNull ResourceLocation getId() {
        return ID;
    }

    @Override
    public void encode(@NotNull RegistryFriendlyByteBuf buf) {
        ByteBufCodecs.INT.encode(buf, tier);
        ByteBufCodecs.INT.encode(buf, totalTiers);
        ByteBufCodecs.INT.encode(buf, levelCap);
        ByteBufCodecs.INT.encode(buf, requirements.seen());
        ByteBufCodecs.INT.encode(buf, requirements.caught());
    }

    public static TrainerTierInfoS2C decode(RegistryFriendlyByteBuf buf) {
        return new TrainerTierInfoS2C(
                ByteBufCodecs.INT.decode(buf),
                ByteBufCodecs.INT.decode(buf),
                ByteBufCodecs.INT.decode(buf),
                new TrainerLevelProgression.DexCounts(
                        ByteBufCodecs.INT.decode(buf),
                        ByteBufCodecs.INT.decode(buf)
                )
        );
    }
}
