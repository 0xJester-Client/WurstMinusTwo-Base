package me.third.right.utils.Client.Utils;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.*;

import static net.minecraft.enchantment.Enchantment.getEnchantmentByID;
import static net.minecraft.enchantment.Enchantment.getEnchantmentID;
import static net.minecraft.enchantment.EnchantmentHelper.getEnchantmentLevel;

public class InventoryUtil {
    protected static final Minecraft mc = Minecraft.getMinecraft();

    //Only counts the number of the given item that in the all section of the inventory.
    public static int getNumberOfItem(final Item item){
        int count = 0;
        for(int i = 0;i != mc.player.inventory.getSizeInventory();i++)
            if(mc.player.inventory.getStackInSlot(i).getItem() == item){
                count+= mc.player.inventory.getStackInSlot(i).getCount();
        }
        return count;
    }
    public static int getNumberOfItem(final Block block){
        int count = 0;
        for(int i = 0;i != mc.player.inventory.getSizeInventory();i++){
            final ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
            if(itemStack.getItem() instanceof ItemBlock){
                final Block block1 = ((ItemBlock) itemStack.getItem()).getBlock();
                if(block.equals(block1))
                    count += mc.player.inventory.getStackInSlot(i).getCount();
            }
        }
        return count;
    }

    public static int getNumberOfItem(final Item item,final Boolean hotBar){
        int count = 0;
        int range = 0;
        if(!hotBar)
            range = 9;
        for(int i = range;i != mc.player.inventory.getSizeInventory();i++){
            if(mc.player.inventory.getStackInSlot(i).getItem() == item)
                count+= mc.player.inventory.getStackInSlot(i).getCount();
        }
        return count;
    }
    public static int getNumberOfItem(final Block block,final Boolean hotBar){
        int count = 0;
        int range = 0;
        if(!hotBar)
            range = 9;
        for(int i = range;i != mc.player.inventory.getSizeInventory();i++){
            final ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
            if(itemStack.getItem() instanceof ItemBlock) {
                final Block block1 = ((ItemBlock) itemStack.getItem()).getBlock();
                if (block.equals(block1)) {
                    count += mc.player.inventory.getStackInSlot(i).getCount();
                }
            }
        }
        return count;
    }

    //Gets number of stacks of a type of item.
    public static int getNumberOfStacks(final Item item){
        int count = 0;
        for(int i = 0;i != mc.player.inventory.getSizeInventory();i++){
            if(mc.player.inventory.getStackInSlot(i).getItem() == item){
                count++;
            }
        }
        return count;
    }

    //Searchs the hotbar for slot for requested item
    public static int getSlot(final Block blocks) {
        int slot = -1;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack == ItemStack.EMPTY || !(stack.getItem() instanceof ItemBlock))
                continue;
            final Block block = ((ItemBlock) stack.getItem()).getBlock();
            if (block.equals(blocks)) {
                slot = i;
                break;
            }
        }
        return slot;
    }
    public static int getSlot(final Item item){
        int slot = -1;
        for (int i = 0; i != 9; i++){
            if(mc.player.inventory.getStackInSlot(i).getItem() == item){
                slot = i;
                return slot;
            }
        }
        return slot;
    }
    public static int getSlotPickAxe(){
        int slot = -1;
        for (int i = 0; i != 9; i++) {
            if(mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemPickaxe){
                slot = i;
                return slot;
            }
        }
        return slot;
    }
    public static int getSlotSword(){
        int slot = -1;
        for (int i = 0; i != 9; i++) {
            if(mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemSword){
                slot = i;
                return slot;
            }
        }
        return slot;
    }
    public static int getSlotTool(){
        int slot = -1;
        for (int i = 0; i != 9; i++) {
            if(mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemTool){
                slot = i;
                return slot;
            }
        }
        return slot;
    }

    //Get item / block slots in the inventory+hotbar
    public static int getSlotInventory(final Block blocks) {
        int slot = -1;
        for (int i = 9; i != 36; i++) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack == ItemStack.EMPTY || !(stack.getItem() instanceof ItemBlock))
                continue;
            Block block = ((ItemBlock) stack.getItem()).getBlock();
            if (block.equals(blocks)) {
                slot = i;
                break;
            }
        }
        return slot;
    }
    public static int getSlotInventory(final Item item){
        int slot = -1;
        for(int i = 9;i != 36;i++){
            if(mc.player.inventory.getStackInSlot(i).getItem() == item){
                slot = i;
                return slot;
            }
        }
        return slot;
    }
    //Empty slots count.
    public static int getEmptySlotsHotBar() {
        int count = -1;
        for(int i = 0; i != 8; i++) {
            if(mc.player.inventory.getStackInSlot(i).isEmpty()) {
                count++;
            }
        }
        return count;
    }
    public static int getEmptySlotsInventory() {
        int count = -1;
        for(int i = 8; i != 36; i++) {
            if(mc.player.inventory.getStackInSlot(i).isEmpty()) {
                count++;
            }
        }
        return count;
    }

    //Counts an Items enchantment Level
    public int countEchant(final Item item,final Enchantment enchantment){
        int count = 0;
        for(int i = 0;i != mc.player.inventory.getSizeInventory();i++) {
            final ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if(stack.getItem() == item &&  EnchantmentHelper.getEnchantmentLevel(enchantment,stack) > 0){
                count++;
            }
        }
        return count;
    }

    //Used to Move Items easily
    public static void moveItem(final int slot, final ClickType clickType){
        mc.playerController.windowClick(0, slot < 9 ? slot + 36 : slot,0, clickType, mc.player);
    }

    public enum MoveType {
        Armour,
        Inventory,
        Slot
    }

    public static int getItemDamagePercentage(final int maxDamage, final int damage) {
        return 100 - (100 * damage / maxDamage);
    }
    public static int getItemDamagePercentage(final ItemStack itemStack) {
        if(itemStack.isEmpty() || !itemStack.isItemStackDamageable()) return 0;
        return getItemDamagePercentage(itemStack.getMaxDamage(),itemStack.getItemDamage());
    }

    //Enchantments shite
    public static boolean hasEnchantment(final int enchantmentID, final ItemStack itemStack) {
        return getEnchantmentLevel(getEnchantmentByID(enchantmentID), itemStack) >= 1;
    }
    public static boolean hasEnchantment(final Enchantment enchantment, final  ItemStack itemStack){
        return hasEnchantment(getEnchantmentID(enchantment),itemStack);
    }

    //Will only create these single checks if they're commonly used.
    public static boolean hasMending(final ItemStack itemStack) {
        return getEnchantmentLevel(getEnchantmentByID(getEnchantmentID(Enchantments.MENDING)), itemStack) >= 1;
    }
    public static boolean hasFeatherFalling(final ItemStack itemStack) {
        return getEnchantmentLevel(getEnchantmentByID(getEnchantmentID(Enchantments.FEATHER_FALLING)), itemStack) >= 1;
    }
}
