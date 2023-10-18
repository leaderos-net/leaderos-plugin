package net.leaderos.plugin.modules.credit.commands;

import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import lombok.RequiredArgsConstructor;
import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.api.handlers.UpdateCacheEvent;
import net.leaderos.plugin.helpers.ChatUtil;
import net.leaderos.shared.helpers.MoneyUtils;
import net.leaderos.shared.helpers.Placeholder;
import net.leaderos.shared.model.Response;
import net.leaderos.shared.module.credit.CreditHelper;
import net.leaderos.shared.module.credit.helper.UpdateType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.net.HttpURLConnection;
import java.util.Objects;

/**
 * @author hyperion, poyrazinan
 * @since 1.0
 */
@RequiredArgsConstructor
@Command(value = "credits", alias = {"credit", "kredi"})
public class CreditCommand extends BaseCommand {

    /**
     * Base command
     * @param player executor
     */
    @Default
    @Permission("leaderos.credit.see")
    public void defaultCommand(Player player) {
        Response targetCurrency = CreditHelper.currencyRequest(player.getName());
        if (Objects.requireNonNull(targetCurrency).getResponseCode() == HttpURLConnection.HTTP_OK) {
            ChatUtil.sendMessage(player, ChatUtil.replacePlaceholders(
                    Bukkit.getInstance().getLangFile().getMessages().getCredit().getCreditInfo(),
                    new Placeholder("{amount}", MoneyUtils.format(targetCurrency.getResponseMessage().getDouble("raw_credits")))
            ));
        }
    }

    /**
     * Send credit command
     * @param player executor
     * @param target player
     * @param amount of credit
     */
    @SubCommand(value = "send", alias = {"gönder", "gonder"})
    @Permission("leaderos.credit.send")
    public void sendCommand(Player player, String target, Double amount) {
        amount = MoneyUtils.parseDouble(amount);
        Player targetPlayer = org.bukkit.Bukkit.getPlayerExact(target);

        if (player.getName().equalsIgnoreCase(target)) {
            ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getCredit().getCannotSendCreditYourself());
            return;
        }

