package me.third.right.hud.Elements;

import me.third.right.ThirdMod;
import me.third.right.hud.Hud;
import me.third.right.hud.IngameHUD;
import me.third.right.modules.Hack;
import me.third.right.modules.HackStandard;
import me.third.right.utils.Client.Enums.Category;
import me.third.right.utils.Client.Enums.ReleaseType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

import static me.third.right.utils.Client.Utils.Colour.rgbToInt;
import static me.third.right.utils.Client.Font.FontDrawing.getStringWidth;

public class ArrayListHud extends Hud {
    protected final Minecraft mc = Minecraft.getMinecraft();

    public ArrayListHud() {
        super("ArrayList");
    }

    @Override
    public void onRender() {
        final ScaledResolution scaledResolution = IngameHUD.getScale();
        final boolean rightSide = scaledResolution.getScaledWidth() / 2 <= getX();
        final boolean bottomSide = scaledResolution.getScaledHeight() / 2 <= getY();
        final int colour = guiHud.getRGBInt();
        int y = this.getY();
        final ArrayList<Hack> hacks = new ArrayList<>(ThirdMod.hax.getValues()).stream().filter(Hack::isEnabled).sorted(Comparator.comparing(Hack::getName)).collect(Collectors.toCollection(ArrayList::new));
        for (Hack hack : hacks) {
            if(hack instanceof HackStandard) {
                final HackStandard hackStandard = (HackStandard) hack;
                if (hackStandard.arrayList.isChecked() || hack.getReleaseState().equals(ReleaseType.Hidden)) continue;
                final String hud = hack.hudInfo();
                final String display;
                if (hud == null || hud.isEmpty()) display = hack.getRenderName();
                else display = rightSide ? String.format("\u00a77[\u00a7f%s\u00a77]\u00a7r %s", hack.hudInfo(), hack.getRenderName()) : String.format("%s \u00a77[\u00a7f%s\u00a77]", hack.getRenderName(), hack.hudInfo());
                drawString(display, rightSide ? (getX() - getStringWidth(display)) + getWindowWidth() : getX(), y, colour);
                if (bottomSide) y -= 10;
                else y += 10;
            }
        }
    }
}
