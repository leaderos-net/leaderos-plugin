package net.leaderos.plugin.modules.webstore.command;

import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import lombok.RequiredArgsConstructor;
import net.leaderos.plugin.api.managers.ModuleManager;
import net.leaderos.plugin.modules.webstore.gui.MainWebStoreGui;
import net.leaderos.plugin.api.LeaderOSAPI;
import org.bukkit.entity.Player;

/**
 * webshop commands
 * @author poyrazinan
 * @since 1.0
 */
@RequiredArgsConstructor
@Command(value = "webshop", alias = {"webstore", "store", "sitemarket", "webmarket"})
public class WebStoreCommand extends BaseCommand {

    /**
     * Default command of webshop
     * @param player executor
     */
    @Default
    @Permission("webshop.open")
    public void defaultCommand(Player player) {
        LeaderOSAPI.getModuleManager();
        if (ModuleManager.getModule("WebStore").isEnabled())
            MainWebStoreGui.showGui(player);
    }
}
