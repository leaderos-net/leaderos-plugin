package net.leaderos.velocity.modules.credit;

import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.SimpleCommand;
import net.leaderos.shared.modules.LeaderOSModule;
import net.leaderos.velocity.Velocity;
import net.leaderos.velocity.modules.credit.commands.CreditCommand;


/**
 * Credit module of leaderos-plugin
 *
 * @author poyrazinan
 * @since 1.0
 */
public class CreditModule extends LeaderOSModule {

    /**
     * Command meta of module
     */
    private CommandMeta commandMeta;

    /**
     * Command object of module
     */
    private SimpleCommand creditCommand;

    /**
     * onEnable method of module
     */
    public void onEnable() {
        commandMeta = Velocity.getInstance().getCommandManager().metaBuilder("credits")
                .aliases("kredi")
                .plugin(Velocity.getInstance())
                .build();
        creditCommand = new CreditCommand();
        Velocity.getInstance().getCommandManager().register(commandMeta, creditCommand);
    }

    /**
     * onDisable method of module
     */
    public void onDisable() {
        Velocity.getInstance().getCommandManager().unregister(commandMeta);
    }


    /**
     * Constructor of Credit
     */
    public CreditModule() {
    }
}