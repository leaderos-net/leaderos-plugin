package net.leaderos.plugin.modules.voucher.commands;

import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.nbtapi.NBTItem;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import lombok.RequiredArgsConstructor;
import net.leaderos.plugin.Main;
import net.leaderos.plugin.api.handlers.UpdateCacheEvent;
import net.leaderos.plugin.helpers.ChatUtil;
import net.leaderos.plugin.helpers.ItemUtils;
import net.leaderos.plugin.modules.voucher.Voucher;
import net.leaderos.shared.helpers.MoneyUtils;
import net.leaderos.shared.helpers.Placeholder;
import net.leaderos.shared.model.Response;
import net.leaderos.shared.module.credit.CreditHelper;
import net.leaderos.shared.module.credit.helper.UpdateType;
import org.bukkit.Bukkit;
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
        if (sender.hasPermission("credits.voucher.give")) {
            ChatUtil.sendMessage(sender, Main.getInstance().getLangFile().getMessages().getVouchers().getHelpStaff());
        }
        if (sender.hasPermission("credits.voucher.create")) {
            ChatUtil.sendMessage(sender, Main.getInstance().getLangFile().getMessages().getVouchers().getHelp());
        }
    }

    /**
     * Give command of Voucher module
     * @param sender executor
     * @param player target
     * @param amount of credit
     */
    @SubCommand(value = "give", alias = {"ver"})
    @Permission("credits.voucher.give")
    public void giveCommand(CommandSender sender, Player player, Double amount) {
        amount = MoneyUtils.parseDouble(amount);
        giveVoucher(player, amount);

        ChatUtil.sendMessage(sender, ChatUtil.replacePlaceholders(
                Main.getInstance().getLangFile().getMessages().getVouchers().getSuccessfullyGave(),
                new Placeholder("{target}", player.getName()),
                new Placeholder("{amount}", MoneyUtils.format(amount))
        ));
    }

    /**
     * Create command of Voucher module
     * @param player executor
     * @param amount of credit
     */
    @SubCommand(value = "create", alias = {"oluştur", "olustur"})
    @Permission("credits.voucher.create")
    public void createCommand(Player player, Double amount) {
        amount = MoneyUtils.parseDouble(amount);

        if (amount <= 0) {
            ChatUtil.sendMessage(player, Main.getInstance().getLangFile().getMessages().getVouchers().getCannotCreateNegative());
            return;
        }

        if (player.getInventory().firstEmpty() == -1) {
            ChatUtil.sendMessage(player, Main.getInstance().getLangFile().getMessages().getCannotCreateFull());
            return;
        }

        try {
            Response removeCreditRequest = CreditHelper.removeCreditRequest(player.getName(), amount);

            if (Objects.requireNonNull(removeCreditRequest).getResponseCode() == HttpURLConnection.HTTP_OK
                    && removeCreditRequest.getResponseMessage().getBoolean("status")) {
                // Calls UpdateCache event for update player's cache
                Bukkit.getPluginManager().callEvent(new UpdateCacheEvent(player.getName(), amount, UpdateType.REMOVE));
                ChatUtil.sendMessage(player, ChatUtil.replacePlaceholders(
                        Main.getInstance().getLangFile().getMessages().getVouchers().getSuccessfullyCreated(),
                        new Placeholder("{amount}", MoneyUtils.format(amount))
                ));
                giveVoucher(player, amount);
            }
            // TODO if code not afforded or not ok
            else
                ChatUtil.sendMessage(player, ChatUtil.replacePlaceholders(
                        Main.getInstance().getLangFile().getMessages().getVouchers().getCannotCreateNotEnough(),
                        new Placeholder("{amount}", MoneyUtils.format(amount))
                ));
        }
        catch (Exception ignored) {
            ChatUtil.sendMessage(player, Main.getInstance().getLangFile().getMessages().getPlayerNotAvailable());
        }
    }

    /**
     * Helper method of voucher give
     * @param player target
     * @param amount of credit
     */
    private void giveVoucher(Player player, Double amount) {
        amount = MoneyUtils.parseDouble(amount);

        if (amount <= 0) {
            ChatUtil.sendMessage(player, Main.getInstance().getLangFile().getMessages().getVouchers().getCannotCreateNegative());
            return;
        }

        int id = Voucher.getVoucherData().getInt("lastCreated") + 1;
        Voucher.getVoucherData().set("lastCreated", id);
        Voucher.getVoucherData().save();

        String name = ChatUtil.replacePlaceholders(Main.getInstance().getLangFile().getMessages().getVouchers().getItemDisplayName(),
                new Placeholder("{id}", String.valueOf(id)), new Placeholder("{amount}", MoneyUtils.format(amount)));
        List<String> lore = ChatUtil.replacePlaceholders(Main.getInstance().getLangFile().getMessages().getVouchers().getItemLore(),
                new Placeholder("{id}", String.valueOf(id)), new Placeholder("{amount}", MoneyUtils.format(amount)));
        ItemStack item = ItemUtils.getItem(XMaterial.PAPER, name, lore, true);
        NBTItem nbtItem = new NBTItem(item);

        nbtItem.setBoolean("voucher", true);
        nbtItem.setDouble("voucher:amount", amount);
        nbtItem.setInteger("voucher:id", id);

        player.getInventory().addItem(nbtItem.getItem());
    }

}