package org.ppvon.ultimateCobblemonProgression.client.gui;

import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.network.chat.Component;
import org.ppvon.ultimateCobblemonProgression.config.CommonConfig;
import org.ppvon.ultimateCobblemonProgression.config.ConfigLoader;

public final class CommonConfigScreen extends BaseConfigScreen {
    private boolean doLevelCap;
    private boolean doDexProgression;

    private Checkbox chkLevelCap, chkDex;

    public CommonConfigScreen(BaseConfigScreen parent) {
        super(parent, "UCP – General");
    }

    @Override
    protected void init() {
        super.init();
        CommonConfig c = ConfigLoader.common();
        doLevelCap = c.doLevelCap;
        doDexProgression = c.doDexProgression;

        int cx = width / 2;
        int y = 40;

        chkLevelCap = Checkbox.builder(Component.literal("Enable Level Cap"), this.font)
                .selected(doLevelCap).pos(cx - 150, y).build();
        addRenderableWidget(chkLevelCap);
        y += 22;

        chkDex = Checkbox.builder(Component.literal("Enable Dex Progression"), this.font)
                .selected(doDexProgression).pos(cx - 150, y).build();
        addRenderableWidget(chkDex);
    }

    @Override
    protected void applyAndSave() {
        CommonConfig c = ConfigLoader.common();
        c.doLevelCap = chkLevelCap.selected();
        c.doDexProgression = chkDex.selected();
        ConfigLoader.saveAll();
    }
}
