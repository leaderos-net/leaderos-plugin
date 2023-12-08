package net.leaderos.velocity.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import net.leaderos.shared.Shared;
import net.leaderos.shared.helpers.UrlUtil;
import net.leaderos.velocity.Velocity;
import net.leaderos.velocity.helpers.ChatUtil;

/**
 * @author poyrazinan
 * @since 1.0
 */
public class LeaderOSCommand implements SimpleCommand {

    /**
     * Default command of auth
     * @param invocation command invocation
     */
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (args.length == 1 && args[0].equals("reload")) {
            if (source.hasPermission("leaderos.reload")) {
                Velocity.getInstance().getConfigFile().load(true);
                Velocity.getInstance().getLangFile().load(true);
                Velocity.getInstance().getModulesFile().load(true);

                Shared.setLink(UrlUtil.format(Velocity.getInstance().getConfigFile().getSettings().getUrl()));
                Shared.setApiKey(Velocity.getInstance().getConfigFile().getSettings().getApiKey());

                Velocity.getInstance().getModuleManager().reloadModules();
                ChatUtil.sendMessage(source, Velocity.getInstance().getLangFile().getMessages().getReload());
            }
            else
                ChatUtil.sendMessage(source, Velocity.getInstance().getLangFile().getMessages().getCommand().getNoPerm());
        }
        else
            ChatUtil.sendMessage(source, Velocity.getInstance().getLangFile().getMessages().getCommand().getInvalidArgument());
    }
}
