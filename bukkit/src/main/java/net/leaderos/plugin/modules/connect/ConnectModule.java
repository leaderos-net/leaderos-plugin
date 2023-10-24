package net.leaderos.plugin.modules.connect;

import lombok.Getter;
import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.helpers.ChatUtil;
import net.leaderos.shared.helpers.Placeholder;
import net.leaderos.shared.modules.LeaderOSModule;
import net.leaderos.shared.modules.connect.socket.SocketClient;

import java.net.URISyntaxException;

/**
 * Connect module main class
 *
 * @author poyrazinan
 * @since 1.0
 */
@Getter
public class ConnectModule extends LeaderOSModule {

    private SocketClient socket;

    /**
     * onEnable method of module
     */
    public void onEnable() {
        try {
            socket = new SocketClient(Bukkit.getInstance().getConfigFile().getSettings().getApiKey(),
                    Bukkit.getInstance().getModulesFile().getConnect().getServerToken()) {
                /**
                 * Executes console command
                 * @param command command to execute
                 */
                @Override
                public void executeCommands(String command) {
                    org.bukkit.Bukkit.getScheduler().runTask(Bukkit.getInstance(), () -> {
                        org.bukkit.Bukkit.dispatchCommand(org.bukkit.Bukkit.getConsoleSender(), command);
                        String msg = ChatUtil.replacePlaceholders(Bukkit.getInstance().getLangFile().getMessages().getConnect().getConnectExecutedCommand(),
                                new Placeholder("%command%", command));
                        ChatUtil.sendMessage(org.bukkit.Bukkit.getConsoleSender(), msg);
                    });
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
