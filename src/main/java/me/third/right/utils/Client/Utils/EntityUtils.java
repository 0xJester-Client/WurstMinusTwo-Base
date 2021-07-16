package me.third.right.utils.Client.Utils;

import me.third.right.utils.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import static me.third.right.utils.Client.Utils.RotationUtils.calcAngle;

public class EntityUtils {
    protected static final Minecraft mc = Minecraft.getMinecraft();

    public static boolean isEntityFriendly(Entity entity){
        return (entity.isCreatureType(EnumCreatureType.AMBIENT,false)) || (entity.isCreatureType(EnumCreatureType.CREATURE,false));
    }
    public static boolean isEntityHostile(Entity entity){
        return (entity.isCreatureType(EnumCreatureType.MONSTER,false)) && !isEntityNeutral(entity);
    }
    public static boolean isEntityNeutral(Entity entity){
        return (entity instanceof EntityPigZombie) && !((EntityPigZombie) entity).isAngry()
                || (entity instanceof EntityEnderman) && !((EntityEnderman) entity).isScreaming();
    }

    public static double getEntitySpeed(Entity entity) {
        if (entity != null) {
            final double distTraveledLastTickX = entity.posX - entity.prevPosX;
            final double distTraveledLastTickZ = entity.posZ - entity.prevPosZ;
            final double speed = MathHelper.sqrt((double)(distTraveledLastTickX * distTraveledLastTickX + distTraveledLastTickZ * distTraveledLastTickZ));
            return speed * 20.0;
        }
        return 0.0;
    }

    public static Vec3d getInterpolatedAmount(Entity entity, double x, double y, double z) {
        return new Vec3d(
                (entity.posX - entity.lastTickPosX) * x,
                (entity.posY - entity.lastTickPosY) * y,
                (entity.posZ - entity.lastTickPosZ) * z
        );
    }
    public static Vec3d getInterpolatedAmount(Entity entity, double ticks) {
        return getInterpolatedAmount(entity, ticks, ticks, ticks);
    }

