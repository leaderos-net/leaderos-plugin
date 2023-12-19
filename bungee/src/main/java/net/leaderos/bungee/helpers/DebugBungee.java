package net.leaderos.bungee.helpers;

import net.leaderos.bungee.Bungee;
import net.leaderos.shared.helpers.DebugAPI;

/**
 * Sends debug to console
 * @author poyrazinan
 * @since 1.0.2
 */
public class DebugBungee implements DebugAPI {

    /**
     * Sends debug to console
     * @param message to debug
     * @param strict if true, it will send debug even if debug mode is disabled
     */
    @Override
    public void send(String message, boolean strict) {
        if (Bungee.getInstance().getConfigFile().getSettings().isDebug() || strict) {
            Bungee.getInstance().getLogger().warning(ChatUtil.color(
                    "&e[DEBUG] &f" + message
            ));
        }
    }

    /**
     * Constructor of debug
     */
    public DebugBungee() {}
}
