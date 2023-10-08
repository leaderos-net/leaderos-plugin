package net.leaderos.plugin.api;

import net.leaderos.plugin.api.managers.ModuleManager;
import net.leaderos.plugin.api.managers.RequestManager;
import net.leaderos.plugin.api.managers.StorageManager;

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
     * RequestManager instance
     */
    private static RequestManager requestManager;

    /**
     * Gets RequestManager
     * @return RequestManager
     * @see RequestManager
     */
    public static RequestManager getRequestManager() {
        if (requestManager == null)
            requestManager = new RequestManager();
        return requestManager;
    }

    /**
     * LeaderOSAPI Constructor
     */
    public LeaderOSAPI() {}
}
