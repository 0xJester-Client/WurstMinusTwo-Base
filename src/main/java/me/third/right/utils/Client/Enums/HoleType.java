package me.third.right.utils.Client.Enums;

import net.minecraft.util.math.BlockPos;

public enum HoleType {
    ONEBYONE,
    TwoByOneNorth(new BlockPos(0,0,-1)),
    TwoByOneEast(new BlockPos(1,0,0));

    private BlockPos offset;
    HoleType() {}
    HoleType(BlockPos offset) {
        this.offset = offset;
    }
    public BlockPos getOffset() {
        return offset;
    }
}
