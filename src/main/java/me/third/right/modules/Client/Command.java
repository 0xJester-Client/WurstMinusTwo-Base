package me.third.right.modules.Client;

import me.third.right.modules.Hack;
import me.third.right.modules.HackClient;
import me.third.right.settings.setting.StringSetting;
import me.third.right.utils.Client.Enums.Category;

public class Command extends HackClient {
    //Vars
    public static Command INSTANCE;
    //Settings
    public final StringSetting commandPrefix = setting(new StringSetting("CommandPrefix",".", 1));

    public Command() {
        super("Command", "Allows you to change how commands work.");
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        disable();
    }
}
