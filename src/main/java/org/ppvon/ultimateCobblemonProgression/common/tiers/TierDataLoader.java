// src/main/java/.../tiers/TierDataLoader.java
package org.ppvon.ultimateCobblemonProgression.common.tiers;

import com.google.gson.*;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.ppvon.ultimateCobblemonProgression.UltimateCobblemonProgression;

import java.util.Map;

import static org.ppvon.ultimateCobblemonProgression.UltimateCobblemonProgression.MOD_ID;

public final class TierDataLoader extends SimpleJsonResourceReloadListener implements IdentifiableResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setLenient().create();

    public TierDataLoader() { super(GSON, "tiers"); }

    public static void register() {
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new TierDataLoader());
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> files, ResourceManager rm, ProfilerFiller profiler) {
        TierRegistry.clear();
        int loaded = 0;

        for (var entry : files.entrySet()) {
            final ResourceLocation id = entry.getKey();
            if (!id.getNamespace().equals(MOD_ID)) continue;

            final String name = id.getPath();
            var idxOpt = TierRegistry.tryParseTierIndex(name);
            if (idxOpt.isEmpty()) {
                UltimateCobblemonProgression.LOG.warn("Ignoring {}:{} (not tier_N.json)", id.getNamespace(), id.getPath());
                continue;
            }
            final int idx = idxOpt.getAsInt();

            try {
                JsonObject obj = entry.getValue().getAsJsonObject();

                TierDef parsed = TierDef.CODEC
                        .parse(com.mojang.serialization.JsonOps.INSTANCE, obj)
                        .resultOrPartial(msg ->
                                UltimateCobblemonProgression.LOG.error("Tier parse error at {}: {}", id, msg)
                        )
                        .orElseThrow(() ->
                                new com.google.gson.JsonSyntaxException("Invalid TierDef at " + id)
                        );

                TierDef tier = new TierDef(idx, parsed.levelCap, parsed.species, parsed.requirements);

                if (tier.levelCap <= 0) {
                    UltimateCobblemonProgression.LOG.warn("Tier {} has non-positive levelCap ({}).", idx, tier.levelCap);
                }
                if (tier.species.isEmpty()) {
                    UltimateCobblemonProgression.LOG.debug("Tier {} has empty species set.", idx);
                }

                boolean replaced = TierRegistry.put(tier);
                if (replaced) {
                    UltimateCobblemonProgression.LOG.warn("Tier {} from {} replaced a previously loaded tier.", idx, id);
                }

                loaded++;
            } catch (Exception ex) {
                UltimateCobblemonProgression.LOG.error("Failed to load tier file {}: {}", id, ex.getMessage());
            }
        }

        UltimateCobblemonProgression.LOG.info("Loaded tier data: {}  ({})", loaded, TierRegistry.summary());
    }

    @Override
    public ResourceLocation getFabricId() {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, "tier_data_loader");
    }
}
