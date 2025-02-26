package net.leaderos.bungee.modules.verify.commands;

import net.leaderos.bungee.Bungee;
import net.leaderos.bungee.helpers.ChatUtil;
import net.leaderos.shared.helpers.RequestUtil;
import net.leaderos.shared.model.Response;
import net.leaderos.shared.model.request.impl.verify.VerifyRequest;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.net.HttpURLConnection;

/**
 * Verify command
 * @author leaderos
 * @since 1.0
 */
public class VerifyCommand extends Command {

    /**
     * Constructor of author command
     * @param name of command
     */
    public VerifyCommand(String name) {
        super(name);
    }

    /**
     * Verify command
     * @param sender executor
     * @param args command args
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if (player.hasPermission("leaderos.verify")) {
                if (args.length == 1) {
                    if (!RequestUtil.canRequest(player.getUniqueId())) {
                        ChatUtil.sendMessage(player, Bungee.getInstance().getLangFile().getMessages().getHaveRequestOngoing());
                        return;
                    }

                    RequestUtil.addRequest(player.getUniqueId());

                    Bungee.getInstance().getProxy().getScheduler().runAsync(Bungee.getInstance(), () -> {
                        try {
                            String code = args[0];
                            String username = player.getName();
                            String uuid = player.getUniqueId().toString();
                            Response verifyRequest = new VerifyRequest(code, username, uuid).getResponse();
                            if (verifyRequest.getResponseCode() == HttpURLConnection.HTTP_OK && verifyRequest.getResponseMessage().getBoolean("status")) {
                                ChatUtil.sendMessage(player, Bungee.getInstance().getLangFile().getMessages().getVerify().getSuccessMessage());
                            } else {
                                ChatUtil.sendMessage(player, Bungee.getInstance().getLangFile().getMessages().getVerify().getFailMessage());
                            }
                        } catch (Exception e) {
                            ChatUtil.sendMessage(player, Bungee.getInstance().getLangFile().getMessages().getVerify().getFailMessage());
                        }

                        RequestUtil.invalidate(player.getUniqueId());
                    });
                } else {
                    ChatUtil.sendMessage(player, Bungee.getInstance().getLangFile().getMessages().getCommand().getNotEnoughArguments());
                }
            } else {
                ChatUtil.sendMessage(player, Bungee.getInstance().getLangFile().getMessages().getCommand().getNoPerm());
            }
        }
    }
}