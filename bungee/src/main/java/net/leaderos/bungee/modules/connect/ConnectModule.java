package net.leaderos.bungee.modules.connect;


import lombok.Getter;
import net.leaderos.bungee.Bungee;
import net.leaderos.bungee.helpers.ChatUtil;
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
            socket = new SocketClient(Bungee.getInstance().getConfigFile().getSettings().getApiKey(),
                    Bungee.getInstance().getModulesFile().getConnect().getServerToken()) {
                /**
                 * Executes console command
                 * @param command command to execute
                 */
                @Override
                public void executeCommands(String command) {
                    Bungee.getInstance().getProxy().getPluginManager().dispatchCommand(
                            Bungee.getInstance().getProxy().getConsole(), command);
                    String msg = ChatUtil.replacePlaceholders(Bungee.getInstance().getLangFile().getMessages().getConnect().getConnectExecutedCommand(),
                            new Placeholder("%command%", command));
                    ChatUtil.sendMessage(Bungee.getInstance().getProxy().getConsole(), msg);
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
        socket.getSocket().close();
    }

    /**
     * Constructor of connect
     */
    public ConnectModule() {
    }
}