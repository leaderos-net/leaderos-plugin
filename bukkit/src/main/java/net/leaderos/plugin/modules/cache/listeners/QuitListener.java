package net.leaderos.plugin.modules.cache.listeners;

import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.modules.cache.model.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Quit listener for remove cache
 *
 * @author poyrazinan
 * @since 1.0
 */
public class QuitListener implements Listener {

    /**
     * QuitListener constructor
     */
    public QuitListener(){}

    /**
     * QuitListener for remove cache
     * @param event QuitEvent
     */
    @EventHandler
    public void quitListener(PlayerQuitEvent event) {
        Bukkit.getFoliaLib().getScheduler().runAsync((task) -> User.unloadPlayerCache(event.getPlayer().getName()));
    }

}
