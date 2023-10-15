package net.leaderos.bungee.modules.connect;


import lombok.Getter;
import net.leaderos.bungee.Bungee;
import net.leaderos.shared.module.LeaderOSModule;
import net.leaderos.shared.socket.Client;

import java.net.URISyntaxException;

/**
 * Connect module main class
 *
 * @author poyrazinan
 * @since 1.0
 */
@Getter
public class Connect extends LeaderOSModule {

    private Client client;

    /**
     * onEnable method of module
     */
    public void onEnable() {
        try {
            client = new Client(Bungee.getInstance().getConfigFile().getSettings().getApiKey(),
                    Bungee.getInstance().getModulesFile().getConnect().getServerToken()) {
                /**
                 * Executes console command
                 * @param command command to execute
                 */
                @Override
                public void executeCommands(String command) {
                    Bungee.getInstance().getProxy().getPluginManager().dispatchCommand(
                            Bungee.getInstance().getProxy().getConsole(), command);
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
        client.getSocket().close();
    }

    /**
     * Constructor of connect
     */
    public Connect() {
    }
}