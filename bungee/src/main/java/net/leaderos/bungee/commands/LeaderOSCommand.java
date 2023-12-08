package net.leaderos.bungee.commands;

import net.leaderos.bungee.Bungee;
import net.leaderos.bungee.helpers.ChatUtil;
import net.leaderos.shared.Shared;
import net.leaderos.shared.helpers.UrlUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

/**
 * @author poyrazinan
 * @since 1.0
 */
public class LeaderOSCommand extends Command {

    /**
     * Constructor of author command
     * @param name of command
     */
    public LeaderOSCommand(String name) {
        super(name);
    }

    /**
     * Default command of auth
     * @param sender executor
     * @param args command args
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 1 && args[0].equals("reload")) {
            if (sender.hasPermission("leaderos.reload")) {
                Bungee.getInstance().getConfigFile().load(true);
                Bungee.getInstance().getLangFile().load(true);
                Bungee.getInstance().getModulesFile().load(true);

                Shared.setLink(UrlUtil.format(Bungee.getInstance().getConfigFile().getSettings().getUrl()));
                Shared.setApiKey(Bungee.getInstance().getConfigFile().getSettings().getApiKey());

                Bungee.getModuleManager().reloadModules();
                ChatUtil.sendMessage(sender, Bungee.getInstance().getLangFile().getMessages().getReload());
            }
            else
                ChatUtil.sendMessage(sender, Bungee.getInstance().getLangFile().getMessages().getCommand().getNoPerm());
        }
        else
            ChatUtil.sendMessage(sender, Bungee.getInstance().getLangFile().getMessages().getCommand().getInvalidArgument());
    }
}
