package net.leaderos.plugin.bukkit.modules.bazaar.model;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.leaderos.plugin.Main;
import net.leaderos.plugin.bukkit.helpers.ChatUtil;
import net.leaderos.plugin.bukkit.helpers.ItemUtils;
import net.leaderos.plugin.bukkit.modules.bazaar.Bazaar;
import net.leaderos.plugin.shared.model.request.DeleteRequest;
import net.leaderos.plugin.shared.model.request.GetRequest;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.PermissionAttachmentInfo;
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
        lore.add(ChatUtil.color(Main.getInstance().getLangFile().getGui().getBazaarGui().getClickLore()));
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


    /**
     * How many item can player store to bazaar.
     *
     * @param player to check player perm
     * @return amount of can add
     */
    public static int getStorageAmount(Player player) {
        String permissionPrefix = "bazaar.maxstorage.";
        int defaultValue =  Main.getInstance().getModulesFile().getBazaar().getDefaultStorageSize();
        try {
            if (player == null)
                return defaultValue;
            else {
                List<Integer> lists = new ArrayList<>();
                for (PermissionAttachmentInfo attachmentInfo : player.getEffectivePermissions()) {
                    if (attachmentInfo.getPermission().startsWith(permissionPrefix))
                        lists.add(Integer.parseInt(attachmentInfo.getPermission().substring(attachmentInfo.getPermission().lastIndexOf(".") +1)));
                }
                if (!lists.isEmpty())
                    defaultValue = lists.stream()
                            .filter(PlayerBazaar::isInteger)
                            .reduce(1, Integer::max);
            }
            return defaultValue;
        }

        catch(Exception e1) {
            return defaultValue;
        }
    }

    /**
     * Checks the input is integer
     *
     * @param input of data
     * @return status of int or not
     */
    private static boolean isInteger(int input) {
        try {
            return input > 0;
        } catch(NumberFormatException exception) {
            return false;
        }
    }
}
