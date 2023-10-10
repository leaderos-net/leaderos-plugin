package net.leaderos.plugin.bukkit.modules.webstore.gui;

import de.themoep.inventorygui.DynamicGuiElement;
import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import net.leaderos.plugin.Main;
import net.leaderos.plugin.bukkit.helpers.ChatUtil;
import net.leaderos.plugin.bukkit.helpers.GuiHelper;
import net.leaderos.plugin.bukkit.modules.webstore.model.Category;
import net.leaderos.plugin.bukkit.modules.webstore.model.Product;
import net.leaderos.plugin.shared.module.auth.model.User;
import net.leaderos.plugin.shared.module.auth.AuthLogin;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

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
     */
    public static void showGui(Player player, Category categoryObj) {
        // Gui template as array
        String[] layout = Main.getInstance().getLangFile().getGui().getWebStoreGui().getLayout().toArray(new String[0]);
        // Inventory object
        String guiName = ChatUtil.color(Main.getInstance().getLangFile().getGui().getWebStoreGui().getGuiName());
        InventoryGui gui = new InventoryGui(Main.getInstance(), null, guiName, layout);
        // Filler item for empty slots
        gui.setFiller(GuiHelper.getFiller());

        // List creator
        List<Category> categoryList;
        List<Product> productList;
        if (categoryObj == null) {
            categoryList = Category.getCategories();
            productList = new ArrayList<>();
        }
        else {
            categoryList = categoryObj.getSubCategories();
            productList = categoryObj.getProductList();
        }

        // Category group creator
        GuiElementGroup categoryGroup = new GuiElementGroup('c');
        if (!categoryList.isEmpty())
            categoryList.stream().forEach(category -> categoryGroup.addElement(new DynamicGuiElement('s', (viewer)
                    -> new StaticGuiElement('s',
                category.getCategoryIcon(),
                1,
                click ->  {
                        showGui(player, category);
                        // TODO Category click event
                        gui.draw();
                        return true;
                })))
            );
        gui.addElement(categoryGroup);

        // Product group creator
        GuiElementGroup productGroup = new GuiElementGroup('p');
        if (!productList.isEmpty())
            // Product Group
            productList.stream().forEach(product -> productGroup.addElement(new DynamicGuiElement('e', (viewer)
                    -> new StaticGuiElement('e',
                    product.getProductIcon(),
                    1,
                    click ->  {
                        click.getEvent().setCancelled(true);
                        // TODO Product click event
                        if (User.isPlayerAuthed(player)) {

                        }
                        else {
                            gui.close();
                            AuthLogin.sendAuthModuleError(player);
                            return true;
                        }
                        gui.draw();
                        return true;
                    })))
            );
        gui.addElement(productGroup);

        // Next and previos page icons
        gui.addElement(GuiHelper.createNextPage());
        gui.addElement(GuiHelper.createPreviousPage());
        gui.show(player);
    }
}
