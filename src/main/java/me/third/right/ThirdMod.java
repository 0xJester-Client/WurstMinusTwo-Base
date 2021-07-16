package me.third.right;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import me.third.right.clickgui.ClickGuiHud;
import me.third.right.events.ForgeEvents;
import me.third.right.hud.HudList;
import me.third.right.modules.Client.ClickGuiHack;
import me.third.right.modules.HackList;
import me.third.right.hud.IngameHUD;
import me.third.right.clickgui.ClickGui;
import me.third.right.commands.CommandList;
import me.third.right.commands.CommandProcessor;
import me.third.right.utils.Client.Enums.ReleaseType;
import me.third.right.utils.Client.Font.CFontRenderer;
import me.third.right.utils.Client.ClientFiles.KeybindList;
import me.third.right.utils.Client.ClientFiles.KeybindProcessor;
import me.third.right.utils.Client.Manage.*;
import me.third.right.utils.Render.GLSLSandboxShader;
import me.zero.alpine.EventBus;
import me.zero.alpine.EventManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = ThirdMod.MODID, name = ThirdMod.NAME, version = ThirdMod.VERSION)
public final class ThirdMod {
    public static final String NAME = "Wurst- Base";
    public static final String MODID = "wurstminustwobase";
    public static final String VERSION = "1";
    public static final ReleaseType releaseType = ReleaseType.PreRelease;
    public static final Logger log = LogManager.getLogger("WurstMinusTwo");
    public static GLSLSandboxShader backgroundShader;
    public static long initTime;
    public static final EventBus EVENT_BUS = new EventManager();
    public static Path configFolder;

    @Instance(MODID)
    private static ThirdMod forgeWurst;
    //WurstMinus Managers
    public static HackList hax;
    public static HudList hud1;
    public static CommandList cmds;
    public static KeybindList keybinds;
    public static ClickGui gui;
    public static ClickGuiHud guiHud;
    public static IngameHUD hud;
    public static CommandProcessor cmdProcessor;
    public static KeybindProcessor keybindProcessor;
    public static PlayerListManager friends;
    public static PlayerListManager enemies;
    public static CFontRenderer cFontRenderer;

    @EventHandler
    public void init(FMLInitializationEvent event) {
        //WurstMinus
        if(event.getSide() == Side.SERVER) return;
        configFolder = Paths.get("wurstminusbase");
        if(!Files.exists(configFolder)) {
            try {
                Files.createDirectories(configFolder);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        hax = new HackList(configFolder.resolve("enabled-hacks.json"), configFolder.resolve("settings.json"));
        hax.loadEnabledHacks();
        hax.loadSettings();
        MinecraftForge.EVENT_BUS.register(new ForgeEvents());
        final ClickGuiHack clickGuiHack = hax.clickGuiHack;
        cFontRenderer = new CFontRenderer(clickGuiHack.font.getSelected().fontName.deriveFont(clickGuiHack.fontSize.getValueF()), clickGuiHack.antiAlias.isChecked(), clickGuiHack.fractionalMetrics.isChecked());
        LagCompensator.INSTANCE = new LagCompensator();
        LagDetection.INSTANCE = new LagDetection();
        hud1 = new HudList();
        hax.title.setTitleOnBoot();
        cmds = new CommandList();
        cmdProcessor = new CommandProcessor(cmds);
        gui = new ClickGui(configFolder.resolve("windows.json"));
        gui.init(hax);
        guiHud = new ClickGuiHud(configFolder.resolve("huds.json"));
        guiHud.init(hud1);
        hud = new IngameHUD(gui, hud1, guiHud);
        keybinds = new KeybindList(configFolder.resolve("keybinds.json"));
        keybinds.init();
        keybindProcessor = new KeybindProcessor(hax, keybinds, cmdProcessor);
        RotationHandler.INSTANCE = new RotationHandler();
        ItemHandler.INSTANCE = new ItemHandler();
        HoleManager.INSTANCE = new HoleManager();
        friends = new PlayerListManager(configFolder.resolve("friends.json"));
        enemies = new PlayerListManager(configFolder.resolve("enemies.json"));
        if(hax.rpc.isEnabled()) DiscordPresence.start();
    }

    public static ThirdMod getForgeWurst() { return forgeWurst; }
    public HackList getHax()
    {
        return hax;
    }
    public CommandList getCmds()
    {
        return cmds;
    }
    public KeybindList getKeybinds()
    {
        return keybinds;
    }
    public ClickGui getGui() { return gui; }
    public ClickGuiHud getGuiHud() { return guiHud; }
}
