package me.third.right.modules.Client;

import me.third.right.modules.HackClient;
import me.third.right.settings.setting.SliderSetting;

public class ItemHandler extends HackClient {
    //Vars
    //Settings
    public final SliderSetting tickDelay = setting(new SliderSetting("TickDelay", "", 0, 0, 5, 1, SliderSetting.ValueDisplay.INTEGER));
    public final SliderSetting movesPerTick = setting(new SliderSetting("MovesPerTick", "", 1, 1, 3, 1, SliderSetting.ValueDisplay.INTEGER));

    public ItemHandler() {
        super("ItemHandler", "Handles item movements.");
    }

    @Override
    public void onDisable() { enable(); }
}
