package net.leaderos.plugin.modules.webstore.helpers;

import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.api.handlers.UpdateCacheEvent;
import net.leaderos.plugin.api.managers.ModuleManager;
import net.leaderos.plugin.helpers.ChatUtil;
import net.leaderos.plugin.helpers.MDChat.MDChatAPI;
import net.leaderos.plugin.modules.cache.model.User;
import net.leaderos.shared.helpers.RequestUtil;
import net.leaderos.shared.model.Response;
import net.leaderos.shared.model.request.impl.store.BuyRequest;
import net.leaderos.shared.modules.auth.AuthHelper;
import net.leaderos.shared.modules.credit.enums.UpdateType;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.net.HttpURLConnection;

public class WebStoreHelper {
    public static void buyItem(Player player, String productId) {
        if (RequestUtil.canRequest(player.getUniqueId())) {

            RequestUtil.addRequest(player.getUniqueId());

            if (User.isPlayerAuthed(player)) {
                User user = User.getUser(player.getName());
                // Titles
                String title = ChatUtil.color(Bukkit.getInstance().getLangFile().getGui().getWebStoreGui().getBuyWebStoreTitle());
                String subtitleError = ChatUtil.color(Bukkit.getInstance().getLangFile().getGui().getWebStoreGui().getBuyWebStoreError());
                String subtitleProgress = ChatUtil.color(Bukkit.getInstance().getLangFile().getGui().getWebStoreGui().getBuyWebStoreProgress());
                String subtitleSuccess = ChatUtil.color(Bukkit.getInstance().getLangFile().getGui().getWebStoreGui().getBuyWebStoreSuccess());
                String subtitleNotEnoughCredit = ChatUtil.color(Bukkit.getInstance().getLangFile().getGui().getWebStoreGui().getBuyWebStoreNotEnoughCredit());
                player.sendTitle(title, subtitleProgress);
                org.bukkit.Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getInstance(), () -> {
                    // Buy progress
                    try {
                        Response buyRequest = new BuyRequest(user.getId(), productId).getResponse();
                        if (buyRequest.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            // Calls UpdateCache event for update player's cache
                            double credits = buyRequest.getResponseMessage().getJSONObject("data").getDouble("credits");
                            org.bukkit.Bukkit.getPluginManager().callEvent(new UpdateCacheEvent(player.getName(), credits, UpdateType.SET));
                            player.sendTitle(title, subtitleSuccess);
                        }
                        else if (buyRequest.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST)
                            player.sendTitle(title, subtitleNotEnoughCredit);
                        else throw new IOException();
                    } catch (IOException e) {
                        player.sendTitle(title, subtitleError);
                    }
                    RequestUtil.invalidate(player.getUniqueId());
                });
            }
            else {
                org.bukkit.Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getInstance(), () -> {
                    // If auth login is enabled
                    if (ModuleManager.getModuleStatus("Auth")) {
                        String authLink = AuthHelper.getAuthLink(player.getName(), player.getUniqueId());
                        if (authLink != null)
                            player.spigot().sendMessage(
                                    MDChatAPI.getFormattedMessage(
                                            ChatUtil.color(Bukkit.getInstance().getLangFile().getMessages().getAuth().getModuleError()
                                                    .replace("%link%", authLink)
                                                    .replace("{prefix}", Bukkit.getInstance().getLangFile().getMessages().getPrefix()))));
                    }
                    // If cache not found and Auth is disabled situation
                    else
                        ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getGui().getWebStoreGui().getBuyWebStoreNoAuthLinkError());

                    RequestUtil.invalidate(player.getUniqueId());
                });
            }
        } else {
            ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getHaveRequestOngoing());
        }
    }
}
