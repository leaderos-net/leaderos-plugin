package net.leaderos.plugin.bukkit.modules.webstore;

import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import lombok.RequiredArgsConstructor;
import net.leaderos.plugin.bukkit.api.LeaderOSAPI;
import org.bukkit.entity.Player;

/**
 * webshop commands
 * @author poyrazinan
 * @since 1.0
 */
@RequiredArgsConstructor
@Command(value = "webshop", alias = {"webstore", "store", "sitemarket", "webmarket"})
public class Commands extends BaseCommand {

    /**
     * Default command of webshop
     * @param player
     */
    @Default
    @Permission("webshop.open")
    public void defaultCommand(Player player) {
        // TODO Open gui
        if (LeaderOSAPI.getModuleManager().getModule("WebStore").isEnabled())
            WebStoreGui.showGui(player, null);
    }
}