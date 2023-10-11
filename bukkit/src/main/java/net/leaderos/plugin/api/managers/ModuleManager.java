package net.leaderos.plugin.api.managers;

import net.leaderos.shared.configuration.Language;
import net.leaderos.plugin.Main;
import net.leaderos.shared.helpers.ChatUtil;
import net.leaderos.shared.module.Modulable;
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
        Language lang = Main.getShared().getLangFile();
        modules.keySet().forEach(moduleName -> {
            Modulable module = getModule(moduleName);
            // Checks if module has dependency
            if (!module.getDependencies().isEmpty()) {
                // Dependency status of module
                boolean dependStatus = module.getDependencies().stream()
                        .allMatch(dependency -> modules.get(dependency).getStatus());
                // If requirements not met disables module
                if (!dependStatus) {
                    module.setEnabled(false);
                    String message = lang.getMessages().getInfo().getMissingDependency()
                            .replace("%module_name%", module.getName())
                            .replace("%dependencies%", module.getDependencyListAsString());
                    ChatUtil.sendMessage(Bukkit.getConsoleSender(), message);
                    return;
                }
            }
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
                String message = Main.getShared().getLangFile().getMessages().getInfo().getModuleDisabled()
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
