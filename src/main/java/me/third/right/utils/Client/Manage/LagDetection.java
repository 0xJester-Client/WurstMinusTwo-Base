package me.third.right.utils.Client.Manage;

import me.third.right.ThirdMod;
import me.third.right.events.event.PacketEvent;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.EventListener;

import static me.third.right.utils.Client.Utils.MathUtils.round;

public class LagDetection implements EventListener {
    public static LagDetection INSTANCE;
    private long serverLastUpdated;
    private boolean responding = true;

    public LagDetection() {
        ThirdMod.EVENT_BUS.subscribe(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public boolean isResponding() {
        return responding;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        responding = true;
        if (!(2 * 1000L <= System.currentTimeMillis() - serverLastUpdated)) return;
        responding = false;
    }

    @EventHandler
    private final Listener<PacketEvent.Receive> receiveListener = new Listener<>(event -> serverLastUpdated = System.currentTimeMillis());

    public double timeDifference() {
        return round((System.currentTimeMillis() - serverLastUpdated) / 1000d, 1);
    }
}
