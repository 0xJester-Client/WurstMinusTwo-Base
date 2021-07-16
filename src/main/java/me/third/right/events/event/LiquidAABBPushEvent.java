package me.third.right.events.event;

import me.third.right.events.ThirdEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class LiquidAABBPushEvent extends ThirdEvents {

    private final BlockPos blockPos;
    private AxisAlignedBB axisAlignedBB;
    private boolean cancel = false;

    public LiquidAABBPushEvent(BlockPos pos, AxisAlignedBB axisAlignedBB) {
        this.blockPos = pos;
        this.axisAlignedBB = axisAlignedBB;
    }

    public void setAxisAlignedBB(AxisAlignedBB axisAlignedBB) {
        this.axisAlignedBB = axisAlignedBB;
    }

    public AxisAlignedBB getAxisAlignedBB() {
        return axisAlignedBB;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public boolean isCancel() {
        return cancel;
    }
}
