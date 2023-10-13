package net.leaderos.plugin.modules.cache.handlers;

import net.leaderos.plugin.Main;
import net.leaderos.plugin.modules.cache.model.User;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

/**
 * Login listener class
 * @author poyrazinan
 * @since 1.0
 */
public class LoginListener implements Listener {

    /**
     * Login event of player
     * @param event LoginEvent
     */
    @EventHandler
    public void playerLoginEvent(PlayerLoginEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> User.loadPlayerCache(event.getPlayer()));
    }
}
