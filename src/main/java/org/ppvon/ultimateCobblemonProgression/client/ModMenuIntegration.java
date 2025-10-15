package org.ppvon.ultimateCobblemonProgression.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import org.ppvon.ultimateCobblemonProgression.client.gui.ConfigHubScreen;

public final class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ConfigHubScreen::new;
    }
}
