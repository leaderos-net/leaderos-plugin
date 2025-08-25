package net.leaderos.bungee.modules.ai;

import net.leaderos.bungee.Bungee;
import net.leaderos.bungee.modules.ai.commands.AiCommand;
import net.leaderos.shared.modules.LeaderOSModule;

/**
 * AI module of leaderos-plugin for Bungee
 * @author leaderos
 * @since 1.0
 */
public class AiModule extends LeaderOSModule {

    /**
     * Command object of module
     */
    private AiCommand command;

    /**
     * onEnable method of module
     */
    @Override
    public void onEnable() {
        this.command = new AiCommand("ai");
        Bungee.getInstance().getProxy().getPluginManager().registerCommand(Bungee.getInstance(), command);
    }

    /**
     * onDisable method of module
     */
    @Override
    public void onDisable() {
        Bungee.getInstance().getProxy().getPluginManager().unregisterCommand(command);
    }

    /**
     * Constructor of AiModule
     */
    public AiModule() {}
} 