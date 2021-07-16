package me.third.right.mixin.client;

import me.third.right.ThirdMod;
import me.third.right.modules.Render.ExtraTab;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;


@Mixin(GuiPlayerTabOverlay.class)
public class MixinGuiPlayerTabOverlay {

    @Redirect(method = "renderPlayerlist", at = @At(value = "INVOKE", target = "Ljava/util/List;subList(II)Ljava/util/List;", remap = false))
    public List<NetworkPlayerInfo>  subList(List<NetworkPlayerInfo>  list, int fromIndex, int toIndex) {
        final ExtraTab extraTab = ThirdMod.hax.extraTab;
        return list.subList(fromIndex, extraTab.isEnabled() ? Math.min(extraTab.size.getValueI(), list.size()) : toIndex);
    }

    @Inject(method = "getPlayerName", at = @At("HEAD"), cancellable = true)
    public void getPlayerName(NetworkPlayerInfo networkPlayerInfoIn, CallbackInfoReturnable<String> returnable) {
        final ExtraTab extraTab = ThirdMod.hax.extraTab;
        if (extraTab.isEnabled() && extraTab.colouredNames.isChecked()) {
            returnable.cancel();
            returnable.setReturnValue(ExtraTab.getPlayerName(networkPlayerInfoIn));
        }
    }
}