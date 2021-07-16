package me.third.right.clickgui.Screen;

import me.third.right.ThirdMod;
import me.third.right.clickgui.ClickGui;
import me.third.right.modules.Render.MainMenuMods;
import me.third.right.utils.Render.ThreeDRender;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

import static me.third.right.utils.Client.Utils.Colour.rgbToInt;

public final class ClickGuiScreenMainMenu extends GuiScreen {
    private final ClickGui gui;
    private final GuiScreen prevScreen;

    public ClickGuiScreenMainMenu(final GuiScreen prevScreen, final ClickGui gui) {
        this.gui = gui;
        this.prevScreen = prevScreen;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        gui.handleMouseClick(mouseX, mouseY, mouseButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        //Draw Background
        final MainMenuMods mainMenuMods = ThirdMod.hax.mainMenuMods;
        final int[] rgb = mainMenuMods.getRGB();
        switch (mainMenuMods.backGroundMode.getSelected()) {
            default:
            case Colour:
                drawRect(0,0, width, height, rgbToInt(rgb[0],rgb[1],rgb[2], 255));
                break;
            case Gradient:
                final int[] rgb1 = mainMenuMods.getRGBGradiant();
                drawGradientRect(0,0, width, height, rgbToInt(rgb[0],rgb[1],rgb[2], 255), rgbToInt(rgb1[0],rgb1[1],rgb1[2], 255));
                break;
        }
        //Render ClickGui Part 1
        gui.render(mouseX, mouseY, partialTicks);
        ThirdMod.gui.updateColors();
        ThreeDRender.release();
    }

    @Override
    protected void keyTyped(char p_keyTyped_1_, int key) {
        if(key == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(prevScreen);
        } else if(isShiftKeyDown() && key == Keyboard.KEY_S) {
            mc.displayGuiScreen(new SessionEditor(this));
        }
    }
}
