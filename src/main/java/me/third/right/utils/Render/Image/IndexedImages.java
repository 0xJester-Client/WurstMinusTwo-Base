package me.third.right.utils.Render.Image;

import me.third.right.utils.Client.Utils.DelayTimer;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class IndexedImages extends ImageBase {
    private final DelayTimer delayTimer = new DelayTimer();
    private int index = 0;
    private int msDelay = 100;
    public IndexedImages(int width, int height, ResourceLocation... images) {
        super(0, 0, width, height, images);
    }
    public IndexedImages(int x, int y, int width, int height, ResourceLocation... images) {
        super(x, y, width, height, images);
    }
    public IndexedImages(int x, int y, int width, int height, int msDelay, ResourceLocation... images) {
        super(x, y, width, height, images);
        this.msDelay = msDelay;
    }

    @Override
    public void onRender() {
        if(images == null || images.length <= 0) return;
        boolean blend = GL11.glGetBoolean(GL11.GL_BLEND);
        GL11.glPushMatrix();
        if(!blend)
            GL11.glEnable(GL11.GL_BLEND);
        GL11.glColor4f(1, 1, 1, 1);
        final ResourceLocation image = images[index];
        mc.getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(getX(), getY(), 0, 0, getWidth(), getHeight(), getWidth(), getHeight());
        if(!blend)
            GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    @Override
    public void onTick() {
        if(images == null || images.length <= 0) return;
        if(delayTimer.passedMs(msDelay)) {
            delayTimer.reset();
            if (index >= images.length - 1) {
                index = 0;
            } else {
                index++;
            }
        }
    }

    public void setMsDelay(int msDelay) {
        this.msDelay = msDelay;
    }
}
