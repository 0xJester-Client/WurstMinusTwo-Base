/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package me.third.right.utils.Client.ClientFiles;

import java.util.Collection;

import me.third.right.commands.Command;
import me.third.right.ThirdMod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

public abstract class WCommandList
{
	private static IForgeRegistry<Command> registry;
	{
		if(registry != null)
			throw new IllegalStateException(
				"Multiple instances of CommandList!");
		
		RegistryBuilder<Command> registryBuilder = new RegistryBuilder<>();
		registryBuilder.setName(new ResourceLocation(ThirdMod.MODID, "cmds"));
		registryBuilder.setType(Command.class);
		registryBuilder.setIDRange(0, Integer.MAX_VALUE - 1);
		registry = registryBuilder.create();
	}
	
	protected final <T extends Command> T register(T cmd)
	{
		cmd.setRegistryName(ThirdMod.MODID, cmd.getName().toLowerCase());
		registry.register(cmd);
		return cmd;
	}
	
	public final IForgeRegistry<Command> getRegistry()
	{
		return registry;
	}
	
	public final Collection<Command> getValues()
	{
		return registry.getValues();
	}
	
	public final Command get(String name)
	{
		ResourceLocation location =
			new ResourceLocation(ThirdMod.MODID, name.toLowerCase());
		return registry.getValue(location);
	}
}
