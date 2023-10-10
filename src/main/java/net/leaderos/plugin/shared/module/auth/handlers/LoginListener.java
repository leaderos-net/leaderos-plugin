package net.leaderos.plugin.shared.module.auth.handlers;

import net.leaderos.plugin.shared.module.auth.model.User;
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
     * @param event
     */
    @EventHandler
    public void playerLoginEvent(PlayerLoginEvent event) {
        // TODO Cache
        new User(null);
    }
}
