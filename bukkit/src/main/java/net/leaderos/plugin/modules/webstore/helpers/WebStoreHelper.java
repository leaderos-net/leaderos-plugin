package net.leaderos.plugin.modules.webstore.helpers;

import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.api.handlers.UpdateCacheEvent;
import net.leaderos.plugin.api.managers.ModuleManager;
import net.leaderos.plugin.helpers.ChatUtil;
import net.leaderos.plugin.helpers.MDChat.MDChatAPI;
import net.leaderos.plugin.modules.cache.model.User;
import net.leaderos.plugin.modules.webstore.model.Product;
import net.leaderos.shared.error.Error;
import net.leaderos.shared.helpers.RequestUtil;
import net.leaderos.shared.model.Response;
import net.leaderos.shared.model.request.impl.store.BuyRequest;
import net.leaderos.shared.modules.auth.AuthHelper;
import net.leaderos.shared.modules.credit.enums.UpdateType;
import net.leaderos.plugin.modules.webstore.model.Category;
import org.bukkit.entity.Player;
import org.json.JSONArray;
import org.json.JSONObject;
import io.github.projectunified.minelib.scheduler.async.AsyncScheduler;
import io.github.projectunified.minelib.scheduler.common.task.Task;
import io.github.projectunified.minelib.scheduler.global.GlobalScheduler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

public class WebStoreHelper {

    private static Bukkit instance;

    public static void buyItem(Player player, Product product) {
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
                AsyncScheduler.get(instance).run(() -> {
                    // Buy progress
                    try {
                        Response buyRequest = new BuyRequest(user.getId(), product.getProductId()).getResponse();
                        if (buyRequest.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            JSONObject responseData = buyRequest.getResponseMessage().getJSONObject("data");

                            // Update Stock
                            JSONArray products = responseData.getJSONArray("products");
                            for (int i = 0; i < products.length(); i++) {
                                JSONObject productData = products.getJSONObject(i);
                                if (productData.getString("id").equals(product.getProductId())) {
                                    product.setStock(productData.getInt("stock"));
                                    break;
                                }
                            }

                            // Calls UpdateCache event for update player's cache
                            double credits = responseData.getDouble("credits");
                            GlobalScheduler.get(instance).run(() -> org.bukkit.Bukkit.getPluginManager().callEvent(new UpdateCacheEvent(player.getName(), credits, UpdateType.SET)));
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
                    } catch (IOException e) {
                        player.sendTitle(title, subtitleError);
                    }
                    RequestUtil.invalidate(player.getUniqueId());
                });
            }
            else {
                AsyncScheduler.get(instance).run(() -> {
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

    public static Product findProductById(String productId) {
        List<Category> categories = Category.getCategories();

        for (Category category : categories) {
            Product foundProduct = findProductByIdRecursive(productId, category);
            if (foundProduct != null) {
                return foundProduct;
            }
        }
        return null;
    }

    private static Product findProductByIdRecursive(String productId, Category category) {
        // Check if the product is in this category
        for (Product product : category.getProductList()) {
            if (product.getProductId().equalsIgnoreCase(productId)) {
                return product;
            }
        }

        // Check if the product is in any sub-category
        for (Category subCategory : category.getSubCategories()) {
            Product foundProduct = findProductByIdRecursive(productId, subCategory);
            if (foundProduct != null) {
                return foundProduct;
            }
        }

        // Product not found
        return null;
    }
}
