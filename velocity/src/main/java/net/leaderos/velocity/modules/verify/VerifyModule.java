package net.leaderos.velocity.modules.verify;

import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.SimpleCommand;
import net.leaderos.shared.modules.LeaderOSModule;
import net.leaderos.velocity.Velocity;
import net.leaderos.velocity.modules.verify.commands.VerifyCommand;

/**
 * Verify module of leaderos-plugin
 *
 * @author leaderos
 * @since 1.0
 */
public class VerifyModule extends LeaderOSModule {

    /**
     * Command meta of module
     */
    private CommandMeta commandMeta;

    /**
     * Command object of module
     */
    private SimpleCommand verifyCommand;

    /**
     * onEnable method of module
     */
    public void onEnable() {
        commandMeta = Velocity.getInstance().getCommandManager().metaBuilder("verify")
                .aliases("dogrula")
                .plugin(Velocity.getInstance())
                .build();
        verifyCommand = new VerifyCommand();
        Velocity.getInstance().getCommandManager().register(commandMeta, verifyCommand);
    }

    /**
     * onDisable method of module
     */
    public void onDisable() {
        Velocity.getInstance().getCommandManager().unregister(commandMeta);
    }

    /**
     * Constructor of VerifyModule
     */
    public VerifyModule() {
    }
}
