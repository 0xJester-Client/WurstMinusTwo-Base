package me.third.right.utils.Render.Image;

import me.third.right.utils.Client.Utils.DelayTimer;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class Flashing extends ImageBase {
    private boolean stage = false;
    private final DelayTimer delayTimer = new DelayTimer();
    private final int displayTime;
    private final int invisibleTime;

    public Flashing(final int displayTime, final int invisibleTime, final int x, final int y, final int width, final int height, final ResourceLocation... images) {
        super(x,y,width,height,images);
        this.displayTime = displayTime;
        this.invisibleTime = invisibleTime;
    }

    @Override
    public void onRender() {
        if(stage) {
            GL11.glPushMatrix();
            GL11.glColor4f(1, 1, 1, 1);
            final ResourceLocation image = images[0];
            mc.getTextureManager().bindTexture(image);
            Gui.drawModalRectWithCustomSizedTexture(getX(), getY(), 0, 0, getWidth(), getHeight(), getWidth(), getHeight());
            GL11.glPopMatrix();
            if(delayTimer.passedMs(displayTime)) {
                delayTimer.reset();
                stage = !stage;
            }
        } else {
            if(delayTimer.passedMs(invisibleTime)) {
                delayTimer.reset();
                stage = !stage;
            }
        }
    }
}
