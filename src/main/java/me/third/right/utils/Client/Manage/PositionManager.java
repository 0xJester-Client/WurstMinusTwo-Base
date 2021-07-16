package me.third.right.utils.Client.Manage;

import net.minecraft.client.Minecraft;

public class PositionManager {
    //PositionManager from phobos and wurst+2
    protected static Minecraft mc = Minecraft.getMinecraft();
    private static double x;
    private static double y;
    private static double z;
    private static boolean onGround;

    public static void getPosition(){
        x = mc.player.posX;
        y = mc.player.posY;
        z = mc.player.posZ;
        onGround = mc.player.onGround;
    }

    public static void resetPosition(){
        mc.player.posX = x;
        mc.player.posY = y;
        mc.player.posZ = z;
        mc.player.onGround = onGround;
    }

    public static void setPosition(final double x, final double y, final double z) {
        mc.player.posX = x;
        mc.player.posY = y;
        mc.player.posZ = z;
    }
}
