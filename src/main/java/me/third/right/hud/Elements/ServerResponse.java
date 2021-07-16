package me.third.right.hud.Elements;

import me.third.right.clickgui.Screen.ClickGuiHudScreen;
import me.third.right.hud.Hud;
import me.third.right.hud.IngameHUD;
import me.third.right.utils.Client.Manage.LagDetection;
import net.minecraft.client.gui.ScaledResolution;

import static me.third.right.utils.Client.Font.FontDrawing.getStringWidth;

public class ServerResponse extends Hud {

    public ServerResponse() {
        super("ServerResponse");
    }

    @Override
    public void onRender() {
        final float percentagePos = ((float) getX() /  (float) IngameHUD.getScale().getScaledWidth()) * 100;
        final boolean middle = percentagePos > 25 && percentagePos < 75;
        final boolean right = percentagePos >= 75;
        final LagDetection lag = LagDetection.INSTANCE;
        final boolean clickHudOpen = (mc.currentScreen != null && (mc.currentScreen instanceof ClickGuiHudScreen));
        if(!lag.isResponding() || clickHudOpen) {
            final String text = "Server Not Responding! "+ (clickHudOpen ? "X" : lag.timeDifference());
            if(middle) drawCenteredString(text, getX() + getWindowWidth() / 2, getY(), guiHud.getRGBInt());
            else drawString(text, right ? (getX() - getStringWidth(text)) + getWindowWidth() :  getX(), getY(), guiHud.getRGBInt());
        }
    }
}
