package me.third.right.hud;

import me.third.right.ThirdMod;
import me.third.right.modules.Client.ClickGuiHudHack;
import me.third.right.modules.Hack;
import me.third.right.utils.Client.Font.FontDrawing;
import me.third.right.utils.Client.ClientFiles.WForgeRegistryEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class Hud extends WForgeRegistryEntry<Hud> {
    protected final Minecraft mc = Minecraft.getMinecraft();
    protected final ClickGuiHudHack guiHud = ThirdMod.hax.clickGuiHud;
    private Hack module = null;
    private final String name;
    private int x;
    private int y;
    private int windowWidth;
    private int windowHeight;
    private boolean isPinned = false;

    public Hud(final String name) {
        this.name = name;
    }
    public Hud(final String name, final Hack module) {
        this(name);
        this.module = module;
    }

    public String getName() { return name; }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }

    public int getY() { return y; }
    public void setY(int y) { this.y = y; }

    public boolean requirementsMet() {
        if(module == null) return true;
        return module.isEnabled();
    }

    public boolean isPinned() { return isPinned; }
    public void setPinned(boolean pinned) {
        isPinned = pinned;
        if(isPinned) onPinned();
        else onUnpinned();
    }

    public void setWindowWidth(int windowWidth) { this.windowWidth = windowWidth; }
    public int getWindowWidth() { return windowWidth; }

    public void setWindowHeight(int windowHeight) { this.windowHeight = windowHeight; }
    public int getWindowHeight() { return windowHeight; }

    public void onUpdate() {

    }

    public void onRender() {

    }

    public void onPinned() {

    }

    public void onUnpinned() {

    }

    public void drawString(String text, int x, int y, int color) {
        FontDrawing.drawString(text,x,y,color, guiHud.shadowText.isChecked());
    }
    public void drawCenteredString(String text, int x, int y, int color) {
        FontDrawing.drawCenteredString(text, x, y, color, guiHud.shadowText.isChecked());
    }
}
