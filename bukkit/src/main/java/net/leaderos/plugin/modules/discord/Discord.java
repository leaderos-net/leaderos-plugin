package net.leaderos.plugin.modules.discord;

import net.leaderos.plugin.Main;
import net.leaderos.plugin.modules.discord.commands.Commands;
import net.leaderos.shared.module.LeaderOSModule;

/**
 * Discord module of leaderos-plugin
 *
 * @author rafaelflromao
 * @since 1.0
 */
public class Discord extends LeaderOSModule {

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
     * Constructor of Discord
     */
    public Discord() {}
}
