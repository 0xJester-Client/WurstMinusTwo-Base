package me.third.right.utils.Client.Utils;

import me.third.right.ThirdMod;
import me.third.right.utils.Client.Enums.SpecialRotations;
import me.third.right.utils.Client.Manage.RotationHandler;
import me.third.right.utils.Client.Objects.Triplet;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;

import static me.third.right.utils.Client.Manage.RotationHandler.resetRotationObject;
import static me.third.right.utils.Client.Utils.EntityUtils.getPositionEyesVec;
import static me.third.right.utils.Client.Utils.BlockInteraction.*;

//Methods used to control AimbotHandler
public class RotationUtils {
    protected static Minecraft mc = Minecraft.getMinecraft();
    private static boolean wallBypassCycle = false;

    public static void lookAtAngle(final float pitch, final float yaw, final SpecialRotations specialRotations){
        RotationHandler.setRotations(new Triplet<>(pitch, yaw, specialRotations));
        RotationHandler.newRotationObject();
    }

    public static void lookAtCrystal(final Entity entity){
        final Vec3d pos = new Vec3d(entity.getPositionVector().x, entity.getPositionVector().y + 1, entity.getPositionVector().z);
        final float[] angle = calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), pos);
        lookAtAngle(angle[1],angle[0], SpecialRotations.None);
    }

    public static void lookAtEntity(final Entity entity){ lookAtEntity(entity,false); }
    public static void lookAtEntity(final Entity entity, final boolean forced){
        final float[] angle = calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), entity.getPositionVector());
        lookAtAngle(angle[1],angle[0], forced ? SpecialRotations.Forced : SpecialRotations.None);
    }

    public static void lookAtEyes(final Entity entity){ lookAtEyes(entity,false);}
    public static void lookAtEyes(final Entity entity, final boolean forced){
        final float[] angle = calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), entity.getPositionEyes(mc.getRenderPartialTicks()));
        lookAtAngle(angle[1],angle[0], forced ? SpecialRotations.Forced : SpecialRotations.None);
    }

    public static void lookAtChest(final Entity entity) {lookAtChest(entity,false);}
    public static void lookAtChest(final Entity entity, final boolean forced) {
        final Vec3d entityPos = entity.getPositionVector();
        final float[] angle = calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()),new Vec3d(entityPos.x,entityPos.y + (entity.getEyeHeight() / 2),entityPos.z));
        lookAtAngle(angle[1],angle[0], forced ? SpecialRotations.Forced : SpecialRotations.None);
    }

    public static void lookAtPos(final BlockPos blockPos, final SpecialRotations specialRotations) {
        if(blockPos == null) return;
        final float[] angles;
        switch (ThirdMod.hax.aimbotHandler.rotationType.getSelected()) {
            case Normal:
            case NormalPlus:
                lookAtPos(new Vec3d(blockPos.x + 0.5f, blockPos.y + 0.5f, blockPos.z + 0.5f), specialRotations);
                break;
            case OFF:
            case Raytrace:
                angles = accurateRotations(blockPos);
                lookAtAngle(angles[1],angles[0], specialRotations);
                break;
        }
    }
    public static void lookAtPos(final BlockPos blockPos, final boolean forced) { lookAtPos(blockPos, forced ? SpecialRotations.Forced : SpecialRotations.None); }
    public static void lookAtPos(final BlockPos blockPos) { lookAtPos(blockPos, false); }
    public static void lookAtPos(final Vec3d vec3d){ lookAtPos(vec3d,false); }
    public static void lookAtPos(final Vec3d vec3d, final boolean forced) { lookAtPos(vec3d, forced ? SpecialRotations.Forced : SpecialRotations.None); }
    public static void lookAtPos(final Vec3d vec3d, final SpecialRotations specialRotations) {
        final float[] angle = calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), vec3d);
        lookAtAngle(angle[1], angle[0], specialRotations);
    }

    public static void lookAtPosPlace(final BlockPos blockPos, final boolean visibilityCheck, final boolean allowWallBypass) {
        final EnumFacing side = getPlaceableSide(blockPos, visibilityCheck);
        if (side == null) return;
        final BlockPos neighbour = blockPos.offset(side);
        if (!canBeClicked(neighbour)) return;
        if(!canBlockBeSeen(neighbour, true) && allowWallBypass) {
            wallBypass(neighbour);
        } else {
            lookAtPos(neighbour, true);
        }
    }

    public static boolean lookAtCrystalSmooth(final Entity entity) {
        final Vec3d entityPos = entity.getPositionVector();
        return lookAtPosSmooth(new Vec3d(entityPos.x,entityPos.y + 1,entityPos.z));
    }
    public static boolean lookAtEntitySmooth(final Entity entity) {
        final Vec3d entityPos = entity.getPositionVector();
        return lookAtPosSmooth(new Vec3d(entityPos.x,entityPos.y + (entity.getEyeHeight() / 2),entityPos.z));
    }
    public static boolean lookAtEyesSmooth(final Entity entity) { return lookAtPosSmooth(entity.getPositionEyes(mc.getRenderPartialTicks())); }
    public static boolean lookAtChestSmooth(final Entity entity) { return lookAtPosSmooth(entity.getPositionEyes(mc.getRenderPartialTicks())); }

    public static boolean lookAtPosSmooth(final BlockPos blockPos) {
        final float[] angle = accurateRotations(blockPos);
        boolean finished = yawSteps(angle[1], angle[0]);
        if(finished) lookAtPos(blockPos);
        return finished;
    }
    public static boolean lookAtPosSmooth(final Vec3d vec3d) {
        final float[] angle = calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), vec3d);
        boolean finished = yawSteps(angle[1], angle[0]);
        if(finished) lookAtPos(vec3d);
        return finished;
    }

    //Stolen from Urmomia
    private static boolean yawSteps(double targetPitch, double targetYaw) {
        final double[] serverAngles = RotationHandler.angles();
        targetYaw = MathHelper.wrapDegrees(targetYaw) + 180;
        double serverYaw = MathHelper.wrapDegrees(serverAngles[1]) + 180;

        if (distanceBetweenAngles(serverYaw, targetYaw) <= 10) return true;

        double delta = Math.abs(targetYaw - serverYaw);
        double yaw = serverAngles[1];

        if (serverYaw < targetYaw) {
            if (delta < 180) yaw += 22.9;
            else yaw -= 22.9;
        }
        else {
            if (delta < 180) yaw -= 22.9;
            else yaw += 22.9;
        }

        lookAtAngle((float) targetPitch, (float) yaw, SpecialRotations.None);
        return false;
    }

    private static double distanceBetweenAngles(double alpha, double beta) {
        double phi = Math.abs(beta - alpha) % 360;
        return phi > 180 ? 360 - phi : phi;
    }

    //Methods used to get looking pos using pitch and yaw. Copied from MC
    public static Vec3d getVectorForRotation(final float pitch, final float yaw) {
        final float f = MathHelper.cos(-yaw * 0.017453292F - 3.1415927F);
        final float f1 = MathHelper.sin(-yaw * 0.017453292F - 3.1415927F);
        final float f2 = -MathHelper.cos(-pitch * 0.017453292F);
        final float f3 = MathHelper.sin(-pitch * 0.017453292F);
        return new Vec3d((f1 * f2), f3, (f * f2));
    }

    public static RayTraceResult rayTrace(final double blockReachDistance, final float pitch, final float yaw) {
        final Vec3d vec3d = getPositionEyesVec(mc.player);
        final Vec3d vec3d1 = getVectorForRotation(pitch,yaw);
        final Vec3d vec3d2 = vec3d.add(vec3d1.x * blockReachDistance, vec3d1.y * blockReachDistance, vec3d1.z * blockReachDistance);
        return mc.world.rayTraceBlocks(vec3d, vec3d2, false, false, true);
    }

    private static float[] accurateRotations(final BlockPos blockPos){
        final Vec3d vec3d = new Vec3d(MathHelper.floor(blockPos.x),MathHelper.floor(blockPos.y),MathHelper.floor(blockPos.z));
        for (double x : blockSearchArea) {//TODO improve this by only searching three closest block faces.
            for (double y : blockSearchArea) {
                for (double z : blockSearchArea) {
                    final float[] angle = calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d(vec3d.x + x, vec3d.y + y, vec3d.z + z));
                    final RayTraceResult traceResult = RotationUtils.rayTrace(6, angle[1], angle[0]);
                    if(traceResult != null && traceResult.typeOfHit.equals(RayTraceResult.Type.BLOCK) && traceResult.getBlockPos().equals(blockPos))
                        return angle;
                }
            }
        }
        return calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d( blockPos.x + 0.5f,blockPos.y + 0.5f,blockPos.z + 0.5f));
    }

    public static void resetRotations() {
        resetRotationObject();
    }

    public static float[] calcAngle(final Vec3d from, final Vec3d to) {
        final double difX = to.x - from.x;
        final double difY = (to.y - from.y) * -1.0D;
        final double difZ = to.z - from.z;
        final double dist = MathHelper.sqrt(difX * difX + difZ * difZ);
        return new float[]{(float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0D), (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difY, dist)))};
    }

    public static void wallBypass(final BlockPos blockPos) {//WallBypass rotations for Const credit goes to amp / cap for helping me.
        if(!wallBypassCycle) {
            lookAtPos(blockPos.up(4), SpecialRotations.WallBypass);
        } else {
            lookAtPos(blockPos, SpecialRotations.WallBypass);
        }
        wallBypassCycle = !wallBypassCycle;
    }

    public static void wallBypassNoRay(final BlockPos blockPos, final boolean down) {
        if(!wallBypassCycle) {
            lookAtPos(new Vec3d(blockPos.up(down ? -4 : 4)), SpecialRotations.WallBypass);
        } else {
            lookAtPos(new Vec3d(blockPos), SpecialRotations.WallBypass);
        }
        wallBypassCycle = !wallBypassCycle;
    }
}
