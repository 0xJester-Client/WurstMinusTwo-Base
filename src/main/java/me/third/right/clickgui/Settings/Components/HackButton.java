/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package me.third.right.clickgui.Settings.Components;

import me.third.right.clickgui.Settings.Component;
import me.third.right.modules.Hack;
import me.third.right.ThirdMod;
import me.third.right.clickgui.ClickGui;
import me.third.right.clickgui.Window;
import me.third.right.settings.Setting;
import me.third.right.settings.setting.CheckboxSetting;
import me.third.right.settings.setting.ScoreSetting;
import me.third.right.utils.Client.Font.CFontRenderer;
import me.third.right.utils.Client.Font.FontDrawing;
import me.third.right.utils.Wrapper;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;

public final class HackButton extends Component
{
	private final Hack hack;
	private Window settingsWindow;
	
	public HackButton(Hack hack)
	{
		this.hack = hack;
		setWidth(getDefaultWidth());
		setHeight(getDefaultHeight());
	}
	
	@Override
	public void handleMouseClick(int mouseX, int mouseY, int mouseButton)
	{
		if(mouseButton != 0) return;
		if(!isEmpty(hack) && mouseX > getX() + getWidth() - 12)
		{
			if(settingsWindow != null && !settingsWindow.isClosing())
			{
				settingsWindow.close();
				settingsWindow = null;
				return;
			}
			
			settingsWindow = new Window(hack.getName() + " Settings");
			for(Setting setting : hack.getSettings().values()) {
				final Component component = setting.getComponent();
				settingsWindow.add(component);
			}
			
			settingsWindow.setClosable(true);
			settingsWindow.setMinimizable(false);
			settingsWindow.setPinnable(false);
			settingsWindow.pack();
			
			int scroll = getParent().isScrollingEnabled() ? getParent().getScrollOffset() : 0;
			int x = getParent().getX() + getParent().getWidth() + 5;
			int y = getParent().getY() + 12 + getY() + scroll;
			final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
			if(x + settingsWindow.getWidth() > sr.getScaledWidth())
				x = getParent().getX() - settingsWindow.getWidth() - 5;
			if(y + settingsWindow.getHeight() > sr.getScaledHeight())
				y -= settingsWindow.getHeight() - 14;
			settingsWindow.setX(x);
			settingsWindow.setY(y);
			
			ClickGui gui = ThirdMod.getForgeWurst().getGui();
			gui.addWindow(settingsWindow);
			return;
		}
		
		hack.setEnabled(!hack.isEnabled());
	}

	private boolean isEmpty(final Hack hack) {
		final int count = hack.getSettings().size() - hiddenCount(hack);
		return count <= 0;
	}

