package me.third.right.commands.commands;

import me.third.right.ThirdMod;
import me.third.right.commands.Command;
import me.third.right.utils.Render.Image.Bouncing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class DVDCmd extends Command {
    private boolean enabled;
    private final Bouncing dvdLogo = new Bouncing(100,100, 125, 64, 0.7f, new ResourceLocation(ThirdMod.MODID+":textures/dvd.png"));

    public DVDCmd() {
        super("dvd", "dvd", "dvd");
    }

    @Override
    public void call(String[] args) throws CmdException {
        if(args.length > 0) throw new CmdSyntaxError();
        enabled = !enabled;
        if(enabled) {
            MinecraftForge.EVENT_BUS.register(this);
        } else {
            MinecraftForge.EVENT_BUS.unregister(this);
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        dvdLogo.onTick();
    }

    @SubscribeEvent
    public void onRenderGUI(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL || mc.gameSettings.showDebugInfo) return;
        dvdLogo.onRender();
    }
}
