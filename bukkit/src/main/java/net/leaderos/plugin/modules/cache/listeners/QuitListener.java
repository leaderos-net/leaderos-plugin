package net.leaderos.plugin.modules.cache.listeners;

import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.modules.cache.model.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import io.github.projectunified.minelib.scheduler.async.AsyncScheduler;
import io.github.projectunified.minelib.scheduler.common.task.Task;
import io.github.projectunified.minelib.scheduler.global.GlobalScheduler;

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

    private static Bukkit instance;

    /**
     * QuitListener for remove cache
     * @param event QuitEvent
     */
    @EventHandler
    public void quitListener(PlayerQuitEvent event) {
        AsyncScheduler.get(instance).run(() -> User.unloadPlayerCache(event.getPlayer().getName()));
    }

}
