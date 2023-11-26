package net.leaderos.plugin;

import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import dev.triumphteam.cmd.bukkit.message.BukkitMessageKey;
import dev.triumphteam.cmd.core.message.MessageKey;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import lombok.Getter;
import net.leaderos.plugin.api.LeaderOSAPI;
import net.leaderos.plugin.configuration.Config;
import net.leaderos.plugin.configuration.Language;
import net.leaderos.plugin.configuration.Modules;
import net.leaderos.plugin.helpers.ChatUtil;
import net.leaderos.plugin.modules.connect.ConnectModule;
import net.leaderos.plugin.modules.credit.CreditModule;
import net.leaderos.plugin.modules.discord.DiscordModule;
import net.leaderos.plugin.modules.donations.DonationsModule;
import net.leaderos.plugin.modules.voucher.VoucherModule;
import net.leaderos.shared.Shared;
import net.leaderos.plugin.modules.cache.CacheModule;
import net.leaderos.plugin.commands.LeaderOSCommand;
import net.leaderos.plugin.modules.bazaar.BazaarModule;
import net.leaderos.plugin.modules.webstore.WebStoreModule;
import net.leaderos.plugin.modules.auth.AuthModule;
import net.leaderos.shared.helpers.UpdateUtil;
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
     * Instance of shared;
     */
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
        setupFiles();
        shared = new Shared(Bukkit.getInstance().getConfigFile().getSettings().getUrl(),
                Bukkit.getInstance().getConfigFile().getSettings().getApiKey());
    }

    /**
     * onEnable override method of Spigot library
     */
    public void onEnable() {
        commandManager = BukkitCommandManager.create(this);
        setupCommands();
        new UpdateUtil(getDescription().getVersion());
        // Loads modules
        LeaderOSAPI.getModuleManager().registerModule(new AuthModule());
        LeaderOSAPI.getModuleManager().registerModule(new DiscordModule());
        LeaderOSAPI.getModuleManager().registerModule(new CacheModule());
        LeaderOSAPI.getModuleManager().registerModule(new CreditModule());
        LeaderOSAPI.getModuleManager().registerModule(new WebStoreModule());
        LeaderOSAPI.getModuleManager().registerModule(new BazaarModule());
        LeaderOSAPI.getModuleManager().registerModule(new VoucherModule());
        LeaderOSAPI.getModuleManager().registerModule(new DonationsModule());
        LeaderOSAPI.getModuleManager().registerModule(new ConnectModule());
        LeaderOSAPI.getModuleManager().enableModules();

        // bStats
        Metrics metrics = new Metrics(this, 20385);
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

    /**
     * Constructor of Main class
     */
    public Bukkit() {}

}
