/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package me.third.right.settings;

import java.util.Objects;

import com.google.gson.JsonElement;
import me.third.right.clickgui.Settings.Component;

public abstract class Setting
{
	private final String name;
	private final String description;
	
	public Setting(String name, String description)
	{
		this.name = Objects.requireNonNull(name);
		this.description = description;
	}
	
	public final String getName()
	{
		return name;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public abstract Component getComponent();
	
	public abstract void fromJson(JsonElement json);
	
	public abstract JsonElement toJson();
}
