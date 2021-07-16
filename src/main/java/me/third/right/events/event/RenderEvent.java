package me.third.right.events.event;

import me.third.right.events.ThirdEvents;

public class RenderEvent extends ThirdEvents {
    private float partialTicks;

    public RenderEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}
