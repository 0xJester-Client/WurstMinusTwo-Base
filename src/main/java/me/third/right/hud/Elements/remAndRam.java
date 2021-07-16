package me.third.right.hud.Elements;

import me.third.right.ThirdMod;
import me.third.right.hud.Hud;
import me.third.right.utils.Render.Image.IndexedImages;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class remAndRam extends Hud {
    private final IndexedImages indexedImages = new IndexedImages(0,0,126,32,
            new ResourceLocation(ThirdMod.MODID+":textures/frame1.png"),
            new ResourceLocation(ThirdMod.MODID+":textures/frame2.png"),
            new ResourceLocation(ThirdMod.MODID+":textures/frame3.png"),
            new ResourceLocation(ThirdMod.MODID+":textures/frame4.png"),
            new ResourceLocation(ThirdMod.MODID+":textures/frame5.png"),
            new ResourceLocation(ThirdMod.MODID+":textures/frame6.png"),
            new ResourceLocation(ThirdMod.MODID+":textures/frame7.png"),
            new ResourceLocation(ThirdMod.MODID+":textures/frame8.png"),
            new ResourceLocation(ThirdMod.MODID+":textures/frame9.png"),
            new ResourceLocation(ThirdMod.MODID+":textures/frame10.png"),
            new ResourceLocation(ThirdMod.MODID+":textures/frame11.png"),
            new ResourceLocation(ThirdMod.MODID+":textures/frame12.png"),
            new ResourceLocation(ThirdMod.MODID+":textures/frame13.png"),
            new ResourceLocation(ThirdMod.MODID+":textures/frame14.png"));

    public remAndRam() {
        super("RemAndRam");
    }

    @Override
    public void onRender() {
        indexedImages.setX(getX());
        indexedImages.setY(getY());
        indexedImages.onRender();
    }

    @Override
    public void onUpdate() {
        indexedImages.onTick();
    }
}
