package org.ppvon.ucp.common.internal.network.client.payload;

import com.cobblemon.mod.common.api.net.NetworkPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import static org.ppvon.ucp.common.UltimateCobblemonProgression.modId;

public record PokedexOpenC2S() implements NetworkPacket<PokedexOpenC2S> {
    public static ResourceLocation ID = modId("net.pokedex_open");

    @Override
    public @NotNull ResourceLocation getId() {
        return ID;
    }

    @Override
    public void encode(@NotNull RegistryFriendlyByteBuf buffer) {}

    public static PokedexOpenC2S decode(@NotNull RegistryFriendlyByteBuf buffer) {
        return new PokedexOpenC2S();
    }
}
