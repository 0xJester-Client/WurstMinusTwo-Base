package me.third.right.utils.Client.Font;

import me.third.right.ThirdMod;
import me.third.right.modules.Client.ClickGuiHack;
import me.third.right.utils.Wrapper;

public class FontDrawing {

    public static void drawString(String text, float x, float y, int color, boolean shadow, boolean customFont){
        if(shadow) {
            if(customFont) Wrapper.getCFontRenderer().drawStringWithShadow(text, x, y, color);
            else Wrapper.getFontRenderer().drawStringWithShadow(text, x, y, color);
        } else {
            if (customFont) Wrapper.getCFontRenderer().drawString(text, x, y, color);
            else Wrapper.getFontRenderer().drawString(text, (int)x, (int)y, color);
        }
    }
    public static void drawString(String text, float x, float y, int color, boolean shadow){
        final ClickGuiHack guiHack = ThirdMod.hax.clickGuiHack;
        drawString(text,x,y, color, shadow, guiHack.customFont.isChecked());
    }
    public static void drawString(String text, float x, float y, int color){
        drawString(text,x,y, color, true);
    }

    //Centered Strings
    public static void drawCenteredString(String text, float x, float y, int color, boolean shadow, boolean customFont) {
        if(shadow) {
            if(customFont) Wrapper.getCFontRenderer().drawStringWithShadow(text, x - (float) getStringWidth(text) / 2, y, color);
            else Wrapper.getFontRenderer().drawStringWithShadow(text, x - (float) getStringWidth(text) / 2, y, color);
        } else {
            if (customFont) Wrapper.getCFontRenderer().drawString(text, x - (float) getStringWidth(text) / 2, y, color);
            else Wrapper.getFontRenderer().drawString(text, (int) (x - getStringWidth(text) / 2), (int)y, color);
        }
    }
    public static void drawCenteredString(String text, float x, float y, int color, boolean shadow) {
        final ClickGuiHack guiHack = ThirdMod.hax.clickGuiHack;
        drawCenteredString(text,x,y, color, shadow, guiHack.customFont.isChecked());
    }
    public static void drawCenteredString(String text, float x, float y, int color){ drawCenteredString(text,x,y, color, true); }

    public static int getStringWidth(String text, boolean customFont) {
        if(customFont) return Wrapper.getCFontRenderer().getStringWidth(text);
        else return Wrapper.getFontRenderer().getStringWidth(text);
    }

    public static int getStringWidth(String text) {
        final ClickGuiHack guiHack = ThirdMod.hax.clickGuiHack;
        return getStringWidth(text, guiHack.customFont.isChecked());
    }

    public static int getStringHeight(String text, boolean customFont) {
        if(customFont) return Wrapper.getCFontRenderer().getStringHeight(text);
        else return Wrapper.getFontRenderer().FONT_HEIGHT;
    }

    public static int getStringHeight(String text) {
        final ClickGuiHack guiHack = ThirdMod.hax.clickGuiHack;
        return getStringHeight(text, guiHack.customFont.isChecked());
    }
}
