package net.leaderos.modules.credits.commands;

import net.leaderos.shared.helpers.MoneyUtils;
import net.leaderos.shared.model.Response;
import net.leaderos.shared.module.credit.CreditHelper;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.net.HttpURLConnection;
import java.util.Objects;

/**
 * Credit module proxy commands
 * @author poyrazinan
 * @since 1.0
 */
public class Commands extends Command {


    /**
     * Constructor of credit commands
     * @param name of command
     */
    public Commands(String name) {
        super(name);
    }

    /**
     * Execute method for command
     * @param sender the executor of this command
     * @param args arguments used to invoke this command
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            Response targetCurrency = CreditHelper.currencyRequest(sender.getName());
            if (Objects.requireNonNull(targetCurrency).getResponseCode() == HttpURLConnection.HTTP_OK) {
                sender.sendMessage("ok");
             /* TODO  ChatUtil.sendMessage(player, ChatUtil.replacePlaceholders(
                        Main.getShared().getLangFile().getMessages().getCreditInfo(),
                        new Placeholder("{amount}", MoneyUtils.format(targetCurrency.getResponseMessage().getDouble("raw_credits")))
                ));

              */
            }
            return;
        }
        if (args.length == 2 && sender.hasPermission("leaderos.admin")) {
            if (args[0].equalsIgnoreCase("see")) {
                showCommand(sender, args[1]);
            }
            return;
        }

        if (args.length == 3) {
            double value = Double.parseDouble(args[2]);
            String target = args[1];
            if (args[0].equalsIgnoreCase("send")) {
                if (sender.hasPermission("leaderos.credit.send")) {
                    sendCommand(sender, target, value);
                }
                else
                    sender.sendMessage("test");
                    // TODO no perm
                return;
            }
            if (!sender.hasPermission("leaderos.admin")) {
                return;
            }

            if (args[0].equalsIgnoreCase("add")) {
                addCommand(sender, target, value);
            }
            else if (args[0].equalsIgnoreCase("set")) {
                setCommand(sender, target, value);
            }
            else if (args[0].equalsIgnoreCase("remove")) {
                removeCommand(sender, target, value);
            }
        }
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
       // TODO     ChatUtil.sendMessage(sender, Shared.getInstance().getLangFile().getMessages().getCannotSendCreditNegative());
            return;
        }

        Response addCreditResponse = CreditHelper.addCreditRequest(target, amount);

        if (Objects.requireNonNull(addCreditResponse).getResponseCode() == HttpURLConnection.HTTP_OK)
            // TODO MSG
            sender.sendMessage("ok");
          /*  ChatUtil.sendMessage(sender, ChatUtil.replacePlaceholders(
                    Main.getShared().getLangFile().getMessages().getSuccessfullyAddedCredit(),
                    new Placeholder("{amount}", MoneyUtils.format(amount)),
                    new Placeholder("{target}", target)
            ));*/
    }

    /**
     * Show credit command
     * @param sender executor
     * @param target player
     */
    public void showCommand(CommandSender sender, String target) {
        Response targetCurrency = CreditHelper.currencyRequest(target);
        if (Objects.requireNonNull(targetCurrency).getResponseCode() == HttpURLConnection.HTTP_OK) {
            sender.sendMessage("ok");
          /*TODO  ChatUtil.sendMessage(sender, ChatUtil.replacePlaceholders(
                    Main.getShared().getLangFile().getMessages().getCreditInfoOther(),
                    new Placeholder("{amount}", MoneyUtils.format(targetCurrency.getResponseMessage().getDouble("raw_credits"))),
                    new Placeholder("{target}", target)
            ));

           */
        }
    }

    /**
     * Send credit command
     * @param player executor
     * @param target player
     * @param amount of credit
     */
    public void sendCommand(CommandSender player, String target, Double amount) {
        amount = MoneyUtils.parseDouble(amount);
        Player targetPlayer = Bukkit.getPlayerExact(target);

        if (player.getName().equalsIgnoreCase(target)) {
          // TODO  ChatUtil.sendMessage(player, Shared.getInstance().getLangFile().getMessages().getCannotSendCreditYourself());
            return;
        }

        if (amount <= 0) {
           // TODO ChatUtil.sendMessage(player, Shared.getInstance().getLangFile().getMessages().getCannotSendCreditNegative());
            return;
        }

        Response sendCreditResponse = CreditHelper.sendCreditRequest(player.getName(), target, amount);

        if (Objects.requireNonNull(sendCreditResponse).getResponseCode() == HttpURLConnection.HTTP_OK) {
          /*TODO  ChatUtil.sendMessage(player, ChatUtil.replacePlaceholders(
                    Main.getShared().getLangFile().getMessages().getSuccessfullySentCredit(),
                    new Placeholder("{amount}", MoneyUtils.format(amount)),
                    new Placeholder("{target}", target)
            ));

            if (targetPlayer != null) {
                ChatUtil.sendMessage(Objects.requireNonNull(targetPlayer), ChatUtil.replacePlaceholders(
                        Main.getShared().getLangFile().getMessages().getReceivedCredit(),
                        new Placeholder("{amount}", MoneyUtils.format(amount)),
                        new Placeholder("{player}", player.getName())
                ));
            }*/
        }
        else
            player.sendMessage("error");
            // TODO Make else
            //ChatUtil.sendMessage(player, Shared.getInstance().getLangFile().getMessages().getCannotSendCreditNotEnough());
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
           // TODO ChatUtil.sendMessage(sender, Shared.getInstance().getLangFile().getMessages().getCannotSendCreditNotEnough());
            return;
        }
/* TODO look at all of them in this class
        long userId = plugin.getPluginDatabase().getUserId(target);
        if (userId == 0) {
            ChatUtil.sendMessage(sender, Shared.getInstance().getLangFile().getMessages().getTargetPlayerNotAvailable());
            return;
        }
 */
        Response removeCreditResponse = CreditHelper.removeCreditRequest(target, amount);
        if (Objects.requireNonNull(removeCreditResponse).getResponseCode() == HttpURLConnection.HTTP_OK) {
            sender.sendMessage("ok");
           /* TODO ChatUtil.sendMessage(sender, ChatUtil.replacePlaceholders(
                    Main.getShared().getLangFile().getMessages().getSuccessfullyRemovedCredit(),
                    new Placeholder("{amount}", MoneyUtils.format(amount)),
                    new Placeholder("{target}", target)
            ));

            */
        }
    }

    /**
     * Sets credit for target player
     * @param sender executor
     * @param target player
     * @param amount new currency
     */
    public void setCommand(CommandSender sender, String target, Double amount) {
        amount = MoneyUtils.parseDouble(amount);

        /*
        long userId = plugin.getPluginDatabase().getUserId(target);
        if (userId == 0) {
            ChatUtil.sendMessage(sender, Shared.getInstance().getLangFile().getMessages().getTargetPlayerNotAvailable());
            return;
        }
         */

        Response setCreditResponse = CreditHelper.setCreditRequest(target, amount);

        if (Objects.requireNonNull(setCreditResponse).getResponseCode() == HttpURLConnection.HTTP_OK) {
            sender.sendMessage("ok");
          /*  ChatUtil.sendMessage(sender, ChatUtil.replacePlaceholders(
                    Main.getShared().getLangFile().getMessages().getSuccessfullySetCredit(),
                    new Placeholder("{amount}", MoneyUtils.format(amount)),
                    new Placeholder("{target}", target)
            ));
           */
        }
    }
}
