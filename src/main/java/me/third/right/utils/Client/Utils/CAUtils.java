package me.third.right.utils.Client.Utils;

import me.third.right.utils.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class CAUtils {
    protected final static Minecraft mc = Minecraft.getMinecraft();
    private static EntityEnderCrystal fakeCrystal = null;

    //Simulates a crystal being in a position.
    public static boolean simulateCrystal(final BlockPos blockPos, final double range, final double rangeWalls) {
        if(fakeCrystal == null) fakeCrystal = new EntityEnderCrystal(mc.world);
        fakeCrystal.setPosition(blockPos.x + .5, blockPos.y + 1, blockPos.z + .5);
        return crystalTargetCheck(fakeCrystal, range, rangeWalls);
    }

    public static boolean crystalTargetCheck(final Entity crystal, final double range, final double rangeWalls) {
        if(crystal instanceof EntityEnderCrystal && !crystal.isDead) {
            if(Wrapper.getPlayer().getDistance(crystal) <= rangeWalls){
                return true;
            } else return Wrapper.getPlayer().getDistance(crystal) <= range && Wrapper.getPlayer().canEntityBeSeen(crystal);
        } else return false;
    }

    public static boolean canPlaceCrystalPlacing (final BlockPos blockPos, boolean thirteen) {
        final BlockPos boost = blockPos.up();
        final BlockPos boost2 = blockPos.up(2);
        if(!thirteen) {
            return (mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN)
                    && mc.world.getBlockState(boost).getBlock() == Blocks.AIR && mc.world.getBlockState(boost2).getBlock() == Blocks.AIR
                    && mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost)).isEmpty() && mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost2)).isEmpty();
        } else {
            return (mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN)
                    && mc.world.getBlockState(boost).getBlock() == Blocks.AIR
                    && mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost)).isEmpty() && mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost2)).isEmpty();
        }
    }
}
