package me.third.right.modules.Client;

import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import baritone.api.Settings;
import baritone.api.event.events.PathEvent;
import baritone.api.event.listener.IGameEventListener;
import baritone.api.utils.SettingsUtil;
import me.third.right.events.BaritoneEvents;
import me.third.right.settings.setting.BaritoneSetting;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatEvent;
import baritone.api.pathing.goals.Goal;
import baritone.api.process.IBaritoneProcess;
import baritone.api.process.PathingCommand;
import baritone.api.process.PathingCommandType;
import me.third.right.ThirdMod;
import me.third.right.modules.Hack;
import me.third.right.modules.HackClient;
import me.third.right.settings.setting.CheckboxSetting;
import me.third.right.settings.setting.SliderSetting;
import me.third.right.utils.Client.Enums.ReleaseType;
import me.third.right.utils.Client.Manage.LagDetection;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Locale;

@Hack.DontSaveState
public class Baritone extends HackClient {
    //Vars
    private boolean listening = false;
    private boolean hasPaused = false;
    private Object prevGoal = null;
    public  Object baritone = null;
    public Object BSettings = null;
    private final Object events;

    //Settings
    private final CheckboxSetting lagPause = setting(new CheckboxSetting("LagPause", false));
    private final SliderSetting lagTime = setting(new SliderSetting("LagTime", "The amount of time required to pause.",4, 2, 20,1, SliderSetting.ValueDisplay.INTEGER));
    private final BaritoneSetting allowBreak = setting(new BaritoneSetting("AllowBreak", BaritoneSetting.SettingType.Boolean));
    private final BaritoneSetting allowPlace = setting(new BaritoneSetting("AllowPlace", BaritoneSetting.SettingType.Boolean));
    private final BaritoneSetting allowSprint = setting(new BaritoneSetting("AllowSprint", BaritoneSetting.SettingType.Boolean));
    private final BaritoneSetting allowDiagonalAscend = setting(new BaritoneSetting("AllowDiagonalAscend", BaritoneSetting.SettingType.Boolean));
    private final BaritoneSetting allowDiagonalDescend = setting(new BaritoneSetting("AllowDiagonalDescend", BaritoneSetting.SettingType.Boolean));
    private final BaritoneSetting assumeWalkOnWater = setting(new BaritoneSetting("AssumeWalkOnWater", BaritoneSetting.SettingType.Boolean));
    private final BaritoneSetting assumeWalkOnLava = setting(new BaritoneSetting("AssumeWalkOnLava", BaritoneSetting.SettingType.Boolean));
    private final BaritoneSetting assumeStep = setting(new BaritoneSetting("AssumeStep", BaritoneSetting.SettingType.Boolean));
    private final BaritoneSetting assumeSafeWalk = setting(new BaritoneSetting("AssumeSafeWalk", BaritoneSetting.SettingType.Boolean));
    private final BaritoneSetting logOnArrival = setting(new BaritoneSetting("LogOnArrival", BaritoneSetting.SettingType.Boolean));
    private final CheckboxSetting logInAutoResume = setting(new CheckboxSetting("LogInAutoResume", "AutoReconnect Must be enabled.", false));

