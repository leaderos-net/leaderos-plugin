package net.leaderos.plugin.modules.bazaar.commands;

import com.cryptomorin.xseries.XMaterial;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import lombok.RequiredArgsConstructor;
import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.api.managers.ModuleManager;
import net.leaderos.plugin.helpers.ChatUtil;
import net.leaderos.plugin.helpers.GameUtil;
import net.leaderos.plugin.helpers.ItemUtil;
import net.leaderos.plugin.modules.bazaar.BazaarModule;
import net.leaderos.plugin.modules.bazaar.gui.BazaarGui;
import net.leaderos.plugin.modules.bazaar.helpers.BazaarHelper;
import net.leaderos.plugin.modules.bazaar.model.PlayerBazaar;
import net.leaderos.plugin.modules.cache.model.User;
import net.leaderos.shared.helpers.RequestUtil;
import net.leaderos.shared.model.Response;
import net.leaderos.shared.model.request.impl.bazaar.AddBazaarItemRequest;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bazaar commands
 *
 * @author poyrazinan
 * @since 1.0
 */
@RequiredArgsConstructor
@Command(value = "bazaar", alias = {"webbazaar", "pazar"})
public class BazaarCommand extends BaseCommand {

    /**
     * Global in-flight lock — holds UUIDs of players currently inside an add-transaction.
     * A ConcurrentHashMap-backed Set gives thread-safe O(1) add/remove/contains.
     *
     * Dupe prevention strategy (layered):
     *   1. ADD_LOCK       — per-player CAS lock, blocks concurrent invocations
     *                       (packet flooding, command spam, macro clients).
     *   2. RequestUtil    — shared per-player cooldown prevents rapid successive calls.
     *   3. Immediate hand removal on the MAIN THREAD before going async. The item
     *      only exists as an in-memory clone during the HTTP round-trip — nothing is
     *      left in any inventory that could be duped by disconnecting or by replaying
     *      inventory packets.
     *   4. Live storage-count check against the API so the cap cannot be bypassed by
     *      racing two concurrent /bazaar add commands.
     *   5. On every failure path the item is returned to the player (or dropped at
     *      their feet if the inventory is full), so nothing is ever silently lost.
     */
    private static final Set<UUID> ADD_LOCK = ConcurrentHashMap.newKeySet();

    // ─────────────────────────────────────────────────────────────────────────
    // Commands
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Default command — opens the storage GUI.
     *
     * @param player executor
     */
    @Default
    @Permission("leaderos.bazaar.open")
    public void defaultCommand(Player player) {
        if (!ModuleManager.getModule("Bazaar").isEnabled()) return;

        if (User.isPlayerAuthed(player))
            BazaarGui.showGui(player);
        else
            ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getRegistrationRequired());
    }

    /**
     * Add subcommand — deposits the item in the player's main hand into bazaar storage.
     *
     * @param player executor
     */
    @SubCommand(value = "add", alias = {"ekle"})
    @Permission("leaderos.bazaar.add")
    public void addCommand(Player player) {
        if (!ModuleManager.getModule("Bazaar").isEnabled()) return;

        // Auth check
        if (!User.isPlayerAuthed(player)) {
            ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getRegistrationRequired());
            return;
        }

        // Per-player transaction lock (CAS)
        // Set.add() is atomic on ConcurrentHashMap; returns false if already present.
        if (!ADD_LOCK.add(player.getUniqueId())) {
            ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getHaveRequestOngoing());
            return;
        }

        // Shared request-rate cooldown
        if (!RequestUtil.canRequest(player.getUniqueId())) {
            ADD_LOCK.remove(player.getUniqueId());
            ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getHaveRequestOngoing());
            return;
        }

        // Validate main-hand item (on main thread)
        ItemStack mainHand = player.getInventory().getItemInMainHand();

        if (mainHand == null || mainHand.getType() == Material.AIR || mainHand.getAmount() == 0) {
            ADD_LOCK.remove(player.getUniqueId());
            ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getGui().getBazaarGui().getNoItemInHandMessage());
            return;
        }

        // Resolve user & storage cap
        User user = User.getUser(player.getName());
        if (user == null) {
            ADD_LOCK.remove(player.getUniqueId());
            ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getRegistrationRequired());
            return;
        }

        int maxStorage = GameUtil.getAmountFromPerm(
                player,
                "bazaar.maxstorage.",
                Bukkit.getInstance().getModulesFile().getBazaar().getDefaultStorageSize()
        );

        // Clone and immediately remove from hand
        // Critical dupe-prevention step: after this line the item no longer exists
        // in any inventory. Packet replays / inventory-open exploits cannot copy it.
        ItemStack itemToStore = mainHand.clone();
        player.getInventory().setItemInMainHand(null);
        player.updateInventory();

        // Acquire shared cooldown only AFTER removal to minimise the lock window.
        RequestUtil.addRequest(player.getUniqueId());

        // Async HTTP call
        Bukkit.getFoliaLib().getScheduler().runAsync((asyncTask) -> {
            try {
                int serverId = BazaarModule.getServerId();

                // Live storage-count check (prevents race-condition cap bypass)
                List<PlayerBazaar> currentItems = PlayerBazaar.getBazaarStorage(user.getId());
                if (currentItems.size() >= maxStorage) {
                    BazaarHelper.returnItemToPlayer(player, itemToStore);
                    String msg = Bukkit.getInstance().getLangFile().getGui().getBazaarGui()
                            .getReturnItemMessage()
                            .replace("%max_amount%", String.valueOf(maxStorage))
                            .replace("%amount%", "1");
                    ChatUtil.sendMessage(player, msg);
                    return;
                }

                // Build API payload
                XMaterial material  = XMaterial.matchXMaterial(itemToStore);
                String name         = ItemUtil.getName(itemToStore);
                String lore         = (itemToStore.hasItemMeta() && itemToStore.getItemMeta().hasLore())
                        ? String.join("\n", itemToStore.getItemMeta().getLore())
                        : null;
                int amount          = itemToStore.getAmount();
                int maxDurability   = itemToStore.getType().getMaxDurability();
                int durability      = ItemUtil.getDurability(itemToStore, maxDurability);
                String base64       = ItemUtil.toBase64(itemToStore);
                String creationDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                String modelId      = ItemUtil.getModelId(itemToStore);
                String enchants     = ItemUtil.getEnchantments(itemToStore);

                Response response = new AddBazaarItemRequest(
                        user.getId(), name, lore, amount, maxDurability,
                        durability, base64, 0.0, creationDate, modelId,
                        enchants, serverId, material.name()
                ).getResponse();

                // Handle API result
                if (response.getResponseCode() == HttpURLConnection.HTTP_OK
                        && response.getResponseMessage().getBoolean("status")) {

                    // Success — notify player
                    String msg = Bukkit.getInstance().getLangFile().getGui().getBazaarGui()
                            .getAddItemMessage()
                            .replace("%item_name%", name);
                    ChatUtil.sendMessage(player, msg);

                } else {
                    // API rejected, return item
                    BazaarHelper.returnItemToPlayer(player, itemToStore);
                    ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getUnexpectedError());
                }

            } catch (Exception e) {
                e.printStackTrace();
                // Safety net: always return the item on unexpected exceptions
                BazaarHelper.returnItemToPlayer(player, itemToStore);
                ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getUnexpectedError());

            } finally {
                // Release both locks unconditionally
                RequestUtil.invalidate(player.getUniqueId());
                ADD_LOCK.remove(player.getUniqueId());
            }
        });
    }
}