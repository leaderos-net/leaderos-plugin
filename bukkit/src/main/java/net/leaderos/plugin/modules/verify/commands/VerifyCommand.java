package net.leaderos.plugin.modules.verify.commands;

import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import lombok.RequiredArgsConstructor;
import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.helpers.ChatUtil;
import net.leaderos.shared.helpers.RequestUtil;
import net.leaderos.shared.model.Response;
import net.leaderos.shared.model.request.impl.verify.VerifyRequest;
import org.bukkit.entity.Player;

import java.net.HttpURLConnection;

/**
 * Verify command
 * @author leaderos
 * @since 1.0
 */
@RequiredArgsConstructor
@Command(value = "verify", alias = {"dogrula"})
public class VerifyCommand extends BaseCommand {

    /**
     * Verify command
     * @param player executor
     * @param code code
     */
    @Default
    @Permission("leaderos.verify")
    public void verifyCommand(Player player, String code) {
        if (!RequestUtil.canRequest(player.getUniqueId())) {
            ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getHaveRequestOngoing());
            return;
        }

        RequestUtil.addRequest(player.getUniqueId());

        Bukkit.getFoliaLib().getScheduler().runAsync((task) -> {
            try {
                String username = player.getName();
                String uuid = player.getUniqueId().toString();
                Response verifyRequest = new VerifyRequest(code, username, uuid).getResponse();
                if (verifyRequest.getResponseCode() == HttpURLConnection.HTTP_OK && verifyRequest.getResponseMessage().getBoolean("status")) {
                    ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getVerify().getSuccessMessage());
                } else {
                    ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getVerify().getFailMessage());
                }
            } catch (Exception e) {
                ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getVerify().getFailMessage());
            }

            RequestUtil.invalidate(player.getUniqueId());
        });
    }
}