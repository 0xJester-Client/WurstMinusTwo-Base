/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package me.third.right.commands.commands;

import me.third.right.commands.Command;
import me.third.right.utils.Client.Utils.MathUtils;
import me.third.right.utils.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;

public final class VClipCmd extends Command
{
	public VClipCmd()
	{
		super("vclip", "Lets you clip through blocks vertically.",
			"Syntax: .vclip <height>");
	}
	
	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length != 1)
			throw new CmdSyntaxError();
		
		if(!MathUtils.isInteger(args[0]))
			throw new CmdSyntaxError();
		
		EntityPlayerSP player = Wrapper.getPlayer();
		player.setPosition(player.posX, player.posY + Integer.parseInt(args[0]), player.posZ);
	}
}
