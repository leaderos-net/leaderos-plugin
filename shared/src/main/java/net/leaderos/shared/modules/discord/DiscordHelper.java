package net.leaderos.shared.modules.discord;

import net.leaderos.shared.model.request.PostRequest;
import net.leaderos.shared.model.request.impl.discord.DiscordSyncRequest;
import org.json.JSONObject;

import java.io.IOException;

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
        PostRequest postRequest = new DiscordSyncRequest(username);
        JSONObject response = postRequest.getResponse().getResponseMessage().getJSONObject("data");
        return response.getString("url");
    }

    /**
     * Generates discord sync link
     * @param username username of executor
     * @return String of url
     */
    public static String getSyncLink(String username) {
        try {
            return generateLink(username);
        } catch (Exception e) {
            return null;
        }
    }
}
