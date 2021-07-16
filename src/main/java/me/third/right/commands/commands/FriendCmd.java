package me.third.right.commands.commands;

import me.third.right.ThirdMod;
import me.third.right.commands.Command;
import me.third.right.utils.Client.Utils.ChatUtils;

import java.util.ArrayList;

public class FriendCmd extends Command {

    public FriendCmd() {
        super("friends", "Manages friends.", "friends add <name>\n friends remove <name>\nfriends list");
    }

    @Override
    public void call(String[] args) throws CmdException
    {
        if(args.length < 1) throw new CmdSyntaxError();
        switch(args[0].toLowerCase()) {
            case "add":
                if(args[1] != null && !args[1].isEmpty()) {
                    ThirdMod.friends.addPlayer(args[1]);
                    ChatUtils.message(String.format("Added %s.", args[1]));
                } else ChatUtils.message("Bad Arguments.");
                break;
            case "remove":
                if(args[1] != null && !args[1].isEmpty()) {
                    ThirdMod.friends.removePlayer(args[1]);
                    ChatUtils.message(String.format("Removed %s.", args[1]));
                } else ChatUtils.message("Bad Arguments.");
                break;
            case "list":
                final ArrayList<String> friends = ThirdMod.friends.getPlayerList();
                if(!friends.isEmpty()) {
                    for (String name : friends) {
                        ChatUtils.message(name);
                    }
                } else {
                    ChatUtils.message("Lol no friends.");
                }
                break;
            default:
                throw new CmdSyntaxError();
        }
    }
}
