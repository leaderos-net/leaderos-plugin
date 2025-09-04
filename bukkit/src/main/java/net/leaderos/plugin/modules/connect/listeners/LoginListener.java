package net.leaderos.plugin.modules.connect.listeners;

import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.helpers.ChatUtil;
import net.leaderos.plugin.modules.connect.ConnectModule;
import net.leaderos.shared.helpers.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

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
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Get commands from queue
        ConnectModule.getCommandsQueue().getExecutor().execute(() -> {
            final List<String> commands = ConnectModule.getCommandsQueue().getCommands(player.getName());

            if (commands == null || commands.isEmpty()) return;

            // Execute commands
            Bukkit.getFoliaLib().getScheduler().runLater(() -> {
                if (!player.isOnline()) return;

                commands.forEach(command -> {
                    org.bukkit.Bukkit.dispatchCommand(org.bukkit.Bukkit.getConsoleSender(), command);

                    String msg = ChatUtil.replacePlaceholders(Bukkit.getInstance().getLangFile().getMessages().getConnect().getConnectExecutedCommandFromQueue(),
                            new Placeholder("%command%", command));
                    ChatUtil.sendMessage(org.bukkit.Bukkit.getConsoleSender(), msg);
                });

                // Remove commands from queue
                ConnectModule.getCommandsQueue().getExecutor().execute(() -> {
                    ConnectModule.getCommandsQueue().removeCommands(player.getName());
                });
            }, Bukkit.getInstance().getModulesFile().getConnect().getExecuteDelay() * 20L);
        });
    }
}
