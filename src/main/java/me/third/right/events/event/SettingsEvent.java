package me.third.right.events.event;

import me.third.right.events.ThirdEvents;
import me.third.right.settings.Setting;

public class SettingsEvent extends ThirdEvents {
    private final Setting setting;

    public SettingsEvent(final Setting setting) {
        this.setting = setting;
    }

    public Setting getSetting() {
        return setting;
    }
}
