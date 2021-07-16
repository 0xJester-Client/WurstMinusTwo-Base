package me.third.right.modules;

import me.third.right.settings.setting.CheckboxSetting;
import me.third.right.utils.Client.Enums.Category;

public class HackStandard extends Hack{
    //Settings
    public final CheckboxSetting notify = setting(new CheckboxSetting("Notify", false, true));
    public final CheckboxSetting arrayList = setting(new CheckboxSetting("ArrayList", false, true));

    public HackStandard(final String name, final String description, final Category category) {
        super(name, description, category);
    }
}
