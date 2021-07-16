package me.third.right.utils.Render;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Vector4f;

public class TwoDRender {
    //Prepare
    public static void preItemRender() {
        GL11.glPushMatrix();
        GL11.glDepthMask(true);
        GlStateManager.clear(256);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
        GlStateManager.scale(1.0f, 1.0f, 0.01f);
    }

    public static void postItemRender() {
        GlStateManager.scale(1.0f, 1.0f, 1.0f);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GL11.glPopMatrix();
    }
    //Draws
    public static void drawRect(float x, float y, float w, float h, int red, int green, int blue, int alpha) {
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(x, h, 0.0D).color(((float)red / 255.0f), ((float)green / 255.0f), ((float)blue / 255.0f), ((float)alpha / 255.0f)).endVertex();
        bufferbuilder.pos(w, h, 0.0D).color(((float)red / 255.0f), ((float)green / 255.0f), ((float)blue / 255.0f), ((float)alpha / 255.0f)).endVertex();
        bufferbuilder.pos(w, y, 0.0D).color(((float)red / 255.0f), ((float)green / 255.0f), ((float)blue / 255.0f), ((float)alpha / 255.0f)).endVertex();
        bufferbuilder.pos(x, y, 0.0D).color(((float)red / 255.0f), ((float)green / 255.0f), ((float)blue / 255.0f), ((float)alpha / 255.0f)).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawOutlineRect(float x , float y , float w , float h , int color, float lineWidth ) {
        float alpha = (float)(color >> 24 & 0xFF ) / 255.0f;
        float red = (float)(color >> 16 & 0xFF ) / 255.0f;
        float green = (float)(color >> 8 & 0xFF ) / 255.0f;
        float blue = (float)(color & 0xFF ) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.glLineWidth(lineWidth);
        GlStateManager.tryBlendFuncSeparate( 770 , 771 , 1 , 0 );
        bufferbuilder.begin (2 , DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(x, h,0.0).color(red , green , blue , alpha).endVertex();
        bufferbuilder.pos(w, h,0.0).color(red , green , blue , alpha).endVertex();
        bufferbuilder.pos(w, y,0.0).color(red , green , blue , alpha).endVertex();
        bufferbuilder.pos(x, y,0.0).color(red , green , blue , alpha).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawGradientRect(float x, float y, float w, float h, int startColor, int endColor) {
        float f = (float)(startColor >> 24 & 255) / 255.0F;
        float f1 = (float)(startColor >> 16 & 255) / 255.0F;
        float f2 = (float)(startColor >> 8 & 255) / 255.0F;
        float f3 = (float)(startColor & 255) / 255.0F;
        float f4 = (float)(endColor >> 24 & 255) / 255.0F;
        float f5 = (float)(endColor >> 16 & 255) / 255.0F;
        float f6 = (float)(endColor >> 8 & 255) / 255.0F;
        float f7 = (float)(endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(w, y, 300).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(x, y, 300).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(x, h, 300).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos(w, h, 300).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawMCToolTip(int x1, int y1, int width, int height) {
        drawGradientRect(x1 - 3, y1 - 4, x1 + width + 3, y1 - 3, -267386864, -267386864);
        drawGradientRect(x1 - 3, y1 + height + 3, x1 + width + 3, y1 + height + 4, -267386864, -267386864);
        drawGradientRect(x1 - 3, y1 - 3, x1 + width + 3, y1 + height + 3, -267386864, -267386864);
        drawGradientRect(x1 - 4, y1 - 3, x1 - 3, y1 + height + 3, -267386864, -267386864);
        drawGradientRect(x1 + width + 3, y1 - 3, x1 + width + 4, y1 + height + 3, -267386864, -267386864);
        drawGradientRect(x1 - 3, y1 - 3 + 1, x1 - 3 + 1, y1 + height + 3 - 1, 1347420415, 1344798847);
        drawGradientRect(x1 + width + 2, y1 - 3 + 1, x1 + width + 3, y1 + height + 3 - 1, 1347420415, 1344798847);
        drawGradientRect(x1 - 3, y1 - 3, x1 + width + 3, y1 - 3 + 1, 1347420415, 1347420415);
        drawGradientRect(x1 - 3, y1 + height + 2, x1 + width + 3, y1 + height + 3, 1344798847, 1344798847);
    }
}
