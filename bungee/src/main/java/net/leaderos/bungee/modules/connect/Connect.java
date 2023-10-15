package net.leaderos.bungee.modules.connect;


import lombok.Getter;
import net.leaderos.bungee.Bungee;
import net.leaderos.bungee.helper.ChatUtil;
import net.leaderos.shared.helpers.Placeholder;
import net.leaderos.shared.module.LeaderOSModule;
import net.leaderos.shared.socket.SocketClient;

import java.net.URISyntaxException;

/**
 * Connect module main class
 *
 * @author poyrazinan
 * @since 1.0
 */
@Getter
public class Connect extends LeaderOSModule {

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
                    String msg = ChatUtil.replacePlaceholders(Bungee.getInstance().getLangFile().getMessages().getConnectExecutedCommand(),
                            new Placeholder("%command%", command));
                    ChatUtil.sendMessage(Bungee.getInstance().getProxy().getConsole(), msg);
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
    public Connect() {
    }
}