    public static Vec3d getInterpolatedPos(Entity entity, float ticks) {
        return new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).add(getInterpolatedAmount(entity, ticks));
    }

    public static double getRelativeX(float yaw) {
        return (double) (MathHelper.sin(-yaw * 0.017453292F));
    }

    public static double getRelativeZ(float yaw) {
        return (double) (MathHelper.cos(yaw * 0.017453292F));
    }

    public static double interpolate ( double previous , double current , float delta ) {
        return previous + ( current - previous ) * (double) delta;
    }

    public static int getPlayersPing(EntityPlayer entityPlayer){
        if(mc.world == null || mc.player == null || entityPlayer == null || mc.getConnection() == null) return 0;
        final NetworkPlayerInfo info = mc.getConnection().getPlayerInfo(entityPlayer.getGameProfile().getId());
        if(info == null) return 0;
        return info.getResponseTime();
    }

    public static boolean isMovementKeysPressed(){ return mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown(); }
    public static boolean isPlayerMovingMomentum() { return mc.player.moveForward > 0 || mc.player.moveStrafing > 0 || mc.player.moveForward < 0 || mc.player.moveStrafing < 0; }

    public static Vec3d getPositionVec(final EntityLivingBase entityLivingBase) {
        final double[] position = getPosition(entityLivingBase);
        return new Vec3d (position[0], position[1], position[2]);
    }

    public static double[] getPositionEyes(final EntityLivingBase entityLivingBase){
        final double[] position = getPosition(entityLivingBase);
        return new double[] {position[0], position[1] + Wrapper.getPlayer().getEyeHeight(), position[2]};
    }

    public static BlockPos getPlayerPos() { return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ)); }
    public static Vec3d getPlayerPosAddEyes() {
        final Vec3d vecPos = mc.player.getPositionVector();
        return new Vec3d(vecPos.x, vecPos.y + mc.player.getEyeHeight() ,vecPos.z);
    }

    public static double[] getPosition(final EntityLivingBase entityLivingBase){
        final double x = entityLivingBase.lastTickPosX + (entityLivingBase.posX - entityLivingBase.lastTickPosX) * mc.getRenderPartialTicks();
        final double y = entityLivingBase.lastTickPosY + (entityLivingBase.posY - entityLivingBase.lastTickPosY) * mc.getRenderPartialTicks();
        final double z = entityLivingBase.lastTickPosZ + (entityLivingBase.posZ - entityLivingBase.lastTickPosZ) * mc.getRenderPartialTicks();
        return new double[] {x,y,z};
    }

    public static Vec3d getPositionEyesVec(final EntityLivingBase entityLivingBase) {
        final double[] position = getPosition(entityLivingBase);
        return new Vec3d (position[0], position[1] + Wrapper.getPlayer().getEyeHeight(), position[2]);
    }

    //Sends players position packet.
    public static void playerPositionPackets(final double x, final double y, final double z){ playerPositionPackets(new Vec3d(x,y,z)); }
    public static void playerPositionPackets(final Vec3d position) {
        Wrapper.getPlayer().connection.sendPacket(new CPacketPlayer.Position(position.x, position.y, position.z, Wrapper.getPlayer().onGround));
        Wrapper.getPlayer().setPosition(position.x, position.y, position.z);
    }

    //Returns the center of the given entity
    private static double getDst(Vec3d vec,Vec3d playerVec) { return playerVec.distanceTo(vec); }
    public static Vec3d center(final Entity entity){
        Vec3d centerLoc = entity.getPositionVector();
        BlockPos centerPos = entity.getPosition();
        Vec3d playerPos = entity.getPositionVector();
        double y = centerPos.getY();
        double x = centerPos.getX();
        double z = centerPos.getZ();

        final Vec3d plusPlus = new Vec3d(x + 0.5, y, z + 0.5);
        final Vec3d plusMinus = new Vec3d(x + 0.5, y, z - 0.5);
        final Vec3d minusMinus = new Vec3d(x - 0.5, y, z - 0.5);
        final Vec3d minusPlus = new Vec3d(x - 0.5, y, z + 0.5);

        if (getDst(plusPlus, playerPos) < getDst(plusMinus, playerPos) && getDst(plusPlus, playerPos) < getDst(minusMinus, playerPos) && getDst(plusPlus, playerPos) < getDst(minusPlus, playerPos)) {
            x = centerPos.getX() + 0.5;
            z = centerPos.getZ() + 0.5;
            centerLoc = new Vec3d(x,y,z);
            return centerLoc;
        } if (getDst(plusMinus, playerPos) < getDst(plusPlus, playerPos) && getDst(plusMinus, playerPos) < getDst(minusMinus, playerPos) && getDst(plusMinus, playerPos) < getDst(minusPlus, playerPos)) {
            x = centerPos.getX() + 0.5;
            z = centerPos.getZ() - 0.5;
            centerLoc = new Vec3d(x,y,z);
            return centerLoc;
        } if (getDst(minusMinus, playerPos) < getDst(plusPlus, playerPos) && getDst(minusMinus, playerPos) < getDst(plusMinus, playerPos) && getDst(minusMinus, playerPos) < getDst(minusPlus, playerPos)) {
            x = centerPos.getX() - 0.5;
            z = centerPos.getZ() - 0.5;
            centerLoc = new Vec3d(x,y,z);
            return centerLoc;
        } if (getDst(minusPlus, playerPos) < getDst(plusPlus, playerPos) && getDst(minusPlus, playerPos) < getDst(plusMinus, playerPos) && getDst(minusPlus, playerPos) < getDst(minusMinus, playerPos)) {
            x = centerPos.getX() - 0.5;
            z = centerPos.getZ() + 0.5;
            centerLoc = new Vec3d(x,y,z);
            return centerLoc;
        }
        return centerLoc;
    }

    //Used to manipulate players motion to push them into a hole.
    public static void moveToPosition(final double x, final double y, final double z, final float speed, final Entity movingEntity){
        final Vec3d targetPos = new Vec3d(x,y,z);
        final float yaw = (float) Math.toRadians(calcAngle(movingEntity.getPositionVector(), new Vec3d(targetPos.x + .5, targetPos.y, targetPos.z + .5))[0]);
        movingEntity.motionX -= MathHelper.sin(yaw) * (speed / 100);
        movingEntity.motionZ += MathHelper.cos(yaw) * (speed / 100);
    }

    public static void moveToPositionVec(final Vec3d pos, float speed, final Entity movingEntity) {
        final float yaw = (float) Math.toRadians(calcAngle(movingEntity.getPositionVector(), pos)[0]);
        movingEntity.setPosition(movingEntity.posX -= MathHelper.sin(yaw) * (speed / 100), movingEntity.posY, movingEntity.posZ += MathHelper.cos(yaw) * (speed / 100));
    }

    public static boolean isPlayerCentered(final EntityPlayer entityPlayer) {
        final Vec3d playerPos = entityPlayer.getPositionVector();
        final Vec3d centerPos = center(entityPlayer);
        return playerPos.distanceTo(centerPos) <= 0.3000;
    }

    public static EnumFacing getMultiBlocks(final EntityPlayer entityPlayer) {
        EnumFacing facing = null;
        final Vec3d pos = center(entityPlayer);
        double dist = 5;
        for(EnumFacing dir : EnumFacing.values()) {
            final Vec3d pos2 = pos.add(dir.getDirectionVec().x,dir.getDirectionVec().y, dir.getDirectionVec().z);
            final double tempDist = entityPlayer.getDistance(pos2.x, pos2.y, pos2.z);
            if(tempDist < dist) {
                dist = tempDist;
                facing = dir;
            }
        }
        return facing;
    }
}
