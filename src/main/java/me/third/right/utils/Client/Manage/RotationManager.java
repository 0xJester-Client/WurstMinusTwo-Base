package me.third.right.utils.Client.Manage;

import net.minecraft.client.Minecraft;

public class RotationManager {
    //Rotation manager based off phobos and wurst+2
    protected static Minecraft mc = Minecraft.getMinecraft();
    private static float prevYaw;
    private static float prevPitch;
    private static boolean isRotating = false;

    //used to reset the rotations
    public static void resetRotation(){
        if(isRotating) {
            mc.player.rotationPitch = prevPitch;
            mc.player.rotationYawHead = prevYaw;
            mc.player.rotationYaw = prevYaw;
            isRotating = false;
        }
    }

    //used to get the current rotations
    public static void getRotation() {
        prevPitch = mc.player.rotationPitch;
        prevYaw = mc.player.rotationYaw;
    }

    public static void setRotations(final float pitch, final float yaw){
        mc.player.rotationPitch = pitch;
        mc.player.rotationYawHead = yaw;
        mc.player.rotationYaw = yaw;
        isRotating = true;
    }

    public static float[] getAngles() {
        return new float[] {prevPitch, prevYaw};
    }

    public static boolean isRotating() {
        return isRotating;
    }
}
