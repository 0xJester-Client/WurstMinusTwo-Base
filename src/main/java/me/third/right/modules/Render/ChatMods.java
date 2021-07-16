package me.third.right.modules.Render;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.third.right.mixin.replacement.ThirdGUINewChat;
import me.third.right.modules.Hack;
import me.third.right.modules.HackStandard;
import me.third.right.settings.setting.CheckboxSetting;
import me.third.right.settings.setting.EnumSetting;
import me.third.right.settings.setting.SliderSetting;
import me.third.right.utils.Client.Utils.Colour;
import me.third.right.utils.Client.Enums.Category;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatMods extends HackStandard {
    //Vars
    private boolean injected = false;
    private ThirdGUINewChat thirdGUINewChat;
    public enum Format {
        Full("(HH:mm:ss) "),
        HoursAndMinutes("(HH:mm) "),
        MinutesAndSeconds("(mm:ss) ");
        public final String formatting;
        Format(final String formatting) {
            this.formatting = formatting;
        }
    }
    //Settings
    public final CheckboxSetting customFont = setting(new CheckboxSetting("CustomFont", true));
    private final CheckboxSetting timeStamps = setting(new CheckboxSetting("TimeStamps", true));
    private final EnumSetting<Format> timeStampFormat = setting(new EnumSetting<Format>("TimeStamp Format", Format.values(), Format.Full));
    private final EnumSetting<Colour.colour> timeStampColour = setting(new EnumSetting<Colour.colour>("TimeStamp Colour", Colour.colour.values(), Colour.colour.DARK_PURPLE));
    public final CheckboxSetting customTextColour = setting(new CheckboxSetting("Custom Text Default Colour", false));
    private final SliderSetting red = setting(new SliderSetting("Text Red", 255, 0, 255, 1, SliderSetting.ValueDisplay.INTEGER));
    private final SliderSetting green = setting(new SliderSetting("Text Green", 255, 0, 255, 1, SliderSetting.ValueDisplay.INTEGER));
    private final SliderSetting blue = setting(new SliderSetting("Text Blue", 255, 0, 255, 1, SliderSetting.ValueDisplay.INTEGER));
    public final CheckboxSetting customRectColour = setting(new CheckboxSetting("Custom Rect Colour", false));
    private final SliderSetting redRect = setting(new SliderSetting("Rect Red", 255, 0, 255, 1, SliderSetting.ValueDisplay.INTEGER));
    private final SliderSetting greenRect = setting(new SliderSetting("Rect Green", 0, 0, 255, 1, SliderSetting.ValueDisplay.INTEGER));
    private final SliderSetting blueRect = setting(new SliderSetting("Rect Blue", 255, 0, 255, 1, SliderSetting.ValueDisplay.INTEGER));
    private final SliderSetting alphaRect = setting(new SliderSetting("Rect Alpha", 100, 0, 255, 1, SliderSetting.ValueDisplay.INTEGER));

    public ChatMods() {
        super("ChatMods", "Modifies how chat works.", Category.RENDER);
    }

    public int[] getColour() {
        return new int[] { red.getValueI(), green.getValueI(), blue.getValueI() };
    }
    public int[] getColourRect() { return new int[] { redRect.getValueI(), greenRect.getValueI(), blueRect.getValueI(), alphaRect.getValueI() }; }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) { activate(); }

    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
        activate();
    }

    public void activate() {
        if(!injected) {
            if (mc.ingameGUI == null) return;
            if (thirdGUINewChat == null) thirdGUINewChat = new ThirdGUINewChat(mc);
            ObfuscationReflectionHelper.setPrivateValue(GuiIngame.class, mc.ingameGUI, thirdGUINewChat, "field_73840_e");
            injected = true;
        }
    }

    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
        if(mc.ingameGUI == null) return;
        ObfuscationReflectionHelper.setPrivateValue(GuiIngame.class, mc.ingameGUI, new GuiNewChat(mc), "field_73840_e");
        injected = false;
    }

    @EventHandler
    public Listener<ClientChatReceivedEvent> listener = new Listener<>(event -> {
        if(mc.player == null || mc.world == null || event.getMessage() == null || !timeStamps.isChecked()) return;
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern(timeStampFormat.getSelected().formatting);
        final LocalDateTime now = LocalDateTime.now();
        final ITextComponent timeStamp = new TextComponentString(timeStampColour.getSelected().codeUI + dtf.format(now) + ChatFormatting.RESET);
        final ITextComponent message = timeStamp.appendSibling(event.getMessage());
        event.setMessage(message);
    });
}
