package me.third.right.modules.Client;

import me.third.right.clickgui.Screen.ClickGuiHudScreen;
import me.third.right.clickgui.Screen.ClickGuiScreenMainMenu;
import me.third.right.modules.Hack;
import me.third.right.modules.HackClient;
import me.third.right.settings.setting.CheckboxSetting;
import me.third.right.settings.setting.SliderSetting;
import me.third.right.utils.Client.Enums.Category;

import static me.third.right.utils.Client.Utils.Colour.rgbToInt;

@Hack.DontSaveState
public class ClickGuiHudHack extends HackClient {
    //Vars
    //Settings
    private final SliderSetting textRed = setting(new SliderSetting("Text red", "Text red", 64, 0, 255, 1, SliderSetting.ValueDisplay.INTEGER));
    private final SliderSetting textGreen = setting(new SliderSetting("Text green", "Text green", 64, 0, 255, 1, SliderSetting.ValueDisplay.INTEGER));
    private final SliderSetting textBlue = setting(new SliderSetting("Text blue", "Text blue", 64, 0, 255, 1, SliderSetting.ValueDisplay.INTEGER));
    private final SliderSetting invAlpha = setting(new SliderSetting("Inventory Alpha", "Alpha for inventory viewer.", 64, 0, 255, 1, SliderSetting.ValueDisplay.INTEGER));
    public final CheckboxSetting shadowText = setting(new CheckboxSetting("Shadow","Add or remove string shadows.", true));

    public ClickGuiHudHack() {
        super("ClickGUIHud", "");
    }

    @Override
    public void onEnable() {
        if(mc.currentScreen instanceof ClickGuiScreenMainMenu) {
            disable();
            return;
        }
        mc.displayGuiScreen(new ClickGuiHudScreen(wurst.getGuiHud()));
        setEnabled(false);
    }

    public int[] getTextRGB() {
        return new int[] { textRed.getValueI(), textGreen.getValueI(), textBlue.getValueI() };
    }
    public int getRGBInt() {
        return rgbToInt(textRed.getValueI(), textGreen.getValueI(), textBlue.getValueI());
    }

    public int[] getTextRGBA() {
        return new int[] { textRed.getValueI(), textGreen.getValueI(), textBlue.getValueI(), invAlpha.getValueI()};
    }
}
