package me.third.right.hud.Elements;

import me.third.right.ThirdMod;
import me.third.right.hud.Hud;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class WurstLogo extends Hud {
    private final ResourceLocation logo = new ResourceLocation(ThirdMod.MODID+":textures/wurst-logo.png");

    public WurstLogo() {
        super("WurstLogo");
    }

    @Override
    public void onRender() {
        GL11.glColor4f(1, 1, 1, 1);
        mc.getTextureManager().bindTexture(logo);
        final int width = 94;
        final int height = 24;
        Gui.drawModalRectWithCustomSizedTexture(getX(), getY(), 0, 0, width, height, width, height);
    }
}
