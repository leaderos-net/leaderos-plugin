package net.leaderos.plugin.bukkit.modules.webstore.gui;

import de.themoep.inventorygui.DynamicGuiElement;
import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import net.leaderos.plugin.Main;
import net.leaderos.plugin.bukkit.helpers.ChatUtil;
import net.leaderos.plugin.bukkit.helpers.GuiHelper;
import net.leaderos.plugin.bukkit.modules.webstore.model.Category;
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
        String[] layout = Main.getInstance().getLangFile().getGui().getWebStoreGui().getLandingGuiLayout().toArray(new String[0]);
        // Inventory object
        String guiName = ChatUtil.color(Main.getInstance().getLangFile().getGui().getWebStoreGui().getGuiName());
        InventoryGui gui = new InventoryGui(Main.getInstance(), null, guiName, layout);
        // Filler item for empty slots
        gui.setFiller(GuiHelper.getFiller());

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
                        gui.draw();
                        return true;
                    })))
            );
        gui.addElement(categoryGroup);

        // Next and previos page icons
        gui.addElement(GuiHelper.createNextPage());
        gui.addElement(GuiHelper.createPreviousPage());
        gui.show(player);
    }
}
