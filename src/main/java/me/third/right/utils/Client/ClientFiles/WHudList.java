package me.third.right.utils.Client.ClientFiles;

import me.third.right.ThirdMod;
import me.third.right.hud.Hud;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.Collection;

public abstract class WHudList {
    private static IForgeRegistry<Hud> registry;
    {
        if(registry != null) throw new IllegalStateException("Multiple instances of Hud!");

        RegistryBuilder<Hud> registryBuilder = new RegistryBuilder<>();
        registryBuilder.setName(new ResourceLocation(ThirdMod.MODID, "hud"));
        registryBuilder.setType(Hud.class);
        registryBuilder.setIDRange(0, Integer.MAX_VALUE - 1);
        registry = registryBuilder.create();
    }

    protected final <T extends Hud> T register(T hud)
    {
        hud.setRegistryName(ThirdMod.MODID, hud.getName().toLowerCase());
        registry.register(hud);
        return hud;
    }

    public final IForgeRegistry<Hud> getRegistry()
    {
        return registry;
    }

    public final Collection<Hud> getValues()
    {
        return registry.getValues();
    }

    public final Hud get(String name)
    {
        ResourceLocation location = new ResourceLocation(ThirdMod.MODID, name.toLowerCase());
        return registry.getValue(location);
    }
}
