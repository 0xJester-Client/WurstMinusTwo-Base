package me.third.right.mixin.replacement;

import com.mojang.authlib.GameProfile;
import me.third.right.ThirdMod;
import me.third.right.modules.Render.ExtraTab;
import me.third.right.utils.Client.Font.FontDrawing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

import static me.third.right.utils.Client.Utils.Colour.getColourRGB;
import static me.third.right.utils.Client.Utils.Colour.rgbToInt;
import static me.third.right.utils.Client.Font.FontDrawing.getStringWidth;

public class ThirdGUIPlayerTabOverlay extends GuiPlayerTabOverlay {

    public ThirdGUIPlayerTabOverlay(Minecraft minecraft, GuiIngame guiIngame) {
        super(minecraft, guiIngame);
    }

    @Override
    public void renderPlayerlist(int p_renderPlayerlist_1_, Scoreboard p_renderPlayerlist_2_, @Nullable ScoreObjective p_renderPlayerlist_3_) {
        final ExtraTab extraTab = ThirdMod.hax.extraTab;
        NetHandlerPlayClient network = this.mc.player.connection;
        List<NetworkPlayerInfo> infoList = ENTRY_ORDERING.sortedCopy(network.getPlayerInfoMap());
        int lvt_6_1_ = 0;
        int lvt_7_1_ = 0;
        Iterator var8 = infoList.iterator();

        int lvt_10_2_;
        while(var8.hasNext()) {
            NetworkPlayerInfo lvt_9_1_ = (NetworkPlayerInfo)var8.next();
            lvt_10_2_ = FontDrawing.getStringWidth(this.getPlayerName(lvt_9_1_));
            lvt_6_1_ = Math.max(lvt_6_1_, lvt_10_2_);
            if (p_renderPlayerlist_3_ != null && p_renderPlayerlist_3_.getRenderType() != IScoreCriteria.EnumRenderType.HEARTS) {
                lvt_10_2_ = FontDrawing.getStringWidth(" " + p_renderPlayerlist_2_.getOrCreateScore(lvt_9_1_.getGameProfile().getName(), p_renderPlayerlist_3_).getScorePoints());
                lvt_7_1_ = Math.max(lvt_7_1_, lvt_10_2_);
            }
        }

        infoList = infoList.subList(0, Math.min(infoList.size(), 80));
        int lvt_8_1_ = infoList.size();
        int lvt_9_2_ = lvt_8_1_;

        for(lvt_10_2_ = 1; lvt_9_2_ > 20; lvt_9_2_ = (lvt_8_1_ + lvt_10_2_ - 1) / lvt_10_2_) {
            ++lvt_10_2_;
        }

        boolean lvt_11_1_ = this.mc.isIntegratedServerRunning() || this.mc.getConnection().getNetworkManager().isEncrypted();
        int lvt_12_3_;
        if (p_renderPlayerlist_3_ != null) {
            if (p_renderPlayerlist_3_.getRenderType() == IScoreCriteria.EnumRenderType.HEARTS) {
                lvt_12_3_ = 90;
            } else {
                lvt_12_3_ = lvt_7_1_;
            }
        } else {
            lvt_12_3_ = 0;
        }

        int lvt_13_1_ = Math.min(lvt_10_2_ * ((lvt_11_1_ ? 9 : 0) + lvt_6_1_ + lvt_12_3_ + 13), p_renderPlayerlist_1_ - 50) / lvt_10_2_;
        int lvt_14_1_ = p_renderPlayerlist_1_ / 2 - (lvt_13_1_ * lvt_10_2_ + (lvt_10_2_ - 1) * 5) / 2;
        int lvt_15_1_ = 10;
        int lvt_16_1_ = lvt_13_1_ * lvt_10_2_ + (lvt_10_2_ - 1) * 5;
        List<String> lvt_17_1_ = null;
        if (this.header != null) {
            lvt_17_1_ = this.mc.fontRenderer.listFormattedStringToWidth(this.header.getFormattedText(), p_renderPlayerlist_1_ - 50);

            String lvt_19_1_;
            for(Iterator var18 = lvt_17_1_.iterator(); var18.hasNext(); lvt_16_1_ = Math.max(lvt_16_1_, this.mc.fontRenderer.getStringWidth(lvt_19_1_))) {
                lvt_19_1_ = (String)var18.next();
            }
        }

        List<String> lvt_18_1_ = null;
        String lvt_20_4_;
        Iterator var35;
        if (this.footer != null) {
            lvt_18_1_ = this.mc.fontRenderer.listFormattedStringToWidth(this.footer.getFormattedText(), p_renderPlayerlist_1_ - 50);

            for(var35 = lvt_18_1_.iterator(); var35.hasNext(); lvt_16_1_ = Math.max(lvt_16_1_, FontDrawing.getStringWidth(lvt_20_4_))) {
                lvt_20_4_ = (String)var35.next();
            }
        }

        int lvt_21_3_;
        if (lvt_17_1_ != null) {
            drawRect(p_renderPlayerlist_1_ / 2 - lvt_16_1_ / 2 - 1, lvt_15_1_ - 1, p_renderPlayerlist_1_ / 2 + lvt_16_1_ / 2 + 1, lvt_15_1_ + lvt_17_1_.size() * this.mc.fontRenderer.FONT_HEIGHT, -2147483648);

            for(var35 = lvt_17_1_.iterator(); var35.hasNext(); lvt_15_1_ += this.mc.fontRenderer.FONT_HEIGHT) {
                lvt_20_4_ = (String)var35.next();
                lvt_21_3_ = getStringWidth(lvt_20_4_);
                FontDrawing.drawString(lvt_20_4_, (float) (p_renderPlayerlist_1_ / 2 - lvt_21_3_ / 2), (float) lvt_15_1_, -1, true, extraTab.customFont.isChecked());
            }

            ++lvt_15_1_;
        }

        drawRect(p_renderPlayerlist_1_ / 2 - lvt_16_1_ / 2 - 1, lvt_15_1_ - 1, p_renderPlayerlist_1_ / 2 + lvt_16_1_ / 2 + 1, lvt_15_1_ + lvt_9_2_ * 9, -2147483648);

        for(int infoListIndex = 0; infoListIndex < lvt_8_1_; ++infoListIndex) {
            int lvt_20_3_ = infoListIndex / lvt_9_2_;
            lvt_21_3_ = infoListIndex % lvt_9_2_;
            int lvt_22_1_ = lvt_14_1_ + lvt_20_3_ * lvt_13_1_ + lvt_20_3_ * 5;
            int lvt_23_1_ = lvt_15_1_ + lvt_21_3_ * 9;
            drawRect(lvt_22_1_, lvt_23_1_, lvt_22_1_ + lvt_13_1_, lvt_23_1_ + 8, 553648127);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            if (infoListIndex < infoList.size()) {
                NetworkPlayerInfo playerInfo = (NetworkPlayerInfo)infoList.get(infoListIndex);
                GameProfile playerProfile = playerInfo.getGameProfile();
                int lvt_28_2_;
                if (lvt_11_1_) {
                    EntityPlayer lvt_26_1_ = this.mc.world.getPlayerEntityByUUID(playerProfile.getId());
                    boolean lvt_27_1_ = lvt_26_1_ != null && lvt_26_1_.isWearing(EnumPlayerModelParts.CAPE) && ("Dinnerbone".equals(playerProfile.getName()) || "Grumm".equals(playerProfile.getName()));
                    this.mc.getTextureManager().bindTexture(playerInfo.getLocationSkin());
                    lvt_28_2_ = 8 + (lvt_27_1_ ? 8 : 0);
                    int lvt_29_1_ = 8 * (lvt_27_1_ ? -1 : 1);
                    Gui.drawScaledCustomSizeModalRect(lvt_22_1_, lvt_23_1_, 8.0F, (float)lvt_28_2_, 8, lvt_29_1_, 8, 8, 64.0F, 64.0F);
                    if (lvt_26_1_ != null && lvt_26_1_.isWearing(EnumPlayerModelParts.HAT)) {
                        int lvt_30_1_ = 8 + (lvt_27_1_ ? 8 : 0);
                        int lvt_31_1_ = 8 * (lvt_27_1_ ? -1 : 1);
                        Gui.drawScaledCustomSizeModalRect(lvt_22_1_, lvt_23_1_, 40.0F, (float)lvt_30_1_, 8, lvt_31_1_, 8, 8, 64.0F, 64.0F);
                    }

                    lvt_22_1_ += 9;
                }

                String username = this.getPlayerName(playerInfo);
                if (playerInfo.getGameType() == GameType.SPECTATOR) {
                    FontDrawing.drawString(TextFormatting.ITALIC + username, (float) lvt_22_1_, (float) lvt_23_1_, -1862270977, true, extraTab.customFont.isChecked());
                } else {
                    FontDrawing.drawString(username, (float) lvt_22_1_, (float) lvt_23_1_, -1, true, extraTab.customFont.isChecked());
                }

                if (p_renderPlayerlist_3_ != null && playerInfo.getGameType() != GameType.SPECTATOR) {
                    int lvt_27_2_ = lvt_22_1_ + lvt_6_1_ + 1;
                    lvt_28_2_ = lvt_27_2_ + lvt_12_3_;
                    if (lvt_28_2_ - lvt_27_2_ > 5) {
                        this.drawScoreboardValues(p_renderPlayerlist_3_, lvt_23_1_, playerProfile.getName(), lvt_27_2_, lvt_28_2_, playerInfo);
                    }
                }

                this.drawPing(lvt_13_1_, lvt_22_1_ - (lvt_11_1_ ? 9 : 0), lvt_23_1_, playerInfo);
            }
        }

        if (lvt_18_1_ != null) {
            lvt_15_1_ += lvt_9_2_ * 9 + 1;
            drawRect(p_renderPlayerlist_1_ / 2 - lvt_16_1_ / 2 - 1, lvt_15_1_ - 1, p_renderPlayerlist_1_ / 2 + lvt_16_1_ / 2 + 1, lvt_15_1_ + lvt_18_1_.size() * this.mc.fontRenderer.FONT_HEIGHT, -2147483648);

            for(var35 = lvt_18_1_.iterator(); var35.hasNext(); lvt_15_1_ += this.mc.fontRenderer.FONT_HEIGHT) {
                lvt_20_4_ = (String)var35.next();
                lvt_21_3_ = this.mc.fontRenderer.getStringWidth(lvt_20_4_);
                FontDrawing.drawString(lvt_20_4_, (float) (p_renderPlayerlist_1_ / 2 - lvt_21_3_ / 2), (float) lvt_15_1_, -1, true, extraTab.customFont.isChecked());
            }
        }
    }

