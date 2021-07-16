package me.third.right.events.event;

import me.third.right.events.ThirdEvents;
import net.minecraft.util.EnumHandSide;

public class TransformSideFirstPersonEvent extends ThirdEvents {
    private final EnumHandSide handSide;

    public TransformSideFirstPersonEvent(EnumHandSide handSide) {
        this.handSide = handSide;
    }

    public EnumHandSide getHandSide() {
        return handSide;
    }
}
