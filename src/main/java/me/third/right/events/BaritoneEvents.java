package me.third.right.events;

import baritone.api.event.events.PathEvent;
import baritone.api.event.listener.AbstractGameEventListener;
import me.third.right.ThirdMod;

public class BaritoneEvents implements AbstractGameEventListener {

    @Override
    public void onPathEvent(PathEvent event) {
        ThirdMod.EVENT_BUS.post(event);
    }
}
