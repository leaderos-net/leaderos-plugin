package net.leaderos.plugin.modules.connect;

import lombok.Getter;
import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.helpers.ChatUtil;
import net.leaderos.plugin.modules.connect.listeners.LoginListener;
import net.leaderos.shared.helpers.Placeholder;
import net.leaderos.shared.modules.LeaderOSModule;
import net.leaderos.shared.modules.connect.socket.SocketClient;
import net.leaderos.shared.modules.connect.data.CommandsQueue;
import org.bukkit.event.HandlerList;

import java.net.URISyntaxException;
import java.util.List;

/**
 * Connect module main class
 *
 * @author poyrazinan
 * @since 1.0
 */
@Getter
public class ConnectModule extends LeaderOSModule {

    /**
     * Socket client for connect to leaderos
     */
    private SocketClient socket;

    /**
     * LoginListener for load cache
     */
    private static LoginListener loginListener;

    /**
     * Commands Queue file
     */
    @Getter
    private static CommandsQueue commandsQueue;

    /**
     * onEnable method of module
     */
    public void onEnable() {
        // Load queue data
        commandsQueue = new CommandsQueue("plugins/" + Bukkit.getInstance().getDescription().getName());

        // Register listeners
        if (Bukkit.getInstance().getModulesFile().getConnect().isOnlyOnline()) {
            loginListener = new LoginListener();
            org.bukkit.Bukkit.getPluginManager().registerEvents(loginListener, Bukkit.getInstance());
        }

        // Socket connection
        try {
            socket = new SocketClient(Bukkit.getInstance().getConfigFile().getSettings().getApiKey(),
                    Bukkit.getInstance().getModulesFile().getConnect().getServerToken()) {
                /**
                 * Executes console command
                 * @param commands command list to execute
                 * @param username username of player
                 */
                @Override
                public void executeCommands(List<String> commands, String username) {
                    // If player is offline and onlyOnline is true
                    if (Bukkit.getInstance().getModulesFile().getConnect().isOnlyOnline() && org.bukkit.Bukkit.getPlayer(username) == null) {
                        commandsQueue.addCommands(username, commands);

                        commands.forEach(command -> {
                            String msg = ChatUtil.replacePlaceholders(
                                    Bukkit.getInstance().getLangFile().getMessages().getConnect().getConnectWillExecuteCommand(),
                                    new Placeholder("%command%", command)
                            );
                            ChatUtil.sendMessage(org.bukkit.Bukkit.getConsoleSender(), msg);
                        });
                    } else {
                        // Execute commands
                        org.bukkit.Bukkit.getScheduler().runTask(Bukkit.getInstance(), () -> {
                            commands.forEach(command -> {
                                org.bukkit.Bukkit.dispatchCommand(org.bukkit.Bukkit.getConsoleSender(), command);

                                String msg = ChatUtil.replacePlaceholders(Bukkit.getInstance().getLangFile().getMessages().getConnect().getConnectExecutedCommand(),
                                        new Placeholder("%command%", command));
                                ChatUtil.sendMessage(org.bukkit.Bukkit.getConsoleSender(), msg);
                            });
                        });
                    }
                }

                @Override
                public void joinedRoom() {
                    ChatUtil.sendMessage(org.bukkit.Bukkit.getConsoleSender(), Bukkit.getInstance().getLangFile().getMessages().getConnect().getJoinedSocketRoom());
                }
            };
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * onDisable method of module
     */
    public void onDisable() {
        // Unregister listeners
        try {
            HandlerList.unregisterAll(loginListener);
            getCommandsQueue().getExecutor().shutdown();
        } catch (Exception ignored) {}
    }

    /**
     * onReload method of module
     */
    public void onReload() {
        socket.getSocket().close();
    }

    /**
     * Constructor of connect
     */
    public ConnectModule() {}
}
