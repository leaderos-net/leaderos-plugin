package net.leaderos.plugin.api.managers;

import de.leonhard.storage.Config;
import net.leaderos.plugin.Main;
import net.leaderos.plugin.modules.Modulable;
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
        Config lang = Main.getInstance().getLangFile();
        modules.forEach(module -> {
            if (module.getStatus()) {
                module.setEnabled(true);
                module.onEnable();
                Bukkit.getConsoleSender().sendMessage(lang.getText("info.module-enabled")
                        .replace("%module_name%", module.getName()));
            }
            else {
                module.setEnabled(false);
                Bukkit.getConsoleSender().sendMessage(lang.getText("info.module-enabled")
                        .replace("%module_name%", module.getName()));
            }
        });
    }

    /**
     * Disables all modules
     */
    public void disableModules() {
        Config lang = Main.getInstance().getLangFile();
        modules.forEach(module -> {
            if (module.isEnabled()) {
                module.setEnabled(false);
                module.onDisable();
                Bukkit.getConsoleSender().sendMessage(lang.getText("info.module-disabled")
                        .replace("%module_name%", module.getName()));
            }
        });
    }

    /**
     * Constructor of ModuleManager
     */
    public ModuleManager() {}
}
