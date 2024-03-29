/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package me.third.right.utils.Client.ClientFiles;

import java.util.Collection;

import me.third.right.modules.Hack;
import me.third.right.ThirdMod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

public abstract class WHackList
{
	private static IForgeRegistry<Hack> registry;{
		if(registry != null) throw new IllegalStateException("Multiple instances of HackList!");
		
		RegistryBuilder<Hack> registryBuilder = new RegistryBuilder<>();
		registryBuilder.setName(new ResourceLocation(ThirdMod.MODID, "hax"));
		registryBuilder.setType(Hack.class);
		registryBuilder.setIDRange(0, Integer.MAX_VALUE - 1);
		registry = registryBuilder.create();
	}
	
	protected final <T extends Hack> T register(T hack)
	{
		hack.setRegistryName(ThirdMod.MODID, hack.getName().toLowerCase());
		registry.register(hack);
		return hack;
	}
	
	public final IForgeRegistry<Hack> getRegistry()
	{
		return registry;
	}
	
	public final Collection<Hack> getValues()
	{
		return registry.getValues();
	}
	
	public final Hack get(String name)
	{
		ResourceLocation location = new ResourceLocation(ThirdMod.MODID, name.toLowerCase());
		return registry.getValue(location);
	}
}
