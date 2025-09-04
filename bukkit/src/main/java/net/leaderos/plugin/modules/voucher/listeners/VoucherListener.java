package net.leaderos.plugin.modules.voucher.listeners;

import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.changeme.nbtapi.NBTItem;
import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.api.handlers.UpdateCacheEvent;
import net.leaderos.plugin.helpers.ChatUtil;
import net.leaderos.plugin.modules.voucher.VoucherModule;
import net.leaderos.shared.error.Error;
import net.leaderos.shared.helpers.MoneyUtil;
import net.leaderos.shared.helpers.Placeholder;
import net.leaderos.shared.helpers.RequestUtil;
import net.leaderos.shared.model.Response;
import net.leaderos.shared.modules.credit.CreditHelper;
import net.leaderos.shared.modules.credit.enums.UpdateType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
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

        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
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

        if (!RequestUtil.canRequest(player.getUniqueId())) {
            ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getHaveRequestOngoing());
            return;
        }

        // Item clone. We will return it if error.
        ItemStack removedVoucher = itemStack.clone();

        // Remove item from the correct hand
        if (event.getHand() == EquipmentSlot.HAND) {
            player.getInventory().setItemInMainHand(null);
        } else if (event.getHand() == EquipmentSlot.OFF_HAND) {
            player.getInventory().setItemInOffHand(null);
        }

        RequestUtil.addRequest(player.getUniqueId());

        Bukkit.getFoliaLib().getScheduler().runAsync((task) -> {
            Response depositResponse = CreditHelper.addCreditRequest(player.getName(), amount);
            if (Objects.requireNonNull(depositResponse).getResponseCode() == HttpURLConnection.HTTP_OK
                    && depositResponse.getResponseMessage().getBoolean("status")) {

                Bukkit.getFoliaLib().getScheduler().runNextTick((file_task) -> {
                    // Add voucher to used list
                    List<Integer> usedList = VoucherModule.getVoucherData().getIntegerList("used");
                    usedList.add(id);
                    VoucherModule.getVoucherData().set("used", usedList);
                    VoucherModule.getVoucherData().save();

                    // Calls UpdateCache event for update player's cache
                    org.bukkit.Bukkit.getPluginManager().callEvent(new UpdateCacheEvent(player.getName(), amount, UpdateType.ADD));
                    awaitingVouchers.remove(String.valueOf(id));
                });

                ChatUtil.sendMessage(player, ChatUtil.replacePlaceholders(
                        Bukkit.getInstance().getLangFile().getMessages().getVouchers()
                                .getSuccessfullyUsed(), new Placeholder("{amount}", MoneyUtil.format(amount))
                ));
            } else {
                // Return item to player if error
                Bukkit.getFoliaLib().getScheduler().runNextTick((item_task) -> {
                    // Drop item if inventory is full
                    if (player.getInventory().firstEmpty() == -1) {
                        player.getWorld().dropItemNaturally(player.getLocation(), removedVoucher);
                    } else {
                        // Add item to inventory
                        player.getInventory().addItem(removedVoucher);
                    }
                });

                // Send message if user not found
                if (depositResponse.getError() == Error.USER_NOT_FOUND) {
                    ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getPlayerNotAvailable());
                }

                // Remove voucher from awaiting list
                awaitingVouchers.remove(String.valueOf(id));
            }

            RequestUtil.invalidate(player.getUniqueId());
        });
    }
}
