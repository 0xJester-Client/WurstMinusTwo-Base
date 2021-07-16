/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package me.third.right.hud;

import me.third.right.clickgui.ClickGuiHud;
import me.third.right.clickgui.Screen.ClickGuiHudScreen;
import me.third.right.ThirdMod;
import me.third.right.clickgui.ClickGui;
import me.third.right.clickgui.Screen.ClickGuiScreen;
import me.third.right.utils.Wrapper;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class IngameHUD {
	private static final Minecraft mc = Minecraft.getMinecraft();
	private final HudList hudList;
	private final ClickGui clickGui;
	private final ClickGuiHud clickGuiHud;
	public static ScaledResolution sr = new ScaledResolution(mc);
	
	public IngameHUD(final ClickGui clickGui, final HudList hudList, final ClickGuiHud clickGuiHud) {
		this.hudList = hudList;
		this.clickGui = clickGui;
		this.clickGuiHud = clickGuiHud;
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		if (mc.player == null || mc.world == null) return;
		sr = new ScaledResolution(mc);
		for(Hud hud : hudList.getValues()) {
			if(hud.isPinned()) {
				hud.onUpdate();
			}
		}
	}
	
	@SubscribeEvent
	public void onRenderGUI(RenderGameOverlayEvent.Post event) {
		if(event.getType() != RenderGameOverlayEvent.ElementType.ALL || mc.gameSettings.showDebugInfo) return;
		boolean blend = GL11.glGetBoolean(GL11.GL_BLEND);

		// pinned windows
		clickGui.updateColors();
		clickGuiHud.updateColors();
		if(blend) GL11.glEnable(GL11.GL_BLEND);
		else GL11.glDisable(GL11.GL_BLEND);
		for(Hud hud : ThirdMod.hud1.getValues()) {
			if ((hud.isPinned() || (mc.currentScreen instanceof ClickGuiHudScreen)) && hud.requirementsMet()) {
				hud.onRender();
			}
		}
	}

	public static ScaledResolution getScale() {
		return sr;
	}
}
