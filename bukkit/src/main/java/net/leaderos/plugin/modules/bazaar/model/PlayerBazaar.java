package net.leaderos.plugin.modules.bazaar.model;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.leaderos.plugin.Main;
import net.leaderos.plugin.modules.bazaar.Bazaar;
import net.leaderos.shared.helpers.ChatUtil;
import net.leaderos.plugin.helpers.ItemUtils;
import net.leaderos.shared.model.request.DeleteRequest;
import net.leaderos.shared.model.request.GetRequest;
import org.bukkit.Bukkit;
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
     */
    @SneakyThrows
    public PlayerBazaar(JSONObject response, String userId) {
        this.id = response.getInt("id");
        this.userId = userId;
        this.base64 = response.getString("base64");
    }

    /**
     * Gets item of bazaar display
     * @return
     */
    public ItemStack getItem() {
        ItemStack item = ItemUtils.fromBase64(getBase64());
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        if (meta != null && meta.getLore() != null)
            lore = meta.getLore();
        lore.add(ChatUtil.color(Main.getShared().getLangFile().getGui().getBazaarGui().getClickLore()));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * withdraw item from bazaar
     */
    @SneakyThrows
    public boolean withdrawItem(Player player) {
        DeleteRequest deleteRequest = new DeleteRequest("bazaar/storage/" + getUserId() + "/items/" + getId());
        // TODO Remove
        Bukkit.broadcastMessage(deleteRequest.getResponse().getResponseCode() + "");
        if (deleteRequest.getResponse().getResponseCode() == HttpURLConnection.HTTP_OK) {
            ItemStack item = ItemUtils.fromBase64(getBase64());
            player.getInventory().addItem(item);
            return true;
        }
        else
            // TODO Throw exception
            return false;
    }

    /**
     * loads player bazaar items
     */
    public static List<PlayerBazaar> getBazaarStorage(String userId) {
        try {
            int serverId = Bazaar.getServerId();
            GetRequest getRequest = new GetRequest("bazaar/storages/" + userId + "/items?serverID=" + serverId);
            JSONObject response = getRequest.getResponse().getResponseMessage();
            List<PlayerBazaar> playerBazaarList = new ArrayList<>();
            // TODO NO ANY TEST HERE FIXXXXX!!
            response.getJSONArray("array").forEach(bazaar -> playerBazaarList.add(new PlayerBazaar((JSONObject) bazaar, userId)));
            return playerBazaarList;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
