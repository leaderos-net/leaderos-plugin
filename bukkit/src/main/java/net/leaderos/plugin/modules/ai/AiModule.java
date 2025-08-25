package net.leaderos.plugin.modules.ai;

import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.modules.ai.commands.AiCommand;
import net.leaderos.shared.modules.LeaderOSModule;

/**
 * AI module of leaderos-plugin
 *
 * @author leaderos
 * @since 1.0
 */
public class AiModule extends LeaderOSModule {

    /**
     * onEnable method of module
     */
    public void onEnable() {
        Bukkit.getCommandManager().registerCommand(new AiCommand());
    }

    /**
     * onDisable method of module
     */
    public void onDisable() {
        Bukkit.getCommandManager().unregisterCommand(new AiCommand());
    }

    /**
     * Constructor of VerifyModule
     */
    public AiModule() {}
}
