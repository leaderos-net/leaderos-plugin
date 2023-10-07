package net.leaderos.plugin;

import de.leonhard.storage.Config;
import lombok.Getter;
import net.leaderos.plugin.api.LeaderOSAPI;
import org.bukkit.plugin.java.JavaPlugin;

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
    private Config langFile;

    /**
     * onLoad override method of spigot library
     */
    public void onLoad() {
        instance = this;
        configFile = LeaderOSAPI.getStorageManager().initConfig("config");
        langFile = LeaderOSAPI.getStorageManager().initLangFile(getConfigFile().getString("settings.lang"));
    }

    /**
     * onEnable override method of Spigot library
     */
    public void onEnable() {

    }

    /**
     * Constructor of Main class
     */
    public Main() {}

}
