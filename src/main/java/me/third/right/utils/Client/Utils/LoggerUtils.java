package me.third.right.utils.Client.Utils;

import me.third.right.ThirdMod;
import me.third.right.modules.Hack;

public class LoggerUtils {
    public static void moduleLog(Hack module, String message) {
        ThirdMod.log.info(ThirdMod.NAME+" | "+module.getName()+" | "+message);
    }

    public static void logBasic(String message) {
        ThirdMod.log.info(message);
    }

    public static void logDebug(String message) {
        ThirdMod.log.info("DEBUG | "+message);
    }

    public static void logWarning(String message) {
        ThirdMod.log.info("WARNING | "+message);
    }
}
