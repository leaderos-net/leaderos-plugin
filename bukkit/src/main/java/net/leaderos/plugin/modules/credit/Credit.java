package net.leaderos.plugin.modules.credit;

import net.leaderos.plugin.Main;
import net.leaderos.plugin.modules.credit.commands.Commands;
import net.leaderos.shared.model.Response;
import net.leaderos.shared.model.request.GetRequest;
import net.leaderos.shared.model.request.PostRequest;
import net.leaderos.shared.module.LeaderOSModule;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Credit module of leaderos-plugin
 *
 * @author poyrazinan
 * @since 1.0
 */
public class Credit extends LeaderOSModule {

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
     * Constructor of Credit
     */
    public Credit() {}
}