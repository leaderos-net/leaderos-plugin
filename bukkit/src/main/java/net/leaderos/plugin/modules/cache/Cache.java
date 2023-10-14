package net.leaderos.plugin.modules.cache;

import net.leaderos.plugin.Main;
import net.leaderos.plugin.modules.cache.handlers.CacheUpdateEvent;
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
     * Update cache event
     */
    private static CacheUpdateEvent cacheUpdateEvent;

    /**
     * onEnable method of module
     */
    public void onEnable() {
        loginListener = new LoginListener();
        quitListener = new QuitListener();
        cacheUpdateEvent = new CacheUpdateEvent();
        Bukkit.getPluginManager().registerEvents(loginListener, Main.getInstance());
        Bukkit.getPluginManager().registerEvents(quitListener, Main.getInstance());
        Bukkit.getPluginManager().registerEvents(cacheUpdateEvent, Main.getInstance());
        // Loads all player data
        User.loadAllPlayers();
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
        HandlerList.unregisterAll(cacheUpdateEvent);
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