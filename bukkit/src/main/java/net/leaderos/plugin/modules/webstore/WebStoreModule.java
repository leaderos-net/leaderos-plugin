package net.leaderos.plugin.modules.webstore;

import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.helpers.CommandHelper;
import net.leaderos.plugin.modules.webstore.commands.WebStoreCommand;
import net.leaderos.plugin.modules.webstore.model.Category;
import net.leaderos.shared.exceptions.RequestException;
import net.leaderos.shared.modules.LeaderOSModule;

import java.io.IOException;
import java.util.Arrays;

/**
 * Web-store module of leaderos-plugin
 *
 * @author poyrazinan
 * @since 1.0
 */
public class WebStoreModule extends LeaderOSModule {

    /**
     * onEnable method of module
     */
    public void onEnable() {
        Bukkit.getCommandManager().registerCommand(new WebStoreCommand());
    }

    /**
     * onDisable method of module
     */
    public void onDisable() {
        CommandHelper.unregisterCommands(Arrays.asList("webstore", "webshop", "sitemarket", "webmarket"));
    }

    /**
     * Constructor of WebStore
     */
    public WebStoreModule() {
        addDependency("Cache");
    }
}
