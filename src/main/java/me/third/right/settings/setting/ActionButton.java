package me.third.right.settings.setting;

import com.google.gson.JsonElement;
import me.third.right.clickgui.Settings.Component;
import me.third.right.clickgui.Settings.Components.Action;
import me.third.right.settings.Setting;

import java.util.function.Consumer;

public class ActionButton extends Setting {
    private final Consumer<?> action;

    public ActionButton(String name, String description, Consumer<?> action) {
        super(name, description);
        this.action = action;
    }

    public Consumer<?> getAction() {
        return action;
    }

    @Override
    public Component getComponent() {
        return new Action(this);
    }

    @Override
    public void fromJson(JsonElement json) {

    }

    @Override
    public JsonElement toJson() {
        return null;
    }
}
