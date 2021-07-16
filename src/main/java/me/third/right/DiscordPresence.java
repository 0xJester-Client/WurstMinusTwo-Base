package me.third.right;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import me.third.right.modules.Client.RPC;

import java.util.Locale;

import static me.third.right.utils.Client.Utils.ChatUtils.applyStringArray;
import static me.third.right.utils.Client.Utils.ChatUtils.getString;

public class DiscordPresence {
    public static DiscordRichPresence presence = new DiscordRichPresence();
    private static boolean hasStarted = false;
    private static final DiscordRPC rpc = DiscordRPC.INSTANCE;
    private final static String appId = "743311543654940815";

    public static void start() {
        if(DiscordPresence.hasStarted) return;
        DiscordPresence.hasStarted = true;

        final DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.disconnected = ((var1, var2) -> ThirdMod.log.info("Discord RPC disconnected, var1: " + var1 + ", var2: " + var2));
        DiscordPresence.rpc.Discord_Initialize(appId, handlers, true, "");
        DiscordPresence.presence.startTimestamp = System.currentTimeMillis() / 1000L;

        setRPCStart();
        new Thread(DiscordPresence::setRPC, "Discord-RPC-Callback-Handler").start();
        ThirdMod.log.info("Discord RPC initialised successfully");
    }

    private static void setRPC() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                final RPC rpcModule = ThirdMod.hax.rpc;
                DiscordPresence.rpc.Discord_RunCallbacks();
                //Text
                final String details = getString(rpcModule.details);
                if (!details.isEmpty() && details.length() > 2) DiscordPresence.presence.details = details;
                final String state = getString(rpcModule.state);
                if (!state.isEmpty() && state.length() > 2) DiscordPresence.presence.state = state;
                //Small Image
                final String smallImage = rpcModule.smallImage.getString();
                if (!smallImage.isEmpty()) DiscordPresence.presence.smallImageKey = smallImage;
                if (!smallImage.isEmpty()) DiscordPresence.presence.smallImageText = smallImage;
                //Large Image
                final String largeImage = applyStringArray(rpcModule.largeImage.getString());
                if (!largeImage.isEmpty()) DiscordPresence.presence.largeImageKey = largeImage;
                if (!largeImage.isEmpty()) DiscordPresence.presence.largeImageText = largeImage;
                DiscordPresence.rpc.Discord_UpdatePresence(DiscordPresence.presence);
            }
            catch (Exception e2) { e2.printStackTrace(); }
            try { Thread.sleep(4000L); }
            catch (InterruptedException e3) { e3.printStackTrace(); }
        }
    }

    private static void setRPCStart() {
        final RPC rpcModule = ThirdMod.hax.rpc;
        //Text
        final String details = getString(rpcModule.details);
        if (!details.isEmpty() && details.length() > 2) DiscordPresence.presence.details = details;
        final String state = getString(rpcModule.state);
        if (!state.isEmpty() && state.length() > 2) DiscordPresence.presence.state = state;
        //Small Image
        final String smallImage = rpcModule.smallImage.getString();
        if (!smallImage.isEmpty()) DiscordPresence.presence.smallImageKey = smallImage;
        if (!smallImage.isEmpty()) DiscordPresence.presence.smallImageText = smallImage;
        //Large Image
        final String largeImage = applyStringArray(rpcModule.largeImage.getString());
        if (!largeImage.isEmpty()) DiscordPresence.presence.largeImageKey = largeImage;
        if (!largeImage.isEmpty()) DiscordPresence.presence.largeImageText = largeImage;
        DiscordPresence.rpc.Discord_UpdatePresence(DiscordPresence.presence);
    }

    public static void shutDown() {
        DiscordPresence.rpc.Discord_Shutdown();
        hasStarted = false;
        ThirdMod.log.info("Discord RPC Shutdown");
    }
}
