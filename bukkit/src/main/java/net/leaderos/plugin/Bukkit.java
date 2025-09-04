package net.leaderos.plugin;

import com.tcoded.folialib.FoliaLib;
import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import dev.triumphteam.cmd.bukkit.message.BukkitMessageKey;
import dev.triumphteam.cmd.core.message.MessageKey;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import lombok.Getter;
import lombok.Setter;
import net.leaderos.plugin.api.LeaderOSAPI;
import net.leaderos.plugin.commands.LeaderOSCommand;
import net.leaderos.plugin.configuration.Config;
import net.leaderos.plugin.configuration.Language;
import net.leaderos.plugin.configuration.Modules;
import net.leaderos.plugin.helpers.ChatUtil;
import net.leaderos.plugin.helpers.DebugBukkit;
import net.leaderos.plugin.modules.ai.AiModule;
import net.leaderos.plugin.modules.bazaar.BazaarModule;
import net.leaderos.plugin.modules.cache.CacheModule;
import net.leaderos.plugin.modules.connect.ConnectModule;
import net.leaderos.plugin.modules.credit.CreditModule;
import net.leaderos.plugin.modules.discord.DiscordModule;
import net.leaderos.plugin.modules.donations.DonationsModule;
import net.leaderos.plugin.modules.verify.VerifyModule;
import net.leaderos.plugin.modules.voucher.VoucherModule;
import net.leaderos.plugin.modules.webstore.WebStoreModule;
import net.leaderos.shared.Shared;
import net.leaderos.shared.helpers.Placeholder;
import net.leaderos.shared.helpers.PluginUpdater;
import net.leaderos.shared.helpers.UrlUtil;
import org.bstats.bukkit.Metrics;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Main class of project
 *
 * @since 1.0
 * @author poyrazinan
 */
public class Bukkit extends JavaPlugin {

    /**
     * Instance of plugin
     */
    @Getter
    private static Bukkit instance;

    /**
     * Instance of FoliaLib
     */
    @Getter
    private static FoliaLib foliaLib;

    /**
     * Instance of shared;
     */
    @Setter
    @Getter
    private static Shared shared;


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
     * CommandManager
     */
    @Getter
    private static BukkitCommandManager<CommandSender> commandManager;

    /**
     * onLoad override method of spigot library
     */
    public void onLoad() {
        instance = this;
        foliaLib = new FoliaLib(this);
        setupFiles();
        shared = new Shared(
                UrlUtil.format(getConfigFile().getSettings().getUrl()),
                getConfigFile().getSettings().getApiKey(),
                new DebugBukkit()
        );
    }

    /**
     * onEnable override method of Spigot library
     */
    public void onEnable() {
        commandManager = BukkitCommandManager.create(this);
        setupCommands();

        // Loads modules
        LeaderOSAPI.getModuleManager().registerModule(new AiModule());
        LeaderOSAPI.getModuleManager().registerModule(new VerifyModule());
        LeaderOSAPI.getModuleManager().registerModule(new DiscordModule());
        LeaderOSAPI.getModuleManager().registerModule(new CacheModule());
        LeaderOSAPI.getModuleManager().registerModule(new CreditModule());
        LeaderOSAPI.getModuleManager().registerModule(new WebStoreModule());
        LeaderOSAPI.getModuleManager().registerModule(new BazaarModule());
        LeaderOSAPI.getModuleManager().registerModule(new VoucherModule());
        LeaderOSAPI.getModuleManager().registerModule(new DonationsModule());
        LeaderOSAPI.getModuleManager().registerModule(new ConnectModule());

        if (getConfigFile().getSettings().getUrl().equals("https://yourwebsite.com")) {
            getLogger().warning(ChatUtil.getMessage(getLangFile().getMessages().getChangeApiUrl()));
        } else if (getConfigFile().getSettings().getUrl().startsWith("http://")) {
            getLogger().warning(ChatUtil.getMessage(getLangFile().getMessages().getChangeApiUrlHttps()));
        } else {
            LeaderOSAPI.getModuleManager().enableModules();
        }

        // bStats
        Metrics metrics = new Metrics(this, 20385);

        // Check updates
        checkUpdate();
    }

    /**
     * onDisable override method of Spigot library
     */
    public void onDisable() {
        LeaderOSAPI.getModuleManager().disableModules();
    }

    /**
     * Setups commands
     */
    private void setupCommands() {
        commandManager.registerCommand(new LeaderOSCommand());
        commandManager.registerMessage(MessageKey.INVALID_ARGUMENT, (sender, invalidArgumentContext) ->
                ChatUtil.sendMessage(sender, getLangFile().getMessages().getCommand().getInvalidArgument()));

        commandManager.registerMessage(MessageKey.UNKNOWN_COMMAND, (sender, invalidArgumentContext) ->
                ChatUtil.sendMessage(sender, getLangFile().getMessages().getCommand().getUnknownCommand()));

        commandManager.registerMessage(MessageKey.NOT_ENOUGH_ARGUMENTS, (sender, invalidArgumentContext) ->
                ChatUtil.sendMessage(sender, getLangFile().getMessages().getCommand().getNotEnoughArguments()));

        commandManager.registerMessage(MessageKey.TOO_MANY_ARGUMENTS, (sender, invalidArgumentContext) ->
                ChatUtil.sendMessage(sender, getLangFile().getMessages().getCommand().getTooManyArguments()));

        commandManager.registerMessage(BukkitMessageKey.NO_PERMISSION, (sender, invalidArgumentContext) ->
                ChatUtil.sendMessage(sender, getLangFile().getMessages().getCommand().getNoPerm()));
    }

    /**
     * Setups config, lang and modules file file
     */
    public void setupFiles() {
        try {
            this.configFile = ConfigManager.create(Config.class, (it) -> {
                it.withConfigurer(new YamlBukkitConfigurer());
                it.withBindFile(new File(getDataFolder(), "config.yml"));
                it.saveDefaults();
                it.load(true);
            });
            this.modulesFile = ConfigManager.create(Modules.class, (it) -> {
                it.withConfigurer(new YamlBukkitConfigurer());
                it.withBindFile(new File(getDataFolder(), "modules.yml"));
                it.saveDefaults();
                it.load(true);
            });
            String langName = configFile.getSettings().getLang();
            Class langClass = Class.forName("net.leaderos.plugin.configuration.lang." + langName);
            Class<Language> languageClass = langClass;
            this.langFile = ConfigManager.create(languageClass, (it) -> {
                it.withConfigurer(new YamlBukkitConfigurer());
                it.withBindFile(new File(getDataFolder() + "/lang", langName + ".yml"));
                it.saveDefaults();
                it.load(true);
            });
        } catch (Exception exception) {
            getPluginLoader().disablePlugin(this);
            throw new RuntimeException("Error loading config.yml");
        }
    }

    public void checkUpdate() {
        foliaLib.getScheduler().runAsync((task) -> {
            PluginUpdater updater = new PluginUpdater(getDescription().getVersion());
            try {
                if (updater.checkForUpdates()) {
                    String msg = ChatUtil.replacePlaceholders(
                            Bukkit.getInstance().getLangFile().getMessages().getUpdate(),
                            new Placeholder("%version%", updater.getLatestVersion())
                    );
                    ChatUtil.sendMessage(org.bukkit.Bukkit.getConsoleSender(), msg);
                }
            } catch (Exception ignored) {}
        });
    }

    /**
     * Constructor of Main class
     */
    public Bukkit() {}

}
