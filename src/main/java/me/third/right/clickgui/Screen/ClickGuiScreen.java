/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package me.third.right.clickgui.Screen;

import java.io.IOException;

import me.third.right.ThirdMod;
import me.third.right.clickgui.ClickGui;
import me.third.right.modules.Client.ClickGuiHack;
import me.third.right.modules.Render.Shaders;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public final class ClickGuiScreen extends GuiScreen {
	private final ClickGui gui;
	private final ClickGuiHack guiHack;
	
	public ClickGuiScreen(ClickGui gui) {
		this.gui = gui;
		guiHack = ThirdMod.hax.clickGuiHack;
		if(guiHack.bgBlur.isChecked() && !ThirdMod.hax.shaders.isEnabled()) {
			if(mc.entityRenderer.getShaderGroup() == null) {
				mc.entityRenderer.loadShader(new ResourceLocation("minecraft:shaders/post/" + Shaders.ShadersMinecraft.blur + ".json"));
				guiHack.blurEffect = true;
			} else if(mc.entityRenderer.getShaderGroup() != null && !mc.entityRenderer.getShaderGroup().getShaderGroupName().equals("minecraft:shaders/post/" + Shaders.ShadersMinecraft.blur + ".json")) {
				mc.entityRenderer.stopUseShader();
			}
		}
	}
	
	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		gui.handleMouseClick(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		gui.render(mouseX, mouseY, partialTicks);
	}

	@Override
	public void onGuiClosed() {
		if(guiHack.blurEffect) {
			mc.entityRenderer.stopUseShader();
			guiHack.blurEffect = false;
		}
	}
}
