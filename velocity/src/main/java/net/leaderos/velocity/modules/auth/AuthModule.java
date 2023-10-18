package net.leaderos.velocity.modules.auth;

import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
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
public class AuthModule extends LeaderOSModule {

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
                // Hover message
                Component hoverMsg = ChatUtil.color(Velocity.getInstance().getLangFile().getMessages().getAuth().getHoverMessage());
                // Main message of auth
                Component commandMessage = ChatUtil.color(
                        Velocity.getInstance().getLangFile().getMessages().getAuth().getCommandMessage()
                                .replace("{prefix}", Velocity.getInstance().getLangFile().getMessages().getPrefix()));
                // Finalized msg
                Component component = commandMessage
                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, link))
                        .hoverEvent(HoverEvent.showText(hoverMsg));
                player.sendMessage(component);
            }
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
            if (link != null) {
                // Hover message
                Component hoverMsg = ChatUtil.color(Velocity.getInstance().getLangFile().getMessages().getAuth().getModuleError());
                // Main message of auth
                Component commandMessage = ChatUtil.color(
                        Velocity.getInstance().getLangFile().getMessages().getAuth().getCommandMessage()
                                .replace("{prefix}", Velocity.getInstance().getLangFile().getMessages().getPrefix()));
                // Finalized msg
                Component component = commandMessage
                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, link))
                        .hoverEvent(HoverEvent.showText(hoverMsg));
                player.sendMessage(component);
            }
            else
                ChatUtil.sendMessage(player, Velocity.getInstance().getLangFile().getMessages().getAuth().getNoLink());
        } catch (Exception ignored) {
            ChatUtil.sendMessage(player, Velocity.getInstance().getLangFile().getMessages().getAuth().getNoLink());
        }
    }

    /**
     * Constructor of Auth
     */
    public AuthModule() {
    }
}
