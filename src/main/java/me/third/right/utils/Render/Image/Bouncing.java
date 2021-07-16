package me.third.right.utils.Render.Image;

import me.third.right.utils.Client.Utils.MathUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class Bouncing extends ImageBase {
    private int xSpeed = 5;
    private int ySpeed = 5;
    private float alpha = 1f;
    private int index = 0;
    private onHitMode onHit = onHitMode.None;
    public enum onHitMode { None, NextFrame, Colour }

    public Bouncing(final int width, final int height, final ResourceLocation... images) {
        super(MathUtils.newRandomNumberInt(2,200), MathUtils.newRandomNumberInt(2,200),width,height,images);
    }
    public Bouncing(final int x, final int y, final int width, final int height, final ResourceLocation... images) {
        super(x,y,width,height,images);
    }
    public Bouncing(final int x, final int y, final int width, final int height, final float alpha, final ResourceLocation... images) {
        super(x,y,width,height,images);
        this.alpha = alpha;
    }

    public void setOnHit(onHitMode onHit) {
        this.onHit = onHit;
    }

    private void hitCheck(final int width, final int height) {
        final ScaledResolution sr = new ScaledResolution(mc);
        if(getX()+width >= sr.getScaledWidth() || getX() <= 0){
            xSpeed *= -1;
            onHit();
        }
        if(getY()+height >= sr.getScaledHeight() || getY() <= 0) {
            ySpeed *= -1;
            onHit();
        }
    }

    private void onHit() {

        switch (onHit) {
            default:
            case None:
                break;
            case Colour:
//        try {
//            final IResource resource = mc.getResourceManager().getResource(images[0]);
//            final BufferedImage bufferedImage = TextureUtil.readBufferedImage(resource.getInputStream());
//            final DynamicTexture dynamicTexture = new DynamicTexture(bufferedImage);
//            dynamicTexture.loadTexture(mc.getResourceManager());
//            images[0] = mc.getTextureManager().getDynamicTextureLocation("dvd", dynamicTexture);
//        } catch (IOException | NullPointerException e) {
//            ThirdMod.log.info(e);
//        }
                break;
            case NextFrame:
                if (index >= images.length - 1) {
                    index = 0;
                } else {
                    index++;
                }
                break;
        }
    }

    @Override
    public void onTick() {
        setX(getX() + xSpeed);
        setY(getY() + ySpeed);
        hitCheck(getWidth(), getHeight());
    }

    @Override
    public void onRender() {
        boolean blend = GL11.glGetBoolean(GL11.GL_BLEND);
        GL11.glPushMatrix();
        if(!blend) GL11.glEnable(GL11.GL_BLEND);
        GL11.glColor4f(1, 1, 1, alpha);
        final ResourceLocation image = images[index];
        mc.getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(getX(), getY(), 0, 0, getWidth(), getHeight(), getWidth(), getHeight());
        if(!blend) GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }
}
