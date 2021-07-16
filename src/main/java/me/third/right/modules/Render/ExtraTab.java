package me.third.right.modules.Render;

import me.third.right.ThirdMod;
import me.third.right.events.event.PacketEvent;
import me.third.right.mixin.replacement.ThirdGUIPlayerTabOverlay;
import me.third.right.modules.HackStandard;
import me.third.right.settings.setting.CheckboxSetting;
import me.third.right.settings.setting.EnumSetting;
import me.third.right.settings.setting.SliderSetting;
import me.third.right.utils.Client.Utils.Colour;
import me.third.right.utils.Client.Enums.Category;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.network.play.server.SPacketTeams;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static me.third.right.utils.Client.Utils.Colour.getColourUI;

public class ExtraTab extends HackStandard {
    //Vars
    private boolean injected = false;
    private ThirdGUIPlayerTabOverlay guiPlayerTabOverlay = null;
    //Settings
    public final SliderSetting size = setting(new SliderSetting("Size", 80, 1, 100, 1, SliderSetting.ValueDisplay.INTEGER));
    public final CheckboxSetting colouredNames = setting(new CheckboxSetting("ColouredNames", true));
    private final EnumSetting<Colour.colour> friendColour = setting(new EnumSetting<>("Friends Colour", Colour.colour.values(), Colour.colour.DARK_PURPLE));
    private final EnumSetting<Colour.colour> enemiesColour = setting(new EnumSetting<>("Enemies Colour", Colour.colour.values(), Colour.colour.DARK_RED));
    private final EnumSetting<Colour.colour> unFriendedColour = setting(new EnumSetting<>("UnFriended Colour", Colour.colour.values(), Colour.colour.LIGHT_PURPLE));
    public final CheckboxSetting numberPing = setting(new CheckboxSetting("NumberPing", true));
    public final EnumSetting<Colour.colour> pingColour = setting(new EnumSetting<>("Ping Colour", Colour.colour.values(), Colour.colour.DARK_GREEN));
    private final CheckboxSetting cancelTeams = setting(new CheckboxSetting("CancelTeams", "Cancels SPacketTeams may stop you getting kicked.", true));
    public final CheckboxSetting customFont = setting(new CheckboxSetting("CustomFont", true));

    public ExtraTab() {
        super("TabMods", "Changes how tab works.", Category.RENDER);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        activate();
    }

    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
        activate();
    }

    public void activate() {
        if(!injected) {
            if (mc.ingameGUI == null || !customFont.isChecked()) return;
            if (guiPlayerTabOverlay == null) guiPlayerTabOverlay = new ThirdGUIPlayerTabOverlay(mc, mc.ingameGUI);
            ObfuscationReflectionHelper.setPrivateValue(GuiIngame.class, mc.ingameGUI, guiPlayerTabOverlay, "field_175196_v");
            injected = true;
        }
    }

    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
        if(mc.ingameGUI == null) return;
        ObfuscationReflectionHelper.setPrivateValue(GuiIngame.class, mc.ingameGUI, new GuiPlayerTabOverlay(mc, mc.ingameGUI),"field_175196_v");
        injected = false;
    }

    public static String getPlayerName(NetworkPlayerInfo networkPlayerInfoIn) {
        final ExtraTab extraTab = ThirdMod.hax.extraTab;
        String dname = networkPlayerInfoIn.getDisplayName() != null ? networkPlayerInfoIn.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName(networkPlayerInfoIn.getPlayerTeam(), networkPlayerInfoIn.getGameProfile().getName());
        if (ThirdMod.friends.isInTheList(dname)) return String.format("%s%s", getColourUI(extraTab.friendColour.getSelected()), dname);
        else if(ThirdMod.enemies.isInTheList(dname)) return String.format("%s%s", getColourUI(extraTab.enemiesColour.getSelected()), dname);
        else return String.format("%s%s",getColourUI(extraTab.unFriendedColour.getSelected()), dname);
    }

    @EventHandler
    private final Listener<PacketEvent.Receive> packetReceiver = new Listener<>(event -> {
        if(event.getPacket() instanceof SPacketTeams && cancelTeams.isChecked()) {
            event.cancel();
        }
    });
}
