package me.third.right.utils.Client.Manage;

import me.third.right.utils.Client.Enums.HoleType;
import me.third.right.utils.Client.Objects.Triplet;
import me.third.right.utils.Client.Utils.BlockInteraction;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class HoleManager {
    protected static final Minecraft mc = Minecraft.getMinecraft();
    public static HoleManager INSTANCE;
    private static final ExecutorService thread = Executors.newFixedThreadPool(1);
    private static List<Triplet<BlockPos, Boolean, HoleType>> holeList = new ArrayList<>();

    public HoleManager() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if(mc.player == null || mc.world == null || mc.player.isDead) return;
        thread.submit(new getHole());
    }

    private static class getHole implements Runnable {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                holeList = BlockInteraction.holeFinder(25);
            }
        }
    }

    public static List<Triplet<BlockPos, Boolean, HoleType>> getHoleList() {
        return holeList;
    }

    public static List<Triplet<BlockPos, Boolean, HoleType>> getHoleList(final float range) {
        final List<Triplet<BlockPos, Boolean, HoleType>> inRange = new ArrayList<>();
        for(Triplet<BlockPos, Boolean, HoleType> triplet : holeList) {
            final BlockPos pos = triplet.getFirst();
            if (mc.player.getDistance(pos.x, pos.y, pos.z) <= range) {
                inRange.add(triplet);
            }
        }
        return inRange;
    }

    public void shutdown() {
        thread.shutdown();
    }
}
