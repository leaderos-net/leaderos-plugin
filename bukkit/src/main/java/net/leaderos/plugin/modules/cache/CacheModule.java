package net.leaderos.plugin.modules.cache;

import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.modules.cache.commands.CacheCommand;
import net.leaderos.plugin.modules.cache.listeners.CacheUpdateEvent;
import net.leaderos.plugin.modules.cache.listeners.LoginListener;
import net.leaderos.plugin.modules.cache.listeners.QuitListener;
import net.leaderos.plugin.modules.cache.model.User;
import net.leaderos.shared.modules.LeaderOSModule;
import org.bukkit.event.HandlerList;

/**
 * Cache module of leaderos-plugin
 *
 * @author poyrazinan
 * @since 1.0
 */
public class CacheModule extends LeaderOSModule {

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
        org.bukkit.Bukkit.getPluginManager().registerEvents(loginListener, Bukkit.getInstance());
        org.bukkit.Bukkit.getPluginManager().registerEvents(quitListener, Bukkit.getInstance());
        org.bukkit.Bukkit.getPluginManager().registerEvents(cacheUpdateEvent, Bukkit.getInstance());
        // Loads all player data
        User.loadAllPlayers();
        // Register Command
        Bukkit.getCommandManager().registerCommand(new CacheCommand());
        // Placeholder loader
        if (org.bukkit.Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
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
        // Unregister Command
        Bukkit.getCommandManager().unregisterCommand(new CacheCommand());
        // Placeholder unloader
        if( org.bukkit.Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
            new Placeholders().unregister();
    }

    /**
     * Constructor of Cache
     */
    public CacheModule() {}
}