package net.leaderos.plugin.modules.connect;

import com.pusher.client.connection.ConnectionState;
import lombok.Getter;
import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.helpers.ChatUtil;
import net.leaderos.plugin.modules.connect.listeners.LoginListener;
import net.leaderos.plugin.modules.connect.timer.HttpTimer;
import net.leaderos.shared.helpers.Placeholder;
import net.leaderos.shared.modules.LeaderOSModule;
import net.leaderos.shared.modules.connect.socket.SocketClient;
import net.leaderos.shared.modules.connect.data.CommandsQueue;
import org.bukkit.event.HandlerList;

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
    @Getter
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
     * Tracks whether the socket is currently considered connected.
     * Used to avoid redundant HttpTimer start/stop calls on repeated state changes.
     */
    private volatile boolean isConnected = true;

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

        String mode = Bukkit.getInstance().getModulesFile().getConnect().getConnectionMode().toUpperCase();

        if (mode.equals("HTTP")) {
            // HTTP-only mode: skip socket entirely, just poll via HTTP
            isConnected = false;
            HttpTimer.run();
            return;
        }

        // Socket connection for AUTO and SOCKET modes
        socket = new SocketClient(
                Bukkit.getInstance().getConfigFile().getSettings().getApiKey(),
                Bukkit.getInstance().getModulesFile().getConnect().getServerToken()
        ) {
            /**
             * Executes console command
             * @param commands command list to execute
             * @param username username of player
             */
            @Override
            public void executeCommands(List<String> commands, String username) {
                ConnectModule.this.executeCommands(commands, username);
            }

            @Override
            public void subscribed() {
                ChatUtil.sendMessage(org.bukkit.Bukkit.getConsoleSender(), Bukkit.getInstance().getLangFile().getMessages().getConnect().getSubscribedChannel());
            }

            @Override
            public void onConnectionLost() {
                if (mode.equals("AUTO")) {
                    // Only trigger if we were previously connected (true → false)
                    if (isConnected) {
                        isConnected = false;
                        System.out.println("LeaderOS Connect [AUTO]: Socket connection lost. Starting HTTP polling as fallback.");
                        HttpTimer.run();
                    }
                }
            }

            @Override
            public void onConnectionFailed(String message, String code, Exception e) {
                if (mode.equals("AUTO")) {
                    // Only trigger if we were previously connected (true → false)
                    if (isConnected) {
                        isConnected = false;
                        System.out.println("LeaderOS Connect [AUTO]: Socket connection failed. Starting HTTP polling as fallback.");
                        HttpTimer.run();
                    }
                }
            }

            @Override
            public void onConnectionRestored() {
                if (mode.equals("AUTO")) {
                    // Only act if HTTP timer was actually running (i.e. we fell back to HTTP before)
                    if (!isConnected) {
                        isConnected = true;

                        if (HttpTimer.isRunning()) {
                            System.out.println("LeaderOS Connect [AUTO]: Socket connection restored. Stopping HTTP polling.");
                            HttpTimer.stop();
                        }
                    }
                }
            }
        };
    }

    /**
     * onDisable method of module
     */
    public void onDisable() {
        try {
            HandlerList.unregisterAll(loginListener);
            getCommandsQueue().getExecutor().shutdown();
            HttpTimer.stop();
            if (socket != null && socket.getPusher() != null) {
                socket.getPusher().disconnect();
            }
        } catch (Exception ignored) {}
    }

    /**
     * onReload method of module
     */
    public void onReload() {
        if (socket != null && socket.getPusher() != null) {
            socket.getPusher().disconnect();
        }
    }

    /**
     * Check connection and reconnect
     */
    public void reconnect() {
        if (socket != null && socket.getPusher() != null && socket.getPusher().getConnection().getState() == ConnectionState.DISCONNECTED) {
            try {
                socket.getPusher().connect();
            } catch (Exception ignored) {}
        }
    }

    /**
     * Executes console command
     * @param commands command list to execute
     * @param username username of player
     */
    public void executeCommands(List<String> commands, String username) {
        List<String> validatedCommands = new ArrayList<>();

        // Get command blacklist from config
        List<String> commandBlacklist = Bukkit.getInstance().getModulesFile().getConnect().getCommandBlacklist();

        // Check if commands are in blacklist
        for (String command : commands) {
            // If command is not empty and starts with a slash, remove the slash
            if (command.startsWith("/")) {
                command = command.substring(1);
            }

            // Get the root command (the first word before space)
            String commandRoot = command.split(" ")[0].toLowerCase();

            // Clean bukkit: prefix
            if (commandRoot.startsWith("bukkit:")) {
                commandRoot = commandRoot.substring(7);
            }

            // Clean minecraft: prefix
            if (commandRoot.startsWith("minecraft:")) {
                commandRoot = commandRoot.substring(10);
            }

            // Check if the command is blacklisted (case-insensitive now)
            boolean isBlacklisted = false;
            for (String blacklisted : commandBlacklist) {
                if (blacklisted.toLowerCase().equals(commandRoot)) {
                    isBlacklisted = true;
                    break;
                }
            }

            if (isBlacklisted) {
                String msg = ChatUtil.replacePlaceholders(
                        Bukkit.getInstance().getLangFile().getMessages().getConnect().getCommandBlacklisted(),
                        new Placeholder("%command%", command)
                );
                ChatUtil.sendMessage(org.bukkit.Bukkit.getConsoleSender(), msg);
            } else {
                // If command is valid, add to validatedCommands
                validatedCommands.add(command);
            }
        }

        // If player is offline and onlyOnline is true
        if (Bukkit.getInstance().getModulesFile().getConnect().isOnlyOnline() && org.bukkit.Bukkit.getPlayer(username) == null) {
            commandsQueue.addCommands(username, validatedCommands);

            validatedCommands.forEach(command -> {
                String msg = ChatUtil.replacePlaceholders(
                        Bukkit.getInstance().getLangFile().getMessages().getConnect().getConnectWillExecuteCommand(),
                        new Placeholder("%command%", command)
                );
                ChatUtil.sendMessage(org.bukkit.Bukkit.getConsoleSender(), msg);
            });
        } else {
            // Execute validated commands
            Bukkit.getFoliaLib().getScheduler().runNextTick((task) -> {
                validatedCommands.forEach(command -> {
                    org.bukkit.Bukkit.dispatchCommand(org.bukkit.Bukkit.getConsoleSender(), command);

                    String msg = ChatUtil.replacePlaceholders(Bukkit.getInstance().getLangFile().getMessages().getConnect().getConnectExecutedCommand(),
                            new Placeholder("%command%", command));
                    ChatUtil.sendMessage(org.bukkit.Bukkit.getConsoleSender(), msg);
                });
            });
        }
    }

    /**
     * Constructor of connect
     */
    public ConnectModule() {}
}
