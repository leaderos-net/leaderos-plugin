package net.leaderos.plugin.api;

import net.leaderos.plugin.api.managers.StorageManager;

/**
 * LeaderOS plugins api class
 *
 * @author poyrazinan
 * @since 1.0
 */
public class LeaderOSAPI {

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
