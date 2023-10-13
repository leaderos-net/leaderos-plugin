package net.leaderos.plugin.modules.auth.commands;

import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import lombok.RequiredArgsConstructor;
import net.leaderos.plugin.Main;
import net.leaderos.plugin.helpers.ChatUtil;
import net.leaderos.shared.helpers.MDChat.MDChatAPI;
import net.leaderos.shared.module.auth.AuthHelper;
import org.bukkit.entity.Player;

/**
 * Auth commands
 * @author poyrazinan
 * @since 1.0
 */
@RequiredArgsConstructor
@Command(value = "auth", alias = {"authy", "kayit", "site", "web"})
public class Commands extends BaseCommand {

    /**
     * Default command of auth
     * @param player executor
     */
    @Default
    @Permission("leaderos.auth")
    public void defaultCommand(Player player) {
        String link = AuthHelper.getAuthLink(player);
        if (link != null)
            player.spigot().sendMessage(
                MDChatAPI.getFormattedMessage(ChatUtil.color(Main.getInstance()
                        .getLangFile().getMessages()
                        .getAuth().getCommandMessage()
                        .replace("%link%", link)
                        .replace("{prefix}", Main.getInstance().getLangFile().getMessages().getPrefix()))));
        else
            ChatUtil.sendMessage(player, Main.getInstance().getLangFile().getMessages().getAuth().getNoLink());
    }
}