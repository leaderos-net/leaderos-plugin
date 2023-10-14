package net.leaderos.plugin.modules.discord.commands;

import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import lombok.RequiredArgsConstructor;
import net.leaderos.plugin.Main;
import net.leaderos.plugin.helpers.ChatUtil;
import net.leaderos.plugin.helpers.MDChat.MDChatAPI;
import net.leaderos.shared.module.discord.DiscordHelper;
import org.bukkit.entity.Player;

/**
 * Discord commands
 * @author poyrazinan
 * @since 1.0
 */
@RequiredArgsConstructor
@Command(value = "discord-sync", alias = {"discord-link"})
public class Commands extends BaseCommand {

    /**
     * Default command of discord-sync
     * @param player executor
     */
    @Default
    @Permission("leaderos.discordsync")
    public void defaultCommand(Player player) {
        String link = DiscordHelper.getSyncLink(player.getName());
        if (link != null)
            player.spigot().sendMessage(
                MDChatAPI.getFormattedMessage(ChatUtil.color(Main.getInstance()
                        .getLangFile().getMessages()
                        .getDiscord().getCommandMessage()
                        .replace("%link%", link)
                        .replace("{prefix}", Main.getInstance().getLangFile().getMessages().getPrefix()))));
        else
            ChatUtil.sendMessage(player, Main.getInstance().getLangFile().getMessages().getAuth().getNoLink());
    }
}