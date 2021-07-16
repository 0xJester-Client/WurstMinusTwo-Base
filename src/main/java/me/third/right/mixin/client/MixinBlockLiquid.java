package me.third.right.mixin.client;

import me.third.right.ThirdMod;
import me.third.right.events.event.LiquidAABBPushEvent;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockLiquid.class)
public class MixinBlockLiquid {

    @Inject(method = "getCollisionBoundingBox", at = @At("HEAD"), cancellable = true)
    public void getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos, final CallbackInfoReturnable<AxisAlignedBB> cb) {
        final LiquidAABBPushEvent event = new LiquidAABBPushEvent(pos, null);
        ThirdMod.EVENT_BUS.post(event);
        cb.setReturnValue(event.getAxisAlignedBB());
        if(event.isCancel()) {
            cb.cancel();
        }
    }
}