        if (amount <= 0) {
            ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getCredit().getCannotSendCreditNegative());
            return;
        }

        /* TODO
        long userId = plugin.getPluginDatabase().getUserId(player.getName());
        if (userId == 0) {
            ChatUtil.sendMessage(player, Main.getInstance().getLangFile().getMessages().getPlayerNotAvailable());
            return;
        }


        long otherUserId = plugin.getPluginDatabase().getUserId(target);
        if (otherUserId == 0) {
            ChatUtil.sendMessage(player, Main.getInstance().getLangFile().getMessages().getTargetPlayerNotAvailable());
            return;
        }

        double credit = plugin.getPluginDatabase().getCredits(player.getName());
        if (credit < amount) {
            ChatUtil.sendMessage(player, Main.getInstance().getLangFile().getMessages().getCannotSendCreditNotEnough());
            return;
        }
        */

        Response sendCreditResponse = CreditHelper.sendCreditRequest(player.getName(), target, amount);

        if (Objects.requireNonNull(sendCreditResponse).getResponseCode() == HttpURLConnection.HTTP_OK
                && sendCreditResponse.getResponseMessage().getBoolean("status")) {
            // Calls UpdateCache event for update player's cache
            org.bukkit.Bukkit.getPluginManager().callEvent(new UpdateCacheEvent(player.getName(), amount, UpdateType.REMOVE));
            ChatUtil.sendMessage(player, ChatUtil.replacePlaceholders(
                    Bukkit.getInstance().getLangFile().getMessages().getCredit().getSuccessfullySentCredit(),
                    new Placeholder("{amount}", MoneyUtils.format(amount)),
                    new Placeholder("{target}", target)
            ));

            if (targetPlayer != null) {
                // Calls UpdateCache event for update player's cache
                org.bukkit.Bukkit.getPluginManager().callEvent(new UpdateCacheEvent(target, amount, UpdateType.ADD));
                ChatUtil.sendMessage(Objects.requireNonNull(targetPlayer), ChatUtil.replacePlaceholders(
                        Bukkit.getInstance().getLangFile().getMessages().getCredit().getReceivedCredit(),
                        new Placeholder("{amount}", MoneyUtils.format(amount)),
                        new Placeholder("{player}", player.getName())
                ));
            }
        }
        else
            // TODO Make else
            ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getCredit().getCannotSendCreditNotEnough());
    }

    /**
     * Show credit command
     * @param sender executor
     * @param target player
     */
    @SubCommand(value = "see", alias = {"göster", "goster", "gör", "gor", "bak"})
    @Permission("leaderos.credit.see.other")
    public void showCommand(CommandSender sender, String target) {
        Response targetCurrency = CreditHelper.currencyRequest(target);
        if (Objects.requireNonNull(targetCurrency).getResponseCode() == HttpURLConnection.HTTP_OK) {
            ChatUtil.sendMessage(sender, ChatUtil.replacePlaceholders(
                    Bukkit.getInstance().getLangFile().getMessages().getCredit().getCreditInfoOther(),
                    new Placeholder("{amount}", MoneyUtils.format(targetCurrency.getResponseMessage().getDouble("raw_credits"))),
                    new Placeholder("{target}", target)
            ));
        }
        else
            ChatUtil.sendMessage(sender, ChatUtil.replacePlaceholders(
                    Bukkit.getInstance().getLangFile().getMessages().getPlayerNotAvailable(),
                    new Placeholder("{amount}", MoneyUtils.format(targetCurrency.getResponseMessage().getDouble("raw_credits"))),
                    new Placeholder("{target}", target)));
    }

    /**
     * Adds credit to targeted player
     * @param sender executor
     * @param target to deposit
     * @param amount of credit
     */
    @SubCommand(value = "add", alias = "ekle")
    @Permission("leaderos.credit.add")
    public void addCommand(CommandSender sender, String target, Double amount) {
        amount = MoneyUtils.parseDouble(amount);

        if (amount <= 0) {
            ChatUtil.sendMessage(sender, Bukkit.getInstance().getLangFile().getMessages().getCredit().getCannotSendCreditNegative());
            return;
        }

        Response addCreditResponse = CreditHelper.addCreditRequest(target, amount);

        if (Objects.requireNonNull(addCreditResponse).getResponseCode() == HttpURLConnection.HTTP_OK) {
            if (org.bukkit.Bukkit.getPlayerExact(target) != null)
                // Calls UpdateCache event for update player's cache
                org.bukkit.Bukkit.getPluginManager().callEvent(new UpdateCacheEvent(target, amount, UpdateType.ADD));
            ChatUtil.sendMessage(sender, ChatUtil.replacePlaceholders(
                    Bukkit.getInstance().getLangFile().getMessages().getCredit().getSuccessfullyAddedCredit(),
                    new Placeholder("{amount}", MoneyUtils.format(amount)),
                    new Placeholder("{target}", target)
            ));
        }
    }

    /**
     * Removes credit from targeted user
     * @param sender executor
     * @param target player
     * @param amount of currency
     */
    @SubCommand(value = "remove", alias = "sil")
    @Permission("leaderos.credit.remove")
    public void removeCommand(CommandSender sender, String target, Double amount) {
        amount = MoneyUtils.parseDouble(amount);

        if (amount <= 0) {
            ChatUtil.sendMessage(sender, Bukkit.getInstance().getLangFile().getMessages().getCredit().getCannotSendCreditNotEnough());
            return;
        }
        Response removeCreditResponse = CreditHelper.removeCreditRequest(target, amount);
        if (Objects.requireNonNull(removeCreditResponse).getResponseCode() == HttpURLConnection.HTTP_OK) {
            if (org.bukkit.Bukkit.getPlayerExact(target) != null)
                // Calls UpdateCache event for update player's cache
                org.bukkit.Bukkit.getPluginManager().callEvent(new UpdateCacheEvent(target, amount, UpdateType.REMOVE));
            ChatUtil.sendMessage(sender, ChatUtil.replacePlaceholders(
                    Bukkit.getInstance().getLangFile().getMessages().getCredit().getSuccessfullyRemovedCredit(),
                    new Placeholder("{amount}", MoneyUtils.format(amount)),
                    new Placeholder("{target}", target)
            ));
        }
        else
            ChatUtil.sendMessage(sender, Bukkit.getInstance().getLangFile().getMessages().getTargetPlayerNotAvailable());
    }

    /**
     * Sets credit for target player
     * @param sender executor
     * @param target player
     * @param amount new currency
     */
    @SubCommand(value = "set", alias = "ayarla")
    @Permission("leaderos.credit.set")
    public void setCommand(CommandSender sender, String target, Double amount) {
        amount = MoneyUtils.parseDouble(amount);
        Response setCreditResponse = CreditHelper.setCreditRequest(target, amount);

        if (Objects.requireNonNull(setCreditResponse).getResponseCode() == HttpURLConnection.HTTP_OK) {
            if (org.bukkit.Bukkit.getPlayerExact(target) != null)
                // Calls UpdateCache event for update player's cache
                org.bukkit.Bukkit.getPluginManager().callEvent(new UpdateCacheEvent(target, amount, UpdateType.SET));
            ChatUtil.sendMessage(sender, ChatUtil.replacePlaceholders(
                    Bukkit.getInstance().getLangFile().getMessages().getCredit().getSuccessfullySetCredit(),
                    new Placeholder("{amount}", MoneyUtils.format(amount)),
                    new Placeholder("{target}", target)
            ));
        }
        else
            ChatUtil.sendMessage(sender, Bukkit.getInstance().getLangFile().getMessages().getTargetPlayerNotAvailable());
    }
}
