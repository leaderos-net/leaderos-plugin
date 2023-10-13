package net.leaderos.modules.credits;

import net.leaderos.Bungee;
import net.leaderos.modules.credits.commands.Commands;
import net.leaderos.shared.module.LeaderOSModule;

/**
 * Credit module of leaderos-plugin
 *
 * @author poyrazinan
 * @since 1.0
 */
public class Credit extends LeaderOSModule {

    /**
     * Commands of module
     */
    private Commands commands;

    /**
     * onEnable method of module
     */
    public void onEnable() {
        this.commands = new Commands("credits");
        Bungee.getInstance().getProxy().getPluginManager().registerCommand(Bungee.getInstance(), commands);
    }

    /**
     * onDisable method of module
     */
    public void onDisable() {
        Bungee.getInstance().getProxy().getPluginManager().unregisterCommand(commands);
    }


    /**
     * Constructor of Credit
     */
    public Credit() {
    }
}
