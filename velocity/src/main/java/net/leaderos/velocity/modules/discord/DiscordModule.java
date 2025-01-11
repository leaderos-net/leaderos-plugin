package net.leaderos.velocity.modules.discord;

import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.leaderos.shared.Shared;
import net.leaderos.shared.modules.LeaderOSModule;
import net.leaderos.velocity.Velocity;
import net.leaderos.velocity.helpers.ChatUtil;
import net.leaderos.velocity.modules.discord.commands.SyncCommand;

/**
 * Discord module of leaderos-plugin
 *
 * @author rafaelflromao
 * @since 1.0
 */
public class DiscordModule extends LeaderOSModule {

    /**
     * Command meta of module
     */
    private CommandMeta commandMeta;

    /**
     * Command object of module
     */
    private SimpleCommand discordSyncCommand;

    /**
     * onEnable method of module
     */
    public void onEnable() {
        commandMeta = Velocity.getInstance().getCommandManager().metaBuilder("discord-sync")
                .aliases("discord-link")
                .plugin(Velocity.getInstance())
                .build();
        discordSyncCommand = new SyncCommand();
        Velocity.getInstance().getCommandManager().register(commandMeta, discordSyncCommand);
    }

    /**
     * onDisable method of module
     */
    public void onDisable() {
        Velocity.getInstance().getCommandManager().unregister(commandMeta);
    }

    /**
     * sends sync command message
     *
     * @param player executor
     */
    public static void sendSyncCommandMessage(Player player) {
        try {
            String link = Shared.getLink() + "/discord/link";
            // Hover message
            Component hoverMsg = ChatUtil.color(Velocity.getInstance().getLangFile().getMessages().getDiscord().getHoverMessage());
            // Main message of auth
            Component commandMessage = ChatUtil.color(
                    Velocity.getInstance().getLangFile().getMessages().getDiscord().getCommandMessage()
                            .replace("{prefix}", Velocity.getInstance().getLangFile().getMessages().getPrefix()));
            // Finalized msg
            Component component = commandMessage
                    .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, link))
                    .hoverEvent(HoverEvent.showText(hoverMsg));
            player.sendMessage(component);
        } catch (Exception ignored) {
            ChatUtil.sendMessage(player, Velocity.getInstance().getLangFile().getMessages().getDiscord().getNoLink());
        }
    }

    /**
     * Constructor of Discord
     */
    public DiscordModule() {
    }
}
