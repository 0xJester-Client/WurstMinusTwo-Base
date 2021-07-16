package me.third.right.mixin.client;

import io.netty.channel.ChannelHandlerContext;
import me.third.right.events.event.PacketEvent;
import me.third.right.events.event.TotemPopEvent;
import me.third.right.ThirdMod;
import me.third.right.utils.Wrapper;
import net.minecraft.entity.Entity;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketEntityStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(NetworkManager.class)
public class MixinNetworkManager {

    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void sendPacket(Packet<?> packet, CallbackInfo callbackInfo) {
        PacketEvent event = new PacketEvent.Send(packet);
        ThirdMod.EVENT_BUS.post(event);

        if (event.isCancelled()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("TAIL"), cancellable = true)
    private void sendPacketPost(Packet<?> packet, CallbackInfo callbackInfo) {
        PacketEvent event = new PacketEvent.SendPost(packet);
        ThirdMod.EVENT_BUS.post(event);

        if (event.isCancelled()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    private void channelRead(ChannelHandlerContext context, Packet<?> packet, CallbackInfo callbackInfo) {
        PacketEvent event = new PacketEvent.Receive(packet);
        ThirdMod.EVENT_BUS.post(event);

        if (event.isCancelled()) {
            callbackInfo.cancel();
        }

        if (event.getPacket() instanceof SPacketEntityStatus) {
            final SPacketEntityStatus packet1 = (SPacketEntityStatus) event.getPacket();
            if (packet1.getOpCode() == 35) {
                final Entity entity = packet1.getEntity(Wrapper.getWorld());
                ThirdMod.EVENT_BUS.post(new TotemPopEvent(entity));
            }
        }
    }

    @Inject(method = "channelRead0", at = @At("TAIL"), cancellable = true)
    private void channelReadPost(ChannelHandlerContext context, Packet<?> packet, CallbackInfo callbackInfo) {
        PacketEvent event = new PacketEvent.ReceivePost(packet);
        ThirdMod.EVENT_BUS.post(event);

        if (event.isCancelled()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "exceptionCaught", at = @At("HEAD"), cancellable = true)
    private void exceptionCaught(ChannelHandlerContext p_exceptionCaught_1_, Throwable p_exceptionCaught_2_, CallbackInfo info) {
        if (p_exceptionCaught_2_ instanceof IOException) info.cancel();
    }
}