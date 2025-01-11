package net.leaderos.plugin.modules.bazaar.commands;

import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import lombok.RequiredArgsConstructor;
import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.api.managers.ModuleManager;
import net.leaderos.plugin.helpers.ChatUtil;
import net.leaderos.plugin.modules.cache.model.User;
import net.leaderos.plugin.modules.bazaar.gui.BazaarGui;
import org.bukkit.entity.Player;

/**
 * bazaar commands
 * @author poyrazinan
 * @since 1.0
 */
@RequiredArgsConstructor
@Command(value = "bazaar", alias = {"webbazaar", "pazar"})
public class BazaarCommand extends BaseCommand {

    /**
     * Default command of bazaar
     * @param player executor
     */
    @Default
    @Permission("leaderos.bazaar.open")
    public void defaultCommand(Player player) {
        if (ModuleManager.getModule("Bazaar").isEnabled())
            if (User.isPlayerAuthed(player))
                BazaarGui.showGui(player);
            else
                ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getRegistrationRequired());
    }
}
