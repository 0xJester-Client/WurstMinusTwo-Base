package me.third.right.hud.Elements;

import me.third.right.hud.Hud;
import me.third.right.hud.IngameHUD;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;

import static me.third.right.utils.Render.TwoDRender.postItemRender;
import static me.third.right.utils.Render.TwoDRender.preItemRender;

public class ArmourHud extends Hud {

    public ArmourHud() {
        super("Armour");
    }

    @Override
    public void onRender() {
        final float percentagePos = ((float) getX() /  (float) IngameHUD.getScale().getScaledWidth()) * 100;
        boolean horizontal = percentagePos <= 25 || percentagePos >= 75;
        boolean farRight = percentagePos >= 75;
        int x = farRight ? getX() + getWindowWidth() - 18 :  getX();
        int y = horizontal ? getY() + getWindowHeight() + 36 : getY();
        for (int i = 0; i != 4; i++) {
            final ItemStack stack = mc.player.inventory.armorItemInSlot(i);
            preItemRender();
            mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
            mc.getRenderItem().renderItemOverlays(mc.fontRenderer, stack, x, y);
            postItemRender();
            if(horizontal) {
                y -= 18;
            } else {
                x += 18;
            }
        }
    }
}
