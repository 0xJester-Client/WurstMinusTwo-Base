/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package me.third.right.commands;

import me.third.right.ThirdMod;
import me.third.right.utils.Client.ClientFiles.WForgeRegistryEntry;
import me.third.right.utils.Client.Utils.ChatUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.registries.IForgeRegistryEntry;

public abstract class Command extends WForgeRegistryEntry<Command> implements IForgeRegistryEntry<Command> {
	protected static final ThirdMod wurst = ThirdMod.getForgeWurst();
	protected static final Minecraft mc = Minecraft.getMinecraft();
	protected static final me.third.right.modules.Client.Command commandModule = ThirdMod.hax.command;

	private final String name;
	private final String description;
	private final String[] syntax;
	
	public Command(String name, String description, String... syntax)
	{
		this.name = name;
		this.description = description;
		this.syntax = syntax;
	}
	
	public abstract void call(String[] args) throws CmdException;
	
	public final String getName()
	{
		return name;
	}
	
	public final String getDescription()
	{
		return description;
	}
	
	public final String[] getSyntax()
	{
		return syntax;
	}

	public abstract class CmdException extends Exception
	{
		public CmdException()
		{
			super();
		}
		
		public CmdException(String message)
		{
			super(message);
		}
		
		public abstract void printToChat();
	}
	
	public final class CmdError extends CmdException
	{
		public CmdError(String message)
		{
			super(message);
		}
		
		@Override
		public void printToChat()
		{
			ChatUtils.error(getMessage());
		}
	}
	
	public final class CmdSyntaxError extends CmdException
	{
		public CmdSyntaxError()
		{
			super();
		}
		
		public CmdSyntaxError(String message)
		{
			super(message);
		}
		
		@Override
		public void printToChat()
		{
			if(getMessage() != null)
				ChatUtils
					.message("\u00a74Syntax error:\u00a7r " + getMessage());
			
			for(String line : syntax)
				ChatUtils.message(line);
		}
	}
}
