package net.leaderos.plugin.modules.auth;

import net.leaderos.plugin.Main;
import net.leaderos.plugin.modules.auth.commands.Commands;
import net.leaderos.shared.module.LeaderOSModule;

/**
 * Auth module of leaderos-plugin
 *
 * @author poyrazinan
 * @since 1.0
 */
public class AuthLogin extends LeaderOSModule {

    /**
     * onEnable method of module
     */
    public void onEnable() {
        Main.getCommandManager().registerCommand(new Commands());
    }

    /**
     * onDisable method of module
     */
    public void onDisable() {
        Main.getCommandManager().unregisterCommand(new Commands());
    }

    /**
     * Constructor of Auth
     */
    public AuthLogin() {}
}
