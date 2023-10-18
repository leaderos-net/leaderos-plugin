package net.leaderos.plugin.modules.donations;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.leaderos.plugin.modules.donations.model.Donation;
import net.leaderos.plugin.modules.donations.model.DonationType;
import net.leaderos.plugin.modules.donations.model.DonatorData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

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
        return "leaderos-donations";
    }

    /**
     * The author of the Placeholder
     * This cannot be null
     * @return author name
     */
    public String getAuthor() {
        return "leaderos";
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
            DonationType type = getDonationType(identifier);
            if (type == null)
                return "";
            DonatorData donation = getDonatorLine(type, identifier);
            if (donation != null)
                return donation.getUserName();
        }
        else if (identifier.startsWith("credit_")) {
            DonationType type = getDonationType(identifier);
            if (type == null)
                return "";
            DonatorData donation = getDonatorLine(type, identifier);
            if (donation != null)
                return String.valueOf(donation.getCredit());
        }

        return "";
    }

    /**
     * Gets donation data for placeholder
     * @param identifier placeholder data
     * @return RecentDonationData of index
     */
    private @Nullable DonatorData getDonatorLine(DonationType type, String identifier) {
        try {
            String[] parts = identifier.split("_");
            String lastPart = parts[parts.length - 1];
            int donatorLine =  Integer.parseInt(lastPart);
            if (donatorLine > 10)
                return null;
            return Donation.getDonationData(type, donatorLine-1);

        }
        catch (Exception e) { return null; }
    }

    /**
     * Gets donation data for placeholder
     * @param identifier placeholder data
     * @return RecentDonationData of index
     */
    private @Nullable DonationType getDonationType(String identifier) {
        try {
            String[] parts = identifier.split("_");
            String part = parts[parts.length - 2].toUpperCase(Locale.ENGLISH);
            DonationType type = DonationType.valueOf(part);
            return type;
        }
        catch (Exception e) { return null; }
    }
}
