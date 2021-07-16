package me.third.right.events.event;

import me.third.right.events.ThirdEvents;
import net.minecraft.client.renderer.EntityRenderer;

public class FogDensityEvent extends ThirdEvents {
    private final int var1;
    private final float var2;
    private final EntityRenderer INSTANCE;

    public FogDensityEvent(int var1, float var2, EntityRenderer instance) {
        this.var1 = var1;
        this.var2 = var2;
        this.INSTANCE = instance;
    }

    public int getVar1() {
        return var1;
    }

    public float getVar2() {
        return var2;
    }

    public EntityRenderer getINSTANCE() {
        return INSTANCE;
    }
}
