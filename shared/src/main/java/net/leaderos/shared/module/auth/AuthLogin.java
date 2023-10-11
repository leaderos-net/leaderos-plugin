package net.leaderos.shared.module.auth;

import net.leaderos.shared.Shared;
import net.leaderos.shared.helpers.ChatUtil;
import net.leaderos.shared.helpers.MDChat.MDChatAPI;
import net.leaderos.shared.model.request.PostRequest;
import net.leaderos.shared.module.LeaderOSModule;
import net.leaderos.shared.exceptions.RequestException;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Auth module of leaderos-plugin
 *
 * @author poyrazinan
 * @since 1.0
 */
public class AuthLogin extends LeaderOSModule {

    /**
     * onEnable method of module
     */
    public void onEnable() {
        // TODO Command register
        //Shared.getInstance().getCommandManager().registerCommand(new Commands());
    }

    /**
     * onDisable method of module
     */
    public void onDisable() {
        // TODO Command register
       // Shared.getInstance().getCommandManager().unregisterCommand(new Commands());
    }


    /**
     * Generates user login link
     *
     * @param username of player
     * @param uuid of player
     * @return String of url
     * @throws IOException
     * @throws RequestException
     */
    public static String generateLink(String username, String uuid) throws IOException {
        Map<String, String> formData = new HashMap<>();
        formData.put("username", username);
        formData.put("uuid", uuid);
        PostRequest postRequest = new PostRequest("auth/generate-link", formData);
        JSONObject response = postRequest.getResponse().getResponseMessage().getJSONObject("data");
        return response.getString("url");
    }

    /**
     * sends auth command message
     * @param player
     */
    public static void sendAuthCommandMessage(Player player) {
        try {
            String link = generateLink(player.getName(), player.getUniqueId().toString());
            player.spigot().sendMessage(
                    MDChatAPI.getFormattedMessage(ChatUtil.color(Shared.getInstance()
                            .getLangFile().getMessages()
                            .getAuth().getCommandMessage().replace("%link%", link))));
        } catch (Exception e) {
            e.printStackTrace();
            // TODO Exception
        }
    }

    /**
     * sends auth module error message
     * @param player
     */
    public static void sendAuthModuleError(Player player) {
        try {
            String link = generateLink(player.getName(), player.getUniqueId().toString());
            player.spigot().sendMessage(
                    MDChatAPI.getFormattedMessage(ChatUtil.color(Shared.getInstance()
                            .getLangFile().getMessages()
                            .getAuth().getModuleError().replace("%link%", link))));
        } catch (Exception e) {
            e.printStackTrace();
            // TODO Exception
        }
    }

    /**
     * Constructor of Auth
     */
    public AuthLogin() {}
}
