package me.third.right.utils.Client.Utils;

import me.third.right.ThirdMod;
import me.third.right.utils.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.util.ConcurrentModificationException;

import static me.third.right.utils.Client.Utils.EntityUtils.center;

public class CombatUtil {
    protected static final Minecraft mc = Minecraft.getMinecraft();

    public static boolean isPlayerEatingGapple(){
        return (mc.player.getActiveItemStack().getItem().equals(Items.GOLDEN_APPLE));
    }

    public static boolean isPlayerEating() {
        return isHandEating(EnumHand.MAIN_HAND) || isHandEating(EnumHand.OFF_HAND);
    }

    public static boolean isHandEating(EnumHand hand) {
        return mc.player.getHeldItem(hand).getItem() instanceof ItemFood && mc.player.getActiveHand().equals(hand);
    }

    //CA calcs PVP shiet innit
    public static float calculateDamage(final EntityEnderCrystal entityEnderCrystal,final EntityPlayer target,final boolean ignoreWebs, final boolean ignorePortals){
        final Vec3d crystalPos = entityEnderCrystal.getPositionVector();
        return calculateDamage(crystalPos.x, crystalPos.y, crystalPos.z, target, ignoreWebs, ignorePortals);
    }

    public static float calculateDamage(final double posX, final double posY, final double posZ, final Entity entity, final boolean ignoreWebs, final boolean ignorePortals) {
        final BlockPos entityPos = new BlockPos(center(entity));
        Block hiddenBlock = null;
        if(ignoreWebs && mc.world.getBlockState(entityPos).getBlock().equals(Blocks.WEB)) {
            mc.world.setBlockToAir(entityPos);
            hiddenBlock = Blocks.WEB;
        }
        if(ignorePortals && mc.world.getBlockState(entityPos).getBlock().equals(Blocks.PORTAL)) {
            mc.world.setBlockToAir(entityPos);
            hiddenBlock = Blocks.PORTAL;
        }
        final float doubleExplosionSize = 12.0f;
        final double distancedsize = entity.getDistance(posX, posY, posZ) / doubleExplosionSize;
        final Vec3d vec3d = new Vec3d(posX, posY, posZ);
        final double blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        final double v = (1.0 - distancedsize) * blockDensity;
        final float damage = (float)(int)((v * v + v) / 2.0 * 7.0 * doubleExplosionSize + 1.0);
        double finald = 1.0;
        if (entity instanceof EntityLivingBase) {
            finald = getBlastReduction((EntityLivingBase)entity, getDamageMultiplied(damage), new Explosion((World)Wrapper.getWorld(), (Entity)null, posX, posY, posZ, 6.0f, true, true));
        }
        if(hiddenBlock != null)
            mc.world.setBlockState(entityPos, hiddenBlock.defaultBlockState);
        return (float)finald;
    }

    public static float getBlastReduction(final EntityLivingBase entity, float damage, final Explosion explosion) {
        if (entity instanceof EntityPlayer) {
            final EntityPlayer ep = (EntityPlayer)entity;
            final DamageSource ds = DamageSource.causeExplosionDamage(explosion);
            try {
                damage = CombatRules.getDamageAfterAbsorb(damage, (float) ep.getTotalArmorValue(), (float) ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
            } catch (ConcurrentModificationException e) {
                ThirdMod.log.info(e);
            }
            int k;
            try {
                k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
            } catch (Exception e) {
                k = 0;
            }
            final float f = MathHelper.clamp((float)k, 0.0f, 20.0f);
            damage *= 1.0f - f / 25.0f;
            if (entity.isPotionActive(Potion.getPotionById(11))) {
                damage -= damage / 4.0f;
            }
            return damage;
        }
        damage = CombatRules.getDamageAfterAbsorb(damage, (float)entity.getTotalArmorValue(), (float)entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        return damage;
    }

    public static float getDamageMultiplied(final float damage) {
        final int diff = mc.world.getDifficulty().getId();
        return damage * ((diff == 0) ? 0.0f : ((diff == 2) ? 1.0f : ((diff == 1) ? 0.5f : 1.5f)));
    }

    public static boolean durabilityCheck(final EntityPlayer entityPlayer, final EntityEquipmentSlot slot, final int damagePercentage){ return durabilityCheck(entityPlayer, slot.getIndex(), damagePercentage, false); }
    public static boolean durabilityCheck(final EntityPlayer entityPlayer, final int slot, final int damagePercentage, final boolean inventory){
        if(entityPlayer == null) return false;
        final ItemStack armourSlot = inventory ? entityPlayer.inventory.mainInventory.get(slot) : entityPlayer.inventory.armorInventory.get(slot);
        try {
            if (armourSlot.isEmpty()) return false;
            else return (100 - (100 * armourSlot.getItemDamage() / armourSlot.getMaxDamage())) < damagePercentage;
        } catch (ArithmeticException e) {
            return false;
        }
    }

    public static boolean canCrystalFit(BlockPos blockPos){
        if(mc.world == null) return false;
        for (final Object entity : mc.world.getEntitiesWithinAABBExcludingEntity((Entity) null, new AxisAlignedBB(blockPos))) {
            if (!(entity instanceof EntityEnderCrystal)) {
                return false;
            }
        }
        return true;
    }
}
