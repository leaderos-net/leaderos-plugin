package net.leaderos.plugin.api.managers;

import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.api.handlers.ModuleDisableEvent;
import net.leaderos.plugin.api.handlers.ModuleEnableEvent;
import net.leaderos.plugin.configuration.Language;
import net.leaderos.plugin.helpers.ChatUtil;
import net.leaderos.shared.modules.Modulable;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
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
     * @param name of module
     * @return module
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
     * Gets module status
     * @param moduleName name of module
     * @return status
     */
    public static boolean getModuleStatus(String moduleName) {
        File moduleFile = new File("plugins/" + Bukkit.getInstance().getDescription().getName() + "/modules.yml");
        FileConfiguration modules = YamlConfiguration.loadConfiguration(moduleFile);
        return modules.getBoolean(moduleName + ".status");
    }

    /**
     * Enables all modules
     */
    public void enableModules() {
        Language lang = Bukkit.getInstance().getLangFile();
        modules.keySet().forEach(moduleName -> {
            Modulable module = getModule(moduleName);
            // Checks if module has dependency
            if (!module.getDependencies().isEmpty()) {
                // Dependency status of module
                boolean dependStatus = module.getDependencies().stream()
                        .allMatch(ModuleManager::getModuleStatus);
                // If requirements not met disables module
                if (!dependStatus) {
                    module.setEnabled(false);
                    String message = lang.getMessages().getInfo().getMissingDependency()
                            .replace("%module_name%", module.getName())
                            .replace("%dependencies%", module.getDependencyListAsString());
                    ChatUtil.sendMessage(org.bukkit.Bukkit.getConsoleSender(), message);
                    return;
                }
            }
            if (getModuleStatus(module.getName())) {
                module.setEnabled(true);
                // Enable event
                org.bukkit.Bukkit.getPluginManager().callEvent(new ModuleEnableEvent(module));
                module.onEnable();
                String message = lang.getMessages().getInfo().getModuleEnabled()
                        .replace("%module_name%", module.getName());
                ChatUtil.sendMessage(org.bukkit.Bukkit.getConsoleSender(), message);
            }
            else {
                module.setEnabled(false);
                String message = lang.getMessages().getInfo().getModuleClosed()
                        .replace("%module_name%", module.getName());
                ChatUtil.sendMessage(org.bukkit.Bukkit.getConsoleSender(), message);
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
                // Disable event
                org.bukkit.Bukkit.getPluginManager().callEvent(new ModuleDisableEvent(module));
                module.onDisable();
                String message = Bukkit.getInstance().getLangFile().getMessages().getInfo().getModuleDisabled()
                        .replace("%module_name%", module.getName());
                ChatUtil.sendMessage(org.bukkit.Bukkit.getConsoleSender(), message);
            }
        });
    }

    /**
     * reload modules
     */
    private void reload() {
        modules.keySet().forEach(moduleName -> {
            Modulable module = modules.get(moduleName);
            if (module.isEnabled())
                module.onReload();
        });
    }

    /**
     * reload module
     */
    public static void reload(String moduleName) {
        Modulable module = modules.get(moduleName);
        if (module.isEnabled())
            module.onReload();
    }

    /**
     * Reload modules
     */
    public void reloadModules() {
        reload();
        disableModules();
        enableModules();
    }

    /**
     * Constructor of ModuleManager
     */
    public ModuleManager() {}
}
