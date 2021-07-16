package me.third.right.modules.Other;

import me.third.right.ThirdMod;
import me.third.right.modules.HackStandard;
import me.third.right.settings.setting.SliderSetting;
import me.third.right.utils.Client.Enums.Category;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.fml.client.FMLClientHandler;

import java.util.Timer;
import java.util.TimerTask;

public class AutoReconnect extends HackStandard {
    //Vars
    private static final Timer timer = new Timer();
    public static GuiButton reconnectButton;
    public static GuiButton autoReconnectButton;
    public static int autoReconnectWaitTime = 5;
    private String ip = "";
    private int port = 0;
    //Settings
    public final SliderSetting delay = setting(new SliderSetting("Delay", 10,4,20,1, SliderSetting.ValueDisplay.DECIMAL));

    static {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                final AutoReconnect autoReconnect = ThirdMod.hax.autoReconnect;
                if (autoReconnect.isEnabled() && mc.currentScreen instanceof me.third.right.clickgui.Screen.GuiDisconnected && autoReconnectButton != null) {
                    autoReconnectWaitTime--;
                    AutoReconnect.autoReconnectButton.displayString = "AutoReconnect (\u00A7a"+AutoReconnect.autoReconnectWaitTime+"\u00A7r)";
                    if (autoReconnectWaitTime <= 0) {
                        ServerData data = new ServerData("", autoReconnect.getIp()+':'+ autoReconnect.getPort(), false);
                        data.setResourceMode(ServerData.ServerResourceMode.PROMPT);
                        mc.addScheduledTask(() -> FMLClientHandler.instance().connectToServer(mc.currentScreen, data));
                        autoReconnectWaitTime = autoReconnect.delay.getValueI();
                    }
                }
            }
        }, 1000, 1000);
    }

    public AutoReconnect() {
        super("AutoReconnect", "", Category.OTHER);
    }

    @Override
    public void onDisable() {
        autoReconnectWaitTime = delay.getValueI();
    }

    public void prevServer(final String ip, final int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
