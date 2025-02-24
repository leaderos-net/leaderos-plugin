package net.leaderos.plugin.modules.verify;

import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.modules.verify.commands.VerifyCommand;
import net.leaderos.shared.modules.LeaderOSModule;

/**
 * Verify module of leaderos-plugin
 *
 * @author leaderos
 * @since 1.0
 */
public class VerifyModule extends LeaderOSModule {

    /**
     * onEnable method of module
     */
    public void onEnable() {
        Bukkit.getCommandManager().registerCommand(new VerifyCommand());
    }

    /**
     * onDisable method of module
     */
    public void onDisable() {
        Bukkit.getCommandManager().unregisterCommand(new VerifyCommand());
    }

    /**
     * Constructor of VerifyModule
     */
    public VerifyModule() {}
}
