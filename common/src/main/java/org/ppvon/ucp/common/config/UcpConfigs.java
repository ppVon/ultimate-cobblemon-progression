package org.ppvon.ucp.common.config;

import com.google.gson.Gson;
import org.ppvon.ucp.common.util.UltimateCobblemonProgressionGson;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public final class UcpConfigs {
    private UcpConfigs() {}
    private static Path configDir;

    private static final Gson GSON = UltimateCobblemonProgressionGson.get();

    private static volatile CommonConfig COMMON = new CommonConfig();
    private static volatile SpawnConfig SPAWN = new SpawnConfig();

    public static void init(Path dir) {
        configDir = dir.resolve("ultimate-cobblemon-progression");
    }

    public static CommonConfig common() {
        return COMMON;
    }

    public static SpawnConfig spawn() {
        return SPAWN;
    }

    public static void load() {
        if (configDir == null) {
            throw new IllegalStateException("Config manager not initialized");
        }

        COMMON = loadJson(
                configDir.resolve("ucp-common.json"),
                CommonConfig.class,
                new CommonConfig()
        );

        SPAWN = loadJson(
                configDir.resolve("ucp-spawn.json"),
                SpawnConfig.class,
                new SpawnConfig()
        );
    }

    private static <T> T loadJson(Path path, Class<T> type, T defaults) {
        try {
            Files.createDirectories(path.getParent());

            if (!Files.exists(path)) {
                try (Writer w = Files.newBufferedWriter(path)) {
                    GSON.toJson(defaults, w);
                }
                return defaults;
            }

            try (Reader r = Files.newBufferedReader(path)) {
                T parsed = GSON.fromJson(r, type);
                return (parsed != null) ? parsed : defaults;
            }
        } catch (Exception e) {
            return defaults;
        }
    }

    public static void reload() {
        load();
    }
}
