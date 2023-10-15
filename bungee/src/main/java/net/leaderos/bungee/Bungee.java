package net.leaderos.bungee;

import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.bungee.YamlBungeeConfigurer;
import lombok.Getter;
import net.leaderos.bungee.api.ModuleManager;
import net.leaderos.bungee.commands.LeaderOSCommand;
import net.leaderos.bungee.configuration.Config;
import net.leaderos.bungee.configuration.Language;
import net.leaderos.bungee.configuration.Modules;
import net.leaderos.bungee.modules.auth.AuthLogin;
import net.leaderos.bungee.modules.connect.Connect;
import net.leaderos.bungee.modules.credits.Credit;
import net.leaderos.bungee.modules.discord.Discord;
import net.leaderos.shared.Shared;
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
        shared = new Shared(getConfigFile().getSettings().getUrl(),
                getConfigFile().getSettings().getApiKey());
        Bungee.getInstance().getProxy().getPluginManager().registerCommand(Bungee.getInstance(), new LeaderOSCommand("leaderos"));
        getModuleManager().registerModule(new AuthLogin());
        getModuleManager().registerModule(new Discord());
        getModuleManager().registerModule(new Credit());
        getModuleManager().registerModule(new Connect());
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
            // TODO MULTI LANG
            //    Class langClass = Class.forName("net.leaderos.plugin.bukkit.configuration.lang." + langName);
            //    Class<Language> languageClass = langClass;
            this.langFile = ConfigManager.create(Language.class, (it) -> {
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