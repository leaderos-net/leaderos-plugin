package net.leaderos.plugin.modules.credit.commands;

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
import net.leaderos.shared.error.Error;
import net.leaderos.shared.helpers.MoneyUtil;
import net.leaderos.shared.helpers.Placeholder;
import net.leaderos.shared.helpers.RequestUtil;
import net.leaderos.shared.modules.credit.enums.UpdateType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
        if (!RequestUtil.canRequest(player.getUniqueId())) {
            ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getHaveRequestOngoing());
            return;
        }

        RequestUtil.addRequest(player.getUniqueId());

        Bukkit.getFoliaLib().getScheduler().runAsync((task) -> {
            final Double amount = LeaderOSAPI.getCreditManager().get(player.getName());

            ChatUtil.sendMessage(player, ChatUtil.replacePlaceholders(
                    Bukkit.getInstance().getLangFile().getMessages().getCredit().getCreditInfo(),
                    new Placeholder("{amount}", MoneyUtil.format(amount == null ? 0 : amount))
            ));

            // Update cache
            Bukkit.getFoliaLib().getScheduler().runNextTick((event_task) -> org.bukkit.Bukkit.getPluginManager().callEvent(new UpdateCacheEvent(player.getName(), amount == null ? 0 : amount, UpdateType.SET)));

            RequestUtil.invalidate(player.getUniqueId());
        });
    }

    /**
     * Send credit command
     * @param player executor
     * @param target player
     * @param a amount of credit
     */
    @SubCommand(value = "send", alias = {"gönder", "gonder"})
    @Permission("leaderos.credit.send")
    public void sendCommand(Player player, String target, Double a) {
        if (!RequestUtil.canRequest(player.getUniqueId())) {
            ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getHaveRequestOngoing());
            return;
        }

        RequestUtil.addRequest(player.getUniqueId());

        Bukkit.getFoliaLib().getScheduler().runAsync((task) -> {
            double amount = MoneyUtil.parseDouble(a);
            Player targetPlayer = org.bukkit.Bukkit.getPlayerExact(target);

            if (player.getName().equalsIgnoreCase(target)) {
                RequestUtil.invalidate(player.getUniqueId());
                ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getCredit().getCannotSendCreditYourself());
                return;
            }

            if (amount <= 0) {
                RequestUtil.invalidate(player.getUniqueId());
                ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getCredit().getCannotSendCreditNegative());
                return;
            }

            Error error = LeaderOSAPI.getCreditManager().send(player.getName(), target, amount);

            if (error == null) {
                // Calls UpdateCache event for update player's cache
                Bukkit.getFoliaLib().getScheduler().runNextTick((event_task) -> org.bukkit.Bukkit.getPluginManager().callEvent(new UpdateCacheEvent(player.getName(), amount, UpdateType.REMOVE)));

                ChatUtil.sendMessage(player, ChatUtil.replacePlaceholders(
                        Bukkit.getInstance().getLangFile().getMessages().getCredit().getSuccessfullySentCredit(),
                        new Placeholder("{amount}", MoneyUtil.format(amount)),
                        new Placeholder("{target}", target)
                ));

                if (targetPlayer != null) {
                    // Calls UpdateCache event for update player's cache
                    Bukkit.getFoliaLib().getScheduler().runNextTick((event_task) -> org.bukkit.Bukkit.getPluginManager().callEvent(new UpdateCacheEvent(target, amount, UpdateType.ADD)));

                    ChatUtil.sendMessage(Objects.requireNonNull(targetPlayer), ChatUtil.replacePlaceholders(
                            Bukkit.getInstance().getLangFile().getMessages().getCredit().getReceivedCredit(),
                            new Placeholder("{amount}", MoneyUtil.format(amount)),
                            new Placeholder("{player}", player.getName())
                    ));
                }
            } else if (error == Error.NOT_ENOUGH_CREDITS || error == Error.USER_NOT_FOUND) {
                ChatUtil.sendMessage(player, ChatUtil.replacePlaceholders(
                        Bukkit.getInstance().getLangFile().getMessages().getCredit().getCannotSendCreditNotEnough(),
                        new Placeholder("{amount}", MoneyUtil.format(amount))
                ));
            } else if (error == Error.INVALID_TARGET
                    || error == Error.TARGET_USER_NOT_FOUND) {
                ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getCredit().getCannotSendCreditsThisUser());
            } else if (error == Error.INVALID_AMOUNT) {
                ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getCredit().getCannotSendCreditNegative());
            } else {
                ChatUtil.sendMessage(player, ChatUtil.replacePlaceholders(
                        Bukkit.getInstance().getLangFile().getMessages().getCredit().getCannotSendCreditNotEnough(),
                        new Placeholder("{amount}", MoneyUtil.format(amount))
                ));
            }

            RequestUtil.invalidate(player.getUniqueId());
        });
    }

    /**
     * Show credit command
     * @param sender executor
     * @param target player
     */
    @SubCommand(value = "see", alias = {"göster", "goster", "gör", "gor", "bak"})
    @Permission("leaderos.credit.see.other")
    public void showCommand(CommandSender sender, String target) {
        if (sender instanceof Player && !RequestUtil.canRequest(((Player)sender).getUniqueId())) {
            ChatUtil.sendMessage(sender, Bukkit.getInstance().getLangFile().getMessages().getHaveRequestOngoing());
            return;
        }

        if (sender instanceof Player) {
            RequestUtil.addRequest(((Player)sender).getUniqueId());
        }

        Bukkit.getFoliaLib().getScheduler().runAsync((task) -> {
            Double amount = LeaderOSAPI.getCreditManager().get(target);

            if (amount != null) {
                ChatUtil.sendMessage(sender, ChatUtil.replacePlaceholders(
                        Bukkit.getInstance().getLangFile().getMessages().getCredit().getCreditInfoOther(),
                        new Placeholder("{amount}", MoneyUtil.format(amount)),
                        new Placeholder("{target}", target)
                ));
            }
            else
                ChatUtil.sendMessage(sender, ChatUtil.replacePlaceholders(
                        Bukkit.getInstance().getLangFile().getMessages().getPlayerNotAvailable()
                ));

            if (sender instanceof Player) {
                RequestUtil.invalidate(((Player)sender).getUniqueId());
            }
        });
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
        amount = MoneyUtil.parseDouble(amount);

        if (amount <= 0) {
            ChatUtil.sendMessage(sender, Bukkit.getInstance().getLangFile().getMessages().getCredit().getCannotSendCreditNegative());
            return;
        }

        if (sender instanceof Player && !RequestUtil.canRequest(((Player)sender).getUniqueId())) {
            ChatUtil.sendMessage(sender, Bukkit.getInstance().getLangFile().getMessages().getHaveRequestOngoing());
            return;
        }

        if (sender instanceof Player) {
            RequestUtil.addRequest(((Player)sender).getUniqueId());
        }

        Double finalAmount = amount;
        Bukkit.getFoliaLib().getScheduler().runAsync((task) -> {
            boolean addCredit = LeaderOSAPI.getCreditManager().add(target, finalAmount);
            if (addCredit) {
                if (org.bukkit.Bukkit.getPlayerExact(target) != null)
                    // Calls UpdateCache event for update player's cache
                    Bukkit.getFoliaLib().getScheduler().runNextTick((event_task) -> org.bukkit.Bukkit.getPluginManager().callEvent(new UpdateCacheEvent(target, finalAmount, UpdateType.ADD)));

                ChatUtil.sendMessage(sender, ChatUtil.replacePlaceholders(
                        Bukkit.getInstance().getLangFile().getMessages().getCredit().getSuccessfullyAddedCredit(),
                        new Placeholder("{amount}", MoneyUtil.format(finalAmount)),
                        new Placeholder("{target}", target)
                ));
            }
            else
                ChatUtil.sendMessage(sender, Bukkit.getInstance().getLangFile().getMessages().getTargetPlayerNotAvailable());

            if (sender instanceof Player) {
                RequestUtil.invalidate(((Player)sender).getUniqueId());
            }
        });
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
        amount = MoneyUtil.parseDouble(amount);

        if (amount <= 0) {
            ChatUtil.sendMessage(sender, Bukkit.getInstance().getLangFile().getMessages().getCredit().getCannotSendCreditNotEnough());
            return;
        }

        if (sender instanceof Player && !RequestUtil.canRequest(((Player)sender).getUniqueId())) {
            ChatUtil.sendMessage(sender, Bukkit.getInstance().getLangFile().getMessages().getHaveRequestOngoing());
            return;
        }

        if (sender instanceof Player) {
            RequestUtil.addRequest(((Player)sender).getUniqueId());
        }

        Double finalAmount = amount;
        Bukkit.getFoliaLib().getScheduler().runAsync((task) -> {
            boolean removeCredit = LeaderOSAPI.getCreditManager().remove(target, finalAmount);
            if (removeCredit) {
                if (org.bukkit.Bukkit.getPlayerExact(target) != null)
                    // Calls UpdateCache event for update player's cache
                    Bukkit.getFoliaLib().getScheduler().runNextTick((event_task) -> org.bukkit.Bukkit.getPluginManager().callEvent(new UpdateCacheEvent(target, finalAmount, UpdateType.REMOVE)));

                ChatUtil.sendMessage(sender, ChatUtil.replacePlaceholders(
                        Bukkit.getInstance().getLangFile().getMessages().getCredit().getSuccessfullyRemovedCredit(),
                        new Placeholder("{amount}", MoneyUtil.format(finalAmount)),
                        new Placeholder("{target}", target)
                ));
            } else
                ChatUtil.sendMessage(sender, Bukkit.getInstance().getLangFile().getMessages().getTargetPlayerNotAvailable());

            if (sender instanceof Player) {
                RequestUtil.invalidate(((Player)sender).getUniqueId());
            }
        });

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
        amount = MoneyUtil.parseDouble(amount);

        if (sender instanceof Player && !RequestUtil.canRequest(((Player)sender).getUniqueId())) {
            ChatUtil.sendMessage(sender, Bukkit.getInstance().getLangFile().getMessages().getHaveRequestOngoing());
            return;
        }

        if (sender instanceof Player) {
            RequestUtil.addRequest(((Player)sender).getUniqueId());
        }

        Double finalAmount = amount;
        Bukkit.getFoliaLib().getScheduler().runAsync((task) -> {
            boolean setCredit = LeaderOSAPI.getCreditManager().set(target, finalAmount);
            if (setCredit) {
                if (org.bukkit.Bukkit.getPlayerExact(target) != null)
                    // Calls UpdateCache event for update player's cache
                    Bukkit.getFoliaLib().getScheduler().runNextTick((event_task) -> org.bukkit.Bukkit.getPluginManager().callEvent(new UpdateCacheEvent(target, finalAmount, UpdateType.SET)));

                ChatUtil.sendMessage(sender, ChatUtil.replacePlaceholders(
                        Bukkit.getInstance().getLangFile().getMessages().getCredit().getSuccessfullySetCredit(),
                        new Placeholder("{amount}", MoneyUtil.format(finalAmount)),
                        new Placeholder("{target}", target)
                ));
            }
            else
                ChatUtil.sendMessage(sender, Bukkit.getInstance().getLangFile().getMessages().getTargetPlayerNotAvailable());

            if (sender instanceof Player) {
                RequestUtil.invalidate(((Player)sender).getUniqueId());
            }
        });
    }
}
