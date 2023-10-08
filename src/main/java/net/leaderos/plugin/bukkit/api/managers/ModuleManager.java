package net.leaderos.plugin.bukkit.api.managers;

import net.leaderos.plugin.Main;
import net.leaderos.plugin.bukkit.configuration.lang.Language;
import net.leaderos.plugin.bukkit.helpers.ChatUtil;
import net.leaderos.plugin.shared.module.Modulable;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

/**
 * ModuleManager of leaderos-plugin
 *
 * @author poyrazinan
 * @since 1.0
 */
public class ModuleManager {

    /**
     * List of modules
     */
    private List<Modulable> modules = new ArrayList<>();

    /**
     * Registers module to module list
     * @param module of leaderos-plugin
     */
    public void registerModule(Modulable module) {
        modules.add(module);
    }

    /**
     * Enables all modules
     */
    public void enableModules() {
        Language lang = Main.getInstance().getLangFile();
        modules.forEach(module -> {
            if (module.getStatus()) {
                module.setEnabled(true);
                module.onEnable();
                String message = lang.getMessages().getInfo().getModuleEnabled()
                        .replace("%module_name%", module.getName());
                ChatUtil.sendMessage(Bukkit.getConsoleSender(), message);
            }
            else {
                module.setEnabled(false);
                String message = lang.getMessages().getInfo().getModuleClosed()
                        .replace("%module_name%", module.getName());
                ChatUtil.sendMessage(Bukkit.getConsoleSender(), message);
            }
        });
    }

    /**
     * Disables all modules
     */
    public void disableModules() {
        modules.forEach(module -> {
            if (module.isEnabled()) {
                module.setEnabled(false);
                module.onDisable();
                String message = Main.getInstance().getLangFile().getMessages().getInfo().getModuleDisabled()
                        .replace("%module_name%", module.getName());
                ChatUtil.sendMessage(Bukkit.getConsoleSender(), message);
            }
        });
    }

    /**
     * Constructor of ModuleManager
     */
    public ModuleManager() {}
}
