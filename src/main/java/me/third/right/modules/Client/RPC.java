package me.third.right.modules.Client;

import me.third.right.DiscordPresence;
import me.third.right.modules.HackClient;
import me.third.right.settings.setting.StringSetting;

public class RPC extends HackClient {
    //Vars
    public int index = 0;
    //Settings
    public final StringSetting details = setting(new StringSetting("Details", "PlaceHolder", Integer.MAX_VALUE));
    public final StringSetting state = setting(new StringSetting("State", "PlaceHolder",Integer.MAX_VALUE));
    public final StringSetting largeImage = setting(new StringSetting("Large Image", "wurstminus",Integer.MAX_VALUE));
    public final StringSetting smallImage = setting(new StringSetting("Small Image", "rat",Integer.MAX_VALUE));

    public RPC() {
        super("RPC", "Discord RPC");
    }

    @Override
    public void onDisable() {
        DiscordPresence.shutDown();
    }

    @Override
    public void onEnable() {
        DiscordPresence.start();
    }
}
