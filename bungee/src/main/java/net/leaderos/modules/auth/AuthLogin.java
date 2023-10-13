package net.leaderos.modules.auth;

import net.leaderos.Bungee;
import net.leaderos.modules.auth.commands.Commands;
import net.leaderos.shared.model.request.PostRequest;
import net.leaderos.shared.module.LeaderOSModule;
import net.md_5.bungee.api.connection.ProxiedPlayer;
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
     * Commands of module
     */
    private Commands commands;

    /**
     * onEnable method of module
     */
    public void onEnable() {
        this.commands = new Commands("auth");
        Bungee.getInstance().getProxy().getPluginManager().registerCommand(Bungee.getInstance(), commands);
    }

    /**
     * onDisable method of module
     */
    public void onDisable() {
        Bungee.getInstance().getProxy().getPluginManager().unregisterCommand(commands);
    }


    /**
     * Generates user login link
     *
     * @param username of player
     * @param uuid     of player
     * @return String of url
     * @throws IOException request exception
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
     *
     * @param player executor
     */
    public static void sendAuthCommandMessage(ProxiedPlayer player) {
        try {
            String link = generateLink(player.getName(), player.getUniqueId().toString());
            player.sendMessage(link);
            /* TODO
            player.sendMessage(
                    MDChatAPI.getFormattedMessage(ChatUtil.color(Shared.getInstance()
                            .getLangFile().getMessages()
                            .getAuth().getCommandMessage().replace("%link%", link))));

             */
        } catch (Exception e) {
            e.printStackTrace();
            // TODO Exception
        }
    }

    /**
     * sends auth module error message
     *
     * @param player executor
     */
    public static void sendAuthModuleError(ProxiedPlayer player) {
        try {
            String link = generateLink(player.getName(), player.getUniqueId().toString());
            player.sendMessage(link);
             /* TODO
            player.sendMessage(
                    MDChatAPI.getFormattedMessage(ChatUtil.color(Shared.getInstance()
                            .getLangFile().getMessages()
                            .getAuth().getModuleError().replace("%link%", link))));

              */
        } catch (Exception e) {
            e.printStackTrace();
            // TODO Exception
        }
    }

    /**
     * Constructor of Auth
     */
    public AuthLogin() {
    }
}
