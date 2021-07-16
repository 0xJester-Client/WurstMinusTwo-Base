package me.third.right.hud.Elements;

import me.third.right.hud.Hud;
import me.third.right.hud.IngameHUD;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.math.Vec3d;

import static me.third.right.utils.Client.Utils.Colour.rgbToInt;
import static me.third.right.utils.Client.Font.FontDrawing.getStringWidth;

public class CoordsHud extends Hud {
    public CoordsHud() {
        super("Coords");
    }

    @Override
    public void onRender() {
        final boolean rightSide = IngameHUD.getScale().getScaledWidth() / 2 <= getX();
        final Vec3d pos = mc.player.getPositionVector();
        String text = null;
        final int[] rgb = guiHud.getTextRGB();
        if(mc.player.dimension == 0)
            text = String.format("X\u00a77:\u00a7r%.1f Y\u00a77:\u00a7r%.1f Z\u00a77:\u00a7r%.1f \u00a77[\u00a7rX\u00a77:\u00a7r%.1f Z\u00a77:\u00a7r%.1f\u00a77]\u00a7r", pos.x, pos.y, pos.z, pos.x / 8, pos.z / 8);
        else if(mc.player.dimension == 1)
            text = String.format("X\u00a77:\u00a7r%.1f Y\u00a77:\u00a7r%.1f Z\u00a77:\u00a7r%.1f", pos.x, pos.y, pos.z);
        else
            text = String.format("X\u00a77:\u00a7r%.1f Y\u00a77:\u00a7r%.1f Z\u00a77:\u00a7r%.1f \u00a77[\u00a7rX\u00a77:\u00a7r%.1f Z\u00a77:\u00a7r%.1f\u00a77]\u00a7r", pos.x, pos.y, pos.z, pos.x * 8, pos.z * 8);
        if(text != null && !text.isEmpty()) {
            drawString(text, rightSide ? (getX() - getStringWidth(text)) + getWindowWidth() : getX(), getY(), rgbToInt(rgb[0], rgb[1], rgb[2]));
        }
    }
}
