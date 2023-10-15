package net.leaderos.plugin.modules.credit;

import net.leaderos.plugin.Main;
import net.leaderos.plugin.modules.credit.commands.CreditCommand;
import net.leaderos.shared.module.LeaderOSModule;

/**
 * Credit module of leaderos-plugin
 *
 * @author poyrazinan
 * @since 1.0
 */
public class Credit extends LeaderOSModule {

    /**
     * onEnable method of module
     */
    public void onEnable() {
        Main.getCommandManager().registerCommand(new CreditCommand());
    }

    /**
     * onDisable method of module
     */
    public void onDisable() {
        Main.getCommandManager().unregisterCommand(new CreditCommand());
    }

    /**
     * Constructor of Credit
     */
    public Credit() {}
}