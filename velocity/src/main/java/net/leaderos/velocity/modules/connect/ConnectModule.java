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

import java.util.ArrayList;
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
                List<String> validatedCommands = new ArrayList<>();

                // Get command blacklist from config
                List<String> commandBlacklist = Velocity.getInstance().getModulesFile().getConnect().getCommandBlacklist();

                // Check if commands are in blacklist
                for (String command : commands) {
                    // If command is not empty and starts with a slash, remove the slash
                    if (command.startsWith("/")) {
                        command = command.substring(1);
                    }

                    // Get the root command (the first word before space)
                    String commandRoot = command.split(" ")[0];

                    // Check if the command is blacklisted
                    if (commandBlacklist.contains(commandRoot)) {
                        Component msg = ChatUtil.replacePlaceholders(
                                Velocity.getInstance().getLangFile().getMessages().getConnect().getCommandBlacklisted(),
                                new Placeholder("%command%", command)
                        );
                        ChatUtil.sendMessage(Velocity.getInstance().getServer().getConsoleCommandSource(), msg);
                    } else {
                        // If command is valid, add to validatedCommands
                        validatedCommands.add(command);
                    }
                }

                // If player is offline and onlyOnline is true
                if (Velocity.getInstance().getModulesFile().getConnect().isOnlyOnline() && Velocity.getInstance().getServer().getPlayer(username).isEmpty()) {
                    commandsQueue.addCommands(username, validatedCommands);

                    validatedCommands.forEach(command -> {
                        Component msg = ChatUtil.replacePlaceholders(
                                Velocity.getInstance().getLangFile().getMessages().getConnect().getConnectWillExecuteCommand(),
                                new Placeholder("%command%", command)
                        );
                        ChatUtil.sendMessage(Velocity.getInstance().getServer().getConsoleCommandSource(), msg);
                    });
                } else {
                    // Execute commands
                    validatedCommands.forEach(command -> {
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