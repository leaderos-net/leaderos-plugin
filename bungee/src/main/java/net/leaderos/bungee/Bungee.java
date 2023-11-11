package net.leaderos.bungee;

import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.bungee.YamlBungeeConfigurer;
import lombok.Getter;
import net.leaderos.bungee.api.ModuleManager;
import net.leaderos.bungee.commands.LeaderOSCommand;
import net.leaderos.bungee.configuration.Config;
import net.leaderos.bungee.configuration.Language;
import net.leaderos.bungee.configuration.Modules;
import net.leaderos.bungee.modules.auth.AuthModule;
import net.leaderos.bungee.modules.connect.ConnectModule;
import net.leaderos.bungee.modules.credit.CreditModule;
import net.leaderos.bungee.modules.discord.DiscordModule;
import net.leaderos.shared.Shared;
import net.leaderos.shared.helpers.UpdateUtil;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;

/**
 * Bungeecord main class
 * @author poyrazinan
 * @since 1.0
 */
public class Bungee extends Plugin {

    /**
     * Config file of plugin
     */
    @Getter
    private Config configFile;

    /**
     * Lang file of plugin
     */
    @Getter
    private Language langFile;

    /**
     * Module file of plugin
     */
    @Getter
    private Modules modulesFile;

    /**
     * Instance of plugin
     */
    @Getter
    private static Bungee instance;

    /**
     * Shared instance
     */
    @Getter
    private static Shared shared;

    /**
     * Module manager
     */
    @Getter
    private static ModuleManager moduleManager;

    /**
     * onEnable method of bungeecord
     */
    public void onEnable() {
        instance = this;
        Bungee.moduleManager = new ModuleManager();
        setupFiles();
        new UpdateUtil(getDescription().getVersion());
        shared = new Shared(getConfigFile().getSettings().getUrl(),
                getConfigFile().getSettings().getApiKey());
        Bungee.getInstance().getProxy().getPluginManager().registerCommand(Bungee.getInstance(), new LeaderOSCommand("leaderos"));
        getModuleManager().registerModule(new AuthModule());
        getModuleManager().registerModule(new DiscordModule());
        getModuleManager().registerModule(new CreditModule());
        getModuleManager().registerModule(new ConnectModule());
        getModuleManager().enableModules();
    }

    /**
     * onDisable method of bungeecord
     */
    public void onDisable() {
        getModuleManager().disableModules();
    }

    /**
     * Setups config, lang and modules file file
     */
    public void setupFiles() {
        try {
            this.configFile = ConfigManager.create(Config.class, (it) -> {
                it.withConfigurer(new YamlBungeeConfigurer());
                it.withBindFile(new File(getDataFolder(), "config.yml"));
                it.saveDefaults();
                it.load(true);
            });
            this.modulesFile = ConfigManager.create(Modules.class, (it) -> {
                it.withConfigurer(new YamlBungeeConfigurer());
                it.withBindFile(new File(getDataFolder(), "modules.yml"));
                it.saveDefaults();
                it.load(true);
            });
            String langName = configFile.getSettings().getLang();
            Class langClass = Class.forName("net.leaderos.bungee.configuration.lang." + langName);
            Class<Language> languageClass = langClass;
            this.langFile = ConfigManager.create(languageClass, (it) -> {
                it.withConfigurer(new YamlBungeeConfigurer());
                it.withBindFile(new File(getDataFolder() + "/lang", langName + ".yml"));
                it.saveDefaults();
                it.load(true);
            });
        } catch (Exception exception) {
            throw new RuntimeException("Error loading config.yml");
        }
    }

}