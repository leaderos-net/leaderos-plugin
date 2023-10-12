package net.leaderos.shared;

import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import lombok.Getter;
import net.leaderos.shared.configuration.Config;
import net.leaderos.shared.configuration.Language;
import net.leaderos.shared.configuration.Modules;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * @author poyrazinan
 * @since 1.0
 */
public class Shared {

    /**
     * Instance of plugin
     */
    @Getter
    private JavaPlugin plugin;

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
     * Shared instance
     */
    @Getter
    private static Shared instance;

    /**
     * Constructor of shared
     *
     * @param plugin Main instance
     */
    public Shared(JavaPlugin plugin) {
        this.plugin = plugin;
        instance = this;
        setupFiles();
    }


    /**
     * Setups config, lang and modules file file
     */
    public void setupFiles() {
        try {
            this.configFile = ConfigManager.create(Config.class, (it) -> {
                it.withConfigurer(new YamlBukkitConfigurer());
                it.withBindFile(new File(getPlugin().getDataFolder(), "config.yml"));
                it.saveDefaults();
                it.load(true);
            });
            this.modulesFile = ConfigManager.create(Modules.class, (it) -> {
                it.withConfigurer(new YamlBukkitConfigurer());
                it.withBindFile(new File(getPlugin().getDataFolder(), "modules.yml"));
                it.saveDefaults();
                it.load(true);
            });
            String langName = configFile.getSettings().getLang();
            // TODO MULTI LANG
            //    Class langClass = Class.forName("net.leaderos.plugin.bukkit.configuration.lang." + langName);
            //    Class<Language> languageClass = langClass;
            this.langFile = ConfigManager.create(Language.class, (it) -> {
                it.withConfigurer(new YamlBukkitConfigurer());
                it.withBindFile(new File(getPlugin().getDataFolder() + "/lang", langName + ".yml"));
                it.saveDefaults();
                it.load(true);
            });
        } catch (Exception exception) {
            getPlugin().getPluginLoader().disablePlugin(getPlugin());
            throw new RuntimeException("Error loading config.yml");
        }
    }
}
