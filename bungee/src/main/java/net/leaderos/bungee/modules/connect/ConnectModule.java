package net.leaderos.bungee.modules.connect;


import lombok.Getter;
import net.leaderos.bungee.Bungee;
import net.leaderos.bungee.helpers.ChatUtil;
import net.leaderos.bungee.modules.connect.listeners.LoginListener;
import net.leaderos.shared.helpers.Placeholder;
import net.leaderos.shared.modules.LeaderOSModule;
import net.leaderos.shared.modules.connect.socket.SocketClient;
import net.leaderos.shared.modules.connect.data.CommandsQueue;

import java.net.URISyntaxException;

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
        commandsQueue = new CommandsQueue("plugins/" + Bungee.getInstance().getDescription().getName());

        // Register listeners
        if (Bungee.getInstance().getModulesFile().getConnect().isOnlyOnline()) {
            loginListener = new LoginListener();
            Bungee.getInstance().getProxy().getPluginManager().registerListener(Bungee.getInstance(), loginListener);
        }

        // Socket connection
        try {
            socket = new SocketClient(Bungee.getInstance().getConfigFile().getSettings().getApiKey(),
                    Bungee.getInstance().getModulesFile().getConnect().getServerToken()) {
                /**
                 * Executes console command
                 * @param command command to execute
                 * @param username username of player
                 */
                @Override
                public void executeCommands(String command, String username) {
                    // If player is offline and onlyOnline is true
                    if (Bungee.getInstance().getModulesFile().getConnect().isOnlyOnline() && Bungee.getInstance().getProxy().getPlayer(username) == null) {
                        commandsQueue.addCommand(username, command);

                        String msg = ChatUtil.replacePlaceholders(Bungee.getInstance().getLangFile().getMessages().getConnect().getConnectWillExecuteCommand(),
                                new Placeholder("%command%", command));
                        ChatUtil.sendMessage(Bungee.getInstance().getProxy().getConsole(), msg);
                    } else {
                        Bungee.getInstance().getProxy().getPluginManager().dispatchCommand(Bungee.getInstance().getProxy().getConsole(), command);
                        String msg = ChatUtil.replacePlaceholders(Bungee.getInstance().getLangFile().getMessages().getConnect().getConnectExecutedCommand(),
                                new Placeholder("%command%", command));
                        ChatUtil.sendMessage(Bungee.getInstance().getProxy().getConsole(), msg);
                    }
                }

                @Override
                public void joinedRoom() {
                    ChatUtil.sendMessage(Bungee.getInstance().getProxy().getConsole(),
                            Bungee.getInstance().getLangFile().getMessages().getConnect().getJoinedSocketRoom());
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
            Bungee.getInstance().getProxy().getPluginManager().unregisterListener(loginListener);
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
    public ConnectModule() {
    }
}