package net.leaderos.plugin.modules.auth;

import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.helpers.CommandHelper;
import net.leaderos.plugin.modules.auth.commands.AuthCommand;
import net.leaderos.shared.modules.LeaderOSModule;

import java.util.Arrays;
import java.util.Collections;

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
        CommandHelper.unregisterCommands(Collections.singletonList("auth"));
    }

    /**
     * Constructor of Auth
     */
    public AuthModule() {}
}
