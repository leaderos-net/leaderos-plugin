package net.leaderos.plugin.modules.cache.listeners;

import lombok.AllArgsConstructor;
import net.leaderos.plugin.api.handlers.UpdateCacheEvent;
import net.leaderos.plugin.modules.cache.model.User;
import net.leaderos.shared.modules.credit.enums.UpdateType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Cache update event, updates cache data of player.
 *
 * @author poyrazinan
 * @since 1.0
 */
@AllArgsConstructor
public class CacheUpdateEvent implements Listener {

    /**
     * Updates player cache
     * @param event of update cache event
     */
    @EventHandler
    public void updateCacheEvent(UpdateCacheEvent event){
        if (event.getUpdateType().equals(UpdateType.ADD))
            User.addCreditCache(event.getPlayerName(), event.getAmount());
        else if (event.getUpdateType().equals(UpdateType.REMOVE))
            User.removeCreditCache(event.getPlayerName(), event.getAmount());
        else if (event.getUpdateType().equals(UpdateType.SET))
            User.setCreditCache(event.getPlayerName(), event.getAmount());
    }
}
