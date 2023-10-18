package net.leaderos.plugin.api.handlers;


import lombok.Getter;
import net.leaderos.shared.modules.credit.enums.UpdateType;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author poyrazinan
 * @since 1.0
 */
@Getter
public class UpdateCacheEvent extends Event {

    /**
     * Player who made action to be updated
     */
    private final String playerName;

    /**
     * Amount of credit changed
     */
    private final double amount;

    /**
     * Type of cache update
     */
    private final UpdateType updateType;

    /**
     * UpdateCacheEvent constructor
     *
     * @param playerName who required to update
     * @param amount changed credit amount
     * @param updateType update type
     */
    public UpdateCacheEvent(String playerName, double amount, UpdateType updateType) {
        this.playerName = playerName;
        this.amount = amount;
        this.updateType = updateType;
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
