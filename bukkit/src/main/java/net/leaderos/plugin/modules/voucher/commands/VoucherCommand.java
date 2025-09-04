package net.leaderos.plugin.modules.voucher.commands;

import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.changeme.nbtapi.NBTItem;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import lombok.RequiredArgsConstructor;
import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.api.LeaderOSAPI;
import net.leaderos.plugin.api.handlers.UpdateCacheEvent;
import net.leaderos.plugin.helpers.ChatUtil;
import net.leaderos.plugin.helpers.ItemUtil;
import net.leaderos.plugin.modules.voucher.VoucherModule;
import net.leaderos.shared.helpers.MoneyUtil;
import net.leaderos.shared.helpers.Placeholder;
import net.leaderos.shared.helpers.RequestUtil;
import net.leaderos.shared.model.Response;
import net.leaderos.shared.modules.credit.CreditHelper;
import net.leaderos.shared.modules.credit.enums.UpdateType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.Objects;

/**
 * Voucher module command class
 *
 * @author poyrazinan, hyperion
 * @since 1.0
 */
@RequiredArgsConstructor
@Command(value = "creditvoucher", alias = {"creditsvoucher", "creditsvouchers","kredikagidi"})
public class VoucherCommand extends BaseCommand {

    /**
     * Default command of module
     * @param sender executor
     */
    @Default
    public void defaultCommand(CommandSender sender) {
        if (sender.hasPermission("leaderos.voucher.give")) {
            ChatUtil.sendMessage(sender, Bukkit.getInstance().getLangFile().getMessages().getVouchers().getHelpStaff());
        }
        if (sender.hasPermission("leaderos.voucher.create")) {
            ChatUtil.sendMessage(sender, Bukkit.getInstance().getLangFile().getMessages().getVouchers().getHelp());
        }
    }

    /**
     * Give command of Voucher module
     * @param sender executor
     * @param player target
     * @param amount of credit
     */
    @SubCommand(value = "give", alias = {"ver"})
    @Permission("leaderos.voucher.give")
    public void giveCommand(CommandSender sender, Player player, Double amount) {
        amount = MoneyUtil.parseDouble(amount);
        giveVoucher(player, amount);

        ChatUtil.sendMessage(sender, ChatUtil.replacePlaceholders(
                Bukkit.getInstance().getLangFile().getMessages().getVouchers().getSuccessfullyGave(),
                new Placeholder("{target}", player.getName()),
                new Placeholder("{amount}", MoneyUtil.format(amount))
        ));
    }

    /**
     * Create command of Voucher module
     * @param player executor
     * @param amount of credit
     */
    @SubCommand(value = "create", alias = {"oluştur", "olustur"})
    @Permission("leaderos.voucher.create")
    public void createCommand(Player player, Double amount) {
        amount = MoneyUtil.parseDouble(amount);

        if (amount <= 0) {
            ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getVouchers().getCannotCreateNegative());
            return;
        }

        if (player.getInventory().firstEmpty() == -1) {
            ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getCannotCreateFull());
            return;
        }

        if (!RequestUtil.canRequest(player.getUniqueId())) {
            ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getHaveRequestOngoing());
            return;
        }

        try {
            Double finalAmount = amount;
            RequestUtil.addRequest(player.getUniqueId());

            Bukkit.getFoliaLib().getScheduler().runAsync((task) -> {
                boolean removeCredit = LeaderOSAPI.getCreditManager().remove(player.getName(), finalAmount);
                if (removeCredit) {
                    // Calls UpdateCache event for update player's cache
                    Bukkit.getFoliaLib().getScheduler().runNextTick((event_task) -> org.bukkit.Bukkit.getPluginManager().callEvent(new UpdateCacheEvent(player.getName(), finalAmount, UpdateType.REMOVE)));

                    ChatUtil.sendMessage(player, ChatUtil.replacePlaceholders(
                            Bukkit.getInstance().getLangFile().getMessages().getVouchers().getSuccessfullyCreated(),
                            new Placeholder("{amount}", MoneyUtil.format(finalAmount))
                    ));
                    giveVoucher(player, finalAmount);
                } else {
                    ChatUtil.sendMessage(player, ChatUtil.replacePlaceholders(
                            Bukkit.getInstance().getLangFile().getMessages().getVouchers().getCannotCreateNotEnough(),
                            new Placeholder("{amount}", MoneyUtil.format(finalAmount))
                    ));
                }

                RequestUtil.invalidate(player.getUniqueId());
            });
        }
        catch (Exception ignored) {
            ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getPlayerNotAvailable());
        }
    }

    /**
     * Helper method of voucher give
     * @param player target
     * @param amount of credit
     */
    private void giveVoucher(Player player, Double amount) {
        amount = MoneyUtil.parseDouble(amount);

        if (amount <= 0) {
            ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getVouchers().getCannotCreateNegative());
            return;
        }

        int id = VoucherModule.getVoucherData().getInt("lastCreated") + 1;
        VoucherModule.getVoucherData().set("lastCreated", id);
        VoucherModule.getVoucherData().save();

        String name = ChatUtil.replacePlaceholders(Bukkit.getInstance().getLangFile().getMessages().getVouchers().getItemDisplayName(),
                new Placeholder("{id}", String.valueOf(id)), new Placeholder("{amount}", MoneyUtil.format(amount)));
        List<String> lore = ChatUtil.replacePlaceholders(Bukkit.getInstance().getLangFile().getMessages().getVouchers().getItemLore(),
                new Placeholder("{id}", String.valueOf(id)), new Placeholder("{amount}", MoneyUtil.format(amount)));
        ItemStack item = ItemUtil.getItem(XMaterial.PAPER, name, lore, true);
        NBTItem nbtItem = new NBTItem(item);

        nbtItem.setBoolean("voucher", true);
        nbtItem.setDouble("voucher:amount", amount);
        nbtItem.setInteger("voucher:id", id);

        player.getInventory().addItem(nbtItem.getItem());
    }

}