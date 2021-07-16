package me.third.right.modules.Client;

import me.third.right.ThirdMod;
import me.third.right.modules.Hack;
import me.third.right.modules.HackClient;
import me.third.right.settings.setting.ActionButton;
import me.third.right.settings.setting.CheckboxSetting;
import me.third.right.settings.setting.StringSetting;
import me.third.right.utils.Client.Utils.ChatUtils;
import me.third.right.utils.Wrapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;

@Hack.DontSaveState
public class Config extends HackClient {
    //Vars
    private final Path managerFolder;
    //Settings
    public final CheckboxSetting enabledModules = setting(new CheckboxSetting("IncludeState", "Loads and Backup the enabled state of the module.", true));
    private final StringSetting fileName = setting(new StringSetting("FileName","none"));

    public Config() {
        super("Config", "Allows you to control your configs.");
        managerFolder = ThirdMod.configFolder.resolve("Configs");
        setting(new ActionButton("Load", "", X -> loadConfig(fileName.getString())));
        setting(new ActionButton("Create","", X -> createConfig(fileName.getString())));
    }

    @Override
    public void onDisable() {
        this.setEnabled(true);
    }

    //Enabled Modules configs
    public void loadEnabled(final String name) {
        if(name.toLowerCase(Locale.ROOT).equals("none") || name.isEmpty()) {
            chatOutput("Invalid name or no name given.", true);
            return;
        }
        checkFolder();
        final Path configFolder = ThirdMod.configFolder;
        if(Files.exists(managerFolder.resolve(name+".json"))) {
            try {
                disableAllModules();
                Files.copy(managerFolder.resolve(name.contains(".json") ? "state-"+name : "state-"+name+".json"), configFolder.resolve("enabled-hacks.json"), StandardCopyOption.REPLACE_EXISTING);
                ThirdMod.hax.loadEnabledHacks();
                chatOutput("Loaded state called: state-"+name, false);
            } catch (IOException e) {
                ThirdMod.log.info(e);
                chatOutput("Failed to load state called: state-"+name, true);
                throw new RuntimeException(e);
            }
        } else {
            chatOutput("Failed to load state called: state-"+name+"! Does not exist.", true);
        }
    }
    public void createEnabled(final String name) {
        if(name.toLowerCase(Locale.ROOT).equals("none") || name.isEmpty()) {
            chatOutput("Invalid name or no name given.", true);
            return;
        }
        checkFolder();
        final Path configFolder = ThirdMod.configFolder;
        final Path configPath = managerFolder.resolve(name.contains(".json") ? "state-"+name : "state-"+name+".json");
        try {
            ThirdMod.hax.saveEnabledHacks();
            Files.copy(configFolder.resolve("enabled-hacks.json"), configPath, StandardCopyOption.REPLACE_EXISTING);
            chatOutput("Created state called: state-"+name, false);
        } catch (IOException e) {
            ThirdMod.log.info(e);
            chatOutput("Failed to create state called: state-"+name, true);
            throw new RuntimeException(e);
        }
    }

    //Main configs
    public void loadConfig(final String name) {
        if(name.toLowerCase(Locale.ROOT).equals("none") || name.isEmpty()) {
            chatOutput("Invalid name or no name given.", true);
            return;
        }
        checkFolder();
        final Path configFolder = ThirdMod.configFolder;
        if(Files.exists(managerFolder.resolve(name+".json"))) {
            try {
                Files.copy(managerFolder.resolve(name.contains(".json") ? name : name+".json"), configFolder.resolve("settings.json"), StandardCopyOption.REPLACE_EXISTING);
                ThirdMod.hax.loadSettings();
                chatOutput("Loaded config called:"+name, false);
            } catch (IOException e) {
                ThirdMod.log.info(e);
                chatOutput("Failed to load config called:"+name, true);
                throw new RuntimeException(e);
            }
        } else {
            chatOutput("Failed to load config called:"+name+"! Does not exist.", true);
        }
        if(enabledModules.isChecked()) {
            loadEnabled(name);
        }
    }
    public void createConfig(final String name) {
        if(name.toLowerCase(Locale.ROOT).equals("none") || name.isEmpty()) {
            chatOutput("Invalid name or no name given.", true);
            return;
        }
        checkFolder();
        final Path configFolder = ThirdMod.configFolder;
        final Path configPath = managerFolder.resolve(name.contains(".json") ? name : name+".json");
        try {
            ThirdMod.hax.saveSettings();
            Files.copy(configFolder.resolve("settings.json"), configPath, StandardCopyOption.REPLACE_EXISTING);
            chatOutput("Created config called:"+name, false);
        } catch (IOException e) {
            ThirdMod.log.info(e);
            chatOutput("Failed to create config called:"+name, true);
            throw new RuntimeException(e);
        }
        if(enabledModules.isChecked()) {
            createEnabled(name);
        }
    }

    private void chatOutput(String string, boolean isWarning) {
        if(Wrapper.getPlayer() != null && Wrapper.getWorld() != null) {
            if(isWarning) {
                ChatUtils.warning(string);
            } else {
                ChatUtils.message(string);
            }
        }
    }

    private void disableAllModules() {
        for(Hack hack : ThirdMod.hax.getRegistry()) {
            if((hack instanceof AimbotHandler) || (hack instanceof ItemHandler) || (hack instanceof Config)) continue;
            hack.setEnabled(false);
        }
    }

    private void checkFolder() {
        if(!Files.exists(managerFolder)) {
            try {
                Files.createDirectories(managerFolder);
            } catch (IOException e) {
                ThirdMod.log.info(e);
                throw new RuntimeException(e);
            }
        }
    }
}
