package org.ppvon.ucp.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.function.Consumer;

public final class UltimateCobblemonProgressionGson {
    private UltimateCobblemonProgressionGson() {}

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static Gson get() {
        return GSON;
    }
}
