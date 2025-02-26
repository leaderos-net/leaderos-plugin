package net.leaderos.bungee.modules.credit.commands;

import net.leaderos.bungee.Bungee;
import net.leaderos.bungee.helpers.ChatUtil;
import net.leaderos.shared.error.Error;
import net.leaderos.shared.helpers.MoneyUtil;
import net.leaderos.shared.helpers.Placeholder;
import net.leaderos.shared.helpers.RequestUtil;
import net.leaderos.shared.model.Response;
import net.leaderos.shared.modules.credit.CreditHelper;
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
        if (sender instanceof ProxiedPlayer && !RequestUtil.canRequest(((ProxiedPlayer)sender).getUniqueId())) {
            ChatUtil.sendMessage(sender, Bungee.getInstance().getLangFile().getMessages().getHaveRequestOngoing());
            return;
        }

        if (sender instanceof ProxiedPlayer) {
            RequestUtil.addRequest(((ProxiedPlayer)sender).getUniqueId());
        }

        Bungee.getInstance().getProxy().getScheduler().runAsync(Bungee.getInstance(), () -> {
            // See player credit
            if (args.length == 0) {
                if (sender instanceof ProxiedPlayer) {
                    ProxiedPlayer player = (ProxiedPlayer) sender;
                    if (player.hasPermission("leaderos.credit.see")) {
                        Response targetCredits = CreditHelper.getRequest(player.getName());
                        if (Objects.requireNonNull(targetCredits).getResponseCode() == HttpURLConnection.HTTP_OK) {
                            ChatUtil.sendMessage(player, ChatUtil.replacePlaceholders(
                                    Bungee.getInstance().getLangFile().getMessages().getCredit().getCreditInfo(),
                                    new Placeholder("{amount}", MoneyUtil.format(targetCredits.getResponseMessage().getDouble("raw_credits")))
                            ));
                        }
                        else
                            ChatUtil.sendMessage(player, ChatUtil.replacePlaceholders(
                                    Bungee.getInstance().getLangFile().getMessages().getCredit().getCreditInfo(),
                                    new Placeholder("{amount}", MoneyUtil.format(0.00)
                                    )));
                    }
                    else {
                        ChatUtil.sendMessage(player, Bungee.getInstance().getLangFile().getMessages().getCommand().getNoPerm());
                    }
                }
            }
            // See other player credits (Admin Command)
            else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("see")) {
                    if (sender.hasPermission("leaderos.credit.see.other"))
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
                    if (sender instanceof ProxiedPlayer) {
                        ProxiedPlayer player = (ProxiedPlayer) sender;

                        if (player.hasPermission("leaderos.credit.send")) {
                            sendCommand(player, target, value);
                        }
                        else {
                            ChatUtil.sendMessage(player, Bungee.getInstance().getLangFile().getMessages().getCommand().getNoPerm());
                        }
                    }
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
            else {
                ChatUtil.sendMessage(sender, Bungee.getInstance().getLangFile().getMessages().getCommand().getNotEnoughArguments());
            }

            if (sender instanceof ProxiedPlayer) {
                RequestUtil.invalidate(((ProxiedPlayer)sender).getUniqueId());
            }
        });
    }

    /**
     * Adds credit to targeted player
     * @param sender executor
     * @param target to deposit
     * @param amount of credit
     */
    public void addCommand(CommandSender sender, String target, Double amount) {
        amount = MoneyUtil.parseDouble(amount);

        if (amount <= 0) {
            ChatUtil.sendMessage(sender, Bungee.getInstance().getLangFile().getMessages().getCredit().getCannotSendCreditNegative());
            return;
        }

        Response addCreditResponse = CreditHelper.addCreditRequest(target, amount);

        if (Objects.requireNonNull(addCreditResponse).getResponseCode() == HttpURLConnection.HTTP_OK) {
            // Calls UpdateCache event for update player's cache
            ChatUtil.sendMessage(sender, ChatUtil.replacePlaceholders(
                Bungee.getInstance().getLangFile().getMessages().getCredit().getSuccessfullyAddedCredit(),
                new Placeholder("{amount}", MoneyUtil.format(amount)),
                new Placeholder("{target}", target)
            ));
        }
        else
            ChatUtil.sendMessage(sender, Bungee.getInstance().getLangFile().getMessages().getTargetPlayerNotAvailable());
    }

    /**
     * Show credit command
     * @param sender executor
     * @param target player
     */
    public void showCommand(CommandSender sender, String target) {
        Response targetCredits = CreditHelper.getRequest(target);
        if (Objects.requireNonNull(targetCredits).getResponseCode() == HttpURLConnection.HTTP_OK) {
            ChatUtil.sendMessage(sender, ChatUtil.replacePlaceholders(
                    Bungee.getInstance().getLangFile().getMessages().getCredit().getCreditInfoOther(),
                    new Placeholder("{amount}", MoneyUtil.format(targetCredits.getResponseMessage().getDouble("raw_credits"))),
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
        amount = MoneyUtil.parseDouble(amount);
        ProxiedPlayer targetPlayer = Bungee.getInstance().getProxy().getPlayer(target);

        if (player.getName().equalsIgnoreCase(target)) {
            ChatUtil.sendMessage(player, Bungee.getInstance().getLangFile().getMessages().getCredit().getCannotSendCreditYourself());
            return;
        }

        if (amount <= 0) {
            ChatUtil.sendMessage(player, Bungee.getInstance().getLangFile().getMessages().getCredit().getCannotSendCreditNegative());
            return;
        }

        Response sendCreditResponse = CreditHelper.sendCreditRequest(player.getName(), target, amount);

        if (Objects.requireNonNull(sendCreditResponse).getResponseCode() == HttpURLConnection.HTTP_OK
                && sendCreditResponse.getResponseMessage().getBoolean("status")) {
            ChatUtil.sendMessage(player, ChatUtil.replacePlaceholders(
                    Bungee.getInstance().getLangFile().getMessages().getCredit().getSuccessfullySentCredit(),
                    new Placeholder("{amount}", MoneyUtil.format(amount)),
                    new Placeholder("{target}", target)
            ));

            if (targetPlayer != null && targetPlayer.isConnected())
                ChatUtil.sendMessage(Objects.requireNonNull(targetPlayer), ChatUtil.replacePlaceholders(
                        Bungee.getInstance().getLangFile().getMessages().getCredit().getReceivedCredit(),
                        new Placeholder("{amount}", MoneyUtil.format(amount)),
                        new Placeholder("{player}", player.getName())
                ));
        } else {
            if (sendCreditResponse.getError() == Error.NOT_ENOUGH_CREDITS) {
                ChatUtil.sendMessage(player, Bungee.getInstance().getLangFile().getMessages().getCredit().getCannotSendCreditNotEnough());
            } else if (sendCreditResponse.getError() == Error.INVALID_TARGET
                    || sendCreditResponse.getError() == Error.TARGET_USER_NOT_FOUND
                    || sendCreditResponse.getError() == Error.USER_NOT_FOUND) {
                ChatUtil.sendMessage(player, Bungee.getInstance().getLangFile().getMessages().getCredit().getCannotSendCreditsThisUser());
            } else if (sendCreditResponse.getError() == Error.INVALID_AMOUNT) {
                ChatUtil.sendMessage(player, Bungee.getInstance().getLangFile().getMessages().getCredit().getCannotSendCreditNegative());
            }
        }
    }


    /**
     * Removes credit from targeted user
     * @param sender executor
     * @param target player
     * @param amount of currency
     */
    public void removeCommand(CommandSender sender, String target, Double amount) {
        amount = MoneyUtil.parseDouble(amount);

        if (amount <= 0) {
            ChatUtil.sendMessage(sender, Bungee.getInstance().getLangFile().getMessages().getCredit().getCannotSendCreditNotEnough());
            return;
        }
        Response removeCreditResponse = CreditHelper.removeCreditRequest(target, amount);
        if (Objects.requireNonNull(removeCreditResponse).getResponseCode() == HttpURLConnection.HTTP_OK) {
            ChatUtil.sendMessage(sender, ChatUtil.replacePlaceholders(
                Bungee.getInstance().getLangFile().getMessages().getCredit().getSuccessfullyRemovedCredit(),
                new Placeholder("{amount}", MoneyUtil.format(amount)),
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
        amount = MoneyUtil.parseDouble(amount);
        Response setCreditResponse = CreditHelper.setCreditRequest(target, amount);

        if (Objects.requireNonNull(setCreditResponse).getResponseCode() == HttpURLConnection.HTTP_OK) {
            ChatUtil.sendMessage(sender, ChatUtil.replacePlaceholders(
                    Bungee.getInstance().getLangFile().getMessages().getCredit().getSuccessfullySetCredit(),
                    new Placeholder("{amount}", MoneyUtil.format(amount)),
                    new Placeholder("{target}", target)
            ));
        }
        else
            ChatUtil.sendMessage(sender, Bungee.getInstance().getLangFile().getMessages().getTargetPlayerNotAvailable());

    }
}
