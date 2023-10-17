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
import net.leaderos.plugin.modules.connect.Connect;
import net.leaderos.plugin.modules.credit.Credit;
import net.leaderos.plugin.modules.discord.Discord;
import net.leaderos.plugin.modules.donations.Donations;
import net.leaderos.plugin.modules.voucher.Voucher;
import net.leaderos.shared.Shared;
import net.leaderos.plugin.modules.cache.Cache;
import net.leaderos.plugin.commands.LeaderOSCommand;
import net.leaderos.plugin.modules.bazaar.Bazaar;
import net.leaderos.plugin.modules.webstore.WebStore;
import net.leaderos.plugin.modules.auth.AuthLogin;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Main class of project
 *
 * @since 1.0
 * @author poyrazinan
 */
public class Main extends JavaPlugin {

    /**
     * Instance of plugin
     */
    @Getter
    private static Main instance;

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
        shared = new Shared(Main.getInstance().getConfigFile().getSettings().getUrl(),
                Main.getInstance().getConfigFile().getSettings().getApiKey());
    }

    /**
     * onEnable override method of Spigot library
     */
    public void onEnable() {
        commandManager = BukkitCommandManager.create(this);
        setupCommands();
        // Loads modules
        LeaderOSAPI.getModuleManager().registerModule(new AuthLogin());
        LeaderOSAPI.getModuleManager().registerModule(new Discord());
        LeaderOSAPI.getModuleManager().registerModule(new Cache());
        LeaderOSAPI.getModuleManager().registerModule(new Credit());
        LeaderOSAPI.getModuleManager().registerModule(new WebStore());
        LeaderOSAPI.getModuleManager().registerModule(new Bazaar());
        LeaderOSAPI.getModuleManager().registerModule(new Voucher());
        LeaderOSAPI.getModuleManager().registerModule(new Donations());
        LeaderOSAPI.getModuleManager().registerModule(new Connect());
        LeaderOSAPI.getModuleManager().enableModules();
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
            // TODO MULTI LANG
            //    Class langClass = Class.forName("net.leaderos.plugin.bukkit.configuration.lang." + langName);
            //    Class<Language> languageClass = langClass;
            this.langFile = ConfigManager.create(Language.class, (it) -> {
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
    public Main() {}

}
