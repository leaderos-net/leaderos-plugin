package net.leaderos.plugin.bukkit.modules.bazaar.model;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.leaderos.plugin.Main;
import net.leaderos.plugin.bukkit.helpers.ChatUtil;
import net.leaderos.plugin.bukkit.helpers.ItemUtils;
import net.leaderos.plugin.bukkit.modules.bazaar.Bazaar;
import net.leaderos.plugin.shared.model.request.GetRequest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.JSONObject;

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
     * PlayerBazaar constructor
     * @param response of request
     */
    @SneakyThrows
    public PlayerBazaar(JSONObject response) {
        this.id = response.getInt("id");
        this.base64 = response.getString("base64");
    }

    /**
     * Gets item of bazaar display
     * @return
     */
    public ItemStack getItem() {
        ItemStack item = ItemUtils.fromBase64(getBase64());
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        lore.add(ChatUtil.color(Main.getInstance().getLangFile().getGui().getBazaarGui().getClickLore()));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * loads player bazaar items
     */
    @SneakyThrows
    public static List<PlayerBazaar> getBazaarStorage(String userId) {
        int serverId = Bazaar.getServerId();
        GetRequest getRequest = new GetRequest("/bazaar/storages/" + userId + "/items/" + serverId);
        JSONObject response = getRequest.getResponse();
        List<PlayerBazaar> playerBazaarList = new ArrayList<>();
        playerBazaarList.add(new PlayerBazaar(response));
        return playerBazaarList;
    }
}
