package me.third.right.clickgui.Screen;

import me.third.right.settings.setting.StringSetting;
import me.third.right.utils.Wrapper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class EditStringSetting extends GuiScreen {

    private final GuiScreen prevScreen;
    private final StringSetting string;

    private GuiTextField valueField;
    private GuiButton doneButton;

    public EditStringSetting(GuiScreen prevScreen, StringSetting string)
    {
        this.prevScreen = prevScreen;
        this.string = string;
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    public void initGui()
    {
        valueField = new GuiTextField(1, Wrapper.getFontRenderer(), width / 2 - 100, 60, 200, 20);
        valueField.setMaxStringLength(string.getMaxLength());
        valueField.setText(string.getString());
        valueField.setSelectionPos(0);
        valueField.setFocused(true);

        buttonList.add(doneButton = new GuiButton(0, width / 2 - 100, height / 3 * 2, "Done"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        String value = valueField.getText();
        if(!value.isEmpty()) string.setString(value);
        mc.displayGuiScreen(prevScreen);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        valueField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        valueField.textboxKeyTyped(typedChar, keyCode);

        if(keyCode == Keyboard.KEY_RETURN)
            actionPerformed(doneButton);
        else if(keyCode == Keyboard.KEY_ESCAPE)
            mc.displayGuiScreen(prevScreen);
    }

    @Override
    public void updateScreen()
    {
        valueField.updateCursorCounter();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        drawDefaultBackground();
        drawCenteredString(Wrapper.getFontRenderer(), string.getName(), width / 2, 20, 0xffffff);
        valueField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
