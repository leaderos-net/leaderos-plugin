package net.leaderos.plugin.modules.webstore;

import net.leaderos.plugin.Main;
import net.leaderos.plugin.modules.webstore.command.WebStoreCommand;
import net.leaderos.plugin.modules.webstore.model.Category;
import net.leaderos.shared.exceptions.RequestException;
import net.leaderos.shared.module.LeaderOSModule;

import java.io.IOException;

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
        Main.getCommandManager().registerCommand(new WebStoreCommand());
        try {
            Category.loadAllCategories();
        } catch (IOException | RequestException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * onDisable method of module
     */
    public void onDisable() {
        Main.getCommandManager().unregisterCommand(new WebStoreCommand());
    }

    /**
     * Constructor of WebStore
     */
    public WebStore() {
        addDependency("Cache");
    }
}
