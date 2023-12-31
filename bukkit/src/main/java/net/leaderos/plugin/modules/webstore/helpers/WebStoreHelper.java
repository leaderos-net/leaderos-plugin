package net.leaderos.plugin.modules.webstore.helpers;

import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.api.handlers.UpdateCacheEvent;
import net.leaderos.plugin.api.managers.ModuleManager;
import net.leaderos.plugin.helpers.ChatUtil;
import net.leaderos.plugin.helpers.MDChat.MDChatAPI;
import net.leaderos.plugin.modules.cache.model.User;
import net.leaderos.plugin.modules.webstore.model.Product;
import net.leaderos.shared.model.Response;
import net.leaderos.shared.model.request.PostRequest;
import net.leaderos.shared.modules.auth.AuthHelper;
import net.leaderos.shared.modules.credit.enums.UpdateType;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class WebStoreHelper {
    public static void buyItem(Player player, String productId) {
        if (User.isPlayerAuthed(player)) {
            User user = User.getUser(player.getName());
            Map<String, String> body = new HashMap<>();
            body.put("user", user.getId());
            body.put("products[0][id]", productId);
            body.put("products[0][quantity]", "1");
            // Titles
            String title = ChatUtil.color(Bukkit.getInstance().getLangFile().getGui().getWebStoreGui().getBuyWebStoreTitle());
            String subtitleError = ChatUtil.color(Bukkit.getInstance().getLangFile().getGui().getWebStoreGui().getBuyWebStoreError());
            String subtitleProgress = ChatUtil.color(Bukkit.getInstance().getLangFile().getGui().getWebStoreGui().getBuyWebStoreProgress());
            String subtitleSuccess = ChatUtil.color(Bukkit.getInstance().getLangFile().getGui().getWebStoreGui().getBuyWebStoreSuccess());
            String subtitleNotEnoughCredit = ChatUtil.color(Bukkit.getInstance().getLangFile().getGui().getWebStoreGui().getBuyWebStoreNotEnoughCredit());
            player.sendTitle(title, subtitleProgress);
            // Buy progress
            try {
                Response buyRequest = new PostRequest("store/buy", body).getResponse();
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
        }
        else {
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
        }
    }
}
