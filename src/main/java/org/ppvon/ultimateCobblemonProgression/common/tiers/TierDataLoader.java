// src/main/java/.../tiers/TierDataLoader.java
package org.ppvon.ultimateCobblemonProgression.common.tiers;

import com.google.gson.*;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.ppvon.ultimateCobblemonProgression.UltimateCobblemonProgression;

import java.util.*;

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
            ResourceLocation id = entry.getKey();
            if (!id.getNamespace().equals(MOD_ID)) continue;

            var name = id.getPath();
            var idxOpt = TierRegistry.tryParseTierIndex(name);
            if (idxOpt.isEmpty()) {
                UltimateCobblemonProgression.LOG.warn("Ignoring {}:{} (not tier_N.json)", id.getNamespace(), id.getPath());
                continue;
            }
            int idx = idxOpt.getAsInt();

            JsonObject obj = entry.getValue().getAsJsonObject();

            int levelCap = obj.has("level_cap") ? obj.get("level_cap").getAsInt() : 0;

            Set<ResourceLocation> species = new HashSet<>();
            if (obj.has("species")) {
                for (JsonElement e : obj.getAsJsonArray("species")) {
                    ResourceLocation sid = ResourceLocation.parse(e.getAsString());
                    species.add(sid);
                }
            }

            TierRegistry.put(new TierDef(idx, levelCap, species));
            loaded++;
        }

        UltimateCobblemonProgression.LOG.info("Loaded tier data: {}  ({})", loaded, TierRegistry.summary());
    }

    @Override
    public ResourceLocation getFabricId() {
        return null;
    }
}
