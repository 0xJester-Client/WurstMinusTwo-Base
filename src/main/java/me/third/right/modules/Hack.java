/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package me.third.right.modules;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import me.third.right.ThirdMod;
import me.third.right.utils.Client.ClientFiles.WForgeRegistryEntry;
import me.third.right.settings.Setting;
import me.third.right.utils.Client.Enums.Category;
import me.third.right.utils.Client.Enums.ReleaseType;
import me.third.right.utils.Client.Utils.ChatUtils;
import net.minecraft.client.Minecraft;

public abstract class Hack extends WForgeRegistryEntry<Hack>
{
	//Vars
	protected static final ThirdMod wurst = ThirdMod.getForgeWurst();
	protected static final Minecraft mc = Minecraft.getMinecraft();

	private ReleaseType releaseState = ReleaseType.Release;
	private final String name;
	private final String description;
	private final Category category;
	private final LinkedHashMap<String, Setting> settings = new LinkedHashMap<>();
	public boolean enabled;
	private final boolean stateSaved = !getClass().isAnnotationPresent(DontSaveState.class);

	public Hack(String name, String description, Category category) {
		this.name = name;
		this.description = description;
		this.category = category;
	}
	
	public final String getName()
	{
		return name;
	}
	public String getRenderName()
	{
		return name;
	}
	public final String getDescription()
	{
		return description;
	}
	public final Category getCategory()
	{
		return category;
	}
	public void setReleaseState(ReleaseType releaseState) { this.releaseState = releaseState; }
	public ReleaseType getReleaseState() { return releaseState; }

	public void disable() { setEnabled(false); }
	public void enable() { setEnabled(true); }
	
	public final Map<String, Setting> getSettings()
	{
		return Collections.unmodifiableMap(settings);
	}
	
	protected final void addSetting(Setting setting) {
		String key = setting.getName().toLowerCase();
		
		if(settings.containsKey(key))
			throw new IllegalArgumentException("Duplicate setting: " + name + " " + key);
		
		settings.put(key, setting);
	}

	protected <T> T setting(Setting setting) {
		addSetting(setting);
		return (T) setting;
	}
	
	public final boolean isEnabled()
	{
		return enabled;
	}
	
	public final void setEnabled(boolean enabled)
	{
		if(this.enabled == enabled)
			return;
		
		this.enabled = enabled;
		
		if(enabled) {
			if(this instanceof HackStandard) {
				if(((HackStandard) this).notify.isChecked() && mc.player != null && mc.world != null)
					ChatUtils.mmReplace("\u00a72Enabled", this, -1420);
			}
			onEnable();
		} else {
			if(this instanceof HackStandard) {
				if(((HackStandard) this).notify.isChecked() && mc.player != null && mc.world != null)
					ChatUtils.mmReplace("\u00a74Disabled", this, -1420);
			}
			onDisable();
		}
		
		if(stateSaved) wurst.getHax().saveEnabledHacks();
	}
	
	public final boolean isStateSaved()
	{
		return stateSaved;
	}
	
	public void onEnable()
	{
		
	}
	
	public void onDisable()
	{
		
	}

	public void onClose() {

	}

	public void onDisconnect() {

	}

	public String hudInfo() {
		return null;
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public static @interface DontSaveState
	{
		
	}
}
