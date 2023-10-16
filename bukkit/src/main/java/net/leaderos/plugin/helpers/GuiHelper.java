package net.leaderos.plugin.helpers;

import com.cryptomorin.xseries.XMaterial;
import de.themoep.inventorygui.GuiElement;
import de.themoep.inventorygui.GuiPageElement;
import net.leaderos.plugin.Main;
import net.leaderos.shared.Shared;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Helper class for gui
 * @author poyrazinan
 * @since 1.0
 */

public class GuiHelper {

    /**
     * Constructor of class
     */
    public GuiHelper() {}

    /**
     * Filler item of guis.
     * Filler item basically fills empty slots of gui
     *
     * @return ItemStack of filler
     */
    public static ItemStack getFiller(boolean status, String materialName) {
        ItemStack item;
        // If enabled
        if (status)
            item = XMaterial.matchXMaterial(materialName).orElse(XMaterial.GRAY_STAINED_GLASS_PANE).parseItem();
        else
            item = new ItemStack(Material.AIR);
        return item;
    }

    /**
     * Previous page item creator
     *
     * @return GuiElement of previous menu icon
     */
    @Contract(" -> new")
    public static @NotNull GuiElement createPreviousPage(String materialName) {
        return new GuiPageElement('b',
                XMaterial.matchXMaterial(materialName).orElse(XMaterial.ARROW).parseItem(),
                GuiPageElement.PageAction.PREVIOUS,
                Main.getInstance().getLangFile().getGui().getDefaultGui().getPreviousPage().getName()
        );
    }

    /**
     * Next page item creator
     *
     * @return GuiElement of next menu icon
     */
    @Contract(" -> new")
    public static @NotNull GuiElement createNextPage(String materialName) {
        return new GuiPageElement('n',
                XMaterial.matchXMaterial(materialName).orElse(XMaterial.ARROW).parseItem(),
                GuiPageElement.PageAction.NEXT,
                Main.getInstance().getLangFile().getGui().getDefaultGui().getPreviousPage().getName()
        );
    }


    /**
     * BazaarGui add item icon creator
     *
     * @return add item icon
     */
    public static ItemStack addItemIcon() {
        String displayName = ChatUtil.color(Main.getInstance().getLangFile().getGui().getBazaarGui().getAddItemName());
        XMaterial material = XMaterial.matchXMaterial(Main.getInstance().getModulesFile().getBazaar().getGui().getAddItemMaterial()).orElse(XMaterial.GREEN_WOOL);
        List<String> lore = ChatUtil.color(Main.getInstance().getLangFile().getGui().getBazaarGui().getAddItemLore());
        return ItemUtils.getItem(material, displayName, lore);
    }
}
