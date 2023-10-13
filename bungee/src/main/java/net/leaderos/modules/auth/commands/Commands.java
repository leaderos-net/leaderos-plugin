package net.leaderos.modules.auth.commands;

import net.leaderos.modules.auth.AuthLogin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

/**
 * Auth commands
 * @author poyrazinan
 * @since 1.0
 */
public class Commands extends Command {

    /**
     * Constructor of author command
     * @param name of command
     */
    public Commands(String name) {
        super(name);
    }

    /**
     * Default command of auth
     * @param sender executor
     * @param args command args
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer)
            AuthLogin.sendAuthCommandMessage((ProxiedPlayer) sender);
    }
}