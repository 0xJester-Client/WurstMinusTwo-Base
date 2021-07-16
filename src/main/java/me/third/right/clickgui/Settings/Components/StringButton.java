package me.third.right.clickgui.Settings.Components;

import me.third.right.ThirdMod;
import me.third.right.clickgui.ClickGui;
import me.third.right.clickgui.Screen.EditStringSetting;
import me.third.right.clickgui.Settings.Component;
import me.third.right.events.event.SettingsEvent;
import me.third.right.settings.setting.StringSetting;
import me.third.right.utils.Client.Font.CFontRenderer;
import me.third.right.utils.Client.Font.FontDrawing;
import me.third.right.utils.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.opengl.GL11;

public class StringButton extends Component {

    private final StringSetting setting;

    public StringButton(StringSetting setting) {
        this.setting = setting;
        setWidth(getDefaultWidth());
        setHeight(getDefaultHeight());
    }

    @Override
    public void handleMouseClick(int mouseX, int mouseY, int mouseButton)
    {
        if(mouseButton == 0) {
            Minecraft.getMinecraft().displayGuiScreen(new EditStringSetting(Minecraft.getMinecraft().currentScreen, setting));
            ThirdMod.EVENT_BUS.post(new SettingsEvent(setting));
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        final ClickGui gui = ThirdMod.getForgeWurst().getGui();
        float[] bgColor = gui.getBgColor();
        float[] acColor = gui.getAcColor();
        float opacity = gui.getOpacity();


        int x1 = getX();
        int x2 = x1 + getWidth();
        int x3 = x2;
        int y1 = getY();
        int y2 = y1 + getHeight();

        int scroll = getParent().isScrollingEnabled() ? getParent().getScrollOffset() : 0;
        boolean hovering = mouseX >= x1 && mouseY >= y1 && mouseX < x2 && mouseY < y2 && mouseY >= -scroll && mouseY < getParent().getHeight() - 13 - scroll;
        boolean hHack = hovering && mouseX < x3;

        // tooltip
        if(hHack) gui.setTooltip(setting.getDescription());

        // color
        GL11.glColor4f(bgColor[0], bgColor[1], bgColor[2], hHack ? opacity * 1.5F : opacity);

        // background
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2i(x1, y1);
        GL11.glVertex2i(x1, y2);
        GL11.glVertex2i(x3, y2);
        GL11.glVertex2i(x3, y1);
        GL11.glEnd();

        // outline
        GL11.glColor4f(acColor[0], acColor[1], acColor[2], 0.5F);
        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex2i(x1, y1);
        GL11.glVertex2i(x1, y2);
        GL11.glVertex2i(x2, y2);
        GL11.glVertex2i(x2, y1);
        GL11.glEnd();

        // setting name
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glEnable(GL11.GL_TEXTURE_2D);



        final boolean customFont = ThirdMod.hax.clickGuiHack.customFont.isChecked();
        final int fy = y1 + 2;
        final int fx;
        if(customFont) {
            final CFontRenderer cfr = Wrapper.getCFontRenderer();
            fx = x1 + (getWidth() - cfr.getStringWidth(setting.getName())) / 2;
        } else {
            final FontRenderer fr = Wrapper.getFontRenderer();
            fx = x1 + (getWidth() - fr.getStringWidth(setting.getName())) / 2;
        }
        FontDrawing.drawString(setting.getName(), fx, fy, 0xf0f0f0);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
    }

    @Override
    public int getDefaultWidth()
    {
        final boolean customFont = ThirdMod.hax.clickGuiHack.customFont.isChecked();
        if(customFont) {
            return Wrapper.getCFontRenderer().getStringWidth(setting.getName()) + 13;
        } else {
            return Wrapper.getFontRenderer().getStringWidth(setting.getName()) + 13;
        }
    }

    @Override
    public int getDefaultHeight()
    {
        return 11;
    }
}
