package net.leaderos.plugin.modules.donations;

import com.cryptomorin.xseries.SkullUtils;
import com.cryptomorin.xseries.XMaterial;
import net.leaderos.plugin.Main;
import net.leaderos.plugin.helpers.ChatUtil;
import net.leaderos.plugin.modules.donations.commands.DonationsCommand;
import net.leaderos.plugin.modules.donations.model.Donation;
import net.leaderos.plugin.modules.donations.model.DonatorData;
import net.leaderos.plugin.modules.donations.timer.Timer;
import net.leaderos.shared.helpers.Placeholder;
import net.leaderos.shared.module.LeaderOSModule;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

/**
 * Donations module main class
 * @author poyrazinan
 * @since 1.0
 */
public class Donations extends LeaderOSModule {

    /**
     * onEnable method of module
     */
    public void onEnable() {
        Main.getCommandManager().registerCommand(new DonationsCommand());
        Timer.run();
        if( Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
            new Placeholders().register();
    }

    /**
     * onDisable method of module
     */
    public void onDisable() {
        Main.getCommandManager().unregisterCommand(new DonationsCommand());
        Timer.taskid.cancel();
        if( Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
            new Placeholders().unregister();
    }

    /**
     * Constructor of RecentDonation
     */
    public Donations() {}

    /**
     * Recent donation gui item
     *
     * @param donatorData donation data of player
     * @return ItemStack of user head
     */
    public static @NotNull ItemStack getDonationHead(@NotNull DonatorData donatorData) {
        // Player head collector
        ItemStack item = XMaterial.PLAYER_HEAD.parseItem();
        assert item != null;
        OfflinePlayer player = Bukkit.getOfflinePlayer(donatorData.getUserName());
        UUID playerUUID = player.getUniqueId();
        SkullMeta meta = SkullUtils.applySkin(Objects.requireNonNull(item.getItemMeta()), playerUUID);
        meta.setDisplayName(ChatUtil.replacePlaceholders(
                Main.getInstance().getLangFile().getGui().getDonationsGui().getDisplayName(),
                new Placeholder("%player%", donatorData.getUserName()),
                new Placeholder("%credit%", String.valueOf(donatorData.getCredit())),
                new Placeholder("%symbol%", donatorData.getSymbol())
        ));
        meta.setLore(ChatUtil.replacePlaceholders(
                Main.getInstance().getLangFile().getGui().getDonationsGui().getLore(),
                new Placeholder("%player%", donatorData.getUserName()),
                new Placeholder("%credit%", String.valueOf(donatorData.getCredit())),
                new Placeholder("%symbol%", donatorData.getSymbol())
        ));
        item.setItemMeta(meta);
        return item;
    }
}
