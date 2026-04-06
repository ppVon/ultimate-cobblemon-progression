package org.ppvon.ucp.common.internal.network.client.payload;

import com.cobblemon.mod.common.api.net.NetworkPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import static org.ppvon.ucp.common.UltimateCobblemonProgression.modId;

public record AnnounceClientPresenceC2S() implements NetworkPacket<AnnounceClientPresenceC2S> {
    public static ResourceLocation ID = modId("net.announce_client_presence");

    @Override
    public @NotNull ResourceLocation getId() {
        return ID;
    }

    @Override
    public void encode(@NotNull RegistryFriendlyByteBuf buffer) {
    }

    public static AnnounceClientPresenceC2S decode(@NotNull RegistryFriendlyByteBuf buffer) {
        return new AnnounceClientPresenceC2S();
    }
}
