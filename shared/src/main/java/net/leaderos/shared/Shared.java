package net.leaderos.shared;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author poyrazinan
 * @since 1.0
 */
@Getter
public class Shared {

    /**
     * Instance of plugin
     */
    private JavaPlugin plugin;

    /**
     * Shared instance
     */
    @Getter
    private static Shared instance;

    /**
     * Link of request
     */
    @Getter
    private static String link;

    /**
     * ApiKey for request
     */
    @Getter
    private static String apiKey;

    /**
     * Constructor of shared
     *
     * @param plugin Main instance
     */
    public Shared(JavaPlugin plugin, String link, String apiKey) {
        this.plugin = plugin;
        Shared.link = link;
        Shared.apiKey = apiKey;
        instance = this;
    }
}
