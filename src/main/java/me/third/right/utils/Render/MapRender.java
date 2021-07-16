package me.third.right.utils.Render;

import net.minecraft.block.material.MapColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.MapData;

import java.util.Arrays;

public class MapRender {
    protected static Minecraft mc = Minecraft.getMinecraft();
    private final MapData mapData;
    private final DynamicTexture mapTexture;
    private final ResourceLocation location;
    private final int[] mapTextureData;

    public MapRender(MapData mapData) {
        this.mapData = mapData;
        this.mapTexture = new DynamicTexture(128, 128);
        this.mapTextureData = mapTexture.getTextureData();
        this.location = mc.getTextureManager().getDynamicTextureLocation("map/" + mapData.mapName, mapTexture);
        Arrays.fill(mapTextureData, 0);
    }

    public void updateMapTexture() {
        for(int i = 0; i < 16384; ++i) {
            int j = this.mapData.colors[i] & 255;
            if (j / 4 == 0) {
                this.mapTextureData[i] = (i + i / 128 & 1) * 8 + 16 << 24;
            } else {
                this.mapTextureData[i] = MapColor.COLORS[j / 4].getMapColor(j & 3);
            }
        }

        this.mapTexture.updateDynamicTexture();
    }

    public void drawMap(float x, float y, float w, float h) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        mc.getTextureManager().bindTexture(location);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
        GlStateManager.disableAlpha();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(w, y, 300).tex(0.0D, 1.0D).endVertex();
        bufferbuilder.pos(x, y, 300).tex(1.0D, 1.0D).endVertex();
        bufferbuilder.pos(x, h, 300).tex(1.0D, 0.0D).endVertex();
        bufferbuilder.pos(w, h, 300).tex(0.0D, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
    }
}
