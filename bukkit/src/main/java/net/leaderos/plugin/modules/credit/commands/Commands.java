package net.leaderos.plugin.modules.credit.commands;

import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import lombok.RequiredArgsConstructor;
import net.leaderos.plugin.modules.credit.Credit;
import net.leaderos.shared.Shared;
import net.leaderos.shared.helpers.ChatUtil;
import net.leaderos.shared.helpers.MoneyUtils;
import net.leaderos.shared.helpers.Placeholder;
import net.leaderos.shared.model.Response;
import org.bukkit.command.CommandSender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.net.HttpURLConnection;
import java.util.Objects;

/**
 * @author hyperion, poyrazinan
 * @since 1.0
 */
@RequiredArgsConstructor
@Command(value = "credits", alias = {"credit", "kredi"})
public class Commands extends BaseCommand {

    /**
     * Base command
     * @param player executor
     */
    @Default
    @Permission("credits.see")
    public void defaultCommand(Player player) {
        Response targetCurrency = Credit.currencyRequest(player.getName());
        if (Objects.requireNonNull(targetCurrency).getResponseCode() == HttpURLConnection.HTTP_OK) {
            ChatUtil.sendMessage(player, ChatUtil.replacePlaceholders(
                    Shared.getInstance().getLangFile().getMessages().getCreditInfo(),
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
    @Permission("credits.send")
    public void sendCommand(Player player, String target, Double amount) {
        amount = MoneyUtils.parseDouble(amount);
        Player targetPlayer = Bukkit.getPlayerExact(target);

        if (player.getName().equalsIgnoreCase(target)) {
            ChatUtil.sendMessage(player, Shared.getInstance().getLangFile().getMessages().getCannotSendCreditYourself());
            return;
        }

        if (amount <= 0) {
            ChatUtil.sendMessage(player, Shared.getInstance().getLangFile().getMessages().getCannotSendCreditNegative());
            return;
        }

        /* TODO
        long userId = plugin.getPluginDatabase().getUserId(player.getName());
        if (userId == 0) {
            ChatUtil.sendMessage(player, Shared.getInstance().getLangFile().getMessages().getPlayerNotAvailable());
            return;
        }


        long otherUserId = plugin.getPluginDatabase().getUserId(target);
        if (otherUserId == 0) {
            ChatUtil.sendMessage(player, Shared.getInstance().getLangFile().getMessages().getTargetPlayerNotAvailable());
            return;
        }

        double credit = plugin.getPluginDatabase().getCredits(player.getName());
        if (credit < amount) {
            ChatUtil.sendMessage(player, Shared.getInstance().getLangFile().getMessages().getCannotSendCreditNotEnough());
            return;
        }
        */

        Response sendCreditResponse = Credit.sendCreditRequest(player.getName(), target, amount);

        if (Objects.requireNonNull(sendCreditResponse).getResponseCode() == HttpURLConnection.HTTP_OK) {
            ChatUtil.sendMessage(player, ChatUtil.replacePlaceholders(
                    Shared.getInstance().getLangFile().getMessages().getSuccessfullySentCredit(),
                    new Placeholder("{amount}", MoneyUtils.format(amount)),
                    new Placeholder("{target}", target)
            ));

            if (targetPlayer != null) {
                ChatUtil.sendMessage(Objects.requireNonNull(targetPlayer), ChatUtil.replacePlaceholders(
                        Shared.getInstance().getLangFile().getMessages().getReceivedCredit(),
                        new Placeholder("{amount}", MoneyUtils.format(amount)),
                        new Placeholder("{player}", player.getName())
                ));
            }
        }
        else
            // TODO Make else
            ChatUtil.sendMessage(player, Shared.getInstance().getLangFile().getMessages().getCannotSendCreditNotEnough());
    }

    /**
     * Show credit command
     * @param sender executor
     * @param target player
     */
    @SubCommand(value = "see", alias = {"göster", "goster", "gör", "gor", "bak"})
    @Permission("credits.see.other")
    public void showCommand(CommandSender sender, String target) {
        Response targetCurrency = Credit.currencyRequest(target);
        if (Objects.requireNonNull(targetCurrency).getResponseCode() == HttpURLConnection.HTTP_OK) {
            ChatUtil.sendMessage(sender, ChatUtil.replacePlaceholders(
                    Shared.getInstance().getLangFile().getMessages().getCreditInfoOther(),
                    new Placeholder("{amount}", MoneyUtils.format(targetCurrency.getResponseMessage().getDouble("raw_credits"))),
                    new Placeholder("{target}", target)
            ));
        }
    }

    /**
     * Adds credit to targeted player
     * @param sender executor
     * @param target to deposit
     * @param amount of credit
     */
    @SubCommand(value = "add", alias = "ekle")
    @Permission("credits.add")
    public void addCommand(CommandSender sender, String target, Double amount) {
        amount = MoneyUtils.parseDouble(amount);

        if (amount <= 0) {
            ChatUtil.sendMessage(sender, Shared.getInstance().getLangFile().getMessages().getCannotSendCreditNegative());
            return;
        }

        Response addCreditResponse = Credit.addCreditRequest(target, amount);

        if (Objects.requireNonNull(addCreditResponse).getResponseCode() == HttpURLConnection.HTTP_OK)
            ChatUtil.sendMessage(sender, ChatUtil.replacePlaceholders(
                    Shared.getInstance().getLangFile().getMessages().getSuccessfullyAddedCredit(),
                    new Placeholder("{amount}", MoneyUtils.format(amount)),
                    new Placeholder("{target}", target)
            ));
    }

    /**
     * Removes credit from targeted user
     * @param sender executor
     * @param target player
     * @param amount of currency
     */
    @SubCommand(value = "remove", alias = "sil")
    @Permission("credits.remove")
    public void removeCommand(CommandSender sender, String target, Double amount) {
        amount = MoneyUtils.parseDouble(amount);

        if (amount <= 0) {
            ChatUtil.sendMessage(sender, Shared.getInstance().getLangFile().getMessages().getCannotSendCreditNotEnough());
            return;
        }
/* TODO look at all of them in this class
        long userId = plugin.getPluginDatabase().getUserId(target);
        if (userId == 0) {
            ChatUtil.sendMessage(sender, Shared.getInstance().getLangFile().getMessages().getTargetPlayerNotAvailable());
            return;
        }
 */
        Response removeCreditResponse = Credit.removeCreditRequest(target, amount);
        if (Objects.requireNonNull(removeCreditResponse).getResponseCode() == HttpURLConnection.HTTP_OK) {
            ChatUtil.sendMessage(sender, ChatUtil.replacePlaceholders(
                    Shared.getInstance().getLangFile().getMessages().getSuccessfullyRemovedCredit(),
                    new Placeholder("{amount}", MoneyUtils.format(amount)),
                    new Placeholder("{target}", target)
            ));
        }
    }

    /**
     * Sets credit for target player
     * @param sender executor
     * @param target player
     * @param amount new currency
     */
    @SubCommand(value = "set", alias = "ayarla")
    @Permission("credits.set")
    public void setCommand(CommandSender sender, String target, Double amount) {
        amount = MoneyUtils.parseDouble(amount);

        /*
        long userId = plugin.getPluginDatabase().getUserId(target);
        if (userId == 0) {
            ChatUtil.sendMessage(sender, Shared.getInstance().getLangFile().getMessages().getTargetPlayerNotAvailable());
            return;
        }
         */

        Response setCreditResponse = Credit.setCreditRequest(target, amount);

        if (Objects.requireNonNull(setCreditResponse).getResponseCode() == HttpURLConnection.HTTP_OK) {
            ChatUtil.sendMessage(sender, ChatUtil.replacePlaceholders(
                    Shared.getInstance().getLangFile().getMessages().getSuccessfullySetCredit(),
                    new Placeholder("{amount}", MoneyUtils.format(amount)),
                    new Placeholder("{target}", target)
            ));
        }
    }
}
