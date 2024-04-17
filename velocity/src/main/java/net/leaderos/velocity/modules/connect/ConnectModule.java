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
        commandsQueue = new CommandsQueue(Velocity.getInstance().getDataDirectory().toString());

        // Register listeners
        if (Velocity.getInstance().getModulesFile().getConnect().isOnlyOnline()) {
            loginListener = new LoginListener();
            Velocity.getInstance().getServer().getEventManager().register(Velocity.getInstance(), loginListener);
        }

        // Socket connection
        socket = new SocketClient(Velocity.getInstance().getConfigFile().getSettings().getApiKey(),
                Velocity.getInstance().getModulesFile().getConnect().getServerToken()) {
            /**
             * Executes console command
             * @param commands command list to execute
             * @param username username of player
             */
            @Override
            public void executeCommands(List<String> commands, String username) {
                // If player is offline and onlyOnline is true
                if (Velocity.getInstance().getModulesFile().getConnect().isOnlyOnline() && Velocity.getInstance().getServer().getPlayer(username).isEmpty()) {
                    commandsQueue.addCommands(username, commands);

                    commands.forEach(command -> {
                        Component msg = ChatUtil.replacePlaceholders(
                                Velocity.getInstance().getLangFile().getMessages().getConnect().getConnectWillExecuteCommand(),
                                new Placeholder("%command%", command)
                        );
                        ChatUtil.sendMessage(Velocity.getInstance().getServer().getConsoleCommandSource(), msg);
                    });
                } else {
                    // Execute commands
                    commands.forEach(command -> {
                        Velocity.getInstance().getCommandManager().executeImmediatelyAsync(Velocity.getInstance().getServer().getConsoleCommandSource(), command);
                        Component msg = ChatUtil.replacePlaceholders(
                                Velocity.getInstance().getLangFile().getMessages().getConnect().getConnectExecutedCommand(),
                                new Placeholder("%command%", command)
                        );
                        ChatUtil.sendMessage(Velocity.getInstance().getServer().getConsoleCommandSource(), msg);
                    });
                }
            }

            @Override
            public void subscribed() {
                ChatUtil.sendMessage(Velocity.getInstance().getServer().getConsoleCommandSource(),
                        Velocity.getInstance().getLangFile().getMessages().getConnect().getSubscribedChannel());
            }
        };
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
        socket.getPusher().disconnect();
    }

    /**
     * Constructor of connect
     */
    public ConnectModule() {
    }
}