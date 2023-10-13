package net.leaderos.shared.module.auth;

import net.leaderos.shared.Shared;
import net.leaderos.shared.helpers.MDChat.MDChatAPI;
import net.leaderos.shared.model.request.PostRequest;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Auth module helper class, share methods over platforms
 * @author poyrazinan
 * @since 1.0
 */
public class AuthHelper {

    /**
     * Generates user login link
     *
     * @param username of player
     * @param uuid of player
     * @return String of url
     * @throws IOException request exception
     */
    private static String generateLink(String username, String uuid) throws IOException {
        Map<String, String> formData = new HashMap<>();
        formData.put("username", username);
        formData.put("uuid", uuid);
        PostRequest postRequest = new PostRequest("auth/generate-link", formData);
        JSONObject response = postRequest.getResponse().getResponseMessage().getJSONObject("data");
        return response.getString("url");
    }

    /**
     * sends auth command message
     * @param player executor
     */
    public static String getAuthLink(Player player) {
        try {
            String link = generateLink(player.getName(), player.getUniqueId().toString());
            return link;
        } catch (Exception e) {
            return null;
        }
    }
}
