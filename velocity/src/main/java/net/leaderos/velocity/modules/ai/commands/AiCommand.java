package net.leaderos.velocity.modules.ai.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.leaderos.shared.helpers.Placeholder;
import net.leaderos.shared.helpers.RequestUtil;
import net.leaderos.shared.model.Response;
import net.leaderos.shared.model.request.impl.ai.AiRequest;
import net.leaderos.velocity.Velocity;
import net.leaderos.velocity.helpers.ChatUtil;

import java.net.HttpURLConnection;
import java.util.Objects;

/**
 * AI command for Velocity
 * @author leaderos
 * @since 1.0
 */
public class AiCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (!(source instanceof Player)) {
            ChatUtil.sendMessage(source, "This command can only be used by players.");
            return;
        }
        Player player = (Player) source;

        if (!player.hasPermission("leaderos.ai")) {
            ChatUtil.sendMessage(player, Velocity.getInstance().getLangFile().getMessages().getCommand().getNoPerm());
            return;
        }

        if (args.length == 0) {
            ChatUtil.sendMessage(player, Velocity.getInstance().getLangFile().getMessages().getAi().getUsage());
            return;
        }

        String prompt = String.join(" ", args);

        if (!RequestUtil.canRequest(player.getUniqueId())) {
            ChatUtil.sendMessage(player, Velocity.getInstance().getLangFile().getMessages().getHaveRequestOngoing());
            return;
        }

        RequestUtil.addRequest(player.getUniqueId());
        ChatUtil.sendMessage(player, Velocity.getInstance().getLangFile().getMessages().getAi().getGenerating());

        Velocity.getInstance().getServer().getScheduler().buildTask(Velocity.getInstance(), () -> {
            try {
                // Get player locale
                String locale = player.getEffectiveLocale() != null ? player.getEffectiveLocale().getLanguage() : "en";

                Response aiRequest = new AiRequest(prompt, locale).getResponse();
                if (aiRequest.getResponseCode() == HttpURLConnection.HTTP_OK && aiRequest.getResponseMessage().getBoolean("status")) {
                    String message = aiRequest.getResponseMessage().getJSONObject("data").getString("message");
                    ChatUtil.sendMessage(player, ChatUtil.replacePlaceholders(
                            Velocity.getInstance().getLangFile().getMessages().getAi().getAiMessage(),
                            new Placeholder("{message}", message)
                    ));
                } else {
                    ChatUtil.sendMessage(player, Velocity.getInstance().getLangFile().getMessages().getAi().getFailMessage());
                }
            } catch (Exception e) {
                ChatUtil.sendMessage(player, Velocity.getInstance().getLangFile().getMessages().getAi().getFailMessage());
            }
            RequestUtil.invalidate(player.getUniqueId());
        }).schedule();
    }
} 