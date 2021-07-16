package me.third.right.mixin.replacement;

import me.third.right.ThirdMod;
import me.third.right.hud.Elements.Chat;
import me.third.right.modules.Render.ChatMods;
import me.third.right.utils.Client.Font.FontDrawing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;

import static me.third.right.utils.Client.Utils.Colour.rgbToInt;

public class ThirdGUINewChat extends GuiNewChat {
    private int x;
    private int y;

    public ThirdGUINewChat(Minecraft minecraft) {
        super(minecraft);
    }

    @Override
    public void drawChat(int p_drawChat_1_) {
        if (this.mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN) {
            final ChatMods chatMods = ThirdMod.hax.chatMods;
            final Chat chat = ThirdMod.hud1.chat;
            final ScaledResolution scaledResolution = new ScaledResolution(mc);
            if(chat.isPinned()) {
                x = chat.getX();
                y = (chat.getY() - (scaledResolution.getScaledHeight() - 40 - chat.getWindowHeight()));
            }
            //###
            int lineCount = this.getLineCount();
            int displayCount = this.drawnChatLines.size();
            float opacity = this.mc.gameSettings.chatOpacity * 0.9F + 0.1F;
            if (displayCount > 0) {
                boolean chatOpen = false;
                if (this.getChatOpen()) chatOpen = true;
                float chatScale = this.getChatScale();
                int width = MathHelper.ceil((float)this.getChatWidth() / chatScale);//Not 100% sure.
                GlStateManager.pushMatrix();
                GlStateManager.translate(2.0F, 8.0F, 0.0F);
                GlStateManager.scale(chatScale, chatScale, 1.0F);

                int messageIndex;
                int update;//Used to update the opacity
                int rgbAlpha;
                for(messageIndex = 0; messageIndex + this.scrollPos < this.drawnChatLines.size() && messageIndex < lineCount; ++messageIndex) {
                    ChatLine message = this.drawnChatLines.get(messageIndex + this.scrollPos);
                    if (message != null) {
                        update = p_drawChat_1_ - message.getUpdatedCounter();
                        if (update < 200 || chatOpen) {
                            double idk = (double)update / 200.0D;//Its used for the opacity calc.
                            idk = 1.0D - idk;
                            idk *= 10.0D;
                            idk = MathHelper.clamp(idk, 0.0D, 1.0D);
                            idk *= idk;
                            rgbAlpha = (int)(255.0D * idk);
                            if (chatOpen) rgbAlpha = 255;

                            rgbAlpha = (int)((float)rgbAlpha * opacity);
                            if (rgbAlpha > 3) {
                                int yPos = -messageIndex * 9;
                                int[] rgb = chatMods.getColourRect();
                                int colourInt = chatMods.customRectColour.isChecked() ? rgbToInt(rgb[0], rgb[1], rgb[2], rgb[3]) : rgbAlpha / 2 << 24;
                                if(chat.isPinned()) drawRect(x -2,y + yPos - 9,x + width + 4,y + yPos, colourInt);
                                else drawRect(-2, yPos - 9, width + 4, yPos, colourInt);
                                String messageString = message.getChatComponent().getFormattedText();
                                GlStateManager.enableBlend();
                                float posX = chat.isPinned() ? x : 0.0F;
                                float posY = chat.isPinned() ? (float) y + (yPos - 8) : (float) (yPos - 8);
                                rgb = chatMods.getColour();
                                colourInt = chatMods.customTextColour.isChecked() ? rgbToInt(rgb[0], rgb[1], rgb[2]) : 16777215 + (rgbAlpha << 24);
                                FontDrawing.drawString(messageString, posX, posY, colourInt, true, chatMods.customFont.isChecked());
                                GlStateManager.disableAlpha();
                                GlStateManager.disableBlend();
                            }
                        }
                    }
                }
                GlStateManager.popMatrix();
            }
        }
    }

    @Nullable
    @Override
    public ITextComponent getChatComponent(int p_getChatComponent_1_, int p_getChatComponent_2_) {
        if (!this.getChatOpen()) {
            return null;
        } else {
            ScaledResolution resolution = new ScaledResolution(this.mc);
            final Chat chat = ThirdMod.hud1.chat;
            int scaleFactor = resolution.getScaleFactor();
            float scale = this.getChatScale();
            int mousePosX = p_getChatComponent_1_ / scaleFactor - 2;
            int mousePosY = p_getChatComponent_2_ / scaleFactor - 40;
            mousePosX = MathHelper.floor((float) mousePosX / scale);
            mousePosY = MathHelper.floor((float) mousePosY / scale);
            if (mousePosX >= 0 && mousePosY >= 0) {
                int lineCount = Math.min(this.getLineCount(), this.drawnChatLines.size());

                //Top Bounds
                int boundX = MathHelper.floor((float) this.getChatWidth() / this.getChatScale());
                int boundY = this.mc.fontRenderer.FONT_HEIGHT * lineCount + lineCount;
                if(chat.isPinned()) {
                    boundX += x;
                    boundY += chat.getY();
                }
                //Bottom Bounds
                int boundX1 = x;
                int boundY1 = chat.getY();
                boolean boundsCheck = chat.isPinned() ? boundX1 < mousePosX && boundY1 < mousePosY && mousePosX <= boundX && mousePosY < boundY : mousePosX <= boundX && mousePosY < boundY;
                //ChatUtils.message("BB1:"+boundX+" Y:"+boundY+" | "+boundX1+" Y:"+boundY1);

                if (boundsCheck) {
                    int messageIndex = mousePosY / this.mc.fontRenderer.FONT_HEIGHT + this.scrollPos;
                    if (messageIndex >= 0 && messageIndex < this.drawnChatLines.size()) {
                        ChatLine message = (ChatLine) this.drawnChatLines.get(messageIndex);
                        int width = 0;

                        for (ITextComponent component : message.getChatComponent()) {
                            if (component instanceof TextComponentString) {
                                width += this.mc.fontRenderer.getStringWidth(GuiUtilRenderComponents.removeTextColorsIfConfigured(((TextComponentString) component).getText(), false));
                                if (width > mousePosX) {
                                    return component;
                                }
                            }
                        }
                    }
                    return null;
                } else return null;
            } else return null;
        }
    }
}
