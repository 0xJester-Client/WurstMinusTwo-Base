package me.third.right.commands.commands;

import me.third.right.commands.Command;
import me.third.right.utils.Client.Utils.ChatUtils;
import me.third.right.utils.Client.Utils.MathUtils;

import static me.third.right.utils.Client.Utils.Colour.rgbToInt;

public class rgbCmd extends Command {

    public rgbCmd() {
        super("rgb", "For development.", "Syntax: .rgb <red> <green> <blue> <alpha>");
    }

    @Override
    public void call(String[] args) throws CmdException {
        if(args.length != 4) throw new CmdSyntaxError();
        for(String arg : args) {
            if(!MathUtils.isInteger(arg)) throw new CmdSyntaxError();
        }
        ChatUtils.message(rgbToInt(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]))+"");
    }
}
