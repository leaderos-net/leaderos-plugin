package net.leaderos.plugin.modules.communitygoal;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.configuration.Modules;
import net.leaderos.plugin.modules.communitygoal.managers.CommunityGoalManager;
import net.leaderos.plugin.modules.communitygoal.model.CommunityGoal;
import net.leaderos.plugin.helpers.ChatUtil;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Community Goal module placeholder system
 * @author leaderos
 * @since 1.0
 */
public class Placeholders extends PlaceholderExpansion {

    /**
     * identifier of placeholder
     * @return identifier
     */
    public String getIdentifier() {
        return "leaderos-communitygoal";
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
        CommunityGoal communityGoal = CommunityGoalManager.getCommunityGoal();
        if (communityGoal == null)
            return null;

        if (identifier.equals("is_active")) {
            return String.valueOf(communityGoal.isActive());
        }
        else if (identifier.equals("currency")) {
            return communityGoal.getCurrency();
        }
        else if (identifier.equals("total_raised")) {
            return formatDouble(communityGoal.getTotalRaised());
        }
        else if (identifier.equals("goal_amount")) {
            return formatDouble(communityGoal.getGoalAmount());
        }
        else if (identifier.equals("percentage_achieved")) {
            return formatDouble(communityGoal.getPercentageAchieved());
        }
        else if (identifier.equals("progress_bar")) {
            return getProgressBar(communityGoal.getPercentageAchieved());
        }

        return null;
    }

    /**
     * Format double value
     * @param value double value
     * @return formatted string
     */
    private String formatDouble(double value) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
        DecimalFormat decimalFormat = new DecimalFormat("0.00", symbols);
        String formatted = decimalFormat.format(value);
        if (formatted.endsWith(".00")) {
            return formatted.substring(0, formatted.length() - 3);
        }
        return formatted;
    }

    /**
     * Get progress bar
     * @param percentage percentage
     * @return progress bar
     */
    private String getProgressBar(double percentage) {
        Modules.CommunityGoal.ProgressBar progressBar = Bukkit.getInstance().getModulesFile().getCommunityGoal().getProgressBar();
        int length = progressBar.getLength();
        String symbol = progressBar.getSymbol();
        String filledColor = progressBar.getFilledColor();
        String emptyColor = progressBar.getEmptyColor();

        int filledAmount = (int) (percentage / 100 * length);
        if (filledAmount > length)
            filledAmount = length;

        StringBuilder sb = new StringBuilder();
        sb.append(filledColor);
        for (int i = 0; i < filledAmount; i++) {
            sb.append(symbol);
        }
        sb.append(emptyColor);
        for (int i = 0; i < length - filledAmount; i++) {
            sb.append(symbol);
        }

        return ChatUtil.color(sb.toString());
    }
}
