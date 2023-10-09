package net.leaderos.plugin.bukkit.modules.auth;

import net.leaderos.plugin.Main;
import net.leaderos.plugin.bukkit.exceptions.RequestException;
import net.leaderos.plugin.bukkit.helpers.ChatUtil;
import net.leaderos.plugin.bukkit.helpers.MDChat.MDChatAPI;
import net.leaderos.plugin.bukkit.model.User;
import net.leaderos.plugin.shared.model.request.PostRequest;
import net.leaderos.plugin.shared.module.LeaderOSModule;
import org.bukkit.entity.Player;

import java.io.IOException;

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
        Main.commandManager.registerCommand(new Commands());
    }

    /**
     * onDisable method of module
     */
    public void onDisable() {
        Main.commandManager.unregisterCommand(new Commands());
    }

    /**
     * sends auth command message
     * @param player
     */
    public static void sendAuthCommandMessage(Player player) {
        try {
            String link = User.generateLink(player.getName(), player.getUniqueId().toString());
            player.spigot().sendMessage(
                    MDChatAPI.getFormattedMessage(ChatUtil.color(Main.getInstance()
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
            String link = User.generateLink(player.getName(), player.getUniqueId().toString());
            player.spigot().sendMessage(
                    MDChatAPI.getFormattedMessage(ChatUtil.color(Main.getInstance()
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
