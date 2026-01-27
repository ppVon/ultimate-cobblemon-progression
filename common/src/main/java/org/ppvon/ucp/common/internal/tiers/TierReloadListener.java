package org.ppvon.ucp.common.internal.tiers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.ppvon.ucp.common.UltimateCobblemonProgression;
import org.ppvon.ucp.common.api.tiers.Tier;
import org.slf4j.Logger;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TierReloadListener extends SimpleJsonResourceReloadListener {
    private static final Logger LOGGER = UltimateCobblemonProgression.LOGGER;

    private static final String DIRECTORY = "tiers";
    private static final Pattern INDEX_PATTERN = Pattern.compile("^(?:tier_)?(\\d+)$");

    private static final ResourceLocation MANIFEST_ID =
            ResourceLocation.fromNamespaceAndPath(UltimateCobblemonProgression.MOD_ID, "manifest");


    public TierReloadListener(Gson gson) {
        super(gson, DIRECTORY);
    }

    @Override
    protected void apply(
            Map<ResourceLocation, JsonElement> jsons,
            ResourceManager resourceManager,
            ProfilerFiller profilerFiller
    ) {
        TierManifestData.Manifest manifest = TierManifestData.DEFAULT;

        JsonElement manifestJson = jsons.get(MANIFEST_ID);
        if (manifestJson != null) {
            var parsedManifest = TierManifestData.CODEC.parse(JsonOps.INSTANCE, manifestJson)
                    .resultOrPartial(msg -> LOGGER.warn("Failed parsing tier manifest {}: {}", MANIFEST_ID, msg));

            manifest = parsedManifest.orElse(TierManifestData.DEFAULT);
        }

        Int2ObjectOpenHashMap<Tier> allLoaded = new Int2ObjectOpenHashMap<>();

        for (var entry : jsons.entrySet()) {
            ResourceLocation fileId = entry.getKey();
            if (fileId.equals(MANIFEST_ID)) continue;

            Integer index = tryParseIndex(fileId);
            if (index == null) {
                LOGGER.warn("Ignoring unrecognized tiers file: {}", fileId);
                continue;
            }

            JsonElement json = entry.getValue();

            var parsedTier = TierData.CODEC.parse(JsonOps.INSTANCE, json)
                    .resultOrPartial(msg -> LOGGER.warn("Failed parsing tier {}: {}", fileId, msg));

            if (parsedTier.isEmpty()) continue;

            Tier tier = parsedTier.get().withIndex(index);

            Tier prev = allLoaded.get(index);
            allLoaded.put(index, tier);

            if (prev != null) {
                LOGGER.warn(
                        "Tier index {} defined multiple times; latest wins.",
                        index
                );
            }
        }

        Int2ObjectOpenHashMap<Tier> finalLoaded;

        if (manifest.includeDefaults) {
            finalLoaded = allLoaded;
        } else {
            finalLoaded = new Int2ObjectOpenHashMap<>();

            Set<Integer> requested = new HashSet<>(manifest.tiers);
            if (requested.isEmpty()) {
                LOGGER.warn("Tier manifest has includeDefaults=false but no tiers were listed. Result: 0 tiers loaded.");
            }

            for (int idx : requested) {
                Tier tier = allLoaded.get(idx);
                if (tier == null) {
                    LOGGER.warn("Tier manifest requested tier index {} but no tier file was found for it.", idx);
                    continue;
                }
                finalLoaded.put(idx, tier);
            }
        }

        TierLoadCoordinator.stage(finalLoaded);

        LOGGER.info(
                "Loaded {} tier definition(s). includeDefaults={}, manifestPresent={}",
                finalLoaded.size(),
                manifest.includeDefaults,
                manifestJson != null
        );
    }

    private static Integer tryParseIndex(ResourceLocation fileId) {
        String name = fileId.getPath();

        Matcher m = INDEX_PATTERN.matcher(name);
        if (!m.matches()) return null;

        try {
            return Integer.parseInt(m.group(1));
        } catch (NumberFormatException ignored) {
            return null;
        }
    }
}
