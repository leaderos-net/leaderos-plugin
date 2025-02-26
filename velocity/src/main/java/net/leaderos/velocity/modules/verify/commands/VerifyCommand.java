package net.leaderos.velocity.modules.verify.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.leaderos.shared.helpers.RequestUtil;
import net.leaderos.shared.model.Response;
import net.leaderos.shared.model.request.impl.verify.VerifyRequest;
import net.leaderos.velocity.Velocity;
import net.leaderos.velocity.helpers.ChatUtil;

import java.net.HttpURLConnection;

/**
 * VerifyCommand of verify module
 * @author leaderos
 * @since 1.0
 */
public class VerifyCommand implements SimpleCommand {

    /**
     * Executes command method
     * @param invocation command manager
     */
    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (source instanceof Player) {
            Player player = (Player) source;

            if (player.hasPermission("leaderos.verify")) {
                if (args.length == 1) {
                    if (!RequestUtil.canRequest(player.getUniqueId())) {
                        ChatUtil.sendMessage(player, Velocity.getInstance().getLangFile().getMessages().getHaveRequestOngoing());
                        return;
                    }

                    RequestUtil.addRequest(player.getUniqueId());

                    Velocity.getInstance().getServer().getScheduler().buildTask(Velocity.getInstance(), () -> {
                        try {
                            String code = args[0];
                            String username = player.getUsername();
                            String uuid = player.getUniqueId().toString();
                            Response verifyRequest = new VerifyRequest(code, username, uuid).getResponse();
                            if (verifyRequest.getResponseCode() == HttpURLConnection.HTTP_OK && verifyRequest.getResponseMessage().getBoolean("status")) {
                                ChatUtil.sendMessage(player, Velocity.getInstance().getLangFile().getMessages().getVerify().getSuccessMessage());
                            } else {
                                ChatUtil.sendMessage(player, Velocity.getInstance().getLangFile().getMessages().getVerify().getFailMessage());
                            }
                        } catch (Exception e) {
                            ChatUtil.sendMessage(player, Velocity.getInstance().getLangFile().getMessages().getVerify().getFailMessage());
                        }

                        RequestUtil.invalidate(player.getUniqueId());
                    }).schedule();
                } else {
                    ChatUtil.sendMessage(player, Velocity.getInstance().getLangFile().getMessages().getCommand().getNotEnoughArguments());
                }
            }
            else {
                ChatUtil.sendMessage(player, Velocity.getInstance().getLangFile().getMessages().getCommand().getNoPerm());
            }
        }
    }
}
