package net.leaderos.plugin.bukkit.modules.bazaar;

import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import lombok.RequiredArgsConstructor;
import net.leaderos.plugin.bukkit.api.LeaderOSAPI;
import org.bukkit.entity.Player;

/**
 * bazaar commands
 * @author poyrazinan
 * @since 1.0
 */
@RequiredArgsConstructor
@Command(value = "bazaar", alias = {"webbazaar", "pazar"})
public class Commands extends BaseCommand {

    /**
     * Default command of webshop
     * @param player
     */
    @Default
    @Permission("bazaar.open")
    public void defaultCommand(Player player) {
        if (LeaderOSAPI.getModuleManager().getModule("Bazaar").isEnabled())
            BazaarGui.showGui(player);
    }
}
