package net.leaderos.plugin.bukkit.modules.bazaar;

import de.themoep.inventorygui.DynamicGuiElement;
import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import lombok.SneakyThrows;
import net.leaderos.plugin.Main;
import net.leaderos.plugin.bukkit.helpers.ChatUtil;
import net.leaderos.plugin.bukkit.helpers.GuiHelper;
import net.leaderos.plugin.bukkit.modules.bazaar.exception.CacheNotFoundException;
import net.leaderos.plugin.bukkit.modules.bazaar.model.PlayerBazaar;
import net.leaderos.plugin.bukkit.modules.webstore.model.Category;
import net.leaderos.plugin.bukkit.modules.webstore.model.Product;
import net.leaderos.plugin.shared.module.auth.AuthLogin;
import net.leaderos.plugin.shared.module.auth.model.User;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Webstore gui
 * @author poyrazinan
 * @since 1.0
 */
public class BazaarGui {

    /**
     * Constructor of gui
     */
    public BazaarGui() {}

    /**
     * Opens gui to player
     * @param player to show gui
     */
    @SneakyThrows
    public static void showGui(Player player) {
        // Gui template as array
        String[] layout = Main.getInstance().getLangFile().getGui().getBazaarGui().getLayout().toArray(new String[0]);
        // Inventory object
        String guiName = ChatUtil.color(Main.getInstance().getLangFile().getGui().getBazaarGui().getGuiName());
        InventoryGui gui = new InventoryGui(Main.getInstance(), null, guiName, layout);
        // Filler item for empty slots
        gui.setFiller(GuiHelper.getFiller());

        // List creator
        List<PlayerBazaar> playerBazaarList;
        try {
            playerBazaarList = PlayerBazaar.getBazaarStorage(User.getUser(player.getName()).getId());
        } catch (Exception e){
            throw new CacheNotFoundException("cache not found");
        }

        // Category group creator
        GuiElementGroup bazaarGui = new GuiElementGroup('i');
        if (!playerBazaarList.isEmpty())
            playerBazaarList.stream().forEach(element -> bazaarGui.addElement(new DynamicGuiElement('s', (viewer)
                    -> new StaticGuiElement('s',
                element.getItem(),
                1,
                click ->  {
                        // TODO bazaar click event
                        gui.draw();
                        return true;
                })))
            );
        gui.addElement(bazaarGui);

        // Next and previos page icons
        gui.addElement(GuiHelper.createNextPage());
        gui.addElement(GuiHelper.createPreviousPage());
        gui.show(player);
    }
}
