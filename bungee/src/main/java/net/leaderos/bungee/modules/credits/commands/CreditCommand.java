package net.leaderos.bungee.modules.credits.commands;

import net.leaderos.bungee.Bungee;
import net.leaderos.bungee.helper.ChatUtil;
import net.leaderos.shared.helpers.MoneyUtils;
import net.leaderos.shared.helpers.Placeholder;
import net.leaderos.shared.model.Response;
import net.leaderos.shared.module.credit.CreditHelper;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.net.HttpURLConnection;
import java.util.Objects;

/**
 * Credit module proxy commands
 * @author poyrazinan
 * @since 1.0
 */
public class CreditCommand extends Command {


    /**
     * Constructor of credit commands
     * @param name of command
     */
    public CreditCommand(String name) {
        super(name);
    }

    /**
     * Execute method for command
     * @param sender the executor of this command
     * @param args arguments used to invoke this command
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        // See player credit
        if (args.length == 0) {
            Response targetCurrency = CreditHelper.currencyRequest(sender.getName());
            if (Objects.requireNonNull(targetCurrency).getResponseCode() == HttpURLConnection.HTTP_OK) {
                ChatUtil.sendMessage(sender, ChatUtil.replacePlaceholders(
                        Bungee.getInstance().getLangFile().getMessages().getCreditInfo(),
                        new Placeholder("{amount}", MoneyUtils.format(targetCurrency.getResponseMessage().getDouble("raw_credits")))
                ));
            }
        }
        // See other player credits (Admin Command)
        else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("see")) {
                if (sender.hasPermission("leaderos.see"))
                    showCommand(sender, args[1]);
                else
                    ChatUtil.sendMessage(sender, Bungee.getInstance().getLangFile().getMessages().getCommand().getNoPerm());
            }
        }
        // Send, add, remove, set commands
        else if (args.length == 3) {
            double value = Double.parseDouble(args[2]);
            String target = args[1];
            // Send command (Not admin command)
            if (args[0].equalsIgnoreCase("send")) {
                if (sender.hasPermission("leaderos.credit.send"))
                    sendCommand(sender, target, value);
                else
                    ChatUtil.sendMessage(sender, Bungee.getInstance().getLangFile().getMessages().getCommand().getNoPerm());
            }
            else if (args[0].equalsIgnoreCase("add")) {
                if (sender.hasPermission("leaderos.credit.add"))
                    addCommand(sender, target, value);
                else
                    ChatUtil.sendMessage(sender, Bungee.getInstance().getLangFile().getMessages().getCommand().getNoPerm());
            }
            else if (args[0].equalsIgnoreCase("set")) {
                if (sender.hasPermission("leaderos.credit.set"))
                    setCommand(sender, target, value);
                else
                    ChatUtil.sendMessage(sender, Bungee.getInstance().getLangFile().getMessages().getCommand().getNoPerm());

            }
            else if (args[0].equalsIgnoreCase("remove")) {
                if (!sender.hasPermission("leaderos.credit.remove"))
                    removeCommand(sender, target, value);
                else
                    ChatUtil.sendMessage(sender, Bungee.getInstance().getLangFile().getMessages().getCommand().getNoPerm());
            }
            else
                ChatUtil.sendMessage(sender, Bungee.getInstance().getLangFile().getMessages().getCommand().getInvalidArgument());
        }
        else
            ChatUtil.sendMessage(sender, Bungee.getInstance().getLangFile().getMessages().getCommand().getNotEnoughArguments());
    }

    /**
     * Adds credit to targeted player
     * @param sender executor
     * @param target to deposit
     * @param amount of credit
     */
    public void addCommand(CommandSender sender, String target, Double amount) {
        amount = MoneyUtils.parseDouble(amount);

        if (amount <= 0) {
            ChatUtil.sendMessage(sender, Bungee.getInstance().getLangFile().getMessages().getCannotSendCreditNegative());
            return;
        }

        Response addCreditResponse = CreditHelper.addCreditRequest(target, amount);

        if (Objects.requireNonNull(addCreditResponse).getResponseCode() == HttpURLConnection.HTTP_OK) {
            // Calls UpdateCache event for update player's cache
            ChatUtil.sendMessage(sender, ChatUtil.replacePlaceholders(
                Bungee.getInstance().getLangFile().getMessages().getSuccessfullyAddedCredit(),
                new Placeholder("{amount}", MoneyUtils.format(amount)),
                new Placeholder("{target}", target)
            ));
        }
        ChatUtil.sendMessage(sender, Bungee.getInstance().getLangFile().getMessages().getTargetPlayerNotAvailable());
    }

