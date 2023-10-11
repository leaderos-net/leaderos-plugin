package net.leaderos.plugin.bukkit.modules.webstore;

import net.leaderos.plugin.Main;
import net.leaderos.plugin.shared.exceptions.RequestException;
import net.leaderos.plugin.bukkit.modules.webstore.command.Commands;
import net.leaderos.plugin.bukkit.modules.webstore.model.Category;
import net.leaderos.plugin.shared.module.LeaderOSModule;

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
        Main.commandManager.registerCommand(new Commands());
        try {
            Category.loadAllCategories();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (RequestException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * onDisable method of module
     */
    public void onDisable() {
        Main.commandManager.unregisterCommand(new Commands());
    }

    /**
     * Constructor of WebStore
     */
    public WebStore() {}
}
