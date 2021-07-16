package me.third.right.utils.Client.Utils;

import me.third.right.utils.Client.Enums.SpecialRotations;
import me.third.right.utils.Client.Enums.HoleType;
import me.third.right.utils.Client.Objects.Pair;
import me.third.right.utils.Client.Objects.Triplet;
import me.third.right.utils.Wrapper;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.*;
import net.minecraft.world.GameType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static me.third.right.utils.Client.Utils.EntityUtils.center;
import static me.third.right.utils.Client.Utils.EntityUtils.getPlayerPos;
import static me.third.right.utils.Client.Utils.EntityUtils.getPlayerPosAddEyes;
import static me.third.right.utils.Client.Utils.RotationUtils.lookAtPos;
import static me.third.right.utils.Client.Utils.RotationUtils.wallBypass;

//Util for world information and methods used to interact with the in-game world.
public class BlockInteraction {
    //(Third_Right | 3rd#1703)
    protected static final Minecraft mc = Minecraft.getMinecraft();
    public static boolean isShulkerBox(final Block block){ return block instanceof BlockShulkerBox; }
    public static boolean isContainer(final Block block) { return block instanceof BlockContainer; }

    public static final List<Block> blackList = Arrays.asList(
            Blocks.ENDER_CHEST,
            Blocks.CHEST,
            Blocks.TRAPPED_CHEST,
            Blocks.CRAFTING_TABLE,
            Blocks.ANVIL,
            Blocks.BREWING_STAND,
            Blocks.HOPPER,
            Blocks.DROPPER,
            Blocks.DISPENSER,
            Blocks.TRAPDOOR,
            Blocks.BEACON
    );