    /**
     * Show credit command
     * @param sender executor
     * @param target player
     */
    public void showCommand(CommandSender sender, String target) {
        Response targetCurrency = CreditHelper.currencyRequest(target);
        if (Objects.requireNonNull(targetCurrency).getResponseCode() == HttpURLConnection.HTTP_OK) {
            ChatUtil.sendMessage(sender, ChatUtil.replacePlaceholders(
                    Bungee.getInstance().getLangFile().getMessages().getCreditInfoOther(),
                    new Placeholder("{amount}", MoneyUtils.format(targetCurrency.getResponseMessage().getDouble("raw_credits"))),
                    new Placeholder("{target}", target)
            ));
        }
        else
            ChatUtil.sendMessage(sender, Bungee.getInstance().getLangFile().getMessages().getPlayerNotAvailable());
    }

    /**
     * Send credit command
     * @param player executor
     * @param target player
     * @param amount of credit
     */
    public void sendCommand(CommandSender player, String target, Double amount) {
        amount = MoneyUtils.parseDouble(amount);
        ProxiedPlayer targetPlayer = Bungee.getInstance().getProxy().getPlayer(target);

        if (player.getName().equalsIgnoreCase(target)) {
            ChatUtil.sendMessage(player, Bungee.getInstance().getLangFile().getMessages().getCannotSendCreditYourself());
            return;
        }

        if (amount <= 0) {
            ChatUtil.sendMessage(player, Bungee.getInstance().getLangFile().getMessages().getCannotSendCreditNegative());
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
            ChatUtil.sendMessage(player, ChatUtil.replacePlaceholders(
                    Bungee.getInstance().getLangFile().getMessages().getSuccessfullySentCredit(),
                    new Placeholder("{amount}", MoneyUtils.format(amount)),
                    new Placeholder("{target}", target)
            ));

            if (targetPlayer.isConnected())
                ChatUtil.sendMessage(Objects.requireNonNull(targetPlayer), ChatUtil.replacePlaceholders(
                        Bungee.getInstance().getLangFile().getMessages().getReceivedCredit(),
                        new Placeholder("{amount}", MoneyUtils.format(amount)),
                        new Placeholder("{player}", player.getName())
                ));
        }
        else
            // TODO Make else
            ChatUtil.sendMessage(player, Bungee.getInstance().getLangFile().getMessages().getCannotSendCreditNotEnough());
    }


    /**
     * Removes credit from targeted user
     * @param sender executor
     * @param target player
     * @param amount of currency
     */
    public void removeCommand(CommandSender sender, String target, Double amount) {
        amount = MoneyUtils.parseDouble(amount);

        if (amount <= 0) {
            ChatUtil.sendMessage(sender, Bungee.getInstance().getLangFile().getMessages().getCannotSendCreditNotEnough());
            return;
        }
        Response removeCreditResponse = CreditHelper.removeCreditRequest(target, amount);
        if (Objects.requireNonNull(removeCreditResponse).getResponseCode() == HttpURLConnection.HTTP_OK) {
            ChatUtil.sendMessage(sender, ChatUtil.replacePlaceholders(
                Bungee.getInstance().getLangFile().getMessages().getSuccessfullyRemovedCredit(),
                new Placeholder("{amount}", MoneyUtils.format(amount)),
                new Placeholder("{target}", target)
            ));
        }
        else
            ChatUtil.sendMessage(sender, Bungee.getInstance().getLangFile().getMessages().getTargetPlayerNotAvailable());
    }

    /**
     * Sets credit for target player
     * @param sender executor
     * @param target player
     * @param amount new currency
     */
    public void setCommand(CommandSender sender, String target, Double amount) {
        amount = MoneyUtils.parseDouble(amount);
        Response setCreditResponse = CreditHelper.setCreditRequest(target, amount);

        if (Objects.requireNonNull(setCreditResponse).getResponseCode() == HttpURLConnection.HTTP_OK) {
            ChatUtil.sendMessage(sender, ChatUtil.replacePlaceholders(
                    Bungee.getInstance().getLangFile().getMessages().getSuccessfullySetCredit(),
                    new Placeholder("{amount}", MoneyUtils.format(amount)),
                    new Placeholder("{target}", target)
            ));
        }
        else
            ChatUtil.sendMessage(sender, Bungee.getInstance().getLangFile().getMessages().getTargetPlayerNotAvailable());

    }
}
