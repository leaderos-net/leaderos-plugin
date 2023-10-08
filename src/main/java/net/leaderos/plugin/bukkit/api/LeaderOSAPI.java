package net.leaderos.plugin.bukkit.api;

import net.leaderos.plugin.bukkit.api.managers.ModuleManager;
import net.leaderos.plugin.bukkit.api.managers.StorageManager;

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
     * StorageManager instance
     */
    private static StorageManager storageManager;

    /**
     * Gets Storage manager
     * @see StorageManager
     * @return StorageManager
     */
    public static StorageManager getStorageManager() {
        if (storageManager == null)
            storageManager = new StorageManager();
        return storageManager;
    }

    /**
     * LeaderOSAPI Constructor
     */
    public LeaderOSAPI() {}
}
