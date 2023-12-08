package net.leaderos.bungee.modules.connect.listeners;

import net.leaderos.bungee.Bungee;
import net.leaderos.bungee.helpers.ChatUtil;
import net.leaderos.bungee.modules.connect.ConnectModule;
import net.leaderos.shared.helpers.Placeholder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * LoginListener listens player join event
 *
 * @author firatkaya
 * @since 1.0
 */
public class LoginListener implements Listener {

    /**
     * Listens player join event to execute commands
     * @param event PlayerJoinEvent
     */
    @EventHandler
    public void onJoin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();

        ConnectModule.getCommandsQueue().getExecutor().execute(() -> {
            final List<String> commands = ConnectModule.getCommandsQueue().getCommands(player.getName());

            if (commands == null || commands.isEmpty()) return;

            Bungee.getInstance().getProxy().getScheduler().schedule(Bungee.getInstance(), () -> {
                if (!player.isConnected()) return;

                // Execute commands
                commands.forEach(command -> {
                    Bungee.getInstance().getProxy().getPluginManager().dispatchCommand(Bungee.getInstance().getProxy().getConsole(), command);

                    String msg = ChatUtil.replacePlaceholders(Bungee.getInstance().getLangFile().getMessages().getConnect().getConnectExecutedCommandFromQueue(),
                            new Placeholder("%command%", command));
                    ChatUtil.sendMessage(Bungee.getInstance().getProxy().getConsole(), msg);
                });

                // Remove commands from queue
                ConnectModule.getCommandsQueue().getExecutor().execute(() -> {
                    ConnectModule.getCommandsQueue().removeCommands(player.getName());
                });
            }, Bungee.getInstance().getModulesFile().getConnect().getExecuteDelay(), TimeUnit.SECONDS);
        });
    }
}
