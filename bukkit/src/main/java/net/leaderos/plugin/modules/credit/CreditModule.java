package net.leaderos.plugin.modules.credit;

import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.helpers.CommandHelper;
import net.leaderos.plugin.modules.credit.commands.CreditCommand;
import net.leaderos.shared.modules.LeaderOSModule;

import java.util.Arrays;

/**
 * Credit module of leaderos-plugin
 *
 * @author poyrazinan
 * @since 1.0
 */
public class CreditModule extends LeaderOSModule {

    /**
     * onEnable method of module
     */
    public void onEnable() {
        Bukkit.getCommandManager().registerCommand(new CreditCommand());
    }

    /**
     * onDisable method of module
     */
    public void onDisable() {
        CommandHelper.unregisterCommands(Arrays.asList("credit", "credits", "kredi"));
    }

    /**
     * Constructor of Credit
     */
    public CreditModule() {}
}