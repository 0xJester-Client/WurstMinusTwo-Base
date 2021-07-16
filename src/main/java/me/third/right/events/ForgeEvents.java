package me.third.right.events;

import me.third.right.events.event.RenderEvent;
import me.third.right.events.event.baritone.BaritoneInitEvent;
import me.third.right.modules.Client.Baritone;
import me.third.right.modules.Hack;
import me.third.right.ThirdMod;
import me.third.right.utils.Client.ClientFiles.SoundRegistrator;
import me.third.right.utils.Client.Enums.ReleaseType;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.lwjgl.opengl.GL11;

public class ForgeEvents {

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent event) {
        if (event.isCanceled()) return;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.disableDepth();

        GlStateManager.glLineWidth(1f);
        ThirdMod.EVENT_BUS.post(new RenderEvent(event.getPartialTicks()));
        GlStateManager.glLineWidth(1f);

        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.enableCull();
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void registerSoundEvents(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(SoundRegistrator.idiot);
    }

    @SubscribeEvent
    public void onBaritoneInit(BaritoneInitEvent event) {
        final Baritone baritone = ThirdMod.hax.baritone;
        ThirdMod.gui.addHack(baritone);
        baritone.setEnabled(true);
        baritone.setReleaseState(ReleaseType.Release);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onMouseInput(InputEvent.MouseInputEvent event) {
        ThirdMod.EVENT_BUS.post(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerDrawn(RenderPlayerEvent.Pre event) {
        ThirdMod.EVENT_BUS.post(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerDrawn(RenderPlayerEvent.Post event) {
        ThirdMod.EVENT_BUS.post(event);
    }

    @SubscribeEvent()
    public void onChunkLoaded(ChunkEvent.Load event) {
        ThirdMod.EVENT_BUS.post(event);
    }

    @SubscribeEvent()
    public void onChunkLoaded(ChunkEvent.Unload event) {
        ThirdMod.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onInputUpdate(InputUpdateEvent event) {
        ThirdMod.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onLivingEntityUseItemEventTick(LivingEntityUseItemEvent.Start entityUseItemEvent) {
        ThirdMod.EVENT_BUS.post(entityUseItemEvent);
    }

    @SubscribeEvent
    public void onLivingDamageEvent(LivingDamageEvent event) {
        ThirdMod.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onEntityJoinWorldEvent(EntityJoinWorldEvent entityJoinWorldEvent) {
        ThirdMod.EVENT_BUS.post(entityJoinWorldEvent);
    }

    @SubscribeEvent
    public void onLivingDeathEvent(LivingDeathEvent livingDeathEvent){
        ThirdMod.EVENT_BUS.post(livingDeathEvent);
    }

    @SubscribeEvent
    public void onPlayerPush(PlayerSPPushOutOfBlocksEvent event) {
        ThirdMod.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        ThirdMod.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent entityEvent) {
        ThirdMod.EVENT_BUS.post(entityEvent);
    }

    @SubscribeEvent
    public void onRenderBlockOverlay(RenderBlockOverlayEvent event) {
        ThirdMod.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        ThirdMod.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onConnectServer(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        ThirdMod.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onDisconnectServer(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        ThirdMod.EVENT_BUS.post(event);
        for(Hack hack : ThirdMod.hax.getRegistry()) {
            hack.onDisconnect();
        }
    }

    @SubscribeEvent
    public void onClientChatReceivedEvent(ClientChatReceivedEvent clientChatReceivedEvent) {
        ThirdMod.EVENT_BUS.post(clientChatReceivedEvent);
    }

    @SubscribeEvent
    public void onClientChatEvent(ClientChatEvent clientChatEvent) {
        ThirdMod.EVENT_BUS.post(clientChatEvent);
    }

    @SubscribeEvent
    public void onGuiOpenEvent(GuiOpenEvent event) { ThirdMod.EVENT_BUS.post(event); }
}