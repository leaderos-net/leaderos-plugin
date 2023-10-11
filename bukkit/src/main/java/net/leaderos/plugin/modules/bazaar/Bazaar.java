package net.leaderos.plugin.modules.bazaar;

import lombok.Getter;
import net.leaderos.plugin.Main;
import net.leaderos.plugin.modules.bazaar.command.Commands;
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
        this.serverId = Main.getShared().getModulesFile().getBazaar().getServerId();
        Main.getCommandManager().registerCommand(new Commands());
    }

    /**
     * onDisable method of module
     */
    public void onDisable() {
        Main.getCommandManager().unregisterCommand(new Commands());
    }

    /**
     * Constructor of WebStore
     */
    public Bazaar() {
        addDependency("Cache");
    }
}
