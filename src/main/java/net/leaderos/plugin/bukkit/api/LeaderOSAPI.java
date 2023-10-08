package net.leaderos.plugin.bukkit.api;

import net.leaderos.plugin.bukkit.api.managers.ModuleManager;

/**
 * LeaderOS plugins api class
 *
 * @author poyrazinan
 * @since 1.0
 */
public class LeaderOSAPI {

    /**
     * ModuleManager instance
     */
    private static ModuleManager moduleManager;

    /**
     * Gets Module Manager
     * @return ModuleManager
     * @see ModuleManager
     */
    public static ModuleManager getModuleManager() {
        if (moduleManager == null)
            moduleManager = new ModuleManager();
        return moduleManager;
    }

    /**
     * LeaderOSAPI Constructor
     */
    public LeaderOSAPI() {}
}
