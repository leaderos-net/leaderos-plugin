package net.leaderos.plugin.modules.webstore.gui;

import de.themoep.inventorygui.DynamicGuiElement;
import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import net.leaderos.plugin.helpers.ChatUtil;
import net.leaderos.plugin.modules.webstore.model.Category;
import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.helpers.GuiHelper;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * MainWebStoreGui gui
 * @author poyrazinan
 * @since 1.0
 */
public class MainWebStoreGui {

    /**
     * Constructor of gui
     */
    public MainWebStoreGui() {
    }

    /**
     * Opens gui to player
     *
     * @param player to show gui
     */
    public static void showGui(Player player) {
        // Gui template as array
        String[] layout = Bukkit.getInstance().getModulesFile().getWebStore().getGui().getLandingGuiLayout().toArray(new String[0]);
        // Inventory object
        String guiName = ChatUtil.replacePlaceholders(Bukkit.getInstance().getLangFile().getGui().getWebStoreGui().getGuiName());
        InventoryGui gui = new InventoryGui(Bukkit.getInstance(), null, guiName, layout);
        // Filler item for empty slots
        gui.setFiller(GuiHelper.getFiller(Bukkit.getInstance().getModulesFile().getWebStore().getGui().getFillerItem().isUseFiller(), Bukkit.getInstance().getModulesFile().getWebStore().getGui().getFillerItem().getMaterial()));

        // List creator
        List<Category> categoryList = Category.getCategories();

        // Category group creator
        GuiElementGroup categoryGroup = new GuiElementGroup('c');
        if (!categoryList.isEmpty())
            categoryList.stream().forEach(category -> categoryGroup.addElement(new DynamicGuiElement('s', (viewer)
                    -> new StaticGuiElement('s',
                    category.getCategoryIcon(),
                    1,
                    click -> {
                        WebStoreGui.showGui(player, category);
                        return true;
                    })))
            );
        gui.addElement(categoryGroup);

        // Next and previous page icons
        gui.addElement(GuiHelper.createNextPage(Bukkit.getInstance().getModulesFile().getWebStore().getGui().getNextPage().getItem()));
        gui.addElement(GuiHelper.createPreviousPage(Bukkit.getInstance().getModulesFile().getWebStore().getGui().getPreviousPage().getItem()));
        gui.show(player);
    }
}
