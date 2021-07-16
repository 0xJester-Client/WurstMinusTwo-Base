/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package me.third.right.commands.commands;

import me.third.right.commands.Command;
import me.third.right.modules.Client.ClickGuiHack;
import me.third.right.utils.Client.Utils.ChatUtils;

public final class VrTweaksCmd extends Command
{
	public VrTweaksCmd()
	{
		super("vrtweaks", "Tweaks your settings for ViveCraft.",
			"Syntax: .vrtweaks");
	}
	
	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length > 0)
			throw new CmdSyntaxError();
		
		ChatUtils.message("Adjusting settings for VR...");
		final ClickGuiHack gui = wurst.getHax().clickGuiHack;
		
		if(gui.getMaxHeight() == 0)
			ChatUtils.message("Scrolling is already disabled.");
		else
		{
			gui.setMaxHeight(0);
			ChatUtils.message("Disabled scrolling.");
		}
		
		ChatUtils.message("Done!");
	}
}
