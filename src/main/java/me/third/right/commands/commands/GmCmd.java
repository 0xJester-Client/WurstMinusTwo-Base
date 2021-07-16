/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package me.third.right.commands.commands;

import me.third.right.commands.Command;
import me.third.right.utils.Wrapper;

public final class GmCmd extends Command
{
	public GmCmd()
	{
		super("gm", "Shortcut for /gamemode.", "Syntax: .gm <gamemode>");
	}
	
	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length < 1)
			throw new CmdSyntaxError();
		
		String message = "/gamemode " + String.join(" ", args);
		Wrapper.getPlayer().sendChatMessage(message);
	}
}
