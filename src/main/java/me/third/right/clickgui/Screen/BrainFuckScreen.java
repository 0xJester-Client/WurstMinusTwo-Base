package me.third.right.clickgui.Screen;

import me.third.right.utils.Client.Utils.ChatUtils;
import me.third.right.utils.Wrapper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class BrainFuckScreen extends GuiScreen {
    private GuiTextField editorField;
    private GuiTextField inputField;

    private GuiButton close;
    private GuiButton run;
    private GuiButton enter;

    public BrainFuckScreen()
    {
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    public void initGui() {
        editorField = new GuiTextField(1, Wrapper.getFontRenderer(), width / 2 - 100, height / 3, 400, 60);
        editorField.setMaxStringLength(Integer.MAX_VALUE);
        editorField.setText("Editor");
        editorField.setSelectionPos(0);
        editorField.canLoseFocus = true;

        inputField = new GuiTextField(1, Wrapper.getFontRenderer(), width / 2 - 100, 60, 200, 20);
        inputField.setMaxStringLength(Integer.MAX_VALUE);
        inputField.setText("Input");
        inputField.setSelectionPos(0);
        inputField.canLoseFocus = true;

        buttonList.add(close = new GuiButton(0, width / 2 - 100 - 200, height / 3 * 2 - 20, "Close"));
        buttonList.add(run = new GuiButton(0, width / 2 - 100, height / 3 * 2 - 20, "Run"));
        buttonList.add(enter = new GuiButton(0, width / 2 - 100 + 200, height / 3 * 2 - 20, "Enter"));
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if(button.equals(close))
            mc.displayGuiScreen(null);
        else if(button.equals(run)) {
            ChatUtils.debug("Run BrainFuck.");
        } else if(button.equals(enter)) {
            ChatUtils.debug("Enter User Input.");
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if(editorField.mouseClicked(mouseX, mouseY, mouseButton)) {
            editorField.setFocused(true);
        } else if(inputField.mouseClicked(mouseX, mouseY, mouseButton)) {
            inputField.setFocused(true);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if(editorField.isFocused()) {
            editorField.textboxKeyTyped(typedChar, keyCode);
        } else if(inputField.isFocused) {
            inputField.textboxKeyTyped(typedChar, keyCode);
        } else if(keyCode == Keyboard.KEY_ESCAPE)
            mc.displayGuiScreen(null);
    }

    @Override
    public void updateScreen()
    {
        if(editorField.isFocused())
            editorField.updateCursorCounter();
        if(inputField.isFocused())
            inputField.updateCursorCounter();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        drawDefaultBackground();
        drawCenteredString(Wrapper.getFontRenderer(), "BrainFuck", width / 2, 20, 0xffffff);
        editorField.drawTextBox();
        inputField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

}
