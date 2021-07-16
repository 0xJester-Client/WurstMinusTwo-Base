package me.third.right.clickgui.Screen;

import me.third.right.ThirdMod;
import me.third.right.modules.Other.AutoReconnect;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.client.FMLClientHandler;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class GuiDisconnected extends GuiScreen {
    public final String reason;
    public final ITextComponent message;
    public List<String> multilineMessage;
    public final GuiScreen parentScreen;
    public int textHeight;

    public GuiDisconnected(GuiScreen parentScreen, String reason, ITextComponent message) {
        this.parentScreen = parentScreen;
        this.reason = I18n.format(reason, new Object[0]);
        this.message = message;
    }

    protected void keyTyped(char p_keyTyped_1_, int p_keyTyped_2_) throws IOException {
    }

    public void initGui() {
        final AutoReconnect autoReconnect = ThirdMod.hax.autoReconnect;
        AutoReconnect.autoReconnectWaitTime = autoReconnect.delay.getValueI();
        this.buttonList.clear();
        this.multilineMessage = this.fontRenderer.listFormattedStringToWidth(this.message.getFormattedText(), this.width - 50);
        this.textHeight = this.multilineMessage.size() * this.fontRenderer.FONT_HEIGHT;
        this.buttonList.add(new GuiButton(0,
                this.width / 2 - 100,
                Math.min(this.height / 2 + this.textHeight / 2 + this.fontRenderer.FONT_HEIGHT, this.height - 30),
                I18n.format("gui.toMenu", new Object[0])));
        this.buttonList.add(AutoReconnect.reconnectButton = new GuiButton(1005,
                this.width / 2 - 100, Math.min(this.height / 2 + this.textHeight / 2 + this.fontRenderer.FONT_HEIGHT + 20,
                this.height - 30),
                "Reconnect"));
        this.buttonList.add(AutoReconnect.autoReconnectButton = new GuiButton(1006,
                this.width / 2 - 100,
                Math.min(this.height / 2 + this.textHeight / 2 + this.fontRenderer.FONT_HEIGHT + 40, this.height - 30),
                String.format("AutoReconnect (%s\u00A7r)", autoReconnect.isEnabled() ? "\u00A7a"+AutoReconnect.autoReconnectWaitTime : "\u00A7cDisabled" )));
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        final AutoReconnect autoReconnect = ThirdMod.hax.autoReconnect;
        if (button.id == 0) {
            this.mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));//Big brain fix
        } else if (button.id == 1005) {
            ServerData data = new ServerData("", autoReconnect.getIp()+':'+ autoReconnect.getPort(), false);
            data.setResourceMode(ServerData.ServerResourceMode.PROMPT);
            FMLClientHandler.instance().connectToServer(this.mc.currentScreen, data);
        } else if (button.id == 1006) {
            if (autoReconnect.isEnabled()) {
                autoReconnect.disable();
            } else {
                autoReconnect.enable();
            }
            AutoReconnect.autoReconnectButton.displayString = String.format("AutoReconnect (%s\u00A7r)", autoReconnect.isEnabled() ? "\u00A7a"+AutoReconnect.autoReconnectWaitTime : "\u00A7cDisabled" );
        }
    }

    public void drawScreen(int p_drawScreen_1_, int p_drawScreen_2_, float p_drawScreen_3_) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, this.reason, this.width / 2, this.height / 2 - this.textHeight / 2 - this.fontRenderer.FONT_HEIGHT * 2, 11184810);
        int lvt_4_1_ = this.height / 2 - this.textHeight / 2;
        if (this.multilineMessage != null) {
            for(Iterator var5 = this.multilineMessage.iterator(); var5.hasNext(); lvt_4_1_ += this.fontRenderer.FONT_HEIGHT) {
                String lvt_6_1_ = (String)var5.next();
                this.drawCenteredString(this.fontRenderer, lvt_6_1_, this.width / 2, lvt_4_1_, 16777215);
            }
        }

        super.drawScreen(p_drawScreen_1_, p_drawScreen_2_, p_drawScreen_3_);
    }
}
