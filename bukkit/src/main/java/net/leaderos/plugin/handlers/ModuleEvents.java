package net.leaderos.plugin.handlers;

import net.leaderos.plugin.Main;
import net.leaderos.plugin.api.handlers.ModuleDisableEvent;
import net.leaderos.plugin.api.handlers.ModuleEnableEvent;
import net.leaderos.shared.module.auth.commands.AuthCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * @author poyrazinan
 * @since 1.0
 */
public class ModuleEvents implements Listener {

    /**
     * Constructor of class
     */
    public ModuleEvents() {}

    /**
     * Registers authlogin command
     * @param event ModuleEnableEvent
     */
    @EventHandler
    public void registerCommandEvent(ModuleEnableEvent event) {
        if (event.getModule().getName().equals("AuthLogin"))
            Main.getCommandManager().registerCommand(new AuthCommand());
    }

    /**
     * Unregisters authlogin command
     * @param event ModuleDisableEvent
     */
    @EventHandler
    public void unRegisterCommandEvent(ModuleDisableEvent event) {
        if (event.getModule().getName().equals("AuthLogin"))
            Main.getCommandManager().unregisterCommand(new AuthCommand());
    }
}
