package net.leaderos.plugin.modules.bazaar.helpers;

import net.leaderos.plugin.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BazaarHelper {
    /**
     * Returns an item to the player on the main thread.
     * Drops the item naturally at the player's feet if the inventory is full.
     * Logs a warning if the player has gone offline in the interim.
     */
    public static void returnItemToPlayer(Player player, ItemStack item) {
        Bukkit.getFoliaLib().getScheduler().runNextTick((t) -> {
            if (!player.isOnline()) {
                Bukkit.getInstance().getLogger().warning(
                        "[Bazaar] Could not return item to offline player '"
                                + player.getName() + "'. Item: " + item.getType()
                );
                return;
            }
            if (player.getInventory().firstEmpty() == -1) {
                player.getWorld().dropItemNaturally(player.getLocation(), item);
            } else {
                player.getInventory().addItem(item);
            }
            player.updateInventory();
        });
    }
}
