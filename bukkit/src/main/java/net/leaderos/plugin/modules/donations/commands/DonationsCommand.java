package net.leaderos.plugin.modules.donations.commands;

import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import lombok.RequiredArgsConstructor;
import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.api.managers.ModuleManager;
import net.leaderos.plugin.helpers.ChatUtil;
import net.leaderos.plugin.modules.donations.gui.DonationGui;
import net.leaderos.plugin.modules.donations.timer.Timer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Recent donators module commands
 * @author poyrazinan
 * @since 1.0
 */
@RequiredArgsConstructor
@Command(value = "donations", alias = {"recentdonations", "krediyukleyenler"})
public class DonationsCommand extends BaseCommand {

    /**
     * Default command of recent donations
     * @param player executor
     */
    @Default
    @Permission("leaderos.donations.open")
    public void defaultCommand(Player player) {
        if (ModuleManager.getModule("Donations").isEnabled())
            DonationGui.showGui(player);
    }

    /**
     * updateCache command
     * @param sender executor
     */
    @SubCommand(value = "update", alias = {"güncelle"})
    @Permission("leaderos.donations.update")
    public void updateCacheCommand(CommandSender sender) {
        ChatUtil.sendMessage(sender, Bukkit.getInstance().getLangFile().getGui().getDonationsGui().getUpdatedDonationDataMessage());
        Timer.run();
    }
}
