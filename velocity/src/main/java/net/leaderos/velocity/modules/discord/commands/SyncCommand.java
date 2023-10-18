package net.leaderos.velocity.modules.discord.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.leaderos.velocity.Velocity;
import net.leaderos.velocity.helpers.ChatUtil;
import net.leaderos.velocity.modules.discord.DiscordModule;

/**
 * AuthCommand of auth module
 * @author poyrazinan
 * @since 1.0
 */
public class SyncCommand implements SimpleCommand {

    /**
     * Executes command method
     * @param invocation command manager
     */
    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();

        if (source instanceof Player) {
            Player player = (Player) source;
            if (player.hasPermission("leaderos.discord.sync"))
                DiscordModule.sendSyncCommandMessage(player);
            else
                ChatUtil.sendMessage(player, Velocity.getInstance().getLangFile().getMessages().getCommand().getNoPerm());
        }

    }
}
