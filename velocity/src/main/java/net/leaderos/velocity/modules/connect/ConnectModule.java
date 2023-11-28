package net.leaderos.velocity.modules.connect;


import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.leaderos.shared.helpers.Placeholder;
import net.leaderos.shared.modules.LeaderOSModule;
import net.leaderos.shared.modules.connect.socket.SocketClient;
import net.leaderos.shared.modules.connect.data.CommandsQueue;
import net.leaderos.velocity.Velocity;
import net.leaderos.velocity.helpers.ChatUtil;
import net.leaderos.velocity.modules.connect.listeners.LoginListener;

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
        commandsQueue = new CommandsQueue(Velocity.getInstance().getDataDirectory().toString());

        // Register listeners
        if (Velocity.getInstance().getModulesFile().getConnect().isOnlyOnline()) {
            loginListener = new LoginListener();
            Velocity.getInstance().getServer().getEventManager().register(Velocity.getInstance(), loginListener);
        }

        // Socket connection
        try {
            socket = new SocketClient(Velocity.getInstance().getConfigFile().getSettings().getApiKey(),
                    Velocity.getInstance().getModulesFile().getConnect().getServerToken()) {
                /**
                 * Executes console command
                 * @param command command to execute
                 * @param username username of player
                 */
                @Override
                public void executeCommands(String command, String username) {
                    // If player is offline and onlyOnline is true
                    if (Velocity.getInstance().getModulesFile().getConnect().isOnlyOnline() && Velocity.getInstance().getServer().getPlayer(username).isEmpty()) {
                        Velocity.getInstance().getServer().getScheduler().buildTask(Velocity.getInstance(), () -> {
                            try {
                                commandsQueue.addCommand(username, command);

                                Component msg = ChatUtil.replacePlaceholders(Velocity.getInstance().getLangFile().getMessages().getConnect().getConnectWillExecuteCommand(),
                                        new Placeholder("%command%", command));
                                ChatUtil.sendMessage(Velocity.getInstance().getServer().getConsoleCommandSource(), msg);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }).schedule();
                    } else {
                        Velocity.getInstance().getCommandManager().executeImmediatelyAsync(Velocity.getInstance().getServer().getConsoleCommandSource(), command);
                        Component msg = ChatUtil.replacePlaceholders(Velocity.getInstance().getLangFile().getMessages().getConnect().getConnectExecutedCommand(),
                                new Placeholder("%command%", command));
                        ChatUtil.sendMessage(Velocity.getInstance().getServer().getConsoleCommandSource(), msg);
                    }
                }

                @Override
                public void joinedRoom() {
                    ChatUtil.sendMessage(Velocity.getInstance().getServer().getConsoleCommandSource(),
                            Velocity.getInstance().getLangFile().getMessages().getConnect().getJoinedSocketRoom());
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
            Velocity.getInstance().getServer().getEventManager().unregisterListener(Velocity.getInstance(), loginListener);
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