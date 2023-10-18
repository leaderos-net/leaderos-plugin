package net.leaderos.bungee.modules.discord.commands;

import net.leaderos.bungee.Bungee;
import net.leaderos.bungee.helpers.ChatUtil;
import net.leaderos.bungee.modules.discord.DiscordModule;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

/**
 * Discord Sync command
 * @author rafaelflromao
 * @since 1.0
 */
public class SyncCommand extends Command {

    /**
     * Constructor of author command
     * @param name of command
     */
    public SyncCommand(String name) {
        super(name);
    }

    /**
     * Default command of discord sync
     * @param sender executor
     * @param args command args
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer)
            if (sender.hasPermission("leaderos.discord.sync"))
                DiscordModule.sendSyncCommandMessage((ProxiedPlayer) sender);
            else
                ChatUtil.sendMessage(sender, Bungee.getInstance().getLangFile().getMessages().getCommand().getNoPerm());
    }
}