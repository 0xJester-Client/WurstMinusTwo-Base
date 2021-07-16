package me.third.right.modules.Render;

import me.third.right.ThirdMod;
import me.third.right.events.event.SettingsEvent;
import me.third.right.modules.Hack;
import me.third.right.modules.HackStandard;
import me.third.right.settings.setting.CheckboxSetting;
import me.third.right.settings.setting.EnumSetting;
import me.third.right.settings.setting.SliderSetting;
import me.third.right.utils.Client.Enums.Category;
import me.third.right.utils.Render.GLSLSandboxShader;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;

import java.io.IOException;

public class MainMenuMods extends HackStandard {
    //Vars

    //Settings
    public final CheckboxSetting customBackground = setting(new CheckboxSetting("CustomBackGround", false));
    public final CheckboxSetting drawGradiant = setting(new CheckboxSetting("DrawGradiant", false));
    public final EnumSetting<BackGround> backGroundMode = setting(new EnumSetting<>("BackGroundMode", BackGround.values(), BackGround.GLSL));
    public final EnumSetting<Shader> shader = setting(new EnumSetting<>("Shader GLSL", Shader.values(), Shader.PurpleSmoke));
    private final SliderSetting red = setting(new SliderSetting("BG Red", 255, 0, 255, 1, SliderSetting.ValueDisplay.INTEGER));
    private final SliderSetting green = setting(new SliderSetting("BG Green", 0, 0, 255, 1, SliderSetting.ValueDisplay.INTEGER));
    private final SliderSetting blue = setting(new SliderSetting("BG Blue", 255, 0, 255, 1, SliderSetting.ValueDisplay.INTEGER));
    private final SliderSetting red1 = setting(new SliderSetting("Gradiant Red", 255, 0, 255, 1, SliderSetting.ValueDisplay.INTEGER));
    private final SliderSetting green1 = setting(new SliderSetting("Gradiant Green", 45, 0, 255, 1, SliderSetting.ValueDisplay.INTEGER));
    private final SliderSetting blue1 = setting(new SliderSetting("Gradiant Blue", 255, 0, 255, 1, SliderSetting.ValueDisplay.INTEGER));

    public MainMenuMods() {
        super("MainMenuMods", "Changes MainMenu features.", Category.RENDER);
        try {
            ThirdMod.backgroundShader = new GLSLSandboxShader("/shaders/"+shader.getSelected()+".fsh");
        }catch (IOException e) {
            ThirdMod.log.info(e);
            ThirdMod.backgroundShader = null;
        }
    }

    @EventHandler
    private final Listener<SettingsEvent> settingsEventListener = new Listener<>(event -> {
        if(event.getSetting() instanceof EnumSetting) {
            final EnumSetting<?> setting = (EnumSetting<?>) event.getSetting();
            if(setting.getName().equals(shader.getName())) {
                try {
                    ThirdMod.backgroundShader = new GLSLSandboxShader("/shaders/"+shader.getSelected()+".fsh");
                }catch (IOException e) {
                    ThirdMod.log.info(e);
                    ThirdMod.backgroundShader = null;
                }
            }
        }
    });

    public int[] getRGB() {
        return new int[] { red.getValueI(), green.getValueI(), blue.getValueI() };
    }
    public int[] getRGBGradiant() {
        return new int[] { red1.getValueI(), green1.getValueI(), blue1.getValueI() };
    }

    public enum BackGround {
        GLSL,
        Colour,
        Gradient,
        DVD
    }
    public enum Shader {
        PurpleSmoke,
        PurpleWaves
    }
}
