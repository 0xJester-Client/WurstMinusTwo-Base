/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package me.third.right.modules;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import me.third.right.modules.Client.*;
import me.third.right.modules.Other.*;
import me.third.right.modules.Render.*;
import me.third.right.utils.Client.ClientFiles.WHackList;
import me.third.right.settings.Setting;
import me.third.right.utils.Client.File.JsonUtils;

public final class HackList extends WHackList {
	//Render
	public final ChatMods chatMods = register(new ChatMods());
	public final MainMenuMods mainMenuMods = register(new MainMenuMods());
	public final Shaders shaders = register(new Shaders());
	public final ExtraTab extraTab = register(new ExtraTab());
	//Client
	public final ClickGuiHack clickGuiHack = register(new ClickGuiHack());
	public final ClickGuiHudHack clickGuiHud = register(new ClickGuiHudHack());
	public final AimbotHandler aimbotHandler = register(new AimbotHandler());
	public final ItemHandler itemHandler = register(new ItemHandler());
	public final Title title = register(new Title());
	public final Config config = register(new Config());
	public final RPC rpc = register(new RPC());
	public final Command command = register(new Command());
	public Baritone baritone;
	//Other
	public final AutoReconnect autoReconnect = register(new AutoReconnect());
	//Combat
	//Movement

	private final Path enabledHacksFile;
	private final Path settingsFile;
	private boolean disableSaving;
	
	public HackList(Path enabledHacksFile, Path settingsFile) {
		this.enabledHacksFile = enabledHacksFile;
		this.settingsFile = settingsFile;
		try {
			baritone = register(new Baritone());
		} catch (NoClassDefFoundError ignored) {
		}
	}
	
	public void loadEnabledHacks() {
		final JsonArray json;
		try(BufferedReader reader = Files.newBufferedReader(enabledHacksFile)) {
			json = JsonUtils.jsonParser.parse(reader).getAsJsonArray();
		} catch(NoSuchFileException e) {
			defaultEnabledModules();
			return;
		} catch(Exception e) {
			System.out.println("Failed to load " + enabledHacksFile.getFileName());
			e.printStackTrace();
			
			saveEnabledHacks();
			return;
		}
		
		disableSaving = true;
		for(JsonElement e : json)
		{
			if(!e.isJsonPrimitive() || !e.getAsJsonPrimitive().isString())
				continue;
			
			Hack hack = get(e.getAsString());
			if(hack == null || !hack.isStateSaved()) continue;
			
			hack.setEnabled(true);
		}
		disableSaving = false;
		
		saveEnabledHacks();
	}

	private void defaultEnabledModules() {
		aimbotHandler.setEnabled(true);
		config.setEnabled(true);
		rpc.setEnabled(false);
		baritone.setEnabled(true);
		saveEnabledHacks();
	}
	
	public void saveEnabledHacks() {
		if(disableSaving) return;
		final JsonArray enabledHacks = new JsonArray();
		for(Hack hack : getRegistry())
			if(hack.isEnabled() && hack.isStateSaved())
				enabledHacks.add(new JsonPrimitive(hack.getName()));
			
		try(BufferedWriter writer = Files.newBufferedWriter(enabledHacksFile)) {
			JsonUtils.prettyGson.toJson(enabledHacks, writer);
		} catch(IOException e) {
			System.out.println("Failed to save " + enabledHacksFile.getFileName());
			e.printStackTrace();
		}
	}
	
	public void loadSettings() {
		final JsonObject json;
		try(BufferedReader reader = Files.newBufferedReader(settingsFile)) {
			json = JsonUtils.jsonParser.parse(reader).getAsJsonObject();
		} catch(NoSuchFileException e) {
			saveSettings();
			return;
		} catch(Exception e) {
			System.out.println("Failed to load " + settingsFile.getFileName());
			e.printStackTrace();
			saveSettings();
			return;
		}
		
		disableSaving = true;
		for(Entry<String, JsonElement> e : json.entrySet()) {
			if(!e.getValue().isJsonObject()) continue;
			
			final Hack hack = get(e.getKey());
			if(hack == null) continue;
			
			final Map<String, Setting> settings = hack.getSettings();
			for(Entry<String, JsonElement> e2 : e.getValue().getAsJsonObject().entrySet()) {
				String key = e2.getKey().toLowerCase();
				if(!settings.containsKey(key)) continue;
				settings.get(key).fromJson(e2.getValue());
			}
		}
		disableSaving = false;
		saveSettings();
	}
	
	public void saveSettings() {
		if(disableSaving) return;
		final JsonObject json = new JsonObject();
		for(Hack hack : getRegistry())
		{
			if(hack.getSettings().isEmpty()) continue;
			
			final JsonObject settings = new JsonObject();
			for(Setting setting : hack.getSettings().values()) {
				final JsonElement value = setting.toJson();
				if(value == null) continue;
				settings.add(setting.getName(), value);
			}
			json.add(hack.getName(), settings);
		}
		
		try(BufferedWriter writer = Files.newBufferedWriter(settingsFile))
		{
			JsonUtils.prettyGson.toJson(json, writer);
			
		}catch(IOException e)
		{
			System.out.println("Failed to save " + settingsFile.getFileName());
			e.printStackTrace();
		}
	}
}
