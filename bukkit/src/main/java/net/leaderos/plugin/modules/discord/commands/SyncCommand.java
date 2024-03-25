package net.leaderos.plugin.modules.discord.commands;

import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import lombok.RequiredArgsConstructor;
import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.helpers.ChatUtil;
import net.leaderos.plugin.helpers.MDChat.MDChatAPI;
import net.leaderos.shared.helpers.RequestUtil;
import net.leaderos.shared.modules.discord.DiscordHelper;
import org.bukkit.entity.Player;
import io.github.projectunified.minelib.scheduler.async.AsyncScheduler;
import io.github.projectunified.minelib.scheduler.common.task.Task;
import io.github.projectunified.minelib.scheduler.global.GlobalScheduler;

/**
 * Discord commands
 * @author poyrazinan
 * @since 1.0
 */
@RequiredArgsConstructor
@Command(value = "discord-sync", alias = {"discord-link"})
public class SyncCommand extends BaseCommand {

    private static Bukkit instance;
    
    /**
     * Default command of discord-sync
     * @param player executor
     */
    @Default
    @Permission("leaderos.discord.sync")
    public void defaultCommand(Player player) {
        if (!RequestUtil.canRequest(player.getUniqueId())) {
            ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getHaveRequestOngoing());
            return;
        }

        RequestUtil.addRequest(player.getUniqueId());

        AsyncScheduler.get(instance).run(() -> {
            String link = DiscordHelper.getSyncLink(player.getName());
            if (link != null)
                player.spigot().sendMessage(
                        MDChatAPI.getFormattedMessage(ChatUtil.color(Bukkit.getInstance()
                                .getLangFile().getMessages()
                                .getDiscord().getCommandMessage()
                                .replace("%link%", link)
                                .replace("{prefix}", Bukkit.getInstance().getLangFile().getMessages().getPrefix()))));
            else
                ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getDiscord().getNoLink());

            RequestUtil.invalidate(player.getUniqueId());
        });
    }
}