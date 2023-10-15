package net.leaderos.plugin.modules.bazaar;

import lombok.Getter;
import net.leaderos.plugin.Main;
import net.leaderos.plugin.modules.bazaar.command.BazaarCommand;
import net.leaderos.shared.module.LeaderOSModule;

/**
 * Bazaar module of leaderos-plugin
 *
 * @author poyrazinan
 * @since 1.0
 */
public class Bazaar extends LeaderOSModule {

    /**
     * Server id
     */
    @Getter
    private static int serverId;

    /**
     * onEnable method of module
     */
    public void onEnable() {
        // TODO check dependency
        serverId = Main.getInstance().getModulesFile().getBazaar().getServerId();
        Main.getCommandManager().registerCommand(new BazaarCommand());
    }

    /**
     * onDisable method of module
     */
    public void onDisable() {
        Main.getCommandManager().unregisterCommand(new BazaarCommand());
    }

    /**
     * Constructor of WebStore
     */
    public Bazaar() {
        addDependency("Cache");
    }
}
