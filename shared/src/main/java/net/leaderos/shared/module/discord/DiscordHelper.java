package net.leaderos.shared.module.discord;

import net.leaderos.shared.model.request.PostRequest;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Auth module helper class, share methods over platforms
 * @author poyrazinan
 * @since 1.0
 */
public class DiscordHelper {

    /**
     * Generates discord sync link
     *
     * @param username of player
     * @return String of url
     * @throws IOException request exception
     */
    private static String generateLink(String username) throws IOException {
        Map<String, String> formData = new HashMap<>();
        formData.put("user", username);
        PostRequest postRequest = new PostRequest("integrations/discord/sync", formData);
        JSONObject response = postRequest.getResponse().getResponseMessage().getJSONObject("data");
        return response.getString("url");
    }

    /**
     * Generates discord sync link
     * @param username username of executor
     */
    public static String getSyncLink(String username) {
        try {
            return generateLink(username);
        } catch (Exception e) {
            return null;
        }
    }
}
