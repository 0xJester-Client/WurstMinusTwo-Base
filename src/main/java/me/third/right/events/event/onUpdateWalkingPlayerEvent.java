package me.third.right.events.event;

import me.third.right.events.ThirdEvents;

public class onUpdateWalkingPlayerEvent extends ThirdEvents {
    private final int stage;

    public onUpdateWalkingPlayerEvent(final int stage) {
        this.stage = stage;
    }

    public int getStage() {
        return stage;
    }
}
