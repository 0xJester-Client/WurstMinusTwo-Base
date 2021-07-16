package me.third.right.hud.Elements;

import me.third.right.ThirdMod;
import me.third.right.hud.Hud;
import me.third.right.hud.IngameHUD;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;

import static me.third.right.utils.Client.Utils.Colour.rgbToInt;
import static me.third.right.utils.Client.Font.FontDrawing.getStringWidth;

public class PlayerList extends Hud {

    public PlayerList() {
        super("PlayerList");
    }

    @Override
    public void onRender() {
        final ScaledResolution scaledResolution = IngameHUD.getScale();
        final boolean rightSide = scaledResolution.getScaledWidth() / 2 <= getX();
        final boolean bottomSide = scaledResolution.getScaledHeight() / 2 <= getY();
        final int[] rgb = guiHud.getTextRGB();
        int y = getY();
        for(Entity entity : mc.world.getLoadedEntityList()) {
            if(entity == null || entity.isDead || !(entity instanceof EntityPlayer) || entity == mc.player) continue;
            final EntityPlayer player = (EntityPlayer) entity;
            final String text;
            if(ThirdMod.friends.isInTheList(player))
                text = String.format("\u00a72%s \u00a77(\u00a72%d\u00a77) [\u00a7r%.1f\u00a77]", player.getDisplayNameString(), (int) (player.getHealth() + player.getAbsorptionAmount()), mc.player.getDistance(player));
            else if(ThirdMod.enemies.isInTheList(player))
                text = String.format("\u00a74%s \u00a77(\u00a72%d\u00a77) [\u00a7r%.1f\u00a77]", player.getDisplayNameString(), (int) (player.getHealth() + player.getAbsorptionAmount()), mc.player.getDistance(player));
            else text = String.format("%s \u00a77(\u00a72%d\u00a77) [\u00a7r%.1f\u00a77]", player.getDisplayNameString(), (int) (player.getHealth() + player.getAbsorptionAmount()), mc.player.getDistance(player));
            drawString(text, rightSide ? (getX() - getStringWidth(text)) + getWindowWidth() : getX(), y, rgbToInt(rgb[0], rgb[1], rgb[2]));
            if(bottomSide) y -= 10;
            else y += 10;
        }
    }
}
