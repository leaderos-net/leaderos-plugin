package net.leaderos.velocity.modules.credits.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.leaderos.shared.helpers.MoneyUtils;
import net.leaderos.shared.helpers.Placeholder;
import net.leaderos.shared.model.Response;
import net.leaderos.shared.module.credit.CreditHelper;
import net.leaderos.velocity.Velocity;
import net.leaderos.velocity.helper.ChatUtil;

import java.net.HttpURLConnection;
import java.util.Objects;
import java.util.Optional;

/**
 * CreditCommand class for commands
 * @author poyrazinan
 * @since 1.0
 */
public class CreditCommand implements SimpleCommand {

    /**
     * Executes command method
     * @param invocation command modifier
     */
    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (source instanceof Player) {
            Player player = (Player) source;

            if (args.length == 0) {
                if (player.hasPermission("leaderos.credit.see")) {
                    Response targetCurrency = CreditHelper.currencyRequest(player.getUsername());
                    if (Objects.requireNonNull(targetCurrency).getResponseCode() == HttpURLConnection.HTTP_OK) {
                        ChatUtil.sendMessage(player,
                                ChatUtil.replacePlaceholders(Velocity.getInstance().getLangFile().getMessages().getCredit().getCreditInfo(),
                                        new Placeholder("{amount}", MoneyUtils.format(targetCurrency.getResponseMessage().getDouble("raw_credits")))));
                    }
                }
                else {
                    ChatUtil.sendMessage(player, Velocity.getInstance().getLangFile().getMessages().getCommand().getNoPerm());
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("see")) {
                    if (player.hasPermission("leaderos.credit.see.other")) {
                        showCommand(player, args[1]);
                    } else {
                        ChatUtil.sendMessage(player, Velocity.getInstance().getLangFile().getMessages().getCommand().getNoPerm());
                    }
                }
            } else if (args.length == 3) {
                double value = Double.parseDouble(args[2]);
                String target = args[1];
                if (args[0].equalsIgnoreCase("send")) {
                    if (player.hasPermission("leaderos.credit.send")) {
                        sendCommand(player, target, value);
                    } else {
                        ChatUtil.sendMessage(player, Velocity.getInstance().getLangFile().getMessages().getCommand().getNoPerm());
                    }
                } else if (args[0].equalsIgnoreCase("add")) {
                    if (player.hasPermission("leaderos.credit.add")) {
                        addCommand(player, target, value);
                    } else {
                        ChatUtil.sendMessage(player, Velocity.getInstance().getLangFile().getMessages().getCommand().getNoPerm());
                    }
                } else if (args[0].equalsIgnoreCase("set")) {
                    if (player.hasPermission("leaderos.credit.set")) {
                        setCommand(player, target, value);
                    } else {
                        ChatUtil.sendMessage(player, Velocity.getInstance().getLangFile().getMessages().getCommand().getNoPerm());
                    }
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (player.hasPermission("leaderos.credit.remove")) {
                        removeCommand(player, target, value);
                    } else {
                        ChatUtil.sendMessage(player, Velocity.getInstance().getLangFile().getMessages().getCommand().getNoPerm());
                    }
                } else {
                    ChatUtil.sendMessage(player, Velocity.getInstance().getLangFile().getMessages().getCommand().getInvalidArgument());
                }
            } else {
                ChatUtil.sendMessage(player, Velocity.getInstance().getLangFile().getMessages().getCommand().getNotEnoughArguments());
            }
        }
    }


    /**
     * Adds credit to targeted player
     * @param sender executor
     * @param target to deposit
     * @param amount of credit
     */
    public void addCommand(CommandSource sender, String target, Double amount) {
        amount = MoneyUtils.parseDouble(amount);

        if (amount <= 0) {
            ChatUtil.sendMessage(sender, Velocity.getInstance().getLangFile().getMessages().getCredit().getCannotSendCreditNegative());
            return;
        }

        Response addCreditResponse = CreditHelper.addCreditRequest(target, amount);

        if (Objects.requireNonNull(addCreditResponse).getResponseCode() == HttpURLConnection.HTTP_OK) {
            // Calls UpdateCache event for update player's cache
            ChatUtil.sendMessage(sender, ChatUtil.replacePlaceholders(
                    Velocity.getInstance().getLangFile().getMessages().getCredit().getSuccessfullyAddedCredit(),
                    new Placeholder("{amount}", MoneyUtils.format(amount)),
                    new Placeholder("{target}", target)
            ));
        }
        ChatUtil.sendMessage(sender, Velocity.getInstance().getLangFile().getMessages().getTargetPlayerNotAvailable());
    }

    /**
     * Show credit command
     * @param sender executor
     * @param target player
     */
    public void showCommand(CommandSource sender, String target) {
        Response targetCurrency = CreditHelper.currencyRequest(target);
        if (Objects.requireNonNull(targetCurrency).getResponseCode() == HttpURLConnection.HTTP_OK) {
            ChatUtil.sendMessage(sender, ChatUtil.replacePlaceholders(
                    Velocity.getInstance().getLangFile().getMessages().getCredit().getCreditInfoOther(),
                    new Placeholder("{amount}", MoneyUtils.format(targetCurrency.getResponseMessage().getDouble("raw_credits"))),
                    new Placeholder("{target}", target)
            ));
        }
        else
            ChatUtil.sendMessage(sender, Velocity.getInstance().getLangFile().getMessages().getPlayerNotAvailable());
    }

    /**
     * Send credit command
     * @param sender executor
     * @param target player
     * @param amount of credit
     */
    public void sendCommand(CommandSource sender, String target, Double amount) {
        amount = MoneyUtils.parseDouble(amount);
        Optional<Player> targetPlayer = Velocity.getInstance().getServer().getPlayer(target);
        if (!(sender instanceof Player))
            return;
        Player player = (Player) sender;

        if (player.getUsername().equalsIgnoreCase(target)) {
            ChatUtil.sendMessage(player, Velocity.getInstance().getLangFile().getMessages().getCredit().getCannotSendCreditYourself());
            return;
        }

        if (amount <= 0) {
            ChatUtil.sendMessage(player, Velocity.getInstance().getLangFile().getMessages().getCredit().getCannotSendCreditNegative());
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

        Response sendCreditResponse = CreditHelper.sendCreditRequest(player.getUsername(), target, amount);

        if (Objects.requireNonNull(sendCreditResponse).getResponseCode() == HttpURLConnection.HTTP_OK
                && sendCreditResponse.getResponseMessage().getBoolean("status")) {
            ChatUtil.sendMessage(player, ChatUtil.replacePlaceholders(
                    Velocity.getInstance().getLangFile().getMessages().getCredit().getSuccessfullySentCredit(),
                    new Placeholder("{amount}", MoneyUtils.format(amount)),
                    new Placeholder("{target}", target)
            ));

            if (targetPlayer.isPresent())
                ChatUtil.sendMessage(player,
                        ChatUtil.replacePlaceholders(Velocity.getInstance().getLangFile().getMessages().getCredit().getReceivedCredit(),
                                new Placeholder("{amount}", MoneyUtils.format(amount)),
                                new Placeholder("{player}", player.getUsername())
                        ));


        }
        else
            // TODO Make else
            ChatUtil.sendMessage(player, Velocity.getInstance().getLangFile().getMessages().getCredit().getCannotSendCreditNotEnough());
    }


    /**
     * Removes credit from targeted user
     * @param sender executor
     * @param target player
     * @param amount of currency
     */
    public void removeCommand(CommandSource sender, String target, Double amount) {
        amount = MoneyUtils.parseDouble(amount);

        if (amount <= 0) {
            ChatUtil.sendMessage(sender, Velocity.getInstance().getLangFile().getMessages().getCredit().getCannotSendCreditNotEnough());
            return;
        }
        Response removeCreditResponse = CreditHelper.removeCreditRequest(target, amount);
        if (Objects.requireNonNull(removeCreditResponse).getResponseCode() == HttpURLConnection.HTTP_OK) {
            ChatUtil.sendMessage(sender, ChatUtil.replacePlaceholders(
                    Velocity.getInstance().getLangFile().getMessages().getCredit().getSuccessfullyRemovedCredit(),
                    new Placeholder("{amount}", MoneyUtils.format(amount)),
                    new Placeholder("{target}", target)
            ));
        }
        else
            ChatUtil.sendMessage(sender, Velocity.getInstance().getLangFile().getMessages().getTargetPlayerNotAvailable());
    }

    /**
     * Sets credit for target player
     * @param sender executor
     * @param target player
     * @param amount new currency
     */
    public void setCommand(CommandSource sender, String target, Double amount) {
        amount = MoneyUtils.parseDouble(amount);
        Response setCreditResponse = CreditHelper.setCreditRequest(target, amount);

        if (Objects.requireNonNull(setCreditResponse).getResponseCode() == HttpURLConnection.HTTP_OK) {
            ChatUtil.sendMessage(sender, ChatUtil.replacePlaceholders(
                    Velocity.getInstance().getLangFile().getMessages().getCredit().getSuccessfullySetCredit(),
                    new Placeholder("{amount}", MoneyUtils.format(amount)),
                    new Placeholder("{target}", target)
            ));
        }
        else
            ChatUtil.sendMessage(sender, Velocity.getInstance().getLangFile().getMessages().getTargetPlayerNotAvailable());

    }
}
