package me.third.right.utils.Client.Objects;

import me.third.right.utils.Client.Utils.InventoryUtil;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;

public class ItemMove {
    public final int slot;
    public int data = 0;
    public final InventoryUtil.MoveType moveType;
    public final ClickType clickType;

    public ItemMove(final int slot, final InventoryUtil.MoveType moveType, final ClickType clickType) {
        this.slot = slot;
        this.moveType = moveType;
        this.clickType = clickType;
    }
    public ItemMove(final int slot, final int data, final InventoryUtil.MoveType moveType, final ClickType clickType) {
        this(slot, moveType, clickType);
        this.data = data;
    }
}
