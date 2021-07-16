package me.third.right.hud.Elements;

import me.third.right.hud.Hud;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import static me.third.right.utils.Render.TwoDRender.*;

public class InventoryHud extends Hud {
    public InventoryHud() {
        super("Inventory");
    }

    @Override
    public void onRender() {
        final NonNullList<ItemStack> items = mc.player.inventory.mainInventory;
        boxRender(getX(),getY(), guiHud.getTextRGBA());
        itemRender(items, getX() ,getY());
    }

    private void boxRender(final int x, final int y, final int[] rgba) {
        drawRect(x + 163, y + 55, x,  y, rgba[0],rgba[1],rgba[2],rgba[3]);
    }

    private void itemRender(final NonNullList<ItemStack> items, final int x, final int y) {
        for (int size = items.size(), item = 9; item < size; ++item) {
            final int slotX = x + 1 + item % 9 * 18;
            final int slotY = y + 1 + (item / 9 - 1) * 18;
            preItemRender();
            mc.getRenderItem().renderItemAndEffectIntoGUI(items.get(item), slotX, slotY);
            mc.getRenderItem().renderItemOverlays(mc.fontRenderer, items.get(item), slotX, slotY);
            postItemRender();
        }
    }
}
