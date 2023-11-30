package net.leaderos.plugin.helpers;

import net.leaderos.plugin.Bukkit;
import net.leaderos.shared.helpers.DebugAPI;

/**
 * Sends debug to console
 * @author poyrazinan
 * @since 1.0.2
 */
public class DebugBukkit implements DebugAPI {

    /**
     * Sends debug to console
     * @param msg to debug
     */
    @Override
    public void sendDebug(String msg) {
        if (Bukkit.getInstance().getConfigFile().getSettings().isDebug())
            Bukkit.getInstance().getLogger().warning(ChatUtil.color(msg));
    }

    /**
     * Constructor of debug
     */
    public DebugBukkit() {}
}
