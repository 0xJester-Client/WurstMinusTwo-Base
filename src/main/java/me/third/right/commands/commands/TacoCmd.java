/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package me.third.right.commands.commands;

import me.third.right.ThirdMod;
import me.third.right.commands.Command;
import me.third.right.utils.Wrapper;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class TacoCmd extends Command
{
	private ResourceLocation[] tacos= null;//Only load the tacos when needed.
	private boolean enabled;
	
	public TacoCmd()
	{
		super("taco", "Spawns a dancing taco on your hotbar.", "Syntax: .taco");
	}
	
	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length > 0)
			throw new CmdSyntaxError();
		
		enabled = !enabled;
		
		if(enabled) {
			MinecraftForge.EVENT_BUS.register(this);
			tacos = new ResourceLocation[] {
					new ResourceLocation(ThirdMod.MODID+":textures/taco1.png"),
					new ResourceLocation(ThirdMod.MODID+":textures/taco2.png"),
					new ResourceLocation(ThirdMod.MODID+":textures/taco3.png"),
					new ResourceLocation(ThirdMod.MODID+":textures/taco4.png")
			};
		} else {
			MinecraftForge.EVENT_BUS.unregister(this);
			tacos = null;
		}
	}
	
	@SubscribeEvent
	public void onRenderGUI(RenderGameOverlayEvent.Post event)
	{
		if(event.getType() != ElementType.ALL || mc.gameSettings.showDebugInfo || tacos == null) return;

		GL11.glColor4f(1, 1, 1, 1);
		int tacoId = Wrapper.getPlayer().ticksExisted % 32 / 8;
		final ResourceLocation tacoLocation = tacos[tacoId];
		mc.getTextureManager().bindTexture(tacoLocation);
		
		final ScaledResolution sr = new ScaledResolution(mc);
		int x = sr.getScaledWidth() / 2 + 44;
		int y = sr.getScaledHeight() - 51;
		int w = 64;
		int h = 32;
		Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, w, h, w, h);
	}
}
