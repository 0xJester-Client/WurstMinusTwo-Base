package me.third.right.clickgui.Screen;

import me.third.right.utils.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.Session;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class SessionEditor extends GuiScreen {
    protected final Minecraft mc = Minecraft.getMinecraft();
    private final GuiScreen prevScreen;

    private GuiTextField usernameField;
    private GuiTextField playerID;
    private GuiTextField token;
    private GuiTextField sessionType;
    private GuiButton setSession;
    private GuiButton close;

    public SessionEditor(GuiScreen prevScreen) {
        this.prevScreen = prevScreen;
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    public void initGui() {
        final Session session = mc.session;

        //Username
        usernameField = new GuiTextField(1, Wrapper.getFontRenderer(), width / 2 - 100, 60, 200, 20);
        usernameField.setMaxStringLength(Integer.MAX_VALUE);
        usernameField.setText(session.getUsername());
        usernameField.setCanLoseFocus(true);

        playerID = new GuiTextField(1, Wrapper.getFontRenderer(), width / 2 - 100, 80, 200, 20);
        playerID.setMaxStringLength(Integer.MAX_VALUE);
        playerID.setText(session.getPlayerID());
        playerID.setCanLoseFocus(true);

        token = new GuiTextField(1, Wrapper.getFontRenderer(), width / 2 - 100, 100, 200, 20);
        token.setMaxStringLength(Integer.MAX_VALUE);
        token.setText(session.getToken());
        token.setCanLoseFocus(true);

        sessionType = new GuiTextField(1, Wrapper.getFontRenderer(), width / 2 - 100, 120, 200, 20);
        sessionType.setMaxStringLength(Integer.MAX_VALUE);
        sessionType.setText("mojang");
        sessionType.setCanLoseFocus(true);

        buttonList.add(setSession = new GuiButton(1, width / 2 - 100, height / 3 * 2 - 20, " SetSession "));
        buttonList.add(close = new GuiButton(1, width / 2 - 100, height / 3 * 2 + 25, " Close "));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if(button.equals(setSession)) mc.session = new Session(usernameField.getText(), playerID.getText(), token.getText(), sessionType.getText());
        if(button.equals(close)) mc.displayGuiScreen(prevScreen);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if(usernameField.mouseClicked(mouseX, mouseY, mouseButton)) {
            usernameField.setFocused(true);
            playerID.setFocused(false);
            token.setFocused(false);
            sessionType.setFocused(false);
        } else if(playerID.mouseClicked(mouseX, mouseY, mouseButton)) {
            usernameField.setFocused(false);
            playerID.setFocused(true);
            token.setFocused(false);
            sessionType.setFocused(false);
        } else if(token.mouseClicked(mouseX,mouseY,mouseButton)) {
            usernameField.setFocused(false);
            playerID.setFocused(false);
            token.setFocused(true);
            sessionType.setFocused(false);
        } else if(sessionType.mouseClicked(mouseX,mouseY,mouseButton)) {
            usernameField.setFocused(false);
            playerID.setFocused(false);
            token.setFocused(false);
            sessionType.setFocused(true);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if(usernameField.isFocused()) {
            usernameField.textboxKeyTyped(typedChar, keyCode);
        } else if(playerID.isFocused()) {
            playerID.textboxKeyTyped(typedChar, keyCode);
        } else if(token.isFocused()) {
            token.textboxKeyTyped(typedChar, keyCode);
        }  else if(sessionType.isFocused()) {
            sessionType.textboxKeyTyped(typedChar, keyCode);
        } else if(keyCode == Keyboard.KEY_ESCAPE)
            mc.displayGuiScreen(prevScreen);
    }

    @Override
    public void updateScreen()
    {
        if(usernameField.isFocused())
            usernameField.updateCursorCounter();
        if(playerID.isFocused())
            playerID.updateCursorCounter();
        if(token.isFocused())
            token.updateCursorCounter();
        if(sessionType.isFocused())
            sessionType.updateCursorCounter();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(Wrapper.getFontRenderer(), "Session Editor", width / 2, 20, 0xffffff);
        drawString(Wrapper.getFontRenderer(), "Username:", (width / 2) - Wrapper.getFontRenderer().getStringWidth("Username:") - 110, 65, 0xffffff);
        drawString(Wrapper.getFontRenderer(), "UUID:", (width / 2) - Wrapper.getFontRenderer().getStringWidth("UUID:") - 110, 85, 0xffffff);
        drawString(Wrapper.getFontRenderer(), "Token:", (width / 2) - Wrapper.getFontRenderer().getStringWidth("Token:") - 110, 105, 0xffffff);
        drawString(Wrapper.getFontRenderer(), "SessionType (Legacy/Mojang):", (width / 2) - Wrapper.getFontRenderer().getStringWidth("SessionType (Legacy/Mojang):") - 110, 125, 0xffffff);
        usernameField.drawTextBox();
        playerID.drawTextBox();
        token.drawTextBox();
        sessionType.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

}
