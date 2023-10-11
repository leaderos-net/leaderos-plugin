package net.leaderos.plugin.modules.cache;

import net.leaderos.plugin.Main;
import net.leaderos.plugin.modules.cache.handlers.LoginListener;
import net.leaderos.plugin.modules.cache.handlers.QuitListener;
import net.leaderos.plugin.modules.cache.model.User;
import net.leaderos.shared.module.LeaderOSModule;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

/**
 * Cache module of leaderos-plugin
 *
 * @author poyrazinan
 * @since 1.0
 */
public class Cache extends LeaderOSModule {

    /**
     * LoginListener for load cache
     */
    private static LoginListener loginListener;

    /**
     * QuitListener for remove cache
     */
    private static QuitListener quitListener;

    /**
     * onEnable method of module
     */
    public void onEnable() {
        loginListener = new LoginListener();
        quitListener = new QuitListener();
        Bukkit.getPluginManager().registerEvents(loginListener, Main.getInstance());
        Bukkit.getPluginManager().registerEvents(quitListener, Main.getInstance());
        // Loads all player data
        User.loginAllOnlinePlayers();
        // Placeholder loader
        if( Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
            new Placeholders().register();
    }

    /**
     * onDisable method of module
     */
    public void onDisable() {
        HandlerList.unregisterAll(loginListener);
        HandlerList.unregisterAll(quitListener);
        // Removes cache
        User.getUserList().clear();
        // Placeholder unloader
        if( Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
            new Placeholders().unregister();
    }

    /**
     * Constructor of Cache
     */
    public Cache() {}
}