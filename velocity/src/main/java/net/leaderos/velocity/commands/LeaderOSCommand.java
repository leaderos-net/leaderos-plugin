package net.leaderos.velocity.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import net.leaderos.velocity.Velocity;
import net.leaderos.velocity.helper.ChatUtil;

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
                Velocity.getInstance().getModuleManager().reloadModules();
                ChatUtil.sendMessage(source, "{prefix} &aPlugin reloaded successfully.");
            }
            else
                ChatUtil.sendMessage(source, Velocity.getInstance().getLangFile().getMessages().getCommand().getNoPerm());
        }
        else
            ChatUtil.sendMessage(source, Velocity.getInstance().getLangFile().getMessages().getCommand().getInvalidArgument());
    }
}
