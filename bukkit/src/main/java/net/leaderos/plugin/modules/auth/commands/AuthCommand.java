package net.leaderos.plugin.modules.auth.commands;

import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import lombok.RequiredArgsConstructor;
import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.helpers.ChatUtil;
import net.leaderos.plugin.helpers.MDChat.MDChatAPI;
import net.leaderos.shared.helpers.RequestUtil;
import net.leaderos.shared.modules.auth.AuthHelper;
import org.bukkit.entity.Player;

/**
 * Auth commands
 * @author poyrazinan
 * @since 1.0
 */
@RequiredArgsConstructor
@Command(value = "auth")
public class AuthCommand extends BaseCommand {

    /**
     * Default command of auth
     * @param player executor
     */
    @Default
    @Permission("leaderos.auth")
    public void defaultCommand(Player player) {
        if (!RequestUtil.canRequest(player.getUniqueId())) {
            ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getHaveRequestOngoing());
            return;
        }

        RequestUtil.addRequest(player.getUniqueId());

        org.bukkit.Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getInstance(), () -> {
            String link = AuthHelper.getAuthLink(player.getName(), player.getUniqueId());
            if (link != null)
                player.spigot().sendMessage(
                        MDChatAPI.getFormattedMessage(ChatUtil.color(Bukkit.getInstance()
                                .getLangFile().getMessages()
                                .getAuth().getCommandMessage()
                                .replace("%link%", link)
                                .replace("{prefix}", Bukkit.getInstance().getLangFile().getMessages().getPrefix()))));
            else
                ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getAuth().getNoLink());

            RequestUtil.invalidate(player.getUniqueId());
        });
    }
}