	private int hiddenCount(final Hack hack) {
		int count = 0;
		for(Setting setting : hack.getSettings().values()) {
			if(setting instanceof CheckboxSetting) {
				final CheckboxSetting checkboxSetting = (CheckboxSetting) setting;
				if(checkboxSetting.isHidden()) {
					count++;
				}
			} else if(setting instanceof ScoreSetting) {
				final ScoreSetting scoreSetting = (ScoreSetting) setting;
				count++;
			}
		}
		return count;
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		final ClickGui gui = ThirdMod.getForgeWurst().getGui();
		final float[] bgColor = gui.getBgColor();
		final float[] acColor = gui.getAcColor();
		final float[] bonColor = gui.getBONColor();
		final float[] opColour = gui.getOpColour();
		float opacity = gui.getOpacity();
		boolean settings = !isEmpty(hack);
		
		int x1 = getX();
		int x2 = x1 + getWidth();
		int x3 = settings ? x2 - 11 : x2;
		int y1 = getY();
		int y2 = y1 + getHeight();
		
		int scroll = getParent().isScrollingEnabled() ? getParent().getScrollOffset() : 0;
		boolean hovering = mouseX >= x1 && mouseY >= y1 && mouseX < x2 && mouseY < y2 && mouseY >= -scroll && mouseY < getParent().getHeight() - 13 - scroll;
		boolean hHack = hovering && mouseX < x3;
		boolean hSettings = hovering && mouseX >= x3;
		
		// tooltip
		if(hHack) gui.setTooltip(hack.getDescription());
		
		// color
		if(hack.isEnabled()) GL11.glColor4f(bonColor[0], bonColor[1], bonColor[2], hHack ? opacity * 1.5F : opacity);
		else GL11.glColor4f(bgColor[0], bgColor[1], bgColor[2], hHack ? opacity * 1.5F : opacity);
		
		// background
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2i(x1, y1);
		GL11.glVertex2i(x1, y2);
		GL11.glVertex2i(x3, y2);
		GL11.glVertex2i(x3, y1);
		if(settings) {
			GL11.glColor4f(bgColor[0], bgColor[1], bgColor[2], hSettings ? opacity * 1.5F : opacity);
			GL11.glVertex2i(x3, y1);
			GL11.glVertex2i(x3, y2);
			GL11.glVertex2i(x2, y2);
			GL11.glVertex2i(x2, y1);
		}
		GL11.glEnd();
		
		// outline
		GL11.glColor4f(acColor[0], acColor[1], acColor[2], 0.5F);
		GL11.glBegin(GL11.GL_LINE_LOOP);
		GL11.glVertex2i(x1, y1);
		GL11.glVertex2i(x1, y2);
		GL11.glVertex2i(x2, y2);
		GL11.glVertex2i(x2, y1);
		GL11.glEnd();
		
		if(settings) {
			// separator
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2i(x3, y1);
			GL11.glVertex2i(x3, y2);
			GL11.glEnd();
			
			double xa1 = x3 + 1;
			double xa2 = (x3 + x2) / 2.0;
			double xa3 = x2 - 1;
			double ya1;
			double ya2;


			if(settingsWindow != null && !settingsWindow.isClosing()) {
				ya1 = y2 - 3.5;
				ya2 = y1 + 3;
				GL11.glColor4f(opColour[0], opColour[1], opColour[2], hSettings ? opacity * 1.5F : opacity);
			} else {
				ya1 = y1 + 3.5;
				ya2 = y2 - 3;
				GL11.glColor4f(opColour[0], opColour[1], opColour[2], hSettings ? opacity * 1.5F : opacity);
			}
			
			// arrow
			GL11.glBegin(GL11.GL_TRIANGLES);
			GL11.glVertex2d(xa1, ya1);
			GL11.glVertex2d(xa3, ya1);
			GL11.glVertex2d(xa2, ya2);
			GL11.glEnd();
			
			// outline
			GL11.glColor4f(0.0625F, 0.0625F, 0.0625F, 0.5F);
			GL11.glBegin(GL11.GL_LINE_LOOP);
			GL11.glVertex2d(xa1, ya1);
			GL11.glVertex2d(xa3, ya1);
			GL11.glVertex2d(xa2, ya2);
			GL11.glEnd();
		}
		
		// hack name
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		final boolean customFont = ThirdMod.hax.clickGuiHack.customFont.isChecked();
		int fy = y1 + 2;
		int fx;
		if(customFont) {
			final CFontRenderer cfr = Wrapper.getCFontRenderer();
			fx = x1 + ((settings ? getWidth() - 11 : getWidth()) - cfr.getStringWidth(hack.getName())) / 2;
		} else {
			final FontRenderer fr = Wrapper.getFontRenderer();
			fx = x1 + ((settings ? getWidth() - 11 : getWidth()) - fr.getStringWidth(hack.getName())) / 2;
		}
		FontDrawing.drawString(hack.getName(), fx, fy, 0xf0f0f0);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}
	
	@Override
	public int getDefaultWidth()
	{
		final boolean customFont = ThirdMod.hax.clickGuiHack.customFont.isChecked();
		int width = customFont ? Wrapper.getCFontRenderer().getStringWidth(hack.getName()) + 2 : Wrapper.getFontRenderer().getStringWidth(hack.getName()) + 2;
		if(!hack.getSettings().isEmpty())
			width += 11;
		return width;
	}
	
	@Override
	public int getDefaultHeight()
	{
		return 11;
	}

	public Hack getHack() {
		return hack;
	}
}
