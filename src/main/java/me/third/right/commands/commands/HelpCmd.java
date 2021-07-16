/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package me.third.right.commands.commands;

import me.third.right.commands.Command;
import me.third.right.utils.Client.Utils.ChatUtils;
import me.third.right.utils.Client.Utils.MathUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public final class HelpCmd extends Command
{
	public HelpCmd()
	{
		super("help", "Shows help.", "Syntax: .help <command>",
			"List commands: .help [<page>]");
	}
	
	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length > 1)
			throw new CmdSyntaxError();
		
		String arg;
		if(args.length < 1)
			arg = "1";
		else
			arg = args[0];
		
		if(MathUtils.isInteger(arg))
			list(Integer.parseInt(arg));
		else
			help(arg);
	}
	
	private void list(int page) throws CmdException
	{
		List<Command> cmds = Arrays.<Command> asList(
			wurst.getCmds().getValues().toArray(new Command[0]));
		cmds.sort(Comparator.comparing(Command::getName));
		int size = cmds.size();
		
		int pages = Math.max((int)Math.ceil(size / 8.0), 1);
		if(page > pages || page < 1)
			throw new CmdSyntaxError("Invalid page: " + page);
		
		ChatUtils.message("Total: " + size + (size == 1 ? " command" : " commands"));
		ChatUtils.message("Command list (page " + page + "/" + pages + ")");
		
		for(int i = (page - 1) * 8; i < Math.min(page * 8, size); i++)
		{
			Command c = cmds.get(i);
			ChatUtils.message(commandModule.commandPrefix.getString() + c.getName() + " - " + c.getDescription());
		}
	}
	
	private void help(String cmdName) throws CmdException
	{
		if(cmdName.startsWith(commandModule.commandPrefix.getString()))
			cmdName = cmdName.substring(1);
		
		Command cmd = wurst.getCmds().get(cmdName);
		if(cmd == null)
			throw new CmdSyntaxError("Unknown command: ." + cmdName);
		
		ChatUtils.message(commandModule.commandPrefix.getString() + cmd.getName() + " - " + cmd.getDescription());
		for(String line : cmd.getSyntax())
			ChatUtils.message(line);
	}
}
