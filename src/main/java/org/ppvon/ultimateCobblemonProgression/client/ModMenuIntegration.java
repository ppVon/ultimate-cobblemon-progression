package org.ppvon.ultimateCobblemonProgression.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.mehvahdjukaar.moonlight.api.platform.configs.ModConfigHolder;
import net.mehvahdjukaar.moonlight.api.platform.configs.fabric.FabricConfigListScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.ppvon.ultimateCobblemonProgression.UltimateCobblemonProgression;
import org.ppvon.ultimateCobblemonProgression.config.CommonConfig;

public final class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (Screen parent) -> {
            CommonConfig.init();
            // If your config is COMMON or COMMON_SYNCED, this will still open clientside
            // editor for that file. For client-only configs, you’d build a separate holder with ConfigType.CLIENT.
            ModConfigHolder[] holder = {
                    CommonConfig.HOLDER
            };

            ItemStack icon = new ItemStack(Items.BOOK);
            return new FabricConfigListScreen(
                    UltimateCobblemonProgression.MOD_ID,
                    icon,
                    Component.literal("UCP - Config"),
                    null,
                    parent,
                    holder
            );
        };
    }
}
