package net.leaderos.bungee.modules.verify;

import net.leaderos.bungee.Bungee;
import net.leaderos.bungee.modules.verify.commands.VerifyCommand;
import net.leaderos.shared.modules.LeaderOSModule;

/**
 * Verify module of leaderos-plugin
 *
 * @author leaderos
 * @since 1.0
 */
public class VerifyModule extends LeaderOSModule {

    /**
     * Commands of module
     */
    private VerifyCommand command;

    /**
     * onEnable method of module
     */
    public void onEnable() {
        this.command = new VerifyCommand("verify");
        Bungee.getInstance().getProxy().getPluginManager().registerCommand(Bungee.getInstance(), command);
    }

    /**
     * onDisable method of module
     */
    public void onDisable() {
        Bungee.getInstance().getProxy().getPluginManager().unregisterCommand(command);
    }

    /**
     * Constructor of Auth
     */
    public VerifyModule() {
    }
}
