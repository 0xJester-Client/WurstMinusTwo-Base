package me.third.right.utils.Client.Manage;

import me.third.right.ThirdMod;
import me.third.right.events.event.PacketEvent;
import me.third.right.utils.Client.Objects.ItemMove;
import me.third.right.utils.Client.Utils.ChatUtils;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ItemHandler {
    protected final static Minecraft mc = Minecraft.getMinecraft();
    private final me.third.right.modules.Client.ItemHandler itemHandler = ThirdMod.hax.itemHandler;
    public static ItemHandler INSTANCE;
    private static boolean isMoving = false;
    private static ItemMove[] toMove;
    private int offsetCounter = 0;
    private int delayCounter = 0;
    private static long id = 0;

    public ItemHandler() { MinecraftForge.EVENT_BUS.register(this); }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if(mc.player == null || mc.world == null || mc.player.isDead || mc.player.getHealth() <= 0.0) return;
        if(delayCounter >= itemHandler.tickDelay.getValueI()) {
            delayCounter = 0;
            if (isMoving) {
                int moves = 0;
                while (moves < itemHandler.movesPerTick.getValueI()) {
                    if (offsetCounter != toMove.length) {
                        final ItemMove move = toMove[offsetCounter];
                        final int slot = move.slot;
                        switch (move.moveType) {
                            case Inventory:
                                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot < 9 ? slot + 36 : slot, 0, move.clickType, mc.player);
                                moves++;
                                break;
                            case Armour:
                                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 8 - slot, 0, move.clickType, mc.player);
                                moves++;
                                break;
                            case Slot:
                                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, move.data, move.clickType, mc.player);
                                moves++;
                                break;
                        }
                        offsetCounter++;
                    } else break;
                }
                //mc.playerController.updateController();
                if (toMove.length <= offsetCounter) {
                    toMove = null;
                    isMoving = false;
                    offsetCounter = 0;
                }
            }
        } else delayCounter++;
    }

    public static long getId() {
        return id;
    }

    public static boolean isMoving() {
        return isMoving;
    }

    public static void sendMoveRequest(ItemMove[] movements) {
        if(isMoving) return;
        id++;
        isMoving = true;
        toMove = movements;
    }
}
