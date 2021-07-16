/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package me.third.right.modules.Client;

import me.third.right.ThirdMod;
import me.third.right.clickgui.Screen.ClickGuiScreen;
import me.third.right.modules.Hack;
import me.third.right.modules.HackClient;
import me.third.right.settings.setting.CheckboxSetting;
import me.third.right.settings.setting.EnumSetting;
import me.third.right.settings.setting.SliderSetting;
import me.third.right.utils.Client.Utils.Colour;

import java.awt.*;

import static me.third.right.utils.Client.Font.CFontRenderer.getFontFromInput;

@Hack.DontSaveState
public final class ClickGuiHack extends HackClient {
	//Vars
	public boolean blurEffect = false;
	public enum FontEnum {
		Arial(new Font("Arial", Font.PLAIN, 18)),
		Monospaced(new Font("Monospaced", Font.PLAIN, 18)),
		VerdanaBold(getFontFromInput("/assets/wurstminustwobase/font/verdanabold.ttf")),
		Queens(getFontFromInput("/assets/wurstminustwobase/font/QueensGambit.ttf")),
		Serif(new Font("Serif", Font.PLAIN, 18));
		public final Font fontName;
		FontEnum(final Font fontName) {
			this.fontName = fontName;
		}
	}

	//Settings
	//Client-Side chat
	private final EnumSetting<Colour.colour> prefixColour = setting(new EnumSetting<>("PrefixColour", Colour.colour.values(), Colour.colour.DARK_PURPLE));
	//Other
	private final SliderSetting opacity = setting(new SliderSetting("Opacity", 0.5, 0.15, 0.85, 0.01, SliderSetting.ValueDisplay.PERCENTAGE));
	private final SliderSetting maxHeight = setting(new SliderSetting("Max height", "Maximum window height\n" + "0 = no limit", 200, 0, 450, 25, SliderSetting.ValueDisplay.INTEGER));
	//BG Colour
	private final SliderSetting bgRed = setting(new SliderSetting("BG red", "Background red", 64, 0, 255, 1, SliderSetting.ValueDisplay.INTEGER));
	private final SliderSetting bgGreen = setting(new SliderSetting("BG green", "Background green", 64, 0, 255, 1, SliderSetting.ValueDisplay.INTEGER));
	private final SliderSetting bgBlue = setting(new SliderSetting("BG blue", "Background blue", 64, 0, 255, 1, SliderSetting.ValueDisplay.INTEGER));
	//Outline Colour
	private final SliderSetting acRed = setting(new SliderSetting("AC red", "Accent red", 16, 0, 255, 1, SliderSetting.ValueDisplay.INTEGER));
	private final SliderSetting acGreen = setting(new SliderSetting("AC green", "Accent green", 16, 0, 255, 1, SliderSetting.ValueDisplay.INTEGER));
	private final SliderSetting acBlue = setting(new SliderSetting("AC blue", "Accent blue", 16, 0, 255, 1, SliderSetting.ValueDisplay.INTEGER));
	//Button Colour
	private final SliderSetting bonRed = setting(new SliderSetting("Button red", "Button colour red", 0, 0, 255, 1, SliderSetting.ValueDisplay.INTEGER));
	private final SliderSetting bonGreen = setting(new SliderSetting("Button green", "Button colour green", 255, 0, 255, 1, SliderSetting.ValueDisplay.INTEGER));
	private final SliderSetting bonBlue = setting(new SliderSetting("Button blue", "Button colour blue", 0, 0, 255, 1, SliderSetting.ValueDisplay.INTEGER));
	//Options Colour
	private final SliderSetting opRed = setting(new SliderSetting("Options red", "Pin, Open and Close colour red", 0, 0, 255, 1, SliderSetting.ValueDisplay.INTEGER));
	private final SliderSetting opGreen = setting(new SliderSetting("Options green", "Pin, Open and Close colour green", 255, 0, 255, 1, SliderSetting.ValueDisplay.INTEGER));
	private final SliderSetting opBlue = setting(new SliderSetting("Options blue", "Pin, Open and Close colour blue", 0, 0, 255, 1, SliderSetting.ValueDisplay.INTEGER));
	//BG Blur
	public final CheckboxSetting bgBlur = setting(new CheckboxSetting("BG Blur","Background blur.", false));
	//Fonts
	public final CheckboxSetting customFont = setting(new CheckboxSetting("CustomFont", false));
	public final EnumSetting<FontEnum> font = setting(new EnumSetting<>("Font", FontEnum.values(), FontEnum.Arial));
	public final SliderSetting fontSize = setting(new SliderSetting("Font Size", 18, 10, 35, 1, SliderSetting.ValueDisplay.INTEGER));
	public final CheckboxSetting antiAlias = setting(new CheckboxSetting("AntiAlias", false));
	public final CheckboxSetting fractionalMetrics = setting(new CheckboxSetting("FractionalMetrics", false));

	public ClickGuiHack() {
		super("ClickGUI", "");
	}
	
	@Override
	public void onEnable() {
		mc.displayGuiScreen(new ClickGuiScreen(wurst.getGui()));
		setEnabled(false);
	}

	public int getMaxHeight()
	{
		return maxHeight.getValueI();
	}
	public void setMaxHeight(int maxHeight) { this.maxHeight.setValue(maxHeight); }

	public float getOpacity()
	{
		return opacity.getValueF();
	}
	public float[] getBgColor() { return new float[]{ bgRed.getValueI() / 255F, bgGreen.getValueI() / 255F, bgBlue.getValueI() / 255F }; }
	public float[] getAcColor() { return new float[]{ acRed.getValueI() / 255F, acGreen.getValueI() / 255F, acBlue.getValueI() / 255F }; }
	public float[] getBONColor() { return new float[]{ bonRed.getValueI() / 255F, bonGreen.getValueI() / 255F, bonBlue.getValueI() / 255F }; }
	public float[] getOptionsColour() { return new float[]{ opRed.getValueI() / 255F, opGreen.getValueI() / 255F, opBlue.getValueI() / 255F }; }

	public void applyFontChanges() {
		if(fontCheck()) {
			ThirdMod.cFontRenderer.setFont(font.getSelected().fontName.deriveFont(fontSize.getValueF()));
			ThirdMod.cFontRenderer.setAntiAlias(antiAlias.isChecked());
			ThirdMod.cFontRenderer.setFractionalMetrics(fractionalMetrics.isChecked());
		}
	}

	public boolean fontCheck() {
		return !ThirdMod.cFontRenderer.getFont().equals(font.getSelected().fontName.deriveFont(fontSize.getValueF()))
				|| ThirdMod.cFontRenderer.isAntiAlias() != antiAlias.isChecked()
				|| ThirdMod.cFontRenderer.isFractionalMetrics() != fractionalMetrics.isChecked();
	}

	public String getPrefixColour() {
		return prefixColour.getSelected().codeUI;
	}
}
