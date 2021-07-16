package me.third.right.utils.Render.Image;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ImageBase {
    protected static Minecraft mc = Minecraft.getMinecraft();
    public final ResourceLocation[] images;
    private int x;
    private int y;
    private int width;
    private int height;

    public ImageBase(final int x, final int y, final int width, final int height, final ResourceLocation... images) {
        this.images = images;
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
    }

    public void onTick() {

    }

    public void onRender() {

    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        onTick();
    }

    @SubscribeEvent
    public void onRenderGUI(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL || mc.gameSettings.showDebugInfo) return;
        onRender();
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }
}
