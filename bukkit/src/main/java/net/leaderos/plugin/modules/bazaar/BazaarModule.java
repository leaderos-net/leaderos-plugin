package net.leaderos.plugin.modules.bazaar;

import lombok.Getter;
import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.helpers.CommandHelper;
import net.leaderos.plugin.modules.bazaar.commands.BazaarCommand;
import net.leaderos.shared.modules.LeaderOSModule;

import java.util.Arrays;

/**
 * Bazaar module of leaderos-plugin
 *
 * @author poyrazinan
 * @since 1.0
 */
public class BazaarModule extends LeaderOSModule {

    /**
     * Server id
     */
    @Getter
    private static int serverId;

    /**
     * onEnable method of module
     */
    public void onEnable() {
        serverId = Bukkit.getInstance().getModulesFile().getBazaar().getServerId();
        Bukkit.getCommandManager().registerCommand(new BazaarCommand());
    }

    /**
     * onDisable method of module
     */
    public void onDisable() {
        CommandHelper.unregisterCommands(Arrays.asList("bazaar", "webbazaar", "pazar"));
    }

    /**
     * Constructor of WebStore
     */
    public BazaarModule() {
        addDependency("Cache");
    }
}
