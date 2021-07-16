package me.third.right.utils;

import me.third.right.ThirdMod;
import me.third.right.utils.Client.Font.CFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

public class Wrapper {
    public static Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }
    public static EntityPlayerSP getPlayer() {
        return getMinecraft().player;
    }
    public static World getWorld() {
        return getMinecraft().world;
    }
    public static int getKey(String keyname) {
        return Keyboard.getKeyIndex(keyname.toUpperCase());
    }

    public static FontRenderer getFontRenderer() { return getMinecraft().fontRenderer; }
    public static CFontRenderer getCFontRenderer() { return ThirdMod.cFontRenderer; }
    public static boolean isCustomFont() { return ThirdMod.hax.clickGuiHack.customFont.isChecked(); }
}
