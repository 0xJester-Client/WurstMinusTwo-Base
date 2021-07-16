package me.third.right.modules.Client;

import me.third.right.ThirdMod;
import me.third.right.modules.Hack;
import me.third.right.modules.HackClient;
import me.third.right.settings.setting.CheckboxSetting;
import me.third.right.settings.setting.StringSetting;
import me.third.right.utils.Client.Utils.ChatUtils;
import me.third.right.utils.Client.Utils.DelayTimer;
import me.third.right.utils.Client.Enums.Category;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.Display;

public class Title extends HackClient {
    //Vars
    public static Title INSTANCE;
    private final DelayTimer delay = new DelayTimer();

    //Settings
    private final CheckboxSetting onBoot = setting(new CheckboxSetting("OnBoot","Sets title when the game boots.", true));
    private final CheckboxSetting inGame = setting(new CheckboxSetting("InGame","Sets the games title to display information.", true));
    private final StringSetting titleSetting = setting(new StringSetting("TitleString", "<name> V<version> <coords>", 100));

    public Title() {
        super("Title","Changes the games window title.");
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (inGame.isChecked() && mc.getConnection() != null) {
            if(delay.passedMs(200) && Display.isActive()) {
                delay.reset();
                String title = titleSetting.getString();
                title = ChatUtils.applyReplacements(title);
                if (!title.isEmpty()) Display.setTitle(title);
                else Display.setTitle(ThirdMod.NAME + " V" + ThirdMod.VERSION);
            }
        } else Display.setTitle(ThirdMod.NAME + " V" + ThirdMod.VERSION);
    }

    @Override
    public void onDisable() {
        Display.setTitle(ThirdMod.NAME + " V" + ThirdMod.VERSION);
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void setTitleOnBoot() {
        if(onBoot.isChecked()) Display.setTitle(ThirdMod.NAME+" V"+ThirdMod.VERSION);
    }
}
