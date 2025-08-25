package net.leaderos.bungee.modules.ai.commands;

import net.leaderos.bungee.Bungee;
import net.leaderos.bungee.helpers.ChatUtil;
import net.leaderos.shared.helpers.Placeholder;
import net.leaderos.shared.helpers.RequestUtil;
import net.leaderos.shared.model.Response;
import net.leaderos.shared.model.request.impl.ai.AiRequest;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.net.HttpURLConnection;

/**
 * AI command for Bungee
 * @author leaderos
 * @since 1.0
 */
public class AiCommand extends Command {

    /**
     * Constructor of AI command
     * @param name command name
     */
    public AiCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            ChatUtil.sendMessage(sender, "This command can only be used by players.");
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;

        if (!player.hasPermission("leaderos.ai")) {
            ChatUtil.sendMessage(player, Bungee.getInstance().getLangFile().getMessages().getCommand().getNoPerm());
            return;
        }

        if (args.length == 0) {
            ChatUtil.sendMessage(player, Bungee.getInstance().getLangFile().getMessages().getAi().getUsage());
            return;
        }

        String prompt = String.join(" ", args);

        if (!RequestUtil.canRequest(player.getUniqueId())) {
            ChatUtil.sendMessage(player, Bungee.getInstance().getLangFile().getMessages().getHaveRequestOngoing());
            return;
        }

        RequestUtil.addRequest(player.getUniqueId());
        ChatUtil.sendMessage(player, Bungee.getInstance().getLangFile().getMessages().getAi().getGenerating());

        Bungee.getInstance().getProxy().getScheduler().runAsync(Bungee.getInstance(), () -> {
            try {
                // Get player locale
                String locale = player.getLocale().getLanguage();

                Response aiRequest = new AiRequest(prompt, locale).getResponse();
                if (aiRequest.getResponseCode() == HttpURLConnection.HTTP_OK && aiRequest.getResponseMessage().getBoolean("status")) {
                    String message = aiRequest.getResponseMessage().getJSONObject("data").getString("message");
                    ChatUtil.sendMessage(player, ChatUtil.replacePlaceholders(
                            Bungee.getInstance().getLangFile().getMessages().getAi().getAiMessage(),
                            new Placeholder("{message}", message)
                    ));
                } else {
                    ChatUtil.sendMessage(player, Bungee.getInstance().getLangFile().getMessages().getAi().getFailMessage());
                }
            } catch (Exception e) {
                ChatUtil.sendMessage(player, Bungee.getInstance().getLangFile().getMessages().getAi().getFailMessage());
            }
            RequestUtil.invalidate(player.getUniqueId());
        });
    }
} 