package me.third.right.hud.Elements;

import me.third.right.hud.Hud;
import me.third.right.hud.IngameHUD;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import static me.third.right.utils.Client.Font.FontDrawing.getStringWidth;

public class PotionEffects extends Hud {

    public PotionEffects() {
        super("PotionEffects");
    }

    @Override
    public void onRender() {
        final ScaledResolution scaledResolution = IngameHUD.getScale();
        final boolean rightSide = scaledResolution.getScaledWidth() / 2 <= getX();
        final boolean bottomSide = scaledResolution.getScaledHeight() / 2 <= getY();
        int y = getY();
        for(PotionEffect effects : mc.player.getActivePotionEffects()) {
            String effectName = I18n.format(effects.getPotion().getName());
            if (effects.getAmplifier() == 1) effectName+= " " + I18n.format("enchantment.level.2");
            else if (effects.getAmplifier() == 2) effectName+= " " + I18n.format("enchantment.level.3");
            else if (effects.getAmplifier() == 3) effectName+= " " + I18n.format("enchantment.level.4");
            final String text = String.format("%s: %s", effectName, Potion.getPotionDurationString(effects,1.0f));
            drawString(text, rightSide ? (getX() - getStringWidth(text)) + getWindowWidth() : getX(), y,  effects.getPotion().getLiquidColor());
            if(bottomSide) y -= 10;
            else y += 10;
        }
    }
}
