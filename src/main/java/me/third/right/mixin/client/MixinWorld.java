package me.third.right.mixin.client;

import me.third.right.ThirdMod;
import me.third.right.events.event.EntityDeathEvent;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(World.class)
public abstract class MixinWorld {

    @Shadow
    @Final
    public List<Entity> loadedEntityList;

    @Shadow public abstract World init();

    @Inject(method = "updateEntities", at = @At("HEAD"), cancellable = true)
    private void onCrystalDeath(CallbackInfo ci) {
        for (Entity entity : loadedEntityList) {
            if(entity.isDead) {
                ThirdMod.EVENT_BUS.post(new EntityDeathEvent(entity));
            }
        }
    }
}
