package me.third.right.events.event;

import me.third.right.events.ThirdEvents;
import net.minecraft.entity.Entity;

public class EntityEvent extends ThirdEvents {

    public static class BlockCollision extends EntityEvent {
        double x,y,z;

        public BlockCollision(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public double getX() { return x; }
        public double getY() { return y; }
        public double getZ() { return z; }

        public void setX(double x) { this.x = x; }
        public void setY(double y) { this.y = y; }
        public void setZ(double z) { this.z = z; }
    }

    public static class EntityCollision extends EntityEvent {
        Entity entity;
        double x,y,z;

        public EntityCollision(Entity entity, double x, double y, double z) {
            this.entity = entity;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public double getX() {
            return x;
        }
        public double getY() {
            return y;
        }
        public double getZ() {
            return z;
        }

        public void setX(double x) {
            this.x = x;
        }
        public void setY(double y) {
            this.y = y;
        }
        public void setZ(double z) {
            this.z = z;
        }

        public Entity getEntity() { return entity; }
        public void setEntity(Entity entity) { this.entity = entity; }
    }
}
