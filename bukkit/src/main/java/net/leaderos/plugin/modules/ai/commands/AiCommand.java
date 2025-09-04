package net.leaderos.plugin.modules.ai.commands;

import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.Join;
import lombok.RequiredArgsConstructor;
import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.helpers.ChatUtil;
import net.leaderos.shared.helpers.Placeholder;
import net.leaderos.shared.helpers.RequestUtil;
import net.leaderos.shared.model.Response;
import net.leaderos.shared.model.request.impl.ai.AiRequest;
import org.bukkit.entity.Player;

import java.net.HttpURLConnection;

/**
 * AI command
 * @author leaderos
 * @since 1.0
 */
@RequiredArgsConstructor
@Command(value = "ai", alias = {"yapayzeka"})
public class AiCommand extends BaseCommand {

    /**
     * AI command
     * @param player executor
     * @param prompt prompt
     */
    @Default
    @Permission("leaderos.ai")
    public void aiCommand(Player player, @Join String prompt) {
        if (prompt == null || prompt.isEmpty()) {
            ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getAi().getUsage());
            return;
        }

        if (!RequestUtil.canRequest(player.getUniqueId())) {
            ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getHaveRequestOngoing());
            return;
        }

        RequestUtil.addRequest(player.getUniqueId());

        ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getAi().getGenerating());

        Bukkit.getFoliaLib().getScheduler().runAsync((task) -> {
            try {
                // Get player locale
                String locale = player.getLocale();

                Response aiRequest = new AiRequest(prompt, locale).getResponse();
                if (aiRequest.getResponseCode() == HttpURLConnection.HTTP_OK && aiRequest.getResponseMessage().getBoolean("status")) {
                    String message = aiRequest.getResponseMessage().getJSONObject("data").getString("message");
                    ChatUtil.sendMessage(player, ChatUtil.replacePlaceholders(
                            Bukkit.getInstance().getLangFile().getMessages().getAi().getAiMessage(),
                            new Placeholder("{message}", message)
                    ));
                } else {
                    ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getAi().getFailMessage());
                }
            } catch (Exception e) {
                ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getAi().getFailMessage());
            }

            RequestUtil.invalidate(player.getUniqueId());
        });
    }
}