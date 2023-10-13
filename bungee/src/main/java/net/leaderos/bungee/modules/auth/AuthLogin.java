package net.leaderos.bungee.modules.auth;

import net.leaderos.bungee.Bungee;
import net.leaderos.bungee.helper.ChatUtil;
import net.leaderos.bungee.modules.auth.commands.AuthCommand;
import net.leaderos.shared.module.LeaderOSModule;
import net.leaderos.shared.module.auth.AuthHelper;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
    private AuthCommand command;

    /**
     * onEnable method of module
     */
    public void onEnable() {
        this.command = new AuthCommand("auth");
        Bungee.getInstance().getProxy().getPluginManager().registerCommand(Bungee.getInstance(), command);
    }

    /**
     * onDisable method of module
     */
    public void onDisable() {
        Bungee.getInstance().getProxy().getPluginManager().unregisterCommand(command);
    }

    /**
     * sends auth command message
     *
     * @param player executor
     */
    public static void sendAuthCommandMessage(ProxiedPlayer player) {
        try {
            String link = AuthHelper.getAuthLink(player.getName(), player.getUniqueId());
            if (link != null)
                player.sendMessage(new TextComponent(TextComponent.fromLegacyText(ChatUtil.color(Bungee.getInstance()
                        .getLangFile().getMessages()
                        .getAuth().getCommandMessage().replace("%link%", link)))));
            else
                ChatUtil.sendMessage(player, Bungee.getInstance().getLangFile().getMessages().getAuth().getNoLink());
        } catch (Exception ignored) {
            ChatUtil.sendMessage(player, Bungee.getInstance().getLangFile().getMessages().getAuth().getNoLink());
        }
    }

    /**
     * sends auth module error message
     *
     * @param player executor
     */
    public static void sendAuthModuleError(ProxiedPlayer player) {
        try {
            String link = AuthHelper.getAuthLink(player.getName(), player.getUniqueId());
            if (link != null)
                player.sendMessage(new TextComponent(TextComponent.fromLegacyText(ChatUtil.color(Bungee.getInstance()
                        .getLangFile().getMessages()
                        .getAuth().getModuleError().replace("%link%", link)))));
            else
                ChatUtil.sendMessage(player, Bungee.getInstance().getLangFile().getMessages().getAuth().getNoLink());
        } catch (Exception ignored) {
            ChatUtil.sendMessage(player, Bungee.getInstance().getLangFile().getMessages().getAuth().getNoLink());
        }
    }

    /**
     * Constructor of Auth
     */
    public AuthLogin() {
    }
}
