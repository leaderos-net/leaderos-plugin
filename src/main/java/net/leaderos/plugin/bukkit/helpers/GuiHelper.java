package net.leaderos.plugin.bukkit.helpers;

import com.cryptomorin.xseries.XMaterial;
import de.themoep.inventorygui.GuiElement;
import de.themoep.inventorygui.GuiPageElement;
import net.leaderos.plugin.Main;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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
    public static ItemStack getFiller() {
        ItemStack item;
        // If enabled
        if (Main.getInstance().getLangFile().getGui().getDefaultGui().getFillerItem().isUseFiller())
            item = XMaterial.matchXMaterial(Main.getInstance().getLangFile().getGui().getDefaultGui().getFillerItem().getMaterial()).get().parseItem();
        else
            item = new ItemStack(Material.AIR);
        return item;
    }

    /**
     * Check for material and
     * get item with a material.
     * @param material of item
     * @param name of item
     * @param lore of item
     * @return ItemStack of destination item
     */
    public static @NotNull ItemStack getItem(XMaterial material, String name, List<String> lore) {
        ItemStack result;
        // material based item
        result = material.parseItem();
        ItemMeta meta = result.getItemMeta();
        meta.setLore(lore);
        meta.setDisplayName(name);
        result.setItemMeta(meta);
        return result;
    }

    /**
     * Previous page item creator
     *
     * @return GuiElement of previous menu icon
     */
    @Contract(" -> new")
    public static @NotNull GuiElement createPreviousPage() {
        return new GuiPageElement('b',
                new ItemStack(Material.ARROW),
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
    public static @NotNull GuiElement createNextPage() {
        return new GuiPageElement('n',
                new ItemStack(Material.ARROW),
                GuiPageElement.PageAction.NEXT,
                Main.getInstance().getLangFile().getGui().getDefaultGui().getPreviousPage().getName()
        );
    }
}
