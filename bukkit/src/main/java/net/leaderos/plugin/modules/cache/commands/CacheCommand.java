package net.leaderos.plugin.modules.cache.commands;

import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import lombok.RequiredArgsConstructor;
import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.api.LeaderOSAPI;
import net.leaderos.plugin.api.handlers.UpdateCacheEvent;
import net.leaderos.plugin.helpers.ChatUtil;
import net.leaderos.shared.helpers.MoneyUtil;
import net.leaderos.shared.helpers.Placeholder;
import net.leaderos.shared.modules.credit.enums.UpdateType;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor
@Command(value = "leaderos-cache")
public class CacheCommand extends BaseCommand {
    /**
     * Update player cache
     * @param sender executor
     * @param target player to update
     */
    @Permission("leaderos.cache.update")
    @SubCommand(value = "update", alias = {"reset"})
    public void updateCommand(CommandSender sender, String target) {
        Bukkit.getFoliaLib().getScheduler().runAsync((task) -> {

            // Check if the player is online
            if (org.bukkit.Bukkit.getPlayer(target) == null) {
                ChatUtil.sendMessage(sender, ChatUtil.replacePlaceholders(
                        Bukkit.getInstance().getLangFile().getMessages().getPlayerNotOnline()
                ));
                return;
            }

            Double amount = LeaderOSAPI.getCreditManager().get(target);

            if (amount != null) {
                Bukkit.getFoliaLib().getScheduler().runNextTick((event_task) -> org.bukkit.Bukkit.getPluginManager().callEvent(new UpdateCacheEvent(target, amount, UpdateType.SET)));

                ChatUtil.sendMessage(sender, ChatUtil.replacePlaceholders(
                        Bukkit.getInstance().getLangFile().getMessages().getCredit().getCacheUpdated(),
                        new Placeholder("{amount}", MoneyUtil.format(amount)),
                        new Placeholder("{target}", target)
                ));
            }
            else
                ChatUtil.sendMessage(sender, ChatUtil.replacePlaceholders(
                        Bukkit.getInstance().getLangFile().getMessages().getPlayerNotAvailable()
                ));
        });
    }
}
