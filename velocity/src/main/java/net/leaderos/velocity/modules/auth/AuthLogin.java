package net.leaderos.velocity.modules.auth;

import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.leaderos.shared.module.LeaderOSModule;
import net.leaderos.shared.module.auth.AuthHelper;
import net.leaderos.velocity.Velocity;
import net.leaderos.velocity.helper.ChatUtil;
import net.leaderos.velocity.modules.auth.commands.AuthCommand;

/**
 * Auth module of leaderos-plugin
 *
 * @author poyrazinan
 * @since 1.0
 */
public class AuthLogin extends LeaderOSModule {

    /**
     * Command meta of module
     */
    private CommandMeta commandMeta;

    /**
     * Command object of module
     */
    private SimpleCommand authCommand;

    /**
     * onEnable method of module
     */
    public void onEnable() {
        commandMeta = Velocity.getInstance().getCommandManager().metaBuilder("auth")
                .aliases("doğrula")
                .plugin(Velocity.getInstance())
                .build();
        authCommand = new AuthCommand();
        Velocity.getInstance().getCommandManager().register(commandMeta, authCommand);
    }

    /**
     * onDisable method of module
     */
    public void onDisable() {
        Velocity.getInstance().getCommandManager().unregister(commandMeta);
    }

    /**
     * sends auth command message
     *
     * @param player executor
     */
    public static void sendAuthCommandMessage(Player player) {
        try {
            String link = AuthHelper.getAuthLink(player.getUsername(), player.getUniqueId());
            if (link != null) {
                TextComponent component = Component.text(
                        Velocity.getInstance().getLangFile().getMessages().getAuth().getCommandMessage()
                                .replace("{prefix}", Velocity.getInstance().getLangFile().getMessages().getPrefix())
                ).clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, link));
                //component = component;
                player.sendMessage(component);
            }
                // TODO Link
                //ChatUtil.sendMessage(player, link);
            else
                ChatUtil.sendMessage(player, Velocity.getInstance().getLangFile().getMessages().getAuth().getNoLink());
        } catch (Exception ignored) {
            ChatUtil.sendMessage(player, Velocity.getInstance().getLangFile().getMessages().getAuth().getNoLink());
        }
    }

    /**
     * sends auth module error message
     *
     * @param player executor
     */
    public static void sendAuthModuleError(Player player) {
        try {
            String link = AuthHelper.getAuthLink(player.getUsername(), player.getUniqueId());
            if (link != null)
                // TODO Link
                ChatUtil.sendMessage(player, link);
            else
                ChatUtil.sendMessage(player, Velocity.getInstance().getLangFile().getMessages().getAuth().getNoLink());
        } catch (Exception ignored) {
            ChatUtil.sendMessage(player, Velocity.getInstance().getLangFile().getMessages().getAuth().getNoLink());
        }
    }

    /**
     * Constructor of Auth
     */
    public AuthLogin() {
    }
}
