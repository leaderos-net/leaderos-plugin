package net.leaderos.plugin.modules.communitygoal.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

/**
 * CommunityGoal model class
 *
 * @author leaderos
 * @since 1.0
 */
@Getter
@Setter
@AllArgsConstructor
public class CommunityGoal {

    /**
     * is active
     */
    private boolean isActive;

    /**
     * currency
     */
    private String currency;

    /**
     * total raised
     */
    private double totalRaised;

    /**
     * goal amount
     */
    private double goalAmount;

    /**
     * percentage achieved
     */
    private double percentageAchieved;

    /**
     * CommunityGoal constructor from JSONObject
     *
     * @param json JSONObject
     */
    public CommunityGoal(@NotNull JSONObject json) {
        this.isActive = json.getBoolean("is_active");
        this.currency = json.getString("currency");
        this.totalRaised = json.getDouble("total_raised");
        this.goalAmount = json.getDouble("goal_amount");
        this.percentageAchieved = json.getDouble("percentage_achieved");
    }
}
