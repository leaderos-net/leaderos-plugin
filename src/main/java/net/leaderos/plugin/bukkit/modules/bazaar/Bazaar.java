package net.leaderos.plugin.bukkit.modules.bazaar;

import lombok.Getter;
import net.leaderos.plugin.Main;
import net.leaderos.plugin.bukkit.modules.bazaar.command.Commands;
import net.leaderos.plugin.shared.module.LeaderOSModule;

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
        this.serverId = Main.getInstance().getModulesFile().getBazaar().getServerId();
        Main.commandManager.registerCommand(new Commands());
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
    public Bazaar() {}
}
