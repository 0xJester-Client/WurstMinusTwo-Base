package me.third.right.clickgui.Settings.Components;

import me.third.right.ThirdMod;
import me.third.right.clickgui.ClickGui;
import me.third.right.clickgui.Settings.Component;
import me.third.right.events.event.SettingsEvent;
import me.third.right.settings.setting.ActionButton;
import me.third.right.utils.Client.Font.CFontRenderer;
import me.third.right.utils.Client.Font.FontDrawing;
import me.third.right.utils.Wrapper;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.opengl.GL11;

public class Action extends Component {
    private final ActionButton actionButton;

    public Action(ActionButton actionButton) {
        this.actionButton = actionButton;
        setWidth(getDefaultWidth());
        setHeight(getDefaultHeight());
    }

    @Override
    public void handleMouseClick(int mouseX, int mouseY, int mouseButton)
    {
        if(mouseButton != 0) return;
        actionButton.getAction().accept(null);
        ThirdMod.EVENT_BUS.post(new SettingsEvent(actionButton));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
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
        if(hHack) gui.setTooltip(actionButton.getDescription());

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

        // hack name
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        final FontRenderer fr = Wrapper.getFontRenderer();
        final CFontRenderer cfr = Wrapper.getCFontRenderer();
        final boolean customFont = ThirdMod.hax.clickGuiHack.customFont.isChecked();
        int fy = y1 + 2;
        final int fx;
        if(customFont) fx = x1 + ((getWidth()) - cfr.getStringWidth(actionButton.getName())) / 2;
        else fx = x1 + ((getWidth()) - fr.getStringWidth(actionButton.getName())) / 2;
        FontDrawing.drawString(actionButton.getName(), fx, fy, 0xf0f0f0);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
    }

    @Override
    public int getDefaultWidth()
    {
        final boolean customFont = ThirdMod.hax.clickGuiHack.customFont.isChecked();
        return customFont ? Wrapper.getCFontRenderer().getStringWidth(actionButton.getName()) + 2 : Wrapper.getFontRenderer().getStringWidth(actionButton.getName()) + 2;
    }

    @Override
    public int getDefaultHeight()
    {
        return 11;
    }
}
