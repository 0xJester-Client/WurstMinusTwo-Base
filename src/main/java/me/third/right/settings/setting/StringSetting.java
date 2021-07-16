package me.third.right.settings.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import me.third.right.ThirdMod;
import me.third.right.clickgui.Settings.Component;
import me.third.right.clickgui.Settings.Components.StringButton;
import me.third.right.settings.Setting;

public class StringSetting extends Setting {
    private String string;
    private int maxLength = 32;

    public StringSetting(final String name, final String description, final String string) {
        super(name, description);
        this.string = string;
    }
    public StringSetting(final String name, final String string) {
        this(name, null, string);
    }
    public StringSetting(final String name, final String string, final int maxLength) {
        this(name, null, string);
        this.maxLength = maxLength;
    }

    public void setString(String string) {
        this.string = string;
        ThirdMod.getForgeWurst().getHax().saveSettings();
    }
    public void setMaxLength(int maxLength) { this.maxLength = maxLength; }

    public String getString() { return string; }
    public int getMaxLength() { return maxLength; }

    @Override
    public Component getComponent()
    {
        return new StringButton(this);
    }

    @Override
    public void fromJson(JsonElement json)
    {
        if(!json.isJsonPrimitive()) return;
        final JsonPrimitive primitive = json.getAsJsonPrimitive();
        if(!primitive.isString()) return;
        setString(primitive.getAsString());
    }

    @Override
    public JsonElement toJson()
    {
        return new JsonPrimitive(string);
    }
}
