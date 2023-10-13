package net.leaderos;

import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * Bungeecord main class
 * @author poyrazinan
 * @since 1.0
 */
public class Bungee extends Plugin {

    /**
     * Instance of plugin
     */
    @Getter
    private static Bungee instance;

    /**
     * onEnable method of bungeecord
     */
    public void onEnable() {
        instance = this;

    }

    /**
     * onDisable method of bungeecord
     */
    public void onDisable() {
    }

}