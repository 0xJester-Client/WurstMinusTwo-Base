package me.third.right.events.event;

import me.third.right.events.ThirdEvents;

public class PlayerJumpEvent extends ThirdEvents {
    final double motionX;
    final double motionZ;

    public PlayerJumpEvent(final double motionX, final double motionZ) {
        this.motionX = motionX;
        this.motionZ = motionZ;
    }

    public double getMotionX() {
        return motionX;
    }

    public double getMotionZ() {
        return motionZ;
    }
}
