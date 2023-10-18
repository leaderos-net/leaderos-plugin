package net.leaderos.bungee.modules.credit;

import net.leaderos.bungee.Bungee;
import net.leaderos.bungee.modules.credit.commands.CreditCommand;
import net.leaderos.shared.modules.LeaderOSModule;

/**
 * Credit module of leaderos-plugin
 *
 * @author poyrazinan
 * @since 1.0
 */
public class CreditModule extends LeaderOSModule {

    /**
     * Commands of module
     */
    private CreditCommand command;

    /**
     * onEnable method of module
     */
    public void onEnable() {
        this.command = new CreditCommand("credits");
        Bungee.getInstance().getProxy().getPluginManager().registerCommand(Bungee.getInstance(), command);
    }

    /**
     * onDisable method of module
     */
    public void onDisable() {
        Bungee.getInstance().getProxy().getPluginManager().unregisterCommand(command);
    }


    /**
     * Constructor of Credit
     */
    public CreditModule() {
    }
}
