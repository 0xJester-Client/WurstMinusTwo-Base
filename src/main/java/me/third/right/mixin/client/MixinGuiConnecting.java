package me.third.right.mixin.client;

import me.third.right.ThirdMod;
import me.third.right.modules.Other.AutoReconnect;
import net.minecraft.client.multiplayer.GuiConnecting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiConnecting.class)
public class MixinGuiConnecting {

    @Inject(method = "connect", at = @At("HEAD"))
    private void connect(String ip, int port, CallbackInfo ci) {
        final AutoReconnect autoReconnect = ThirdMod.hax.autoReconnect;
        autoReconnect.prevServer(ip, port);
    }
}
