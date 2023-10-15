package net.leaderos.plugin.modules.bazaar.gui;

import de.themoep.inventorygui.DynamicGuiElement;
import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import lombok.SneakyThrows;
import net.leaderos.plugin.helpers.ChatUtil;
import net.leaderos.plugin.modules.bazaar.model.PlayerBazaar;
import net.leaderos.plugin.modules.cache.model.User;
import net.leaderos.plugin.Main;
import net.leaderos.shared.Shared;
import net.leaderos.shared.exceptions.CacheNotFoundException;
import net.leaderos.plugin.helpers.GuiHelper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Bazaar gui
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

        // Add item icon
        gui.addElement(new StaticGuiElement('a', GuiHelper.addItemIcon(), 1, click -> {
            gui.close();
            click.getEvent().setCancelled(true);
            BazaarAddItemGui.showGui(player, playerBazaarList.size());
            return false;
        }));

        // Bazaar group creator
        GuiElementGroup bazaarGui = new GuiElementGroup('i');
        if (!playerBazaarList.isEmpty())
            playerBazaarList.forEach(playerBazaarItem -> {
                ItemStack bazaarItem = playerBazaarItem.getItem();
                bazaarGui.addElement(new DynamicGuiElement('s', (viewer)
                        -> new StaticGuiElement('s',
                        bazaarItem,
                        bazaarItem.getAmount(),
                        click ->  {
                            click.getEvent().setCancelled(true);
                            gui.close();
                            if (player.getInventory().firstEmpty() == -1) {
                                ChatUtil.sendMessage(player, Main.getInstance().getLangFile().getMessages().getCannotCreateFull());
                                return false;
                            }
                            String title = ChatUtil.color(Main.getInstance().getLangFile().getGui().getBazaarGui().getWithdrawTitle());
                            String subtitleError = ChatUtil.color(Main.getInstance().getLangFile().getGui().getBazaarGui().getWithdrawErrorSubtitle());
                            String subtitleSuccess = ChatUtil.color(Main.getInstance().getLangFile().getGui().getBazaarGui().getWithdrawSuccessSubtitle());
                            String subtitleProgress = ChatUtil.color(Main.getInstance().getLangFile().getGui().getBazaarGui().getWithdrawProgressSubtitle());
                            player.sendTitle(title, subtitleProgress);
                            boolean withdrawStatus = playerBazaarItem.withdrawItem(player);
                            if (withdrawStatus)
                                player.sendTitle(title, subtitleSuccess);
                            else
                                player.sendTitle(title, subtitleError);
                            return false;
                        })));
                    }
            );
        gui.addElement(bazaarGui);

        // Next and previos page icons
        gui.addElement(GuiHelper.createNextPage());
        gui.addElement(GuiHelper.createPreviousPage());
        gui.show(player);
    }
}
