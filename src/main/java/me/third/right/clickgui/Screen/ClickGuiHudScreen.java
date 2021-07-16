package me.third.right.clickgui.Screen;

import me.third.right.ThirdMod;
import me.third.right.clickgui.ClickGuiHud;
import me.third.right.modules.Client.ClickGuiHack;
import me.third.right.modules.Render.Shaders;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class ClickGuiHudScreen extends GuiScreen {
    private final ClickGuiHud gui;
    private final ClickGuiHack guiHack;

    public ClickGuiHudScreen(ClickGuiHud gui) {
        this.gui = gui;
        guiHack = ThirdMod.hax.clickGuiHack;
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        gui.handleMouseClick(mouseX, mouseY, mouseButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        gui.render(mouseX, mouseY, partialTicks);
        if(guiHack.bgBlur.isChecked() && !ThirdMod.hax.shaders.isEnabled()) {
            if(mc.entityRenderer.getShaderGroup() == null) {
                mc.entityRenderer.loadShader(new ResourceLocation("minecraft:shaders/post/" + Shaders.ShadersMinecraft.blur + ".json"));
                guiHack.blurEffect = true;
            } else if(mc.entityRenderer.getShaderGroup() != null && !mc.entityRenderer.getShaderGroup().getShaderGroupName().equals("minecraft:shaders/post/" + Shaders.ShadersMinecraft.blur + ".json")) {
                mc.entityRenderer.stopUseShader();
            }
        }
    }

    @Override
    public void onGuiClosed() {
        final ClickGuiHack guiHack = ThirdMod.hax.clickGuiHack;
        if(guiHack.blurEffect) {
            mc.entityRenderer.stopUseShader();
            guiHack.blurEffect = false;
        }
    }
}
