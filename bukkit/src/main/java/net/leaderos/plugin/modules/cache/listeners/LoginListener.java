package net.leaderos.plugin.modules.cache.listeners;

import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.modules.cache.model.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import io.github.projectunified.minelib.scheduler.async.AsyncScheduler;
import io.github.projectunified.minelib.scheduler.common.task.Task;
import io.github.projectunified.minelib.scheduler.global.GlobalScheduler;

/**
 * Login listener class
 * @author poyrazinan
 * @since 1.0
 */
public class LoginListener implements Listener {

    private static Bukkit instance;

    /**
     * Login event of player
     * @param event LoginEvent
     */
    @EventHandler
    public void playerLoginEvent(PlayerLoginEvent event) {
       AsyncScheduler.get(instance).run(() -> User.loadPlayerCache(event.getPlayer()));
    }
}
