package net.leaderos.bungee;

import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.bungee.YamlBungeeConfigurer;
import lombok.Getter;
import net.leaderos.bungee.api.ModuleManager;
import net.leaderos.bungee.commands.LeaderOSCommand;
import net.leaderos.bungee.configuration.Config;
import net.leaderos.bungee.configuration.Language;
import net.leaderos.bungee.configuration.Modules;
import net.leaderos.bungee.helpers.ChatUtil;
import net.leaderos.bungee.helpers.DebugBungee;
import net.leaderos.bungee.modules.connect.ConnectModule;
import net.leaderos.bungee.modules.credit.CreditModule;
import net.leaderos.bungee.modules.discord.DiscordModule;
import net.leaderos.bungee.modules.verify.VerifyModule;
import net.leaderos.bungee.modules.ai.AiModule;
import net.leaderos.shared.Shared;
import net.leaderos.shared.helpers.Placeholder;
import net.leaderos.shared.helpers.PluginUpdater;
import net.leaderos.shared.helpers.UrlUtil;
import net.md_5.bungee.api.plugin.Plugin;
import org.bstats.bungeecord.Metrics;

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

        shared = new Shared(
                UrlUtil.format(getConfigFile().getSettings().getUrl()),
                getConfigFile().getSettings().getApiKey(),
                new DebugBungee()
        )
        ;
        Bungee.getInstance().getProxy().getPluginManager().registerCommand(Bungee.getInstance(), new LeaderOSCommand("leaderos"));
        getModuleManager().registerModule(new AiModule());
        getModuleManager().registerModule(new VerifyModule());
        getModuleManager().registerModule(new DiscordModule());
        getModuleManager().registerModule(new CreditModule());
        getModuleManager().registerModule(new ConnectModule());

        if (getConfigFile().getSettings().getUrl().equals("https://yourwebsite.com")) {
            getLogger().warning(ChatUtil.getMessage(getLangFile().getMessages().getChangeApiUrl()));
        } else if (getConfigFile().getSettings().getUrl().startsWith("http://")) {
            getLogger().warning(ChatUtil.getMessage(getLangFile().getMessages().getChangeApiUrlHttps()));
        } else {
            getModuleManager().enableModules();
        }

        // bStats
        Metrics metrics = new Metrics(this, 20386);

        // Check updates
        checkUpdate();
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

    public void checkUpdate() {
        Bungee.getInstance().getProxy().getScheduler().runAsync(Bungee.getInstance(), () -> {
            PluginUpdater updater = new PluginUpdater(getDescription().getVersion());
            try {
                if (updater.checkForUpdates()) {
                    String msg = ChatUtil.replacePlaceholders(
                            Bungee.getInstance().getLangFile().getMessages().getUpdate(),
                            new Placeholder("%version%", updater.getLatestVersion())
                    );
                    ChatUtil.sendMessage(getProxy().getConsole(), msg);
                }
            } catch (Exception ignored) {}
        });
    }

}