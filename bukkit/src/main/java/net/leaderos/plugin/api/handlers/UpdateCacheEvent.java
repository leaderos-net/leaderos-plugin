package net.leaderos.plugin.api.handlers;


import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author poyrazinan
 * @since 1.0
 */
public class UpdateCacheEvent extends Event {

    /**
     * Player who made action to be updated
     */
    @Getter
    private final String playerName;

    /**
     * UpdateCacheEvent constructor
     *
     * @param playerName who required to update
     */
    public UpdateCacheEvent(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Spigot handlers requirements
     * @see HandlerList
     */
    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * Spigot handlers requirement
     * @return handler list
     */
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    /**
     * Spigot handlers requirement
     *      * @return handler list
     * @return HandlerList list
     */
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
