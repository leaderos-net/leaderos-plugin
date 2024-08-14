package net.leaderos.plugin.helpers;

import com.cryptomorin.xseries.XMaterial;
import de.themoep.inventorygui.GuiElement;
import de.themoep.inventorygui.GuiPageElement;
import de.themoep.inventorygui.StaticGuiElement;
import net.leaderos.plugin.Bukkit;
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
     * @param status status of filler
     * @param materialName material name of filler
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
     * @param materialName material name of previous page item
     *
     * @return GuiElement of previous menu icon
     */
    @Contract(" -> new")
    public static @NotNull GuiElement createPreviousPage(String materialName) {
        return new GuiPageElement('b',
                XMaterial.matchXMaterial(materialName).orElse(XMaterial.ARROW).parseItem(),
                GuiPageElement.PageAction.PREVIOUS,
                Bukkit.getInstance().getLangFile().getGui().getDefaultGui().getPreviousPage().getName()
        );
    }

    /**
     * Next page item creator
     * @param materialName material name of next page item
     *
     * @return GuiElement of next menu icon
     */
    @Contract(" -> new")
    public static @NotNull GuiElement createNextPage(String materialName) {
        return new GuiPageElement('n',
                XMaterial.matchXMaterial(materialName).orElse(XMaterial.ARROW).parseItem(),
                GuiPageElement.PageAction.NEXT,
                Bukkit.getInstance().getLangFile().getGui().getDefaultGui().getNextPage().getName()
        );
    }


    /**
     * BazaarGui add item icon creator
     *
     * @return add item icon
     */
    public static ItemStack addItemIcon() {
        String displayName = ChatUtil.color(Bukkit.getInstance().getLangFile().getGui().getBazaarGui().getAddItemName());
        XMaterial material = XMaterial.matchXMaterial(Bukkit.getInstance().getModulesFile().getBazaar().getGui().getAddItemMaterial()).orElse(XMaterial.GREEN_WOOL);
        List<String> lore = ChatUtil.color(Bukkit.getInstance().getLangFile().getGui().getBazaarGui().getAddItemLore());
        return ItemUtil.getItem(material, displayName, lore);
    }

    /**
     * WebStore GUI add credit icon
     * @param credits to show
     *
     * @return add item icon
     */
    public static StaticGuiElement addCreditIcon(double credits) {
        String displayName = ChatUtil.color(Bukkit.getInstance().getLangFile().getGui().getWebStoreGui().getCredit().getTitle()).replace("%credits%", String.valueOf(credits));
        XMaterial material = XMaterial.matchXMaterial(Bukkit.getInstance().getModulesFile().getWebStore().getGui().getCredit().getMaterial()).orElse(XMaterial.SUNFLOWER);
        //List<String> lore = ChatUtil.color(Bukkit.getInstance().getLangFile().getGui().getWebStoreGui().getCredit().getLore());
        return new StaticGuiElement(
                'i',
                ItemUtil.getItem(material, displayName)
        );
    }
}
