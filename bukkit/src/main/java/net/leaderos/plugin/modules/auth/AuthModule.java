package net.leaderos.plugin.modules.auth;

import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.modules.auth.commands.AuthCommand;
import net.leaderos.shared.module.LeaderOSModule;

/**
 * Auth module of leaderos-plugin
 *
 * @author poyrazinan
 * @since 1.0
 */
public class AuthModule extends LeaderOSModule {

    /**
     * onEnable method of module
     */
    public void onEnable() {
        Bukkit.getCommandManager().registerCommand(new AuthCommand());
    }

    /**
     * onDisable method of module
     */
    public void onDisable() {
        Bukkit.getCommandManager().unregisterCommand(new AuthCommand());
    }

    /**
     * Constructor of Auth
     */
    public AuthModule() {}
}
