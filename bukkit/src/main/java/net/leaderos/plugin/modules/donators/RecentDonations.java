package net.leaderos.plugin.modules.donators;

import com.cryptomorin.xseries.SkullUtils;
import com.cryptomorin.xseries.XMaterial;
import net.leaderos.plugin.Main;
import net.leaderos.plugin.modules.donators.model.RecentDonationData;
import net.leaderos.plugin.modules.donators.timer.Timer;
import net.leaderos.shared.helpers.ChatUtil;
import net.leaderos.shared.helpers.Placeholder;
import net.leaderos.shared.module.LeaderOSModule;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

/**
 * RecentDonations module main class
 * @author poyrazinan
 * @since 1.0
 */
public class RecentDonations extends LeaderOSModule {

    /**
     * onEnable method of module
     */
    public void onEnable() {
        Timer.run();
        if( Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
            new Placeholders().register();
    }

    /**
     * onDisable method of module
     */
    public void onDisable() {
        Timer.taskid.cancel();
        if( Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
            new Placeholders().unregister();
    }

    /**
     * Constructor of RecentDonation
     */
    public RecentDonations() {}

    /**
     * Recent donation gui item
     *
     * @param donationData donation data of player
     * @return ItemStack of user head
     */
    public static @NotNull ItemStack getDonationHead(@NotNull RecentDonationData donationData) {
        // Player head collector
        ItemStack item = XMaterial.PLAYER_HEAD.parseItem();
        assert item != null;
        OfflinePlayer player = Bukkit.getOfflinePlayer(donationData.getUserName());
        UUID playerUUID = player.getUniqueId();
        SkullMeta meta = SkullUtils.applySkin(Objects.requireNonNull(item.getItemMeta()), playerUUID);
        meta.setDisplayName(ChatUtil.replacePlaceholders(
                Main.getShared().getLangFile().getGui().getDonationsGui().getDisplayName(),
                new Placeholder("%player%", donationData.getUserName()),
                new Placeholder("%credit%", donationData.getCredit()+"")
        ));
        meta.setLore(ChatUtil.replacePlaceholders(
                Main.getShared().getLangFile().getGui().getDonationsGui().getLore(),
                new Placeholder("%player%", donationData.getUserName()),
                new Placeholder("%credit%", donationData.getCredit()+"")
        ));
        item.setItemMeta(meta);
        return item;
    }
}
