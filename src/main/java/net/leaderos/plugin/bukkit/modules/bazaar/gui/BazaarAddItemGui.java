package net.leaderos.plugin.bukkit.modules.bazaar.gui;

import com.cryptomorin.xseries.XMaterial;
import de.themoep.inventorygui.*;
import lombok.SneakyThrows;
import net.leaderos.plugin.Main;
import net.leaderos.plugin.bukkit.helpers.ChatUtil;
import net.leaderos.plugin.bukkit.helpers.ItemUtils;
import net.leaderos.plugin.bukkit.modules.bazaar.Bazaar;
import net.leaderos.plugin.shared.model.request.PostRequest;
import net.leaderos.plugin.shared.module.auth.model.User;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author poyrazinan
 * @since 1.0
 */
public class BazaarAddItemGui {

    /**
     * Constructor of gui
     */
    public BazaarAddItemGui() {}


    /**
     * Opens gui to player
     * @param player to show gui
     */
    @SneakyThrows
    public static void showGui(Player player) {
        // Gui template as array
        String[] layout = Main.getInstance().getLangFile().getGui().getBazaarGui().getAddItemLayout().toArray(new String[0]);
        // Inventory object
        String guiName = ChatUtil.color(Main.getInstance().getLangFile().getGui().getBazaarGui().getGuiName());
        InventoryGui gui = new InventoryGui(Main.getInstance(), null, guiName, layout);
        // Filler item for empty slots

        // With a virtual inventory to access items later on
        Inventory inv = Bukkit.createInventory(null, InventoryType.CHEST);
        gui.addElement(new GuiStorageElement('i', inv));
        gui.setCloseAction(close -> {

            ItemStack[] items = inv.getContents();
            String userId = User.getUser(player.getName()).getId();
            int serverId = Bazaar.getServerId();

            for (ItemStack item : items) {
                if (item == null)
                    continue;
                if (item.getType() == null)
                    continue;
                if (item.getType().equals(Material.AIR))
                    continue;
                // Item info
                String material = item.getType().name();
                String name = (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) ?
                        item.getItemMeta().getDisplayName() : material.toString();
                String lore = (item.hasItemMeta() && item.getItemMeta().hasLore()) ?
                        String.join("\n", item.getItemMeta().getLore()) : null;
                int amount = item.getAmount();
                int maxDurability = item.getType().getMaxDurability();
                int durability = getDurability(item, maxDurability);
                String base64 = ItemUtils.toBase64(item);
                double price = 0.0;
                String creationDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
                int sold = 0;
                String modelId = getModelId(item);
                String enchantment = getEnchantments(item);

                Map<String, String> body = new HashMap<>();
                body.put("owner", userId);
                body.put("name", name);
                body.put("lore", lore);
                body.put("amount", amount+"");
                body.put("maxDurability", maxDurability+"");
                body.put("durability", durability+"");
                body.put("base64", base64);
                body.put("price", price+"");
                body.put("creationDate", creationDate);
                // TODO Check
                body.put("sold", sold+"");
                body.put("modelID", modelId);
                body.put("enchantment", enchantment);
                body.put("description", null);
                body.put("serverID", serverId+"");
                body.put("itemID", item.getType().name());

                try {
                    PostRequest postItem = new PostRequest("bazaar/storages/" + userId + "/items", body);
                    if (postItem.getResponse().getResponseCode() == HttpURLConnection.HTTP_OK) {
                        // TODO Success
                        BazaarGui.showGui(player);
                    }
                    // TODO Else
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            return false; // Don't go back to the previous GUI (true would automatically go back to the previously opened one)
        });
        gui.show(player);
    }

    /**
     * Gets modelId of item
     * @param itemStack of item
     * @return String of model data
     */
    private static String getModelId(ItemStack itemStack) {
        String modelId = null;
        if (XMaterial.supports(14) && itemStack.hasItemMeta() && itemStack.getItemMeta().hasCustomModelData())
            modelId = itemStack.getItemMeta().getCustomModelData()+"";
        return modelId;
    }

    /**
     * Gets durability of item
     * @param item itemstack
     * @param maxDurability max durability of item
     * @return durability as int
     */
    private static int getDurability(ItemStack item, int maxDurability) {
        int durability = 0;
        if (XMaterial.supports(13))
            durability = maxDurability - ((Damageable) item.getItemMeta()).getDamage();
        else
            durability = maxDurability + 1 - item.getDurability();
        return durability;
    }

    /**
     * Enchantment data to serialize
     * @param itemStack item
     * @return String of serialized data
     */
    private static String getEnchantments(ItemStack itemStack) {
        StringBuilder builder = new StringBuilder();
        Set<Map.Entry<Enchantment, Integer>> entries;
        if (itemStack.getType() == Material.ENCHANTED_BOOK) {
            EnchantmentStorageMeta meta =(EnchantmentStorageMeta)itemStack.getItemMeta();
            entries = meta.getStoredEnchants().entrySet();
        } else {
            entries = itemStack.getEnchantments().entrySet();
        }
        for (Map.Entry<Enchantment, Integer> entry : entries) {
            builder.append((XMaterial.supports(13) ? entry.getKey().getKey().getKey() : entry.getKey().getName()).toUpperCase())
                    .append(':').append(entry.getValue()).append(',');
        }

        String enchantments = builder.length() > 1 ?
                builder.deleteCharAt(builder.lastIndexOf(",")).toString() : null;
        return enchantments;
    }
}
