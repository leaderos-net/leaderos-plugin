package net.leaderos.plugin.modules.donators.commands;

import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import lombok.RequiredArgsConstructor;
import net.leaderos.plugin.api.managers.ModuleManager;
import net.leaderos.plugin.modules.donators.gui.RecentDonationGui;
import org.bukkit.entity.Player;

/**
 * Recent donators module commands
 * @author poyrazinan
 * @since 1.0
 */
@RequiredArgsConstructor
@Command(value = "donations", alias = {"recentdonations", "krediyukleyenler"})
public class Commands extends BaseCommand {

    /**
     * Default command of recent donations
     * @param player executor
     */
    @Default
    @Permission("recent.donations.open")
    public void defaultCommand(Player player) {
        if (ModuleManager.getModule("RecentDonations").isEnabled())
            RecentDonationGui.showGui(player);
    }
}
