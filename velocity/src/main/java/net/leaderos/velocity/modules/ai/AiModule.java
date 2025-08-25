package net.leaderos.velocity.modules.ai;

import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.SimpleCommand;
import net.leaderos.shared.modules.LeaderOSModule;
import net.leaderos.velocity.Velocity;
import net.leaderos.velocity.modules.ai.commands.AiCommand;

/**
 * AI module of leaderos-plugin for Velocity
 * @author leaderos
 * @since 1.0
 */
public class AiModule extends LeaderOSModule {

    /**
     * Command meta of module
     */
    private CommandMeta commandMeta;

    /**
     * Command object of module
     */
    private SimpleCommand aiCommand;

    /**
     * onEnable method of module
     */
    @Override
    public void onEnable() {
        commandMeta = Velocity.getInstance().getCommandManager().metaBuilder("ai")
                .aliases("yapayzeka")
                .plugin(Velocity.getInstance())
                .build();
        aiCommand = new AiCommand();
        Velocity.getInstance().getCommandManager().register(commandMeta, aiCommand);
    }

    /**
     * onDisable method of module
     */
    @Override
    public void onDisable() {
        Velocity.getInstance().getCommandManager().unregister(commandMeta);
    }

    /**
     * Constructor of AiModule
     */
    public AiModule() {}
} 