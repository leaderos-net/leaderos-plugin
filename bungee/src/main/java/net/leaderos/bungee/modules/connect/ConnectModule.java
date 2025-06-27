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
        commandsQueue = new CommandsQueue("plugins/" + Bungee.getInstance().getDescription().getName());

        // Register listeners
        if (Bungee.getInstance().getModulesFile().getConnect().isOnlyOnline()) {
            loginListener = new LoginListener();
            Bungee.getInstance().getProxy().getPluginManager().registerListener(Bungee.getInstance(), loginListener);
        }

        // Socket connection
        socket = new SocketClient(Bungee.getInstance().getConfigFile().getSettings().getApiKey(),
                Bungee.getInstance().getModulesFile().getConnect().getServerToken()) {
            /**
             * Executes console command
             * @param commands command list to execute
             * @param username username of player
             */
            @Override
            public void executeCommands(List<String> commands, String username) {
                List<String> validatedCommands = new ArrayList<>();

                // Get command blacklist from config
                List<String> commandBlacklist = Bungee.getInstance().getModulesFile().getConnect().getCommandBlacklist();

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
                        String msg = ChatUtil.replacePlaceholders(
                                Bungee.getInstance().getLangFile().getMessages().getConnect().getCommandBlacklisted(),
                                new Placeholder("%command%", command)
                        );
                        ChatUtil.sendMessage(Bungee.getInstance().getProxy().getConsole(), msg);
                    } else {
                        // If command is valid, add to validatedCommands
                        validatedCommands.add(command);
                    }
                }

                // If player is offline and onlyOnline is true
                if (Bungee.getInstance().getModulesFile().getConnect().isOnlyOnline() && Bungee.getInstance().getProxy().getPlayer(username) == null) {
                    commandsQueue.addCommands(username, validatedCommands);

                    validatedCommands.forEach(command -> {
                        String msg = ChatUtil.replacePlaceholders(
                                Bungee.getInstance().getLangFile().getMessages().getConnect().getConnectWillExecuteCommand(),
                                new Placeholder("%command%", command)
                        );
                        ChatUtil.sendMessage(Bungee.getInstance().getProxy().getConsole(), msg);
                    });
                } else {
                    // Execute commands
                    validatedCommands.forEach(command -> {
                        Bungee.getInstance().getProxy().getPluginManager().dispatchCommand(Bungee.getInstance().getProxy().getConsole(), command);
                        String msg = ChatUtil.replacePlaceholders(
                                Bungee.getInstance().getLangFile().getMessages().getConnect().getConnectExecutedCommand(),
                                new Placeholder("%command%", command)
                        );
                        ChatUtil.sendMessage(Bungee.getInstance().getProxy().getConsole(), msg);
                    });
                }
            }

            @Override
            public void subscribed() {
                ChatUtil.sendMessage(Bungee.getInstance().getProxy().getConsole(),
                        Bungee.getInstance().getLangFile().getMessages().getConnect().getSubscribedChannel());
            }
        };
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
        socket.getPusher().disconnect();
    }

    /**
     * Constructor of connect
     */
    public ConnectModule() {
    }
}