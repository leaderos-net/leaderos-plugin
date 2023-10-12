package net.leaderos.plugin.modules.donators;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.leaderos.plugin.Main;
import net.leaderos.plugin.modules.donators.model.RecentDonationData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

/**
 * Cache module placeholder system
 * @author poyrazinan
 * @since 1.0
 */
public class Placeholders extends PlaceholderExpansion {

    /**
     * identifier of placeholder
     * @return identifier
     */
    public String getIdentifier() {
        return "leaderos-donator";
    }

    /**
     * The author of the Placeholder
     * This cannot be null
     * @return author name
     */
    public String getAuthor() {
        return "Geik";
    }

    /**
     * Same with #getAuthor() but for versioon
     * This cannot be null
     * @return version of plugin
     */

    public String getVersion() {
        return "1.0";
    }

    /**
     * PlaceholderRequest method
     * @param p player
     * @param identifier placeholder identifier
     * @return placeholder value
     */
    @Override
    public String onPlaceholderRequest(Player p, String identifier) {
        if (identifier.startsWith("name_")) {
            RecentDonationData donationData = getDonationData(identifier);
            if (donationData != null)
                return donationData.getUserName();
        }
        else if (identifier.startsWith("credit_")) {
            RecentDonationData donationData = getDonationData(identifier);
            if (donationData != null)
                return donationData.getCredit()+"";
        }

        return "";
    }

    /**
     * Gets donation data for placeholder
     * @param identifier placeholder data
     * @return RecentDonationData of index
     */
    private @Nullable RecentDonationData getDonationData(String identifier) {
        try {
            String[] parts = identifier.split("_");
            String lastPart = parts[parts.length - 1];
            int donatorLine =  Integer.parseInt(lastPart);
            if (donatorLine > Main.getShared().getModulesFile().getRecentDonations().getRecentDonationLimit())
                return null;
            return RecentDonationData.getRecentDonation(donatorLine-1);

        }
        catch (Exception e) { return null; }
    }
}
