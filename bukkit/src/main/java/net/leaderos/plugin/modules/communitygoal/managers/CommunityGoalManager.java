package net.leaderos.plugin.modules.communitygoal.managers;

import lombok.Getter;
import lombok.Setter;
import net.leaderos.plugin.modules.communitygoal.model.CommunityGoal;
import net.leaderos.shared.model.Response;
import net.leaderos.shared.model.request.impl.communitygoal.GetCommunityGoalRequest;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * CommunityGoal manager class
 *
 * @author leaderos
 * @since 1.0
 */
@Getter
@Setter
public class CommunityGoalManager {

    /**
     * CommunityGoal object
     */
    @Getter
    private static CommunityGoal communityGoal;

    /**
     * Update the community goal data
     */
    public static void update() {
        try {
            Response response = new GetCommunityGoalRequest().getResponse();
            if (response.getResponseCode() == HttpURLConnection.HTTP_OK) {
                JSONObject json = response.getResponseMessage();
                communityGoal = new CommunityGoal(json);
            }
        } catch (IOException ignored) {}
    }
}
