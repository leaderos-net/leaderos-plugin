package net.leaderos.plugin.modules.webstore.gui;

import com.cryptomorin.xseries.XMaterial;
import de.themoep.inventorygui.DynamicGuiElement;
import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.helpers.ChatUtil;
import net.leaderos.plugin.helpers.GuiHelper;
import net.leaderos.plugin.helpers.ItemUtil;
import net.leaderos.plugin.modules.cache.model.User;
import net.leaderos.plugin.modules.webstore.helpers.WebStoreHelper;
import net.leaderos.plugin.modules.webstore.model.Category;
import net.leaderos.plugin.modules.webstore.model.Product;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * ConfirmPurchase GUI
 * @author firatkaya
 * @since 1.0.4
 */
public class ConfirmPurchaseGui {

    /**
     * Constructor of gui
     */
    public ConfirmPurchaseGui() {}

    /**
     * Opens gui to player
     * @param player to show gui
     * @param product to buy
     */
    public static void showGui(Player player, Product product) {
        // Gui template as array
        String[] layout = Bukkit.getInstance().getModulesFile().getWebStore().getGui().getConfirmPurchase().getLayout().toArray(new String[0]);
        // Inventory object
        String guiName = ChatUtil.replacePlaceholders(Bukkit.getInstance().getLangFile().getGui().getWebStoreGui().getConfirmPurchase().getGuiName());
        InventoryGui gui = new InventoryGui(Bukkit.getInstance(), null, guiName, layout);
        // Filler item for empty slots
        gui.setFiller(GuiHelper.getFiller(Bukkit.getInstance().getModulesFile().getWebStore().getGui().getFillerItem().isUseFiller(), Bukkit.getInstance().getModulesFile().getWebStore().getGui().getFillerItem().getMaterial()));

        // Add product icon
        gui.addElement(productIcon(product));

        // Add buttons
        gui.addElement(confirmIcon(player, product, gui));
        gui.addElement(cancelIcon(gui));

        gui.show(player);
    }

    /**
     * ConfirmPurchase GUI add product icon
     * @param product to buy
     *
     * @return product icon
     */
    public static StaticGuiElement productIcon(Product product) {
        return new StaticGuiElement(
                'p',
                product.getProductIcon()
        );
    }

    /**
     * ConfirmPurchase GUI add confirm button
     * @param player to buy product
     * @param product to buy
     * @param gui to manage gui
     *
     * @return confirm icon
     */
    public static StaticGuiElement confirmIcon(Player player, Product product, InventoryGui gui) {
        String displayName = ChatUtil.color(Bukkit.getInstance().getLangFile().getGui().getWebStoreGui().getConfirmPurchase().getConfirmTitle());
        XMaterial material = XMaterial.matchXMaterial(Bukkit.getInstance().getModulesFile().getWebStore().getGui().getConfirmPurchase().getConfirmMaterial()).orElse(XMaterial.LIME_WOOL);
        return new StaticGuiElement(
                'y',
                ItemUtil.getItem(material, displayName),
                1,
                click -> {
                    gui.close();
                    WebStoreHelper.buyItem(player, product.getProductId());
                    return true;
                }
        );
    }

    /**
     * ConfirmPurchase GUI add cancel button
     * @param gui to manage gui
     *
     * @return cancel icon
     */
    public static StaticGuiElement cancelIcon(InventoryGui gui) {
        String displayName = ChatUtil.color(Bukkit.getInstance().getLangFile().getGui().getWebStoreGui().getConfirmPurchase().getCancelTitle());
        XMaterial material = XMaterial.matchXMaterial(Bukkit.getInstance().getModulesFile().getWebStore().getGui().getConfirmPurchase().getCancelMaterial()).orElse(XMaterial.RED_WOOL);
        return new StaticGuiElement(
                'n',
                ItemUtil.getItem(material, displayName),
                1,
                click -> {
                    gui.close();
                    return true;
                }
        );
    }
}
