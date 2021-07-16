package me.third.right.utils.Client.Manage;

import me.third.right.ThirdMod;
import me.third.right.events.event.PacketEvent;
import me.third.right.events.event.onUpdateWalkingPlayerEvent;
import me.third.right.modules.Client.AimbotHandler;
import me.third.right.utils.Client.Enums.RotationsType;
import me.third.right.utils.Client.Enums.SpecialRotations;
import me.third.right.utils.Client.Objects.Triplet;
import me.third.right.utils.Client.Utils.DelayTimer;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Objects;
import java.util.Random;

import static me.third.right.utils.Client.Utils.RotationUtils.*;
import static me.third.right.utils.Client.Utils.RotationUtils.lookAtPos;

public class RotationHandler {
    public static RotationHandler INSTANCE;
    protected final static Minecraft mc = Minecraft.getMinecraft();
    private static final AimbotHandler handlerModule = ThirdMod.hax.aimbotHandler;
    private final static Random random = new Random();
    private static final DelayTimer delayTimer = new DelayTimer();
    public static Triplet<Float, Float, SpecialRotations> rotation = null;
    private static float serverPitch, serverYaw;
    private static Vec3d position = null;

    public RotationHandler() {
        MinecraftForge.EVENT_BUS.register(this);
        ThirdMod.EVENT_BUS.subscribe(this);
    }

    @EventHandler
    private final Listener<onUpdateWalkingPlayerEvent> playerWalkingListener = new Listener<>(event -> {
        switch (event.getStage()) {
            case 0:
                PositionManager.getPosition();
                RotationManager.getRotation();
                if(rotation != null && (rotation.getThird().equals(SpecialRotations.Forced) || !handlerModule.rotationType.getSelected().equals(RotationsType.OFF))) {
                    RotationManager.setRotations(handlerModule.randomRotations.isChecked()
                            ? rotation.getFirst() + random.nextFloat() / 100 : rotation.getFirst(), handlerModule.randomRotations.isChecked()
                            ? rotation.getSecond() + random.nextFloat() / 100 : rotation.getSecond());
                }
                break;
            case 1:
                PositionManager.resetPosition();
                RotationManager.resetRotation();
                break;
            default:
                break;
        }
    });

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if(rotation != null && mc.player != null) {
            if (handlerModule.noSpoof.isChecked()) {
                mc.player.rotationYaw = rotation.getSecond() + random.nextFloat() / 100;
                mc.player.rotationPitch = rotation.getFirst() + random.nextFloat() / 100;
                if(position != null) {
                    mc.player.posX = position.x;
                    mc.player.posY = position.y;
                    mc.player.posZ = position.z;
                }
                position = null;
            }
            if (delayTimer.passedMs(handlerModule.resetDelaySeconds.getValueI() * 1000L)) {
                resetRotations();
                delayTimer.reset();
            }
        }
    }

    @EventHandler
    private final Listener<PacketEvent.Send> packetSender = new Listener<>(event -> {
        switch (handlerModule.rotationType.getSelected()){
            case OFF:
            case Normal:
                break;
            case NormalPlus:
            case Raytrace:
                if(rotation != null && !rotation.getThird().equals(SpecialRotations.WallBypass) && !rotation.getThird().equals(SpecialRotations.Forced)) {
                    if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
                        final CPacketPlayerTryUseItemOnBlock packet = (CPacketPlayerTryUseItemOnBlock) event.getPacket();
                        final ItemStack itemStack = mc.player.getHeldItem(packet.getHand());
                        if (isValidItemStack(itemStack)) {
                            lookAtPos(packet.getPos());
                        }
                    } else if (event.getPacket() instanceof CPacketPlayerDigging) {
                        final CPacketPlayerDigging packet = (CPacketPlayerDigging) event.getPacket();
                        lookAtPos(packet.getPosition());
                    }
                }
                break;
        }
        if(event.getPacket() instanceof CPacketPlayer) {
            final CPacketPlayer packet = (CPacketPlayer) event.getPacket();
            if(packet.rotating) {
                serverPitch = packet.pitch;
                serverYaw = packet.yaw;
            }
        }
    });

    private boolean isValidItemStack(final ItemStack itemStack){
        return itemStack.getItem().equals(Items.END_CRYSTAL) || itemStack.getItem() instanceof ItemBlock;
    }
    public static double[] angles() {
        return new double[] { serverPitch, serverYaw };
    }

    public static void resetRotationObject() { rotation = null; }
    public static void newRotationObject(){ delayTimer.reset(); }
    public Triplet<Float, Float, SpecialRotations> getRotation(){ return rotation; }
    public static void setRotations(final Triplet<Float, Float, SpecialRotations> rotation1) { rotation = rotation1; }
    public static void setPosition(Vec3d position) {
        RotationHandler.position = position;
        mc.player.setPosition(position.x, position.y, position.z);
    }
}
