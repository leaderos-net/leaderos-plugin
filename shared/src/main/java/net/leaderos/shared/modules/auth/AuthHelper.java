package net.leaderos.shared.modules.auth;

import net.leaderos.shared.model.request.impl.auth.AuthRequest;
import org.json.JSONObject;

import java.io.IOException;
import java.util.UUID;

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
        AuthRequest authRequest = new AuthRequest(username, uuid);
        JSONObject response = authRequest.getResponse().getResponseMessage().getJSONObject("data");
        return response.getString("url");
    }

    /**
     * sends auth command message
     * @param playerName name of executor
     * @param playerUUID uuid of executor
     * @return String of url
     */
    public static String getAuthLink(String playerName, UUID playerUUID) {
        try {
            return generateLink(playerName, playerUUID.toString());
        } catch (Exception e) {
            return null;
        }
    }
}
