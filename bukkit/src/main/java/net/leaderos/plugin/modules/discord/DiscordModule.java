package net.leaderos.plugin.modules.discord;

import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.modules.discord.commands.SyncCommand;
import net.leaderos.shared.modules.LeaderOSModule;

/**
 * Discord module of leaderos-plugin
 *
 * @author rafaelflromao
 * @since 1.0
 */
public class DiscordModule extends LeaderOSModule {

    /**
     * onEnable method of module
     */
    public void onEnable() {
        Bukkit.getCommandManager().registerCommand(new SyncCommand());
    }

    /**
     * onDisable method of module
     */
    public void onDisable() {
        Bukkit.getCommandManager().unregisterCommand(new SyncCommand());
    }

    /**
     * Constructor of Discord
     */
    public DiscordModule() {}
}
