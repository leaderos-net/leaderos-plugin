package net.leaderos.plugin.modules.bazaar.gui;

import de.themoep.inventorygui.*;
import lombok.SneakyThrows;
import net.leaderos.plugin.Main;
import net.leaderos.plugin.helpers.GuiHelper;
import net.leaderos.plugin.modules.bazaar.model.PlayerBazaar;
import net.leaderos.plugin.modules.cache.model.User;
import net.leaderos.plugin.modules.bazaar.Bazaar;
import net.leaderos.shared.helpers.ChatUtil;
import net.leaderos.shared.helpers.GameUtils;
import net.leaderos.plugin.helpers.ItemUtils;
import net.leaderos.shared.model.request.PostRequest;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

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
        String[] layout = Main.getShared().getLangFile().getGui().getBazaarGui().getAddItemLayout().toArray(new String[0]);
        // Inventory object
        String guiName = ChatUtil.color(Main.getShared().getLangFile().getGui().getBazaarGui().getGuiName());
        InventoryGui gui = new InventoryGui(Main.getInstance(), null, guiName, layout);
        // Filler item for empty slots
        ItemStack fillerItem = GuiHelper.getFiller();
        gui.setFiller(fillerItem);

        // With a virtual inventory to access items later on
        Inventory inv = Bukkit.createInventory(null, InventoryType.CHEST);
        gui.addElement(new GuiStorageElement('i', inv));
        // Close action area (event)
        gui.setCloseAction(close -> {
            // Calculating storage amounts
            int maxStorageAmount = GameUtils.getAmountFromPerm(player,
                    "bazaar.maxstorage.",
                    Main.getShared().getModulesFile().getBazaar().getDefaultStorageSize());

            int storedItemAmount = PlayerBazaar.getBazaarStorage(User.getUser(player.getName()).getId()).size();
            int canStoreAmount = maxStorageAmount - storedItemAmount;
            // Items which stored (airs included)
            ItemStack[] items = inv.getContents();
            String userId = User.getUser(player.getName()).getId();
            int serverId = Bazaar.getServerId();

            // If player maxed out storage limit items will be added to
            // this list then gives back to player.
            List<ItemStack> returnItems = new ArrayList<>();

            // item loop
            for (ItemStack item : items) {
                // Checks if item is empty or null (can be AIR etc.)
                if (item == null)
                    continue;
                if (item.getType() == null)
                    continue;
                if (item.getType().equals(Material.AIR))
                    continue;
                // Checks if area is filler item
                if (item.equals(fillerItem))
                    continue;

                // Calculates storage amount
                if (canStoreAmount > 0)
                    canStoreAmount--;
                // If maxed out then add items to temp array
                else {
                    returnItems.add(item);
                    continue;
                }
                // Item info
                String material = item.getType().name();
                String name = (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) ?
                        item.getItemMeta().getDisplayName() : material;
                String lore = (item.hasItemMeta() && item.getItemMeta().hasLore()) ?
                        String.join("\n", item.getItemMeta().getLore()) : null;
                int amount = item.getAmount();
                int maxDurability = item.getType().getMaxDurability();
                int durability = ItemUtils.getDurability(item, maxDurability);
                String base64 = ItemUtils.toBase64(item);
                double price = 0.0;
                String creationDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                String modelId = ItemUtils.getModelId(item);
                String enchantment = ItemUtils.getEnchantments(item);

                Map<String, String> body = new HashMap<>();
                body.put("owner", userId);
                body.put("name", name);
                if (lore != null)
                    body.put("lore", lore);
                body.put("amount", amount+"");
                body.put("maxDurability", maxDurability+"");
                body.put("durability", durability+"");
                body.put("base64", base64);
                body.put("price", price+"");
                body.put("creationDate", creationDate);
                // TODO Check
                body.put("modelID", modelId);
                if (enchantment != null)
                    body.put("enchantment", enchantment);
                body.put("serverID", serverId+"");
                body.put("itemID", item.getType().name());

                // Sends response
                try {
                    PostRequest postItem = new PostRequest("bazaar/storages/" + userId + "/items", body);
                    if (postItem.getResponse().getResponseCode() == HttpURLConnection.HTTP_OK) {
                        // TODO Success
                        BazaarGui.showGui(player);
                    }
                    else throw new Exception();
                    // TODO Else
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            // Gives items back to player
            if (!returnItems.isEmpty()) {
                PlayerInventory playerInventory = player.getInventory();
                returnItems.forEach(playerInventory::addItem);
                String returnMessage = Main.getShared().getLangFile().getGui().getBazaarGui().getReturnItemMessage();
                returnMessage = returnMessage.replace("%max_amount%", maxStorageAmount+"")
                                .replace("%amount%", returnItems.size()+"");
                ChatUtil.sendMessage(player, returnMessage);
            }
            return false; // Don't go back to the previous GUI (true would automatically go back to the previously opened one)
        });
        gui.show(player);
    }
}
