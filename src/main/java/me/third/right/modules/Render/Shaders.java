package me.third.right.modules.Render;

import me.third.right.ThirdMod;
import me.third.right.modules.HackStandard;
import me.third.right.settings.setting.EnumSetting;
import me.third.right.utils.Client.Enums.Category;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Shaders extends HackStandard {
    //Vars

    //Settings
    private final EnumSetting<ShaderMode> shaderMode = setting(new EnumSetting<>("ShaderMode", ShaderMode.values(), ShaderMode.Minecraft));
    private final EnumSetting<ShadersMinecraft> minecraftShader = setting(new EnumSetting<>("Minecraft Shader", ShadersMinecraft.values(), ShadersMinecraft.art));
    private final EnumSetting<ShadersCustom> customShader = setting(new EnumSetting<>("Custom Shader", ShadersCustom.values(), ShadersCustom.cuboyd));

    public Shaders() {
        super("Shaders", "Secret Shaders and Custom ones.", Category.RENDER);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (OpenGlHelper.shadersSupported && mc.getRenderViewEntity() instanceof EntityPlayer) {
            switch (shaderMode.getSelected()) {
                case Minecraft:
                    if(mc.entityRenderer.getShaderGroup() == null) {
                        mc.entityRenderer.loadShader(new ResourceLocation("minecraft:shaders/post/" + minecraftShader.getSelected().toString() + ".json"));
                    } else if(mc.entityRenderer.getShaderGroup() != null && !mc.entityRenderer.getShaderGroup().getShaderGroupName().equals("minecraft:shaders/post/" + minecraftShader.getSelected().toString() + ".json")) {
                        mc.entityRenderer.stopUseShader();
                    }
                    break;
                case Custom:
                    if(mc.entityRenderer.getShaderGroup() == null) {
                        mc.entityRenderer.loadShader(new ResourceLocation(ThirdMod.MODID+":shaders/" + customShader.getSelected().toString() + ".json"));
                    } else if(mc.entityRenderer.getShaderGroup() != null && !mc.entityRenderer.getShaderGroup().getShaderGroupName().equals(ThirdMod.MODID+":shaders/" + customShader.getSelected().toString() + ".json")) {
                        mc.entityRenderer.stopUseShader();
                    }
                    break;
            }
        } else {
            mc.entityRenderer.stopUseShader();
        }
    }

    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
        mc.entityRenderer.stopUseShader();
    }

    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    private enum ShaderMode {
        Minecraft, Custom
    }

    public enum ShadersMinecraft {
        antialias, art, bits,
        blobs, blobs2, blur,
        bumpy, color_convolve, creeper,
        deconverge, desaturate, entity_outline,
        flip, fxaa, green,
        invert, notch, ntsc,
        outline, pencil, phosphor,
        scan_pincusion, sobel, spider,
        wobble;
    }

    private enum ShadersCustom {
        cuboyd, rainbow, saturation
    }
}
