package net.leaderos.plugin.modules.webstore.gui;

import de.themoep.inventorygui.DynamicGuiElement;
import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import net.leaderos.plugin.helpers.ChatUtil;
import net.leaderos.plugin.modules.cache.model.User;
import net.leaderos.plugin.modules.webstore.helpers.WebStoreHelper;
import net.leaderos.plugin.modules.webstore.model.Category;
import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.modules.webstore.model.Product;
import net.leaderos.plugin.helpers.GuiHelper;
import net.leaderos.shared.helpers.RequestUtil;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Webstore gui
 * @author poyrazinan
 * @since 1.0
 */
public class WebStoreGui {

    /**
     * Constructor of gui
     */
    public WebStoreGui() {}

    /**
     * Opens gui to player
     * @param player to show gui
     * @param categoryObj category to open
     */
    public static void showGui(Player player, Category categoryObj) {
        if (!RequestUtil.canRequest(player.getUniqueId())) {
            ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getHaveRequestOngoing());
            return;
        }

        RequestUtil.addRequest(player.getUniqueId());

        Bukkit.getFoliaLib().getScheduler().runAsync((task) -> {
            // Gui template as array
            String[] layout = Bukkit.getInstance().getModulesFile().getWebStore().getGui().getLayout().toArray(new String[0]);
            // Inventory object
            String guiName = ChatUtil.replacePlaceholders(Bukkit.getInstance().getLangFile().getGui().getWebStoreGui().getGuiName());
            InventoryGui gui = new InventoryGui(Bukkit.getInstance(), null, guiName, layout);
            // Filler item for empty slots
            gui.setFiller(GuiHelper.getFiller(Bukkit.getInstance().getModulesFile().getWebStore().getGui().getFillerItem().isUseFiller(), Bukkit.getInstance().getModulesFile().getWebStore().getGui().getFillerItem().getMaterial()));

            // List creator
            List<Category> categoryList;
            List<Product> productList;
            if (categoryObj == null) {
                categoryList = WebStoreHelper.getCategories(player.getName());
                productList = new ArrayList<>();
            } else {
                categoryList = categoryObj.getSubCategories();
                productList = categoryObj.getProductList();
            }

            GuiElementGroup elementGroup = new GuiElementGroup('e');
            // Category group creator
            GuiElementGroup categoryGroup = new GuiElementGroup('c');
            if (!categoryList.isEmpty()) {
                // Element group
                addCategoriesToGroup(categoryList, elementGroup, player);

                // Category group
                addCategoriesToGroup(categoryList, categoryGroup, player);
                categoryGroup.setFiller(GuiHelper.getFiller(Bukkit.getInstance().getModulesFile().getWebStore().getGui().getFillerItem().isUseFiller(), Bukkit.getInstance().getModulesFile().getWebStore().getGui().getFillerItem().getMaterial()));
                gui.addElement(categoryGroup);
            }
            // Product group creator
            GuiElementGroup productGroup = new GuiElementGroup('p');
            if (!productList.isEmpty()) {
                // Element group
                addProductsToGroup(productList, elementGroup, player, gui);

                // Product Group
                addProductsToGroup(productList, productGroup, player, gui);
                productGroup.setFiller(GuiHelper.getFiller(Bukkit.getInstance().getModulesFile().getWebStore().getGui().getFillerItem().isUseFiller(), Bukkit.getInstance().getModulesFile().getWebStore().getGui().getFillerItem().getMaterial()));
                gui.addElement(productGroup);
            }
            elementGroup.setFiller(GuiHelper.getFiller(Bukkit.getInstance().getModulesFile().getWebStore().getGui().getFillerItem().isUseFiller(), Bukkit.getInstance().getModulesFile().getWebStore().getGui().getFillerItem().getMaterial()));
            gui.addElement(elementGroup);

            // Credits icon
            User user = User.getUser(player.getName());
            double credit = user == null ? 0.00 : user.getCredit();
            gui.addElement(GuiHelper.addCreditIcon(credit));

            // Next and previous page icons
            gui.addElement(GuiHelper.createNextPage(Bukkit.getInstance().getModulesFile().getWebStore().getGui().getNextPage().getItem()));
            gui.addElement(GuiHelper.createPreviousPage(Bukkit.getInstance().getModulesFile().getWebStore().getGui().getPreviousPage().getItem()));

            Bukkit.getFoliaLib().getScheduler().runNextTick((gui_task) -> {
                gui.show(player);
            });

            RequestUtil.invalidate(player.getUniqueId());
        });
    }

    /**
     * Adds product to gui element group
     * @param productList list of product
     * @param group element group of gui
     * @param player inventory holder
     * @param gui opened gui
     */
    private static void addProductsToGroup(List<Product> productList, GuiElementGroup group, Player player, InventoryGui gui) {
        productList.forEach(product -> group.addElement(new DynamicGuiElement('e', (viewer)
                -> new StaticGuiElement('e',
                product.getProductIcon(),
                1,
                click -> {
                    // Disable click
                    if (product.isRestricted()) return true;

                    ConfirmPurchaseGui.showGui(player, product);
                    return true;
                })))
        );
    }

    /**
     * Adds category object to element group
     * @param categoryList list of category
     * @param group element group
     * @param player gui opener
     */
    private static void addCategoriesToGroup(List<Category> categoryList, GuiElementGroup group, Player player) {
        categoryList.forEach(category -> group.addElement(new DynamicGuiElement('s', (viewer)
                -> new StaticGuiElement('s',
                category.getCategoryIcon(),
                1,
                click -> {
                    showGui(player, category);
                    return true;
                })))
        );
    }
}