    public Baritone() {
        super("Baritone", "Baritone API");
        setReleaseState(ReleaseType.Hidden);
        events = new BaritoneEvents();
        MinecraftForge.EVENT_BUS.register(this);
        ThirdMod.EVENT_BUS.subscribe(this);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        try {
            if(baritone == null) baritone = BaritoneAPI.getProvider().getPrimaryBaritone();
            if(BSettings == null) {
                BSettings = BaritoneAPI.getSettings();
                allowBreak.setSetting(((Settings) BSettings).allowBreak);
                allowPlace.setSetting(((Settings) BSettings).allowPlace);
                allowSprint.setSetting(((Settings) BSettings).allowSprint);
                allowDiagonalAscend.setSetting(((Settings) BSettings).allowDiagonalAscend);
                allowDiagonalDescend.setSetting(((Settings) BSettings).allowDiagonalDescend);
                assumeWalkOnWater.setSetting(((Settings) BSettings).assumeWalkOnWater);
                assumeWalkOnLava.setSetting(((Settings) BSettings).assumeWalkOnLava);
                assumeStep.setSetting(((Settings) BSettings).assumeStep);
                assumeSafeWalk.setSetting(((Settings) BSettings).assumeSafeWalk);
                logOnArrival.setSetting(((Settings) BSettings).disconnectOnArrival);
                ((Settings) BSettings).chatControl.value = false;
                ((Settings) BSettings).logger.value = (loggz) -> {
                    if(mc.ingameGUI != null) {
                        mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString("["+ThirdMod.NAME+"] ").setStyle(new Style().setColor(TextFormatting.DARK_PURPLE)).appendSibling(loggz));
                    }
                };
            }
            if(!listening) {
                ((IBaritone) baritone).getGameEventHandler().registerEventListener((IGameEventListener) events);
                listening = true;
            }
            if (baritone != null) {
                ((IBaritone) baritone).getPathingControlManager().registerProcess(
                        new IBaritoneProcess() {
                            @Override
                            public boolean isActive() {
                                return hasPaused;
                            }

                            @Override
                            public PathingCommand onTick(boolean calcFailed, boolean isSafeToCancel) {
                                return new PathingCommand(null, PathingCommandType.REQUEST_PAUSE);
                            }

                            @Override
                            public boolean isTemporary() {
                                return true;
                            }

                            @Override
                            public void onLostControl() { }

                            @Override
                            public double priority() {
                                return DEFAULT_PRIORITY + 1;
                            }

                            @Override
                            public String displayName0() {
                                return "Pause/Resume Wurst- 2";
                            }
                        }
                );

                if (lagPause.isChecked() && !LagDetection.INSTANCE.isResponding()) {
                    if (isPathing()) {
                        if (lagTime.getValueI() == LagDetection.INSTANCE.timeDifference()) {
                            ((IBaritone) baritone).getBuilderProcess().pause();
                            hasPaused = true;
                        } else {
                            ((IBaritone) baritone).getBuilderProcess().resume();
                            hasPaused = false;
                        }
                    }
                } else if (hasPaused) {
                    ((IBaritone) baritone).getBuilderProcess().resume();
                    hasPaused = false;
                }

                if(logInAutoResume.isChecked()) {
                    if(prevGoal == null) {
                        if(isPathing()) {
                            prevGoal = ((IBaritone) baritone).getPathingBehavior().getGoal();
                        }
                    } else {
                        if(mc.player == null || mc.world == null) return;
                        if(((Goal) prevGoal).isInGoal(mc.player.getPosition())) {
                            prevGoal = null;
                            ThirdMod.hax.autoReconnect.disable();
                        } else {
                            ((IBaritone) baritone).getCustomGoalProcess().setGoalAndPath((Goal) prevGoal);
                        }
                    }
                }
            }
        } catch (ClassCastException | NoClassDefFoundError ignored){
        }
    }

    @EventHandler
    public Listener<PathEvent> pathEventListener = new Listener<>(event -> {
        if(event.equals(PathEvent.AT_GOAL)) {
            if(mc.player == null || mc.world == null) return;
            prevGoal = null;
            ThirdMod.hax.autoReconnect.disable();
        }
    });

    @EventHandler
    public Listener<ClientChatEvent> clientChatEventListener = new Listener<>(event -> {
        if(BSettings != null) {
            final String message = event.getMessage().toLowerCase(Locale.ROOT).replaceAll("<"+mc.player.gameProfile.getName()+">","");
            if(message.startsWith(((Settings) BSettings).prefix.value+"stop")) {
                prevGoal = null;
                ((IBaritone) baritone).getPathingBehavior().cancelEverything();
            }
        }
    });

    public boolean isPathing() {
        if(baritone != null) return ((IBaritone) baritone).getPathingBehavior().isPathing();
        else return false;
    }

    /* 3ʀᴅ suicide note probable cause baritone.
        For some reason Baritone does not save its setting when using its API.
        Alongside shit documentation and chat controls completely breaks when using it.
        so i rate it 4/10.
        Im fixing more shit then actually interacting with the API.
     */
    public void saveSettings() {
        if(baritone != null && BSettings != null) {
            SettingsUtil.save((Settings) BSettings);
        }
    }

    @Override
    public void onClose() {
        saveSettings();
    }

    @Override
    public void onDisable() {
        enable();
    }
}
