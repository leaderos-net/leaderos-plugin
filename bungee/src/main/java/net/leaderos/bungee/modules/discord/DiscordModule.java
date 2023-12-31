package net.leaderos.bungee.modules.discord;

import net.leaderos.bungee.Bungee;
import net.leaderos.bungee.helpers.ChatUtil;
import net.leaderos.bungee.helpers.MDChat.MDChatAPI;
import net.leaderos.bungee.modules.discord.commands.SyncCommand;
import net.leaderos.shared.modules.LeaderOSModule;
import net.leaderos.shared.modules.discord.DiscordHelper;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * Discord module of leaderos-plugin
 *
 * @author rafaelflromao
 * @since 1.0
 */
public class DiscordModule extends LeaderOSModule {

    /**
     * Commands of module
     */
    private SyncCommand command;

    /**
     * onEnable method of module
     */
    public void onEnable() {
        this.command = new SyncCommand("discord-sync");
        Bungee.getInstance().getProxy().getPluginManager().registerCommand(Bungee.getInstance(), command);
    }

    /**
     * onDisable method of module
     */
    public void onDisable() {
        Bungee.getInstance().getProxy().getPluginManager().unregisterCommand(command);
    }

    /**
     * sends discord command message
     *
     * @param player executor
     */
    public static void sendSyncCommandMessage(ProxiedPlayer player) {
        try {
            String link = DiscordHelper.getSyncLink(player.getName());
            if (link != null)
                player.sendMessage(MDChatAPI.getFormattedMessage(ChatUtil.color(Bungee.getInstance()
                        .getLangFile().getMessages()
                        .getAuth().getCommandMessage()
                        .replace("%link%", link)
                        .replace("{prefix}", Bungee.getInstance().getLangFile().getMessages().getPrefix()))));
            else
                ChatUtil.sendMessage(player, Bungee.getInstance().getLangFile().getMessages().getDiscord().getNoLink());
        } catch (Exception ignored) {
            ChatUtil.sendMessage(player, Bungee.getInstance().getLangFile().getMessages().getDiscord().getNoLink());
        }
    }

    /**
     * Constructor of Auth
     */
    public DiscordModule() {
    }
}
