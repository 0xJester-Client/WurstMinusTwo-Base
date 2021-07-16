package me.third.right.events.event;

import me.third.right.events.ThirdEvents;
import net.minecraft.entity.Entity;

public class EntityDeathEvent extends ThirdEvents {
    private final Entity crystal;

    public EntityDeathEvent(Entity entity) {
        crystal = entity;
    }

    public Entity getEntity() {
        return crystal;
    }
}
