package net.leaderos.plugin;

import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import lombok.Getter;
import net.leaderos.plugin.bukkit.configuration.Config;
import net.leaderos.plugin.bukkit.configuration.Modules;
import net.leaderos.plugin.bukkit.configuration.lang.Language;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Main class of project
 *
 * @since 1.0
 * @author poyrazinan
 */
public class Main extends JavaPlugin {

    /**
     * Instance of plugin
     */
    @Getter
    private static Main instance;

    /**
     * Config file of plugin
     */
    @Getter
    private Config configFile;

    /**
     * Lang file of plugin
     */
    @Getter
    private Language langFile;

    /**
     * Module file of plugin
     */
    @Getter
    private Modules modulesFile;

    /**
     * onLoad override method of spigot library
     */
    public void onLoad() {
        instance = this;
        setupFiles();
    }

    /**
     * onEnable override method of Spigot library
     */
    public void onEnable() {
    }

    /**
     * Setups config, lang and modules file file
     */
    public void setupFiles() {
        try {
            this.configFile = ConfigManager.create(Config.class, (it) -> {
                it.withConfigurer(new YamlBukkitConfigurer());
                it.withBindFile(new File(this.getDataFolder(), "config.yml"));
                it.saveDefaults();
                it.load(true);
            });
            this.modulesFile = ConfigManager.create(Modules.class, (it) -> {
                it.withConfigurer(new YamlBukkitConfigurer());
                it.withBindFile(new File(this.getDataFolder(), "modules.yml"));
                it.saveDefaults();
                it.load(true);
            });
            String langName = configFile.getSettings().getLang();
            Class langClass = Class.forName("net.leaderos.plugin.bukkit.configuration.lang." + langName);
            Class<Language> languageClass = langClass;
            this.langFile = ConfigManager.create(languageClass, (it) -> {
                it.withConfigurer(new YamlBukkitConfigurer());
                it.withBindFile(new File(this.getDataFolder() + "/lang", langName));
                it.saveDefaults();
                it.load(true);
            });
        } catch (Exception exception) {
            this.getPluginLoader().disablePlugin(this);
            throw new RuntimeException("Error loading config.yml");
        }
    }

    /**
     * Constructor of Main class
     */
    public Main() {}

}
