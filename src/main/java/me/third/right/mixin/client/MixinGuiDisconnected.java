package me.third.right.mixin.client;

import me.third.right.utils.Wrapper;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiDisconnected.class)
public abstract class MixinGuiDisconnected extends GuiScreen {

    @Shadow @Final public String reason;

    @Shadow @Final public GuiScreen parentScreen;

    @Shadow @Final public ITextComponent message;

    @Inject(method = "initGui", at = @At("HEAD"), cancellable = true)
    public void initGui(CallbackInfo ci) {
        Wrapper.getMinecraft().displayGuiScreen(new me.third.right.clickgui.Screen.GuiDisconnected(parentScreen, reason, message));
        ci.cancel();
    }
}
