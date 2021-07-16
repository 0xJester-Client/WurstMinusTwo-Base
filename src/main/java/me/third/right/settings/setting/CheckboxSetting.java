/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package me.third.right.settings.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import me.third.right.ThirdMod;
import me.third.right.clickgui.Settings.Components.Checkbox;
import me.third.right.clickgui.Settings.Component;
import me.third.right.settings.Setting;

public final class CheckboxSetting extends Setting
{
	private boolean checked;
	private final boolean checkedByDefault;
	private boolean hidden = false;
	
	public CheckboxSetting(String name, String description, boolean checked)
	{
		super(name, description);
		this.checked = checked;
		checkedByDefault = checked;
	}
	public CheckboxSetting(String name, boolean checked)
	{
		this(name, null, checked);
	}
	public CheckboxSetting(String name, boolean checked, boolean hidden) {
		this(name, null, checked);
		this.hidden = hidden;
	}


	public boolean isHidden() { return hidden; }
	public boolean isChecked()
	{
		return checked;
	}
	public boolean isCheckedByDefault()
	{
		return checkedByDefault;
	}
	
	public void setChecked(boolean checked)
	{
		this.checked = checked;
		ThirdMod.getForgeWurst().getHax().saveSettings();
	}
	
	@Override
	public Component getComponent()
	{
		if(hidden) {
			return null;
		}
		return new Checkbox(this);
	}
	
	@Override
	public void fromJson(JsonElement json)
	{
		if(!json.isJsonPrimitive()) return;
		JsonPrimitive primitive = json.getAsJsonPrimitive();
		if(!primitive.isBoolean()) return;
		setChecked(primitive.getAsBoolean());
	}
	
	@Override
	public JsonElement toJson()
	{
		return new JsonPrimitive(checked);
	}
}
