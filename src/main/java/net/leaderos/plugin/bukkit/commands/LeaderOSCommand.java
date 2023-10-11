package net.leaderos.plugin.bukkit.commands;

import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import lombok.RequiredArgsConstructor;
import net.leaderos.plugin.Main;
import net.leaderos.plugin.shared.helpers.ChatUtil;
import org.bukkit.command.CommandSender;

/**
 * @author poyrazinan, hyperion
 * @since 1.0
 */
@Command("leaderos")
@RequiredArgsConstructor
public class LeaderOSCommand extends BaseCommand {

    /**
     * default command of leaderos
     * @param sender command sender
     */
    @Default
    @Permission("leaderos.help")
    public void defaultCommand(CommandSender sender) {
        for (String message : Main.getInstance().getLangFile().getMessages().getHelp()) {
            ChatUtil.sendMessage(sender, message);
        }
    }

    /**
     * reload command of plugin
     * @param sender commandsender
     */
    @Permission("leaderos.reload")
    @SubCommand("reload")
    public void reloadCommand(CommandSender sender) {
        Main.getInstance().getConfigFile().load(true);
        // TODO Reload
        ChatUtil.sendMessage(sender, "{prefix} &aPlugin reloaded successfully.");
    }

}