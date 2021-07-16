package me.third.right.commands.commands;

import me.third.right.commands.Command;
import me.third.right.utils.Client.Utils.ChatUtils;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ItemData extends Command {
    public ItemData()
    {
        super("itemdata", "Spits out item data.", "Syntax: .itemdata");
    }

    @Override
    public void call(String[] args) throws Command.CmdException {
        if(args.length > 0) throw new Command.CmdSyntaxError();
        final ItemStack stack = mc.player.getHeldItemMainhand();
        if(!stack.getItem().equals(Items.AIR)) {
            if (stack.getTagCompound() != null) {
                ChatUtils.message("&6&lNBT:\n" + stack.getTagCompound() + "");
            } else ChatUtils.message("No NBT on " + stack.getDisplayName());
        }
    }
}
