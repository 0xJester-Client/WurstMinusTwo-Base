package me.third.right.mixin.client;

import me.third.right.ThirdMod;
import me.third.right.clickgui.Screen.ClickGuiScreenMainMenu;
import me.third.right.modules.Render.MainMenuMods;
import me.third.right.utils.Render.GLSLSandboxShader;
import me.third.right.utils.Render.Image.Bouncing;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

import static me.third.right.utils.Client.Utils.Colour.rgbToInt;

@Mixin(GuiMainMenu.class)
public abstract class MixinGuiMainMenu extends GuiScreen {
    private Bouncing dvd;

    @Shadow
    protected abstract void renderSkybox(int mouseX, int mouseY, float partialTicks);

    @Inject(method = "initGui", at = @At(value = "RETURN"), cancellable = true)
    public void initGui(CallbackInfo info) {
        ThirdMod.initTime = System.currentTimeMillis();
        this.buttonList.add(new GuiButton(1001, 5, 5, fontRenderer.getStringWidth(ThirdMod.NAME) + 10, 20, ThirdMod.NAME));
        dvd = new Bouncing(125, 64, new ResourceLocation(ThirdMod.MODID+":textures/dvd.png"));
    }

    @Redirect(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiMainMenu;renderSkybox(IIF)V"))
    private void renderSkybox(GuiMainMenu guiMainMenu, int mouseX, int mouseY, float partialTicks) {
        final MainMenuMods mainMenuMods = ThirdMod.hax.mainMenuMods;
        if (!mainMenuMods.isEnabled() || !mainMenuMods.customBackground.isChecked()) {
            renderSkybox(mouseX, mouseY, partialTicks);
        } else {
            final int[] rgb = mainMenuMods.getRGB();
            switch (mainMenuMods.backGroundMode.getSelected()) {
                case GLSL:
                    if(Display.isActive()) {
                        if (OpenGlHelper.shadersSupported) {
                            final GLSLSandboxShader shader = ThirdMod.backgroundShader;
                            if (shader != null) {
                                GlStateManager.disableCull();
                                shader.useShader(this.width * 2, this.height * 2, mouseX * 2, mouseY * 2, (System.currentTimeMillis() - ThirdMod.initTime) / 1000f);
                                GL11.glBegin(GL11.GL_QUADS);
                                GL11.glVertex2f(-1f, -1f);
                                GL11.glVertex2f(-1f, 1f);
                                GL11.glVertex2f(1f, 1f);
                                GL11.glVertex2f(1f, -1f);
                                GL11.glEnd();
                                GL20.glUseProgram(0);
                            } else {
                                try {
                                    ThirdMod.backgroundShader = new GLSLSandboxShader("/shaders/" + mainMenuMods.shader.getSelected() + ".fsh");
                                } catch (IOException e) {
                                    ThirdMod.log.info(e);
                                    ThirdMod.backgroundShader = null;
                                }
                            }
                        } else mainMenuMods.backGroundMode.setSelected(MainMenuMods.BackGround.Colour);
                    } else {
                        GlStateManager.enableAlpha();
                        drawRect(0,0, width, height, rgbToInt(rgb[0],rgb[1],rgb[2], 255));
                        GlStateManager.disableAlpha();
                    }
                    break;
                case Colour:
                    GlStateManager.enableAlpha();
                    drawRect(0,0, width, height, rgbToInt(rgb[0],rgb[1],rgb[2], 255));
                    GlStateManager.disableAlpha();
                    break;
                case Gradient:
                    final int[] rgb1 = mainMenuMods.getRGBGradiant();
                    GlStateManager.enableAlpha();
                    drawGradientRect(0,0, width, height, rgbToInt(rgb[0],rgb[1],rgb[2], 255), rgbToInt(rgb1[0],rgb1[1],rgb1[2], 255));
                    GlStateManager.disableAlpha();
                    break;
                case DVD:
                    GlStateManager.enableAlpha();
                    drawRect(0,0, width, height, rgbToInt(rgb[0],rgb[1],rgb[2], 255));
                    if(dvd != null) {
                        dvd.onRender();
                        dvd.onTick();
                    } else {
                        GlStateManager.enableAlpha();
                        drawRect(0,0, width, height, rgbToInt(rgb[0],rgb[1],rgb[2], 255));
                        GlStateManager.disableAlpha();
                    }
                    GlStateManager.disableAlpha();
                    break;
            }
        }
    }

    @Inject(method = "actionPerformed", at = @At(value = "HEAD"), cancellable = true)
    public void actionPerformed(GuiButton button, CallbackInfo info) {
        if(button.id == 1001) {
            this.mc.displayGuiScreen(new ClickGuiScreenMainMenu(this, ThirdMod.getForgeWurst().getGui()));
        }
    }

    @Redirect(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiMainMenu;drawGradientRect(IIIIII)V", ordinal = 0))
    private void Rect1(GuiMainMenu guiMainMenu, int left, int top, int right, int bottom, int startColor, int endColor) {
        final MainMenuMods mainMenuMods = ThirdMod.hax.mainMenuMods;
        if (mainMenuMods.drawGradiant.isChecked() || !mainMenuMods.isEnabled() && !mainMenuMods.customBackground.isChecked()) {
            drawGradientRect(left, top, right, bottom, startColor, endColor);
        }
    }

    @Redirect(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiMainMenu;drawGradientRect(IIIIII)V", ordinal = 1))
    private void Rect2(GuiMainMenu guiMainMenu, int left, int top, int right, int bottom, int startColor, int endColor) {
        final MainMenuMods mainMenuMods = ThirdMod.hax.mainMenuMods;
        if (mainMenuMods.drawGradiant.isChecked() || !mainMenuMods.isEnabled() && !mainMenuMods.customBackground.isChecked()) {
            drawGradientRect(left, top, right, bottom, startColor, endColor);
        }
    }

    @Redirect(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiMainMenu;drawString(Lnet/minecraft/client/gui/FontRenderer;Ljava/lang/String;III)V"))
    public void removeAllDrawStrings(GuiMainMenu guiMainMenu, FontRenderer fontRenderer1, String string, int i1, int i2, int i3) {
    }
}
