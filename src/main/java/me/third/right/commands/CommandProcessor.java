/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package me.third.right.commands;

import java.util.Arrays;

import me.third.right.ThirdMod;
import me.third.right.utils.Client.Utils.ChatUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class CommandProcessor {
	private final CommandList cmds;
	private String prefix;
	
	public CommandProcessor(CommandList cmds)
	{
		this.cmds = cmds;
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onChatSent(ClientChatEvent event) {
		String message = event.getMessage().trim();
		prefix = ThirdMod.hax.command.commandPrefix.getString();
		if(!message.startsWith(prefix))
			return;

		event.setCanceled(true);
		Minecraft.getMinecraft().ingameGUI.getChatGUI().addToSentMessages(message);

		runCommand(message.substring(1));
	}
	
	public void runCommand(String input)
	{
		final String[] parts = input.split(" ");
		Command cmd = cmds.get(parts[0]);
		
		if(cmd == null) {
			ChatUtils.error("Unknown command: ." + parts[0]);
			if(input.startsWith("/"))
				ChatUtils.message("Use \""+prefix+"say " + input + "\" to send it as a chat command.");
			else
				ChatUtils.message("Type \""+prefix+"help\" for a list of commands or \""+prefix+".say ." + input + "\" to send it as a chat message.");
			return;
		}
		
		try {
			cmd.call(Arrays.copyOfRange(parts, 1, parts.length));
		} catch(Command.CmdException e) {
			e.printToChat();
		}
	}
}
