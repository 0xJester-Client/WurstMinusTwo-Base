package me.third.right.hud.Elements;

import me.third.right.ThirdMod;
import me.third.right.hud.Hud;
import me.third.right.hud.IngameHUD;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

import static me.third.right.utils.Client.Utils.Colour.rgbToInt;
import static me.third.right.utils.Client.Font.FontDrawing.getStringWidth;

public class TitleHud extends Hud {

    public TitleHud() {
        super("Title");
    }

    @Override
    public void onRender() {
        final boolean rightSide = IngameHUD.getScale().getScaledWidth() / 2 <= getX();
        GL11.glPushMatrix();
        final String text;
        switch (ThirdMod.releaseType) {
            default:
            case Release:
                text = ThirdMod.NAME + " V" + ThirdMod.VERSION;
                break;
            case PreRelease:
                text = ThirdMod.NAME + " \u00a77[\u00a7rPre\u00a77]\u00a7r V" + ThirdMod.VERSION;
                break;
            case Development:
                text = ThirdMod.NAME + " \u00a77[\u00a7rDev\u00a77]\u00a7r V" + ThirdMod.VERSION;
                break;
        }
        drawString(text, rightSide ? (getX() - getStringWidth(text)) + getWindowWidth() :  getX(), this.getY(), guiHud.getRGBInt());
        GL11.glPopMatrix();
    }
}
