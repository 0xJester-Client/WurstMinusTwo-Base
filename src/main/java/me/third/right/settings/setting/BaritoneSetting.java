package me.third.right.settings.setting;

import baritone.api.Settings;
import com.google.gson.JsonElement;
import me.third.right.ThirdMod;
import me.third.right.clickgui.Settings.Component;
import me.third.right.clickgui.Settings.Components.BaritoneComponent;
import me.third.right.settings.Setting;

public class BaritoneSetting extends Setting {
    public enum SettingType {//TODO add more settings if they're needed.
        Boolean,
        Double
    }
    private Object setting;
    private final SettingType type;

    public BaritoneSetting(String name, String description , SettingType settingType) {
        super(name, description);
        this.type = settingType;
    }
    public BaritoneSetting(String name, SettingType settingType) {
        super(name, null);
        this.type = settingType;
    }

    public void toggle(boolean state) {
        if(type.equals(SettingType.Boolean) && setting != null) {
            if(setting instanceof Settings.Setting) {
                final Settings.Setting<Boolean> var = (Settings.Setting<Boolean>) setting;
                var.value = state;
                ThirdMod.hax.baritone.saveSettings();
            } else {
                ThirdMod.log.info("BaritoneAPI Settings configured incorrectly!");
            }
        }
    }
    public boolean toggleState() {
        if(type.equals(SettingType.Boolean) && setting != null) {
            if(setting instanceof Settings.Setting) {
                final Settings.Setting<Boolean> var = (Settings.Setting<Boolean>) setting;
                return var.value;
            } else {
                ThirdMod.log.info("BaritoneAPI Settings configured incorrectly!");
            }
        }
        return false;
    }

    public void setValue(double value) {
        if(type.equals(SettingType.Double) && setting != null) {
            if(setting instanceof Settings.Setting) {
                final Settings.Setting<Double> var = (Settings.Setting<Double>) setting;
                var.value = value;
                ThirdMod.hax.baritone.saveSettings();
            } else {
                ThirdMod.log.info("BaritoneAPI Settings configured incorrectly!");
            }
        }
    }
    public double getValue() {
        if(type.equals(SettingType.Double) && setting != null) {
            if(setting instanceof Settings.Setting) {
                final Settings.Setting<Double> var = (Settings.Setting<Double>) setting;
                return var.value;
            } else {
                ThirdMod.log.info("BaritoneAPI Settings configured incorrectly!");
            }
        }
        return -1;
    }

    public void setSetting(Object setting) {
        this.setting = setting;
    }

    public SettingType getType() {
        return type;
    }

    @Override
    public Component getComponent() {
        return new BaritoneComponent(this);
    }

    //We do not save this
    @Override
    public void fromJson(JsonElement json) {
    }
    @Override
    public JsonElement toJson() {
        return null;
    }
}
