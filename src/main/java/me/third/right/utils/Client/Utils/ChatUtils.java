/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package me.third.right.utils.Client.Utils;

import me.third.right.ThirdMod;
import me.third.right.modules.Client.RPC;
import me.third.right.modules.Hack;
import me.third.right.settings.setting.StringSetting;
import me.third.right.utils.Client.Manage.LagCompensator;
import me.third.right.utils.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiVideoSettings;
import net.minecraft.init.Items;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import static me.third.right.utils.Client.Utils.EntityUtils.getPlayersPing;

public final class ChatUtils {
	protected final static Minecraft mc = Wrapper.getMinecraft();
	
	public static void component(ITextComponent component) {
		mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString("\u00a77["+ThirdMod.hax.clickGuiHack.getPrefixColour()+"Wurst-\u00a77]\u00a7r ").appendSibling(component));
	}
	
	public static void message(String message) {
		component(new TextComponentString(message));
	}
	public static void warning(String message) {
		message("\u00a77[\u00a76\u00a7lWARNING\u00a77]\u00a7r " + message);
	}
	public static void error(String message) {
		message("\u00a77[\u00a74\u00a7lERROR\u00a77]\u00a7r " + message);
	}
	public static void debug(String message) {
		message("\u00a77[\u00a74\u00a7lDEBUG\u00a77]\u00a7r " + message);
	}
	public static void moduleMessage(String message, Hack module) { message(String.format("\u00a77[\u00a74"+ThirdMod.hax.clickGuiHack.getPrefixColour()+"%s\u00a77]\u00a7r %s", module.getName(), message)); }

	public static void comReplace(ITextComponent component, int id) {
		mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString("\u00a77["+ThirdMod.hax.clickGuiHack.getPrefixColour()+"Wurst-\u00a77]\u00a7r ").appendSibling(component), id);
	}

	public static void msReplace(String message, int id) {
		comReplace(new TextComponentString(message), id);
	}
	public static void mmReplace(String message, Hack module, int id) { msReplace(String.format("\u00a77[\u00a74"+ThirdMod.hax.clickGuiHack.getPrefixColour()+"%s\u00a77]\u00a7r %s", module.getName(), message), id); }

	public enum EnumReplace {
		MEM("<mem>", ""),
		MS("<ms>", ""),
		TPS("<tps>",""),
		FPS("<fps>", ""),
		Gapple("<gapple>",""),
		Crystal("<crystal>",""),
		XP("<xp>",""),
		Coords("<coords>", ""),
		IP("<ip>",""),
		uname("<username>",""),
		Random("<random>", ""),
		Name("<name>", ThirdMod.NAME),
		Version("<version>", ThirdMod.VERSION);
		public final String replace;
		public String replacement;
		EnumReplace(String replace, String replacement) {
			this.replace = replace;
			this.replacement = replacement;
		}
	}

	public static String applyReplacements(String string) {
		for (ChatUtils.EnumReplace replace : ChatUtils.EnumReplace.values()) {
			if(string.contains(replace.replace)) {
				switch (replace) {//Get and Make Replacement.
					case MEM:
						long i = Runtime.getRuntime().maxMemory();
						long j = Runtime.getRuntime().totalMemory();
						long k = Runtime.getRuntime().freeMemory();
						long l = j - k;
						replace.replacement = String.format("% 2d%% %03d/%03dMB", l * 100L / i, l / 1024L / 1024L, i / 1024L / 1024L);
						break;
					case MS:
						if(mc.player == null || mc.world == null) replace.replacement = "0";
						else replace.replacement = getPlayersPing(mc.player)+"";
						break;
					case TPS:
						if(mc.player == null || mc.world == null) replace.replacement = "0";
						else replace.replacement = (Math.round(LagCompensator.INSTANCE.getTickRate()))+"";
						break;
					case FPS:
						if(mc.player == null || mc.world == null) replace.replacement = "0";
						else replace.replacement = Minecraft.debugFPS+"";
						break;
					case XP:
						if(mc.player == null || mc.world == null) replace.replacement = "0";
						else replace.replacement = InventoryUtil.getNumberOfItem(Items.EXPERIENCE_BOTTLE)+"";
						break;
					case Coords:
						if(mc.player == null || mc.world == null) replace.replacement = "X:0 Y:0 Z:0 [X:0 Z:0]";
						else replace.replacement = getDisplayCoords();
						break;
					case Gapple:
						if(mc.player == null || mc.world == null) replace.replacement = "0";
						else replace.replacement = InventoryUtil.getNumberOfItem(Items.GOLDEN_APPLE)+"";
						break;
					case Crystal:
						if(mc.player == null || mc.world == null) replace.replacement = "0";
						else replace.replacement = InventoryUtil.getNumberOfItem(Items.END_CRYSTAL)+"";
						break;
					case IP:
						if(mc.currentServerData != null) {
							if(mc.currentServerData.isOnLAN()) {
								replace.replacement = "SinglePlayer";
							} else {
								replace.replacement = mc.currentServerData.serverIP + "";
							}
						} else {
							if(mc.currentScreen instanceof GuiMainMenu) replace.replacement = "MainMenu";
							else if(mc.currentScreen instanceof GuiMultiplayer) replace.replacement = "MultiplayerMenu";
							else if(mc.currentScreen instanceof GuiVideoSettings) replace.replacement = "Turning RTX ON!";
							else return replace.replacement = "Not In-game ;-(";
						}
						break;
					case uname:
						replace.replacement = mc.getSession().getUsername();
						break;
					case Random:
						replace.replacement = (int)((Math.random() * (10000 - 1)) + 1)+"";
						break;
					case Version://No need to update these values.
					case Name:
						break;
				}
				string = string.replaceAll(replace.replace, replace.replacement);//Apply
			}
		}
		return string;
	}

	public static String getString(StringSetting setting) {
		String returnValue = setting.getString();
		returnValue = ChatUtils.applyReplacements(returnValue);
		return returnValue;
	}

	public static String applyStringArray(String string) {
		final String[] array = string.split(",");
		final RPC rpc = ThirdMod.hax.rpc;
		if (rpc.index >= array.length - 1) {
			rpc.index = 0;
		} else {
			rpc.index++;
		}
		return string.replaceAll(string, array[rpc.index]);
	}

	private static String getDisplayCoords() {
		if (mc.player.dimension == 0) {
			return String.format("X:%.1f Y:%.1f Z:%.1f [X:%.1f Z:%.1f]", mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.posX / 8, mc.player.posZ / 8);
		} else if (mc.player.dimension == -1) {
			return String.format("X:%.1f Y:%.1f Z:%.1f [X:%.1f Z:%.1f]", mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.posX * 8, mc.player.posZ * 8);
		} else {
			return String.format("X:%.1f Y:%.1f Z:%.1f", mc.player.posX, mc.player.posY, mc.player.posZ);
		}
	}
}
