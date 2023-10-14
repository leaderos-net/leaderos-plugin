package net.leaderos.bungee.modules.discord.commands;

import net.leaderos.bungee.modules.discord.Discord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

/**
 * Auth commands
 * @author rafaelflromao
 * @since 1.0
 */
public class DiscordSyncCommand extends Command {

    /**
     * Constructor of author command
     * @param name of command
     */
    public DiscordSyncCommand(String name) {
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
            Discord.sendSyncCommandMessage((ProxiedPlayer) sender);
    }
}