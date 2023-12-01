package net.leaderos.plugin.api;

import net.leaderos.plugin.api.managers.ModuleManager;
import net.leaderos.plugin.api.modules.credit.CreditManager;

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
    private static ModuleManager moduleManager = new ModuleManager();

    /**
     * ModuleManager instance
     */
    private static CreditManager creditManager = new CreditManager();

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
     * Gets Credit Manager
     * @return CreditManager
     * @see CreditManager
     */
    public static CreditManager getCreditManager() {
        if (creditManager == null)
            creditManager = new CreditManager();
        return creditManager;
    }

    /**
     * LeaderOSAPI Constructor
     */
    public LeaderOSAPI() {}
}
