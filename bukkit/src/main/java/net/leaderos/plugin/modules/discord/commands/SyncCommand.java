package net.leaderos.plugin.modules.discord.commands;

import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import lombok.RequiredArgsConstructor;
import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.helpers.ChatUtil;
import net.leaderos.plugin.helpers.MDChat.MDChatAPI;
import net.leaderos.shared.Shared;
import net.leaderos.shared.helpers.RequestUtil;
import org.bukkit.entity.Player;

/**
 * Discord commands
 * @author poyrazinan
 * @since 1.0
 */
@RequiredArgsConstructor
@Command(value = "discord-sync", alias = {"discord-link"})
public class SyncCommand extends BaseCommand {

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

        Bukkit.getFoliaLib().getScheduler().runAsync((task) -> {
            String link = Shared.getLink() + "/discord/link";
            player.spigot().sendMessage(
                    MDChatAPI.getFormattedMessage(ChatUtil.color(Bukkit.getInstance()
                            .getLangFile().getMessages()
                            .getDiscord().getCommandMessage()
                            .replace("%link%", link)
                            .replace("{prefix}", Bukkit.getInstance().getLangFile().getMessages().getPrefix()))));

            RequestUtil.invalidate(player.getUniqueId());
        });
    }
}