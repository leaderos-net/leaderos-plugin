package net.leaderos.plugin;

import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import dev.triumphteam.cmd.bukkit.message.BukkitMessageKey;
import dev.triumphteam.cmd.core.message.MessageKey;
import lombok.Getter;
import net.leaderos.plugin.api.LeaderOSAPI;
import net.leaderos.shared.Shared;
import net.leaderos.plugin.modules.cache.Cache;
import net.leaderos.plugin.commands.LeaderOSCommand;
import net.leaderos.plugin.modules.bazaar.Bazaar;
import net.leaderos.plugin.modules.cache.handlers.LoginListener;
import net.leaderos.plugin.modules.webstore.WebStore;
import net.leaderos.shared.helpers.ChatUtil;
import net.leaderos.shared.module.auth.AuthLogin;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

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
     * CommandManager
     */
    @Getter
    private static BukkitCommandManager<CommandSender> commandManager;

    /**
     * onLoad override method of spigot library
     */
    public void onLoad() {
        instance = this;
        shared = new Shared(this);
    }

    /**
     * onEnable override method of Spigot library
     */
    public void onEnable() {
        this.commandManager = BukkitCommandManager.create(this);
        setupCommands();

        LeaderOSAPI.getModuleManager().registerModule(new AuthLogin());
        LeaderOSAPI.getModuleManager().registerModule(new Cache());
        LeaderOSAPI.getModuleManager().registerModule(new WebStore());
        LeaderOSAPI.getModuleManager().registerModule(new Bazaar());
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
                ChatUtil.sendMessage(sender, shared.getLangFile().getMessages().getCommand().getInvalidArgument()));

        commandManager.registerMessage(MessageKey.UNKNOWN_COMMAND, (sender, invalidArgumentContext) ->
                ChatUtil.sendMessage(sender, shared.getLangFile().getMessages().getCommand().getUnknownCommand()));

        commandManager.registerMessage(MessageKey.NOT_ENOUGH_ARGUMENTS, (sender, invalidArgumentContext) ->
                ChatUtil.sendMessage(sender, shared.getLangFile().getMessages().getCommand().getNotEnoughArguments()));

        commandManager.registerMessage(MessageKey.TOO_MANY_ARGUMENTS, (sender, invalidArgumentContext) ->
                ChatUtil.sendMessage(sender, shared.getLangFile().getMessages().getCommand().getTooManyArguments()));

        commandManager.registerMessage(BukkitMessageKey.NO_PERMISSION, (sender, invalidArgumentContext) ->
                ChatUtil.sendMessage(sender, shared.getLangFile().getMessages().getCommand().getNoPerm()));
    }

    /**
     * Constructor of Main class
     */
    public Main() {}

}
