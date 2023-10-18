package net.leaderos.plugin.modules.credit;

import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.modules.credit.commands.CreditCommand;
import net.leaderos.shared.modules.LeaderOSModule;

/**
 * Credit module of leaderos-plugin
 *
 * @author poyrazinan
 * @since 1.0
 */
public class CreditModule extends LeaderOSModule {

    /**
     * onEnable method of module
     */
    public void onEnable() {
        Bukkit.getCommandManager().registerCommand(new CreditCommand());
    }

    /**
     * onDisable method of module
     */
    public void onDisable() {
        Bukkit.getCommandManager().unregisterCommand(new CreditCommand());
    }

    /**
     * Constructor of Credit
     */
    public CreditModule() {}
}