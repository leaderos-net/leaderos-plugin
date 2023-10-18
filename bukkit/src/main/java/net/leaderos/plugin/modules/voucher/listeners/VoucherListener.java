package net.leaderos.plugin.modules.voucher.listeners;

import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.nbtapi.NBTItem;
import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.api.handlers.UpdateCacheEvent;
import net.leaderos.plugin.helpers.ChatUtil;
import net.leaderos.plugin.modules.voucher.VoucherModule;
import net.leaderos.shared.helpers.MoneyUtil;
import net.leaderos.shared.helpers.Placeholder;
import net.leaderos.shared.model.Response;
import net.leaderos.shared.modules.credit.CreditHelper;
import net.leaderos.shared.modules.credit.enums.UpdateType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * VoucherListener listens right click event for deposit credit.
 *
 * @author poyrazinan, hyperion
 * @since 1.0
 */
public class VoucherListener implements Listener {

    private List<String> awaitingVouchers = new ArrayList<>();

    /**
     * Right-Click event for voucher
     * @param event PlayerInteractEvent
     */
    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (!event.hasItem()) return;

        Player player = event.getPlayer();

        if (!player.hasPermission("leaderos.voucher.use")) return;

        ItemStack itemStack = event.getItem();
        if (itemStack.getType() == Material.AIR)
            return;
        NBTItem nbtItem = new NBTItem(itemStack);
        if (!nbtItem.hasKey("voucher"))
            return;

        // not actually necessary but just in case
        event.setCancelled(true);
        int id = nbtItem.getInteger("voucher:id");
        // Cache check
        if (awaitingVouchers.contains(String.valueOf(id)))
            return;
        else
            awaitingVouchers.add(String.valueOf(id));
        double amount = MoneyUtil.parseDouble(nbtItem.getDouble("voucher:amount"));
        // Checks amount just in case
        if (amount <= 0) {
            ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getVouchers().getCannotCreateNegative());
            return;
        }
        // Checks is voucher has been used before
        List<Integer> list = VoucherModule.getVoucherData().getIntegerList("used");
        if (list.contains(id)) {
            ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getVouchers().getAlreadyUsed());
            return;
        }

        Response depositResponse = CreditHelper.addCreditRequest(player.getName(), amount);
        if (Objects.requireNonNull(depositResponse).getResponseCode() == HttpURLConnection.HTTP_OK
                && depositResponse.getResponseMessage().getBoolean("status")) {
            remove(player, id);
            list.add(id);
            VoucherModule.getVoucherData().set("used", list);
            VoucherModule.getVoucherData().save();
            awaitingVouchers.remove(String.valueOf(id));

            // Calls UpdateCache event for update player's cache
            org.bukkit.Bukkit.getPluginManager().callEvent(new UpdateCacheEvent(player.getName(), amount, UpdateType.ADD));
            ChatUtil.sendMessage(player, ChatUtil.replacePlaceholders(
                    Bukkit.getInstance().getLangFile().getMessages().getVouchers()
                            .getSuccessfullyUsed(), new Placeholder("{amount}", MoneyUtil.format(amount))
            ));
        }
        else {
            ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getPlayerNotAvailable());
            awaitingVouchers.remove(String.valueOf(id));
        }
    }

    /**
     * Removes item from player inventory
     * @param target player
     * @param id of slot
     */
    private void remove(@NotNull Player target, int id) {
        int slot = -1;
        for (ItemStack item : target.getInventory().getContents()) {
            slot++;
            if (item == null || item.getType() != XMaterial.PAPER.parseMaterial()) continue;

            NBTItem nbtItem = new NBTItem(item);
            if (nbtItem.hasKey("voucher") && nbtItem.getInteger("voucher:id") == id) {
                if (item.getAmount() > 1) {
                    item.setAmount(item.getAmount() - 1);
                    break;
                } else if (item.getAmount() == 1) {
                    target.getInventory().setItem(slot, null);
                    break;
                }
            }
        }
    }
}
