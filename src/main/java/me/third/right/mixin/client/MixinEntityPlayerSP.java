package me.third.right.mixin.client;

import me.third.right.ThirdMod;
import me.third.right.events.event.EntityEvent;
import me.third.right.events.event.PlayerJumpEvent;
import me.third.right.events.event.PlayerMoveEvent;
import me.third.right.events.event.onUpdateWalkingPlayerEvent;
import me.third.right.modules.Client.AimbotHandler;
import me.third.right.utils.Client.Manage.RotationHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.MoverType;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends MixinEntityPlayer {

    @Shadow protected abstract void updateAutoJump(float p_updateAutoJump_1_, float p_updateAutoJump_2_);

    public MixinEntityPlayerSP(World worldIn) {
        super(worldIn);
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("HEAD"), cancellable = true)
    public void PreUpdateWalkingPlayer(CallbackInfo p_Info) {
        final onUpdateWalkingPlayerEvent event = new onUpdateWalkingPlayerEvent(0);
        ThirdMod.EVENT_BUS.post(event);
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("RETURN"), cancellable = true)
    public void PostUpdateWalkingPlayer(CallbackInfo p_Info) {
        final onUpdateWalkingPlayerEvent event = new onUpdateWalkingPlayerEvent(1);
        ThirdMod.EVENT_BUS.post(event);
    }

   @Inject(method = "move", at = @At("HEAD"), cancellable = true)
    public void move(MoverType type, double x, double y, double z, CallbackInfo ci) {
        final PlayerMoveEvent event = new PlayerMoveEvent(type, x, y, z);
        ThirdMod.EVENT_BUS.post(event);
        super.move(type, event.getX(), event.getY(), event.getZ());
        ci.cancel();
    }

    @Inject(method={"pushOutOfBlocks"}, at={@At(value="HEAD")}, cancellable=true)
    private void pushOutOfBlocksHook(double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        final EntityEvent.BlockCollision event = new EntityEvent.BlockCollision(x,y,z);
        ThirdMod.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            cir.setReturnValue(false);
        }
    }

    @Override
    public void jump()
    {
        try
        {
            final double motionX1 = motionX;
            final double motionZ1 = motionZ;
            super.jump();
            ThirdMod.EVENT_BUS.post(new PlayerJumpEvent(motionX1, motionZ1));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}