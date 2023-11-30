package net.leaderos.plugin.modules.bazaar.gui;

import com.cryptomorin.xseries.XMaterial;
import de.themoep.inventorygui.*;
import lombok.SneakyThrows;
import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.helpers.ChatUtil;
import net.leaderos.plugin.helpers.GameUtil;
import net.leaderos.plugin.helpers.GuiHelper;
import net.leaderos.plugin.modules.cache.model.User;
import net.leaderos.plugin.modules.bazaar.BazaarModule;
import net.leaderos.plugin.helpers.ItemUtil;
import net.leaderos.shared.helpers.Placeholder;
import net.leaderos.shared.model.Response;
import net.leaderos.shared.model.request.PostRequest;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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
     * @param itemAmount amount of items in storage
     */
    @SneakyThrows
    public static void showGui(Player player, int itemAmount) {
        // Gui template as array
        String[] layout = Bukkit.getInstance().getModulesFile().getBazaar().getGui().getAddItemLayout().toArray(new String[0]);
        // Inventory object
        String guiName = ChatUtil.replacePlaceholders(Bukkit.getInstance().getLangFile().getGui().getBazaarGui().getAddItemGuiName());
        InventoryGui gui = new InventoryGui(Bukkit.getInstance(), null, guiName, layout);
        // Filler item for empty slots
        ItemStack fillerItem = GuiHelper.getFiller(Bukkit.getInstance().getModulesFile().getBazaar().getGui().getFillerItem().isUseFiller(), Bukkit.getInstance().getModulesFile().getBazaar().getGui().getFillerItem().getMaterial());
        gui.setFiller(fillerItem);

        // With a virtual inventory to access items later on
        Inventory inv = org.bukkit.Bukkit.createInventory(null, InventoryType.CHEST);
        gui.addElement(new GuiStorageElement('i', inv));
        // Close action area (event)
        gui.setCloseAction(close -> {
            // Calculating storage amounts
            int maxStorageAmount = GameUtil.getAmountFromPerm(player,
                    "bazaar.maxstorage.",
                    Bukkit.getInstance().getModulesFile().getBazaar().getDefaultStorageSize());

            int canStoreAmount = maxStorageAmount - itemAmount;
            // Items which stored (airs included)
            List<ItemStack> items =  Arrays.stream(inv.getContents()).collect(Collectors.toList());
            String userId = User.getUser(player.getName()).getId();
            int serverId = BazaarModule.getServerId();

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
                XMaterial material = XMaterial.matchXMaterial(item);
                String name = ItemUtil.getName(item);
                String lore = (item.hasItemMeta() && item.getItemMeta().hasLore()) ?
                        item.getItemMeta().getLore().toString()
                                .replace("ı", "i")
                                .replace("u", "ü")
                                .replace("ş", "s")
                                .replace("ö", "o") : null;
                int amount = item.getAmount();
                int maxDurability = item.getType().getMaxDurability();
                int durability = ItemUtil.getDurability(item, maxDurability);
                String base64 = ItemUtil.toBase64(item);
                double price = 0.0;
                String creationDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                String modelId = ItemUtil.getModelId(item);
                String enchantment = ItemUtil.getEnchantments(item);

                Map<String, String> body = new HashMap<>();
                body.put("owner", userId);
                body.put("name", ChatUtil.removeColorCode(name));
                if (lore != null)
                    body.put("lore", ChatUtil.removeColorCode(lore));
                body.put("amount", String.valueOf(amount));
                body.put("maxDurability", String.valueOf(maxDurability));
                body.put("durability", String.valueOf(durability));

                body.put("base64", base64);
                body.put("price", String.valueOf(price));
                body.put("creationDate", creationDate);
                if (modelId != null)
                    body.put("modelID", modelId);
                if (enchantment != null)
                    body.put("enchantment", enchantment);
                body.put("serverID", String.valueOf(serverId));
                body.put("itemID", material.name());

                // Sends response
                try {
                    Response postBazaarItem = new PostRequest("bazaar/storages/" + userId + "/items", body).getResponse();
                    if (postBazaarItem.getResponseCode() == HttpURLConnection.HTTP_OK
                            && postBazaarItem.getResponseMessage().getBoolean("status")) {
                        ChatUtil.sendMessage(player, ChatUtil.replacePlaceholders(
                                Bukkit.getInstance().getLangFile().getGui().getBazaarGui().getAddItemMessage(),
                                new Placeholder("%item_name%", name)
                        ));
                    }
                    else throw new Exception();
                } catch (Exception e) {
                    // TODO error msg
                    e.printStackTrace();
                    // If something occur when adding item it will pop item back to player inventory
                    returnItems.add(item);
                }
            }

            // Gives items back to player
            if (!returnItems.isEmpty()) {
                PlayerInventory playerInventory = player.getInventory();
                returnItems.forEach(playerInventory::addItem);
                String returnMessage = Bukkit.getInstance().getLangFile().getGui().getBazaarGui().getReturnItemMessage();
                returnMessage = returnMessage.replace("%max_amount%", String.valueOf(maxStorageAmount))
                                .replace("%amount%", String.valueOf(returnItems.size()));
                ChatUtil.sendMessage(player, returnMessage);
            }
            return false; // Don't go back to the previous GUI (true would automatically go back to the previously opened one)
        });
        gui.show(player);
    }
}