    @Override
    public void drawPing(int p_drawPing_1_, int p_drawPing_2_, int p_drawPing_3_, NetworkPlayerInfo p_drawPing_4_) {
        final ExtraTab extraTab = ThirdMod.hax.extraTab;
        if(extraTab.numberPing.isChecked()) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            final String ping = String.format("%d",p_drawPing_4_.getResponseTime());
            final int[] rgbColour = getColourRGB(ThirdMod.hax.extraTab.pingColour.getSelected());
            final int colour = rgbToInt(rgbColour[0],rgbColour[1],rgbColour[2]);
            FontDrawing.drawString(ping, p_drawPing_2_ + p_drawPing_1_ - FontDrawing.getStringWidth(ping), p_drawPing_3_, colour,true, extraTab.customFont.isChecked());
        } else {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.mc.getTextureManager().bindTexture(ICONS);
            final byte index;
            final int ping = p_drawPing_4_.getResponseTime();
            if (ping < 0) {
                index = 5;
            } else if (ping < 150) {
                index = 0;
            } else if (ping < 300) {
                index = 1;
            } else if (ping < 600) {
                index = 2;
            } else if (ping < 1000) {
                index = 3;
            } else {
                index = 4;
            }

            this.drawTexturedModalRect(p_drawPing_2_ + p_drawPing_1_ - 11, p_drawPing_3_, 0, 176 + index * 8, 10, 8);
        }
    }
}
