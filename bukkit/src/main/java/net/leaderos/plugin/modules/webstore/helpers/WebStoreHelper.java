package net.leaderos.plugin.modules.webstore.helpers;

import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.api.handlers.UpdateCacheEvent;
import net.leaderos.plugin.api.managers.ModuleManager;
import net.leaderos.plugin.helpers.ChatUtil;
import net.leaderos.plugin.helpers.MDChat.MDChatAPI;
import net.leaderos.plugin.modules.cache.model.User;
import net.leaderos.shared.Shared;
import net.leaderos.shared.error.Error;
import net.leaderos.shared.helpers.RequestUtil;
import net.leaderos.shared.model.Response;
import net.leaderos.shared.model.request.GetRequest;
import net.leaderos.shared.model.request.impl.store.BuyRequest;
import net.leaderos.shared.model.request.impl.store.ListingRequest;
import net.leaderos.shared.modules.credit.enums.UpdateType;
import net.leaderos.plugin.modules.webstore.model.Category;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

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
                Bukkit.getFoliaLib().getScheduler().runAsync((task) -> {
                    // Buy progress
                    try {
                        if (user == null) {
                            ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getGui().getWebStoreGui().getBuyWebStoreUserNotFound());
                            RequestUtil.invalidate(player.getUniqueId());
                            return;
                        }

                        Response buyRequest = new BuyRequest(user.getId(), productId).getResponse();
                        if (buyRequest.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            JSONObject responseData = buyRequest.getResponseMessage().getJSONObject("data");

                            // Calls UpdateCache event for update player's cache
                            double credits = responseData.getDouble("credits");
                            Bukkit.getFoliaLib().getScheduler().runNextTick((event_task) -> org.bukkit.Bukkit.getPluginManager().callEvent(new UpdateCacheEvent(player.getName(), credits, UpdateType.SET)));
                            player.sendTitle(title, subtitleSuccess);
                        }
                        else if (buyRequest.getError() == Error.INVALID_QUANTITY
                                || buyRequest.getError() == Error.INSUFFICIENT_BALANCE)
                            player.sendTitle(title, subtitleNotEnoughCredit);
                        else if (buyRequest.getError() == Error.OUT_OF_STOCK)
                            player.sendTitle(title, ChatUtil.color(Bukkit.getInstance().getLangFile().getGui().getWebStoreGui().getBuyWebStoreOutOfStock()));
                        else if (buyRequest.getError() == Error.USER_NOT_FOUND)
                            player.sendTitle(title, ChatUtil.color(Bukkit.getInstance().getLangFile().getGui().getWebStoreGui().getBuyWebStoreUserNotFound()));
                        else if (buyRequest.getError() == Error.PRODUCT_NOT_FOUND)
                            player.sendTitle(title, ChatUtil.color(Bukkit.getInstance().getLangFile().getGui().getWebStoreGui().getBuyWebStoreProductNotFound()));
                        else if (buyRequest.getError() == Error.REQUIRED_PRODUCT)
                            player.sendTitle(title, ChatUtil.color(Bukkit.getInstance().getLangFile().getGui().getWebStoreGui().getBuyWebStoreRequiredProduct()));
                        else if (buyRequest.getError() == Error.REQUIRED_LINKED_ACCOUNT)
                            player.sendTitle(title, ChatUtil.color(Bukkit.getInstance().getLangFile().getGui().getWebStoreGui().getBuyWebStoreRequiredLinkedAccount()));
                        else if (buyRequest.getError() == Error.DOWNGRADE_NOT_ALLOWED)
                            player.sendTitle(title, ChatUtil.color(Bukkit.getInstance().getLangFile().getGui().getWebStoreGui().getBuyWebStoreDowngradeNotAllowed()));
                        else if (buyRequest.getError() == Error.INVALID_VARIABLE)
                            player.sendTitle(title, ChatUtil.color(Bukkit.getInstance().getLangFile().getGui().getWebStoreGui().getBuyWebStoreDowngradeNotAllowed()));
                    } catch (IOException e) {
                        player.sendTitle(title, subtitleError);
                    }
                    RequestUtil.invalidate(player.getUniqueId());
                });
            }
            else {
                Bukkit.getFoliaLib().getScheduler().runAsync((task) -> {
                    ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getRegistrationRequired());

                    RequestUtil.invalidate(player.getUniqueId());
                });
            }
        } else {
            ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getHaveRequestOngoing());
        }
    }

    public static List<Category> getCategories(String username) {
        List<Category> categories = new ArrayList<>();
        try {
            GetRequest getRequest = new ListingRequest(username);
            JSONObject response = getRequest.getResponse().getResponseMessage();
            response.getJSONArray("categories").forEach(jsonObj -> {
                Category category = new Category((JSONObject) jsonObj);
                if (category.isStatus()) {
                    categories.add(category);
                }
            });
        } catch (Exception e) {
            Shared.getDebugAPI().send(e.getMessage(), true);
        }

        return categories;
    }

    public static Category findCategoryById(String username, String categoryId) {
        List<Category> categories = getCategories(username);

        for (Category category : categories) {
            Category foundCategory = findCategoryByIdRecursive(categoryId, category);
            if (foundCategory != null) {
                return foundCategory;
            }
        }
        return null;
    }

    private static Category findCategoryByIdRecursive(String categoryId, Category category) {
        // Check if the category is this category
        if (category.getCategoryId().equalsIgnoreCase(categoryId)) {
            return category;
        }

        // Check if the category is in any sub-category
        for (Category subCategory : category.getSubCategories()) {
            Category foundCategory = findCategoryByIdRecursive(categoryId, subCategory);
            if (foundCategory != null) {
                return foundCategory;
            }
        }

        // Category not found
        return null;
    }
}
