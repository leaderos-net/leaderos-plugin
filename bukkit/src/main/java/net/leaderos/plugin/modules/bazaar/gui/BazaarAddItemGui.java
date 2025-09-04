package net.leaderos.plugin.modules.bazaar.gui;

import com.cryptomorin.xseries.XMaterial;
import de.themoep.inventorygui.GuiStorageElement;
import de.themoep.inventorygui.InventoryGui;
import lombok.SneakyThrows;
import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.helpers.ChatUtil;
import net.leaderos.plugin.helpers.GameUtil;
import net.leaderos.plugin.helpers.GuiHelper;
import net.leaderos.plugin.helpers.ItemUtil;
import net.leaderos.plugin.modules.bazaar.BazaarModule;
import net.leaderos.plugin.modules.cache.model.User;
import net.leaderos.shared.error.Error;
import net.leaderos.shared.helpers.Placeholder;
import net.leaderos.shared.model.Response;
import net.leaderos.shared.model.request.impl.bazaar.AddBazaarItemRequest;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
     * @param itemAmount amount of items in bazaar storage
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
            int maxStorageAmount = GameUtil.getAmountFromPerm(player,
                    "bazaar.maxstorage.",
                    Bukkit.getInstance().getModulesFile().getBazaar().getDefaultStorageSize());
            int canStoreAmount = maxStorageAmount - itemAmount;
            List<ItemStack> items = Arrays.stream(inv.getContents()).collect(Collectors.toList());
            String userId = User.getUser(player.getName()).getId();
            int serverId = BazaarModule.getServerId();

            // List to store items that need to be returned to the player
            List<ItemStack> returnItems = new ArrayList<>();

            // List to store items that will be processed
            List<ItemStack> itemsToStore = new ArrayList<>();

            // First, determine which items exceed the storage limit
            for (ItemStack item : items) {
                if (item == null || item.getType() == Material.AIR || item.equals(fillerItem))
                    continue;

                if (canStoreAmount > 0) {
                    canStoreAmount--;
                    itemsToStore.add(item);
                } else {
                    returnItems.add(item);
                }
            }

            // Start asynchronous processing
            Bukkit.getFoliaLib().getScheduler().runAsync((task) -> {
                List<ItemStack> failedItems = new ArrayList<>();

                for (ItemStack item : itemsToStore) {
                    try {
                        XMaterial material = XMaterial.matchXMaterial(item);
                        String name = ItemUtil.getName(item);
                        String lore = (item.hasItemMeta() && item.getItemMeta().hasLore()) ?
                                String.join("\n", item.getItemMeta().getLore()) : null;
                        int amount = item.getAmount();
                        int maxDurability = item.getType().getMaxDurability();
                        int durability = ItemUtil.getDurability(item, maxDurability);
                        String base64 = ItemUtil.toBase64(item);
                        double price = 0.0;
                        String creationDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                        String modelId = ItemUtil.getModelId(item);
                        String enchantments = ItemUtil.getEnchantments(item);

                        Response postBazaarItem = new AddBazaarItemRequest(userId, name, lore, amount, maxDurability,
                                durability, base64, price, creationDate, modelId, enchantments, serverId, material.name()).getResponse();

                        // If the request fails, add the item to the failed list
                        if (postBazaarItem.getResponseCode() != HttpURLConnection.HTTP_OK || !postBazaarItem.getResponseMessage().getBoolean("status")) {
                            failedItems.add(item);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        failedItems.add(item);
                    }
                }

                // Add failed items to the return list
                returnItems.addAll(failedItems);

                // Return the failed or excess items to the player
                if (!returnItems.isEmpty()) {
                    Bukkit.getFoliaLib().getScheduler().runNextTick((items_task) -> {
                        PlayerInventory playerInventory = player.getInventory();
                        returnItems.forEach(playerInventory::addItem);
                        String returnMessage = Bukkit.getInstance().getLangFile().getGui().getBazaarGui().getReturnItemMessage();
                        returnMessage = returnMessage.replace("%max_amount%", String.valueOf(maxStorageAmount))
                                .replace("%amount%", String.valueOf(returnItems.size()));
                        ChatUtil.sendMessage(player, returnMessage);
                    });
                }
            });

            return false; // Don't go back to the previous GUI (true would automatically go back to the previously opened one)
        });

        gui.show(player);
    }
}
