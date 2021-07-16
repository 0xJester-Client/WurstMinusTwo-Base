package me.third.right.events.event;

import me.third.right.events.ThirdEvents;
import net.minecraft.entity.Entity;

public class TotemPopEvent extends ThirdEvents {
    private final Entity entity;

    public TotemPopEvent(Entity entity) {
        super();
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

}
