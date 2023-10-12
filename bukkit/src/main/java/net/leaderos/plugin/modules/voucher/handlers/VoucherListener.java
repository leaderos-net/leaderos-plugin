package net.leaderos.plugin.modules.voucher.handlers;

import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.nbtapi.NBTItem;
import net.leaderos.plugin.Main;
import net.leaderos.plugin.modules.credit.Credit;
import net.leaderos.shared.Shared;
import net.leaderos.shared.helpers.ChatUtil;
import net.leaderos.shared.helpers.MoneyUtils;
import net.leaderos.shared.helpers.Placeholder;
import net.leaderos.shared.model.Response;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.Objects;

/**
 * VoucherListener listens right click event for deposit credit.
 *
 * @author poyrazinan, hyperion
 * @since 1.0
 */
public class VoucherListener implements Listener {

    /**
     * Right-Click event for voucher
     * @param event PlayerInteractEvent
     */
    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (!event.hasItem()) return;

        Player player = event.getPlayer();

        if (!player.hasPermission("credits.voucher.use")) return;

        ItemStack itemStack = event.getItem();
        if (itemStack.getType() == Material.AIR)
            return;
        NBTItem nbtItem = new NBTItem(itemStack);
        if (!nbtItem.hasKey("voucher"))
            return;

        // not actually necessary but just in case
        event.setCancelled(true);
        int id = nbtItem.getInteger("voucher:id");
        double amount = MoneyUtils.parseDouble(nbtItem.getDouble("voucher:amount"));
        // Checks amount just in case
        if (amount <= 0) {
            ChatUtil.sendMessage(player, Shared.getInstance().getLangFile().getMessages().getVouchers().getCannotCreateNegative());
            return;
        }
        // Checks is voucher has been used before
        List<Integer> list = Main.getInstance().getVoucherData().getIntegerList("used");
        if (list.contains(id)) {
            ChatUtil.sendMessage(player, Shared.getInstance().getLangFile().getMessages().getVouchers().getAlreadyUsed());
            return;
        }

        Response depositResponse = Credit.addCreditRequest(player.getName(), amount);
        if (Objects.requireNonNull(depositResponse).getResponseCode() == HttpURLConnection.HTTP_OK) {
            remove(player, id);
            list.add(id);
            Main.getInstance().getVoucherData().set("used", list);
            Main.getInstance().getVoucherData().save();

            // TODO update cache event
            ChatUtil.sendMessage(player, ChatUtil.replacePlaceholders(
                    Shared.getInstance().getLangFile().getMessages().getVouchers()
                            .getSuccessfullyUsed(), new Placeholder("{amount}", MoneyUtils.format(amount) + "")
            ));
        }
        /* TODO else
        long userId = plugin.getPluginDatabase().getUserId(player.getName());
        if (userId == 0) {
            ChatUtils.sendMessage(player, Shared.getInstance().getLangFile().getMessages().getPlayerNotAvailable());
            return;
        }
         */
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
