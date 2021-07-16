package me.third.right.commands.commands;

import me.third.right.ThirdMod;
import me.third.right.commands.Command;
import me.third.right.modules.Hack;
import me.third.right.settings.Setting;
import me.third.right.settings.setting.*;
import me.third.right.utils.Client.Utils.ChatUtils;
import me.third.right.utils.Client.Utils.MathUtils;

public class SetCmd extends Command {

    public SetCmd() {
        super("set", "Modifies settings.",
                "Syntax: .set <hack> <setting> <value>");
    }

    @Override
    public void call(String[] args) throws CmdException {
        if(args.length != 3) throw new CmdSyntaxError();

        final Hack hack = wurst.getHax().get(args[0]);
        if(hack == null) throw new CmdError("Hack \"" + args[0] + "\" could not be found.");

        final Setting setting = hack.getSettings().get(args[1].toLowerCase().replace("_", " "));
        if(setting == null) throw new CmdError("Setting \"" + args[0] + " " + args[1] + "\" could not be found.");

        if(setting instanceof SliderSetting) {//Slider
            final SliderSetting slider = (SliderSetting)setting;
            if(MathUtils.isDouble(args[2])) {
                slider.setValue(Double.parseDouble(args[2]));
                ChatUtils.message(String.format("%s to: %.1f ", slider.getName(), slider.getValue()));
            } else if(args[2].startsWith("~") && MathUtils.isDouble(args[2].substring(1))) {
                slider.setValue(slider.getValue() + Double.parseDouble(args[2].substring(1)));
                ChatUtils.message(String.format("%s to: %.1f ", slider.getName(), slider.getValue()));
            }
            else throw new CmdSyntaxError("Not a number: " + args[2]);
        } else if(setting instanceof EnumSetting) {//Enum
            final EnumSetting<?> e = (EnumSetting<?>)setting;
            e.setSelected(args[2].replace("_", " "));
            ChatUtils.message(String.format("%s to: %s ", e.getName(), e.getSelected()));
        } else if(setting instanceof CheckboxSetting) {//Boolean
            final CheckboxSetting e = (CheckboxSetting)setting;
            if(!args[2].equalsIgnoreCase("true") && !args[2].equalsIgnoreCase("false")) throw new CmdSyntaxError("Not a boolean: " + args[2]);
            e.setChecked(Boolean.parseBoolean(args[2]));
            ChatUtils.message(String.format("%s to: %s ", e.getName(), e.isChecked()));
        } else if(setting instanceof ScoreSetting) {//Score
            final ScoreSetting slider = (ScoreSetting)setting;
            if(MathUtils.isDouble(args[2])) {
                slider.setScore(Double.parseDouble(args[2]));
                ChatUtils.message(String.format("%s to: %s ", slider.getName(), slider.getScore()));
            }
            else throw new CmdSyntaxError("Not a number: " + args[2]);
        } else if(setting instanceof StringSetting) {//String
            final StringSetting stringSetting = (StringSetting) setting;
            stringSetting.setString(args[2].replaceAll("_", " "));
            ChatUtils.message(String.format("%s to: %s ", stringSetting.getName(), stringSetting.getString()));
        } else {
            throw new CmdError("No set functionality set for this setting.");
        }
        ThirdMod.hax.saveSettings();
    }
}
