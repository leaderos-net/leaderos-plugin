package net.leaderos.plugin.modules.bazaar.model;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.helpers.ChatUtil;
import net.leaderos.plugin.helpers.ItemUtil;
import net.leaderos.plugin.modules.bazaar.BazaarModule;
import net.leaderos.shared.error.Error;
import net.leaderos.shared.model.Response;
import net.leaderos.shared.model.request.GetRequest;
import net.leaderos.shared.model.request.impl.bazaar.GetBazaarItemsRequest;
import net.leaderos.shared.model.request.impl.bazaar.RemoveBazaarItemRequest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * @author poyrazinan
 * @since 1.0
 */
@Getter
@Setter
public class PlayerBazaar {

    /**
     * id of item
     */
    private int id;

    /**
     * base64 item
     */
    private String base64;

    /**
     * Owner
     */
    private String userId;

    /**
     * PlayerBazaar constructor
     * @param response of request
     * @param userId of player
     */
    @SneakyThrows
    public PlayerBazaar(JSONObject response, String userId) {
        this.id = response.getInt("id");
        this.userId = userId;
        this.base64 = response.getString("base64");
    }

    /**
     * Gets item of bazaar display
     * @return ItemStack of bazaar item
     */
    public ItemStack getItem() {
        ItemStack item = ItemUtil.fromBase64(getBase64());
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        if (meta != null && meta.getLore() != null)
            lore = meta.getLore();
        lore.add(ChatUtil.color(Bukkit.getInstance().getLangFile().getGui().getBazaarGui().getClickLore()));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * withdraw item from bazaar
     * @param player clicker
     * @return status of withdraw
     */
    @SneakyThrows
    public Error withdrawItem(Player player, int itemId) {
        Response deleteRequest = new RemoveBazaarItemRequest(getUserId(), itemId).getResponse();
        if (deleteRequest.getResponseCode() == HttpURLConnection.HTTP_OK
                && deleteRequest.getResponseMessage().getBoolean("status")) {
            ItemStack item = ItemUtil.fromBase64(getBase64());
            player.getInventory().addItem(item);
            return null;
        }

        return deleteRequest.getError();
    }

    /**
     * loads player bazaar items
     * @param userId of player
     * @return list of storage
     */
    public static List<PlayerBazaar> getBazaarStorage(String userId) {
        try {
            int serverId = BazaarModule.getServerId();
            GetRequest getRequest = new GetBazaarItemsRequest(userId, String.valueOf(serverId));
            JSONObject response = getRequest.getResponse().getResponseMessage();
            List<PlayerBazaar> playerBazaarList = new ArrayList<>();
            response.getJSONArray("array").forEach(bazaar -> playerBazaarList.add(new PlayerBazaar((JSONObject) bazaar, userId)));
            return playerBazaarList;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
