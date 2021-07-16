package me.third.right.mixin.client;

import me.third.right.DiscordPresence;
import me.third.right.ThirdMod;
import me.third.right.modules.Hack;
import me.third.right.utils.Client.Manage.HoleManager;
import me.third.right.utils.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

    @Shadow public EntityPlayerSP player;

    @Shadow @Nullable public GuiScreen currentScreen;

    @Inject(method = "shutdown", at = @At("HEAD"))
    public void shutdown(CallbackInfo info) {
        DiscordPresence.shutDown();
        ThirdMod.hax.saveSettings();
        HoleManager.INSTANCE.shutdown();
        for(Hack hack : ThirdMod.hax.getRegistry()) {
            hack.onClose();
        }
    }
}