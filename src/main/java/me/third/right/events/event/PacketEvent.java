package me.third.right.events.event;

import me.third.right.events.ThirdEvents;
import net.minecraft.network.Packet;

public class PacketEvent extends ThirdEvents {

    private final Packet packet;
    public PacketEvent(Packet packet) {
        super();
        this.packet = packet;
    }

    public Packet getPacket() {
        return packet;
    }

    public static class Receive extends PacketEvent {
        public Receive(Packet packet) {
            super(packet);
        }
    }
    public static class ReceivePost extends PacketEvent {
        public ReceivePost(Packet packet) {
            super(packet);
        }
    }

    public static class Send extends PacketEvent {
        public Send(Packet packet) {
            super(packet);
        }
    }
    public static class SendPost extends PacketEvent {
        public SendPost(Packet packet) {
            super(packet);
        }
    }
}
