package net.leaderos.plugin.bukkit.api.managers;

import net.leaderos.plugin.Main;
import net.leaderos.plugin.bukkit.configuration.Language;
import net.leaderos.plugin.bukkit.helpers.ChatUtil;
import net.leaderos.plugin.shared.module.Modulable;
import org.bukkit.Bukkit;

import java.util.HashMap;

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
    private static HashMap<String, Modulable> modules = new HashMap<>();

    /**
     * Module getter
     */
    public static Modulable getModule(String name) {
        return modules.get(name);
    }

    /**
     * Registers module to module list
     * @param module of leaderos-plugin
     */
    public void registerModule(Modulable module) {
        modules.put(module.getName(), module);
    }

    /**
     * Enables all modules
     */
    public void enableModules() {
        Language lang = Main.getInstance().getLangFile();
        modules.keySet().forEach(moduleName -> {
            Modulable module = getModule(moduleName);
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
        modules.keySet().forEach(moduleName -> {
            Modulable module = modules.get(moduleName);
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
