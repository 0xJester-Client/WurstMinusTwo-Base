package me.third.right.commands.commands;

import me.third.right.commands.Command;
import me.third.right.utils.Client.Utils.MathUtils;

public class FOVCmd extends Command {

    public FOVCmd() {
        super("fov","Changes players FOV","fov <value>");
    }

    @Override
    public void call(String[] args) throws CmdException {
        if(args.length != 1)
            throw new CmdSyntaxError();

        if(!MathUtils.isInteger(args[0]))
            throw new CmdSyntaxError();

        mc.gameSettings.fovSetting = Float.parseFloat(args[0]);
    }
}
