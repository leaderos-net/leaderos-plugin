package net.leaderos.bungee.api;

import lombok.SneakyThrows;
import net.leaderos.bungee.Bungee;
import net.leaderos.bungee.configuration.Language;
import net.leaderos.bungee.helpers.ChatUtil;
import net.leaderos.shared.modules.Modulable;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

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
     *
     * @param name of module
     * @return module
     */
    public static Modulable getModule(String name) {
        return modules.get(name);
    }

    /**
     * Registers module to module list
     *
     * @param module of leaderos-plugin
     */
    public void registerModule(Modulable module) {
        modules.put(module.getName(), module);
    }

    /**
     * Gets module status
     *
     * @param moduleName name of module
     * @return status
     */
    @SneakyThrows
    public static boolean getModuleStatus(String moduleName) {
        Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File("plugins/" + Bungee.getInstance().getDescription().getName() + "/modules.yml"));
        return configuration.getBoolean(moduleName + ".status");
    }

    /**
     * Enables all modules
     */
    public void enableModules() {
        Language lang = Bungee.getInstance().getLangFile();
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
                    ChatUtil.sendMessage(Bungee.getInstance().getProxy().getConsole(), message);
                    return;
                }
            }
            if (getModuleStatus(module.getName())) {
                module.setEnabled(true);
                module.onEnable();
                String message = lang.getMessages().getInfo().getModuleEnabled()
                        .replace("%module_name%", module.getName());
                ChatUtil.sendMessage(Bungee.getInstance().getProxy().getConsole(), message);
            } else {
                module.setEnabled(false);
                String message = lang.getMessages().getInfo().getModuleClosed()
                        .replace("%module_name%", module.getName());
                ChatUtil.sendMessage(Bungee.getInstance().getProxy().getConsole(), message);
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
                String message = Bungee.getInstance().getLangFile().getMessages().getInfo().getModuleDisabled()
                        .replace("%module_name%", module.getName());
                ChatUtil.sendMessage(Bungee.getInstance().getProxy().getConsole(), message);
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
    public ModuleManager() {
    }
}
