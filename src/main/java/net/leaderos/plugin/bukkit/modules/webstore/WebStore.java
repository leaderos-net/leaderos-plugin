package net.leaderos.plugin.bukkit.modules.webstore;

import net.leaderos.plugin.Main;
import net.leaderos.plugin.shared.module.LeaderOSModule;

/**
 * Web-store module of leaderos-plugin
 *
 * @author poyrazinan
 * @since 1.0
 */
public class WebStore extends LeaderOSModule {

    /**
     * onEnable method of module
     */
    public void onEnable() {
        Main.commandManager.registerCommand(new Commands());
    }

    /**
     * onDisable method of module
     */
    public void onDisable() {

    }

    /**
     * Constructor of WebStore
     */
    public WebStore() {}
}