    public static boolean canBlockBeSeen(final BlockPos blockPos, final boolean advanced) {
        if(advanced){ return rayTraceCanSeeBlock(blockPos);
        } else return mc.player != null && mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(blockPos.getX() + .5D, blockPos.getY() + .5D, blockPos.getZ() + .5D), false, true, false) == null;
    }

    public static boolean rayTraceCanSeeBlock(final BlockPos blockPos){
        final Vec3d vec3d = new Vec3d(MathHelper.floor(blockPos.x), MathHelper.floor(blockPos.y), MathHelper.floor(blockPos.z));
        for (double x : blockSearchArea) {
            for (double y : blockSearchArea) {
                for (double z : blockSearchArea) {
                    final Pair<RayTraceResult.Type, BlockPos> traceResult = fastRayTraceBlocks(getPlayerPosAddEyes(), new Vec3d(vec3d.x + x, vec3d.y + y, vec3d.z + z));
                    if ((traceResult != null && traceResult.getFirst().equals(RayTraceResult.Type.BLOCK) && traceResult.getSecond().equals(blockPos))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static Pair<RayTraceResult.Type, BlockPos> fastRayTraceBlocks(Vec3d vec31, Vec3d vec32) {//TODO do as many improvements as possible to make this faster
        if (!Double.isNaN(vec31.x) && !Double.isNaN(vec31.y) && !Double.isNaN(vec31.z)) {
            if (!Double.isNaN(vec32.x) && !Double.isNaN(vec32.y) && !Double.isNaN(vec32.z)) {
                int x = MathHelper.floor(vec32.x);
                int y = MathHelper.floor(vec32.y);
                int z = MathHelper.floor(vec32.z);
                int x1 = MathHelper.floor(vec31.x);
                int y1 = MathHelper.floor(vec31.y);
                int z1 = MathHelper.floor(vec31.z);
                BlockPos blockpos = new BlockPos(x1, y1, z1);
                IBlockState iblockstate = mc.world.getBlockState(blockpos);
                Block block = iblockstate.getBlock();
                Pair<RayTraceResult.Type, BlockPos> raytraceresult2;
                if ((iblockstate.getCollisionBoundingBox(mc.world, blockpos) != Block.NULL_AABB) && block.canCollideCheck(iblockstate, false)) {
                    final RayTraceResult result = iblockstate.collisionRayTrace(mc.world, blockpos, vec31, vec32);
                    if(result != null) {
                        raytraceresult2 = new Pair<>(result.typeOfHit, result.getBlockPos());
                        return raytraceresult2;
                    }
                }

                raytraceresult2 = null;
                int var16 = 200;

                while(var16-- >= 0) {
                    if (Double.isNaN(vec31.x) || Double.isNaN(vec31.y) || Double.isNaN(vec31.z)) {
                        return null;
                    }

                    if (x1 == x && y1 == y && z1 == z) {
                        return raytraceresult2;
                    }

                    boolean flag2 = true;
                    boolean flag = true;
                    boolean flag1 = true;
                    double d0 = 999.0D;
                    double d1 = 999.0D;
                    double d2 = 999.0D;
                    if (x > x1) {
                        d0 = (double)x1 + 1.0D;
                    } else if (x < x1) {
                        d0 = (double)x1 + 0.0D;
                    } else {
                        flag2 = false;
                    }

                    if (y > y1) {
                        d1 = (double)y1 + 1.0D;
                    } else if (y < y1) {
                        d1 = (double)y1 + 0.0D;
                    } else {
                        flag = false;
                    }

                    if (z > z1) {
                        d2 = (double)z1 + 1.0D;
                    } else if (z < z1) {
                        d2 = (double)z1 + 0.0D;
                    } else {
                        flag1 = false;
                    }

                    double d3 = 999.0D;
                    double d4 = 999.0D;
                    double d5 = 999.0D;
                    double d6 = vec32.x - vec31.x;
                    double d7 = vec32.y - vec31.y;
                    double d8 = vec32.z - vec31.z;
                    if (flag2) {
                        d3 = (d0 - vec31.x) / d6;
                    }

                    if (flag) {
                        d4 = (d1 - vec31.y) / d7;
                    }

                    if (flag1) {
                        d5 = (d2 - vec31.z) / d8;
                    }

                    if (d3 == -0.0D) {
                        d3 = -1.0E-4D;
                    }

                    if (d4 == -0.0D) {
                        d4 = -1.0E-4D;
                    }

                    if (d5 == -0.0D) {
                        d5 = -1.0E-4D;
                    }

                    EnumFacing enumfacing;
                    if (d3 < d4 && d3 < d5) {
                        enumfacing = x > x1 ? EnumFacing.WEST : EnumFacing.EAST;
                        vec31 = new Vec3d(d0, vec31.y + d7 * d3, vec31.z + d8 * d3);
                    } else if (d4 < d5) {
                        enumfacing = y > y1 ? EnumFacing.DOWN : EnumFacing.UP;
                        vec31 = new Vec3d(vec31.x + d6 * d4, d1, vec31.z + d8 * d4);
                    } else {
                        enumfacing = z > z1 ? EnumFacing.NORTH : EnumFacing.SOUTH;
                        vec31 = new Vec3d(vec31.x + d6 * d5, vec31.y + d7 * d5, d2);
                    }

                    x1 = MathHelper.floor(vec31.x) - (enumfacing == EnumFacing.EAST ? 1 : 0);
                    y1 = MathHelper.floor(vec31.y) - (enumfacing == EnumFacing.UP ? 1 : 0);
                    z1 = MathHelper.floor(vec31.z) - (enumfacing == EnumFacing.SOUTH ? 1 : 0);
                    blockpos = new BlockPos(x1, y1, z1);
                    IBlockState iblockstate1 = mc.world.getBlockState(blockpos);
                    Block block1 = iblockstate1.getBlock();
                    if (iblockstate1.getMaterial() == Material.PORTAL || iblockstate1.getCollisionBoundingBox(mc.world, blockpos) != Block.NULL_AABB) {
                        if (block1.canCollideCheck(iblockstate1, false)) {
                            final RayTraceResult result = iblockstate1.collisionRayTrace(mc.world, blockpos, vec31, vec32);
                            if(result != null) {
                                return new Pair<>(result.typeOfHit, result.getBlockPos());
                            }
                        } else {
                            raytraceresult2 = new Pair<RayTraceResult.Type, BlockPos>(RayTraceResult.Type.MISS, blockpos);
                        }
                    }
                }
                return raytraceresult2;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static boolean canPlaceBlock(final BlockPos blockPos){
        if(!mc.world.getBlockState(blockPos).getBlock().material.isReplaceable()) return false;
        for (Entity entity : mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(blockPos))) {
            if (!(entity instanceof EntityItem) && !(entity instanceof EntityXPOrb)) {
                return false;
            }
        }
        return true;
    }

    public static boolean pastDistance(final EntityPlayer player, final BlockPos pos, final double dist) {
        return player.getDistanceSqToCenter(pos) <= Math.pow(dist, 2);
    }

    public static boolean isPlayerSafe(final EntityPlayer entityPlayer) {
        if(isEntityInHole(entityPlayer, false, false)) {
            return true;
        } else {
            return isEntityInTwoByOne(entityPlayer);
        }
    }

    //Hole info utils rewrite 2
    public static boolean isBlockPosHole(final BlockPos blockPos, final boolean airCheck, final boolean bedRockOnly) {
        boolean isSafe = true;
        if(airCheck)
            for(int i = 0;i < 3;i++)
                if(!mc.world.getBlockState(blockPos.up(i)).getBlock().equals(Blocks.AIR))
                    return false;
        for(BlockPos blockPos1 : holeOffset1[0]){
            final Block block = mc.world.getBlockState(blockPos.add(blockPos1)).getBlock();
            if(!block.equals(Blocks.BEDROCK) && bedRockOnly)
                return false;
            if(!block.equals(Blocks.BEDROCK) && !block.equals(Blocks.OBSIDIAN) && !block.equals(Blocks.ANVIL) && !block.equals(Blocks.ENDER_CHEST)) {
                isSafe = false;
                break;
            }
        }
        return isSafe;
    }
    public static boolean isBlockPosHole(final BlockPos blockPos)   {
        return isBlockPosHole(blockPos, true, false);
    }
    public static boolean isEntityInHole(final Entity entity, final boolean airCheck, final boolean bedRockOnly) {
        final BlockPos entityPosition = new BlockPos(center(entity));
        return isBlockPosHole(entityPosition,airCheck,bedRockOnly);
    }
    private static final BlockPos[] pos = {
            new BlockPos(0,0,0),
            new BlockPos(-1,0,0),
            new BlockPos(0,0, 1)
    };
    public static boolean isEntityInTwoByOne(final Entity entity) {
        for(BlockPos pos : pos) {
            final BlockPos blockPos = new BlockPos(center(entity)).add(pos);
            for (int i = 0; i != holeOffset2.length; i++) {
                if (!mc.world.getBlockState(blockPos).getBlock().equals(Blocks.AIR)) continue;
                boolean isSafe = true;
                switch (i) {
                    case 1:
                        if (!mc.world.getBlockState(blockPos.add(new BlockPos(1, 0, 0))).getBlock().equals(Blocks.AIR))
                            continue;
                        break;
                    case 0:
                        if (!mc.world.getBlockState(blockPos.add(new BlockPos(0, 0, -1))).getBlock().equals(Blocks.AIR))
                            continue;
                        break;
                    default:
                        continue;
                }

                for (BlockPos blockPos1 : holeOffset2[i]) {
                    final Block block = mc.world.getBlockState(blockPos.add(blockPos1)).getBlock();
                    if (!block.equals(Blocks.BEDROCK) && !block.equals(Blocks.OBSIDIAN) && !block.equals(Blocks.ANVIL) && !block.equals(Blocks.ENDER_CHEST)) {
                        isSafe = false;
                        break;
                    }
                }

                if (isSafe) {
                    return true;
                }
            }
        }
        return false;
    }
    public static List<Triplet<BlockPos, Boolean, HoleType>> holeFinder(final float range) {
        final List<Triplet<BlockPos, Boolean, HoleType>> list = holeFinderOneByOne(range);
        list.addAll(holeFinderTwoByOne(range));
        return list;
    }

    public static List<Triplet<BlockPos, Boolean, HoleType>> holeFinderOneByOne(final float range) {
        final NonNullList<Triplet<BlockPos,Boolean,HoleType>> positions = NonNullList.create();
        final List<BlockPos> blockPosList = getSphere(getPlayerPos(), range, (int) range, false, true, 0);
        if(!blockPosList.isEmpty()) {
            for (BlockPos blockPos : blockPosList) {
                if (blockPos == null) continue;
                boolean isSafe = true;
                boolean isBedrock = true;
                for (int i = 0; i < 3; i++)
                    if (!mc.world.getBlockState(blockPos.up(i)).getBlock().equals(Blocks.AIR))
                        isSafe = false;
                if (!isSafe) continue;
                for (BlockPos blockPos1 : holeOffset1[0]) {
                    final Block block = mc.world.getBlockState(blockPos.add(blockPos1)).getBlock();
                    if (!block.equals(Blocks.BEDROCK))
                        isBedrock = false;
                    if (!block.equals(Blocks.BEDROCK) && !block.equals(Blocks.OBSIDIAN) && !block.equals(Blocks.ANVIL) && !block.equals(Blocks.ENDER_CHEST)) {
                        isSafe = false;
                        break;
                    }
                }
                if (!isSafe) continue;
                positions.add(new Triplet<>(blockPos, isBedrock, HoleType.ONEBYONE));
            }
        }
        return positions;
    }

    public static List<Triplet<BlockPos, Boolean, HoleType>> holeFinderTwoByOne(final float range) {
        final NonNullList<Triplet<BlockPos,Boolean,HoleType>> positions = NonNullList.create();
        final List<BlockPos> blockPosList = getSphere(getPlayerPos(), range, (int)range, false, true, 0);
        if(!blockPosList.isEmpty()) {
            for(BlockPos blockPos : blockPosList) {
                for(int i = 0; i != holeOffset2.length; i++) {
                    if (!mc.world.getBlockState(blockPos).getBlock().equals(Blocks.AIR)) continue;
                    final HoleType holeType;
                    boolean isBedrock = true;
                    boolean isSafe = true;
                    final Block block2 = mc.world.getBlockState(blockPos.up()).getBlock();
                    switch (i) {
                        case 1:
                            if (!mc.world.getBlockState(blockPos.add(new BlockPos(1,0,0))).getBlock().equals(Blocks.AIR)
                                    || (!block2.equals(Blocks.AIR) && !mc.world.getBlockState(blockPos.up().add(new BlockPos(1,0,0))).getBlock().equals(Blocks.AIR))) {
                                continue;
                            }
                            holeType = HoleType.TwoByOneEast;
                            break;
                        case 0:
                            if (!mc.world.getBlockState(blockPos.add(new BlockPos(0,0,-1))).getBlock().equals(Blocks.AIR)
                                    || (!block2.equals(Blocks.AIR) && !mc.world.getBlockState(blockPos.up().add(new BlockPos(0,0,-1))).getBlock().equals(Blocks.AIR))) {
                                continue;
                            }
                            holeType = HoleType.TwoByOneNorth;
                            break;
                        default:
                            continue;
                    }

                    for (BlockPos blockPos1 : holeOffset2[i]) {
                        final Block block = mc.world.getBlockState(blockPos.add(blockPos1)).getBlock();
                        if (!block.equals(Blocks.BEDROCK))
                            isBedrock = false;
                        if (!block.equals(Blocks.BEDROCK) && !block.equals(Blocks.OBSIDIAN) && !block.equals(Blocks.ANVIL) && !block.equals(Blocks.ENDER_CHEST)) {
                            isSafe = false;
                            break;
                        }
                    }

                    if(isSafe) {
                        positions.add(new Triplet<>(blockPos, isBedrock, holeType));
                        break;
                    }
                }
            }
        }
        return positions;
    }

    public enum PlaceSpecial {//To add specialised features to placeBlock
        NoRotate,
        PlaceRotate,
        ConstWallBypass
    }
    public static boolean placeBlock(final BlockPos blockPos, final boolean packetPlace, final boolean entityCheck, final boolean visibilityCheck, final int blockSlot, final PlaceSpecial special){
        boolean isSneaking = false;
        if (!mc.world.getBlockState(blockPos).getBlock().material.isReplaceable())
            return false;

        if(entityCheck)
            for (Entity entity : mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(blockPos)))
                if (!(entity instanceof EntityItem) && !(entity instanceof EntityXPOrb))
                    return false;

        final EnumFacing side = getPlaceableSide(blockPos, visibilityCheck);
        if (side == null) return false;

        final BlockPos neighbour = blockPos.offset(side);
        final EnumFacing opposite = side.getOpposite();
        if (!canBeClicked(neighbour)) return false;

        final Vec3d hitVec = new Vec3d(neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        final Block neighbourBlock = mc.world.getBlockState(neighbour).getBlock();
        if(blockSlot == -1 || blockSlot > 8) return false;

        int lastHotbarSlot = -1;
        if (mc.player.inventory.currentItem != blockSlot) {
            lastHotbarSlot = mc.player.inventory.currentItem;
            mc.player.inventory.currentItem = blockSlot;
            mc.playerController.syncCurrentPlayItem();
        }

        if (blackList.contains(neighbourBlock) || isShulkerBox(neighbourBlock)) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
            isSneaking = true;
        }

        switch (special) {
            case NoRotate:
                break;
            case PlaceRotate:
                lookAtPos(neighbour);
                break;
            case ConstWallBypass:
                if(!canBlockBeSeen(neighbour, true)) wallBypass(neighbour);
                else lookAtPos(neighbour, SpecialRotations.WallBypass);
                break;
        }
        if (packetPlace) mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(neighbour, opposite, EnumHand.MAIN_HAND, blockPos.x, blockPos.y, blockPos.z));
        else mc.playerController.processRightClickBlock(mc.player, mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);

        if (!mc.playerController.getCurrentGameType().equals(GameType.CREATIVE)) {
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, neighbour, opposite));
        }

        mc.player.swingArm(EnumHand.MAIN_HAND);
        mc.rightClickDelayTimer = 4;

        if (lastHotbarSlot != -1) {
            mc.player.inventory.currentItem = lastHotbarSlot;
            mc.playerController.syncCurrentPlayItem();
        }
        if(isSneaking) mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        return true;
    }

    public static boolean placeBlock(final BlockPos blockPos, final boolean packetPlace, final boolean entityCheck, final boolean visibilityCheck, final int blockSlot) {
        return placeBlock(blockPos, packetPlace, entityCheck, visibilityCheck,blockSlot, PlaceSpecial.PlaceRotate);
    }

    public static boolean canBeClicked(BlockPos pos) {
        return getBlock(pos).canCollideCheck(getState(pos), false);
    }
    private static Block getBlock(BlockPos pos) {
        return getState(pos).getBlock();
    }
    private static IBlockState getState(BlockPos pos) {
        return Wrapper.getWorld().getBlockState(pos);
    }

    //Stolen from kami
    public static EnumFacing getPlaceableSide(final BlockPos pos, final boolean visibilityCheck) {
        for (EnumFacing side : EnumFacing.values()) {
            final BlockPos neighbour = pos.offset(side);
            if (!mc.world.getBlockState(neighbour).getBlock().canCollideCheck(mc.world.getBlockState(neighbour), false) || (visibilityCheck && !canBlockBeSeen(neighbour, true))) continue;
            final IBlockState blockState = mc.world.getBlockState(neighbour);
            if (!blockState.getMaterial().isReplaceable()) return side;
        }
        return null;
    }

    public static List<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y) {
        final List<BlockPos> circleBlocks = new ArrayList<>();
        int cx = loc.getX();
        int cy = loc.getY();
        int cz = loc.getZ();
        for (int x = cx - (int) r; x <= cx + r; x++) {
            for (int z = cz - (int) r; z <= cz + r; z++) {
                for (int y = (sphere ? cy - (int) r : cy); y < (sphere ? cy + r : cy + h); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < r * r && !(hollow && dist < (r - 1) * (r - 1))) {
                        BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleBlocks.add(l);
                    }
                }
            }
        }
        return circleBlocks;
    }

    public static List<BlockPos> getCircle(BlockPos loc, int y, float r, boolean hollow) {
        final List<BlockPos> circleBlocks = new ArrayList<>();
        int cx = loc.getX();
        int cz = loc.getZ();
        for (int x = cx - (int) r; x <= cx + r; x++) {
            for (int z = cz - (int) r; z <= cz + r; z++) {
                double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z);
                if (dist < r * r && !(hollow && dist < (r - 1) * (r - 1))) {
                    BlockPos l = new BlockPos(x, y, z);
                    circleBlocks.add(l);
                }
            }
        }
        return circleBlocks;
    }

    //Offsets
    public static final Vec3d[] offsets = {
            new Vec3d(1, 0, 0),
            new Vec3d(0, 0, 1),
            new Vec3d(-1, 0, 0),
            new Vec3d(0, 0, -1)
    };

    //Better offset arrays
    public static final BlockPos[][] holeOffset1 = {{
            new BlockPos(0, 0, -1), // north
            new BlockPos(1, 0, 0), // east
            new BlockPos(0, 0, 1), // south
            new BlockPos(-1, 0, 0), // west
            new BlockPos(0, -1, 0) // down
    },{
            new BlockPos(0, 0, -1), // north
            new BlockPos(1, 0, 0), // east
            new BlockPos(0, 0, 1), // south
            new BlockPos(-1, 0, 0) // west
    }};

    public static final BlockPos[][] holeOffset2 = {{//North and South
            new BlockPos(0, 0, -2), // north
            new BlockPos(0, 0, 1), // south

            new BlockPos(-1, 0, -1), // west
            new BlockPos(1, 0, -1), // east

            new BlockPos(-1, 0, 0), // west
            new BlockPos(1, 0, 0), // east

            new BlockPos(0, -1, 0), // down
            new BlockPos(0, -1, -1) // down2
    },{//EAST and WEST
            new BlockPos(0, 0, -1), // north
            new BlockPos(0, 0, 1), // south
            new BlockPos(1, 0, -1), // north
            new BlockPos(1, 0, 1), // south

            new BlockPos(2, 0, 0), // east
            new BlockPos(-1, 0, 0), // west

            new BlockPos(0, -1, 0), // down
            new BlockPos(1, -1, 0) // down
    }};

    public static final BlockPos[][] offsets2 = {{//North and South
            new BlockPos(0, 0, -2), // north
            new BlockPos(0, 0, 1), // south
            new BlockPos(-1, 0, -1), // west
            new BlockPos(1, 0, -1), // east
            new BlockPos(-1, 0, 0), // west
            new BlockPos(1, 0, 0), // east
    },{//EAST and WEST
            new BlockPos(0, 0, -1), // north
            new BlockPos(0, 0, 1), // south
            new BlockPos(1, 0, -1), // north
            new BlockPos(1, 0, 1), // south
            new BlockPos(2, 0, 0), // east
            new BlockPos(-1, 0, 0), // west
    }};

    public final static double[] blockSearchArea = {//Best surface area and performance.
            0.5D,
            0.4D,
            0.3D,
            0.8D,
            0.9D
    };

    public static boolean canBreakBlock(final BlockPos blockPos) {
        final IBlockState blockState = mc.world.getBlockState(blockPos);
        return blockState.getBlock().blockHardness != -1;
    }
}
