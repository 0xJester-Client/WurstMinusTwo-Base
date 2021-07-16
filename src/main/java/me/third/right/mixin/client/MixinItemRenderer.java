package me.third.right.mixin.client;

import me.third.right.ThirdMod;
import me.third.right.events.event.TransformSideFirstPersonEvent;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.util.EnumHandSide;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer {
    @Inject(method = "transformSideFirstPerson", at = @At("HEAD"))
    public void transformSideFirstPerson(EnumHandSide hand, float p_187459_2_, CallbackInfo ci) {
        TransformSideFirstPersonEvent event = new TransformSideFirstPersonEvent(hand);
        ThirdMod.EVENT_BUS.post(event);
    }

    @Inject(method = "renderArmFirstPerson", at = @At("HEAD"))
    public void renderArmFirstPerson(float p_renderArmFirstPerson_1_, float p_renderArmFirstPerson_2_, EnumHandSide hand,CallbackInfo ci) {
        TransformSideFirstPersonEvent event = new TransformSideFirstPersonEvent(hand);
        ThirdMod.EVENT_BUS.post(event);
    }
}