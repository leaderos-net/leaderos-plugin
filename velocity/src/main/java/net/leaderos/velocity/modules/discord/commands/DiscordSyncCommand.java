package net.leaderos.velocity.modules.discord.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.leaderos.velocity.modules.discord.Discord;

/**
 * AuthCommand of auth module
 * @author poyrazinan
 * @since 1.0
 */
public class DiscordSyncCommand implements SimpleCommand {

    /**
     * Executes command method
     * @param invocation command manager
     */
    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (source instanceof Player)
            Discord.sendSyncCommandMessage((Player) source);
    }
}
