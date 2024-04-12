package net.leaderos.plugin.helpers;

import net.leaderos.plugin.Bukkit;
import net.leaderos.shared.helpers.DebugAPI;
import net.leaderos.shared.model.DebugMode;

/**
 * Sends debug to console
 * @author poyrazinan
 * @since 1.0.2
 */
public class DebugBukkit implements DebugAPI {

    /**
     * Sends debug to console
     * @param message to debug
     * @param strict if true, it will send debug even if debug mode is disabled
     */
    @Override
    public void send(String message, boolean strict) {
        if (
                Bukkit.getInstance().getConfigFile().getSettings().getDebugMode() == DebugMode.ENABLED ||
                        (Bukkit.getInstance().getConfigFile().getSettings().getDebugMode() == DebugMode.ONLY_ERRORS && strict)
        ) {
            Bukkit.getInstance().getLogger().warning(ChatUtil.color(
                    "&e[DEBUG] &f" + message
            ));
        }
    }

    /**
     * Constructor of debug
     */
    public DebugBukkit() {}
}
