package net.leaderos.plugin.modules.bazaar.gui;

import de.themoep.inventorygui.DynamicGuiElement;
import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import lombok.SneakyThrows;
import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.helpers.ChatUtil;
import net.leaderos.plugin.helpers.GuiHelper;
import net.leaderos.plugin.modules.bazaar.model.PlayerBazaar;
import net.leaderos.plugin.modules.cache.model.User;
import net.leaderos.shared.error.Error;
import net.leaderos.shared.helpers.RequestUtil;
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
    if (!RequestUtil.canRequest(player.getUniqueId())) {
        ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getHaveRequestOngoing());
        return;
    }

    RequestUtil.addRequest(player.getUniqueId());

    // Obtain an instance of Bukkit instead of calling a non-static method
    Bukkit bukkit = Bukkit.getInstance();

    bukkit.getFoliaLib().getImpl().runAsync(() -> {
        String[] layout = bukkit.getModulesFile().getBazaar().getGui().getLayout().toArray(new String[0]);
        String guiName = ChatUtil.replacePlaceholders(bukkit.getLangFile().getGui().getBazaarGui().getGuiName());
        InventoryGui gui = new InventoryGui(bukkit, null, guiName, layout);
        gui.setFiller(GuiHelper.getFiller(bukkit.getModulesFile().getBazaar().getGui().getFillerItem().isUseFiller(), bukkit.getModulesFile().getBazaar().getGui().getFillerItem().getMaterial()));

        List<PlayerBazaar> playerBazaarList = PlayerBazaar.getBazaarStorage(User.getUser(player.getName()).getId());

        gui.addElement(new StaticGuiElement('a', GuiHelper.addItemIcon(), 1, click -> {
            gui.close();
            click.getEvent().setCancelled(true);
            BazaarAddItemGui.showGui(player, playerBazaarList.size());
            return false;
        }));

        GuiElementGroup bazaarGui = new GuiElementGroup('i');
        if (!playerBazaarList.isEmpty()) {
            playerBazaarList.forEach(playerBazaarItem -> {
                ItemStack bazaarItem = playerBazaarItem.getItem();
                bazaarGui.addElement(new DynamicGuiElement('s', viewer -> new StaticGuiElement('s', bazaarItem, bazaarItem.getAmount(), itemClick -> {
                    itemClick.getEvent().setCancelled(true);
                    gui.close();
                    if (player.getInventory().firstEmpty() == -1) {
                        ChatUtil.sendMessage(player, bukkit.getLangFile().getMessages().getCannotCreateFull());
                        return false;
                    }
                    String title = ChatUtil.color(bukkit.getLangFile().getGui().getBazaarGui().getWithdrawTitle());
                    String subtitleError = ChatUtil.color(bukkit.getLangFile().getGui().getBazaarGui().getWithdrawErrorSubtitle());
                    String subtitleSuccess = ChatUtil.color(bukkit.getLangFile().getGui().getBazaarGui().getWithdrawSuccessSubtitle());
                    String subtitleProgress = ChatUtil.color(bukkit.getLangFile().getGui().getBazaarGui().getWithdrawProgressSubtitle());
                    player.sendTitle(title, subtitleProgress);

                    bukkit.getFoliaLib().getImpl().runNextTick(bukkit, () -> {
                        Error error = playerBazaarItem.withdrawItem(player);
                        if (error == null)
                            player.sendTitle(title, subtitleSuccess);
                        else if (error == Error.DELETE_ERROR)
                            player.sendTitle(title, subtitleError);

                        RequestUtil.invalidate(player.getUniqueId());
                    });

                    return false;
                })));
            });
        }
        gui.addElement(bazaarGui);

        gui.addElement(GuiHelper.createNextPage(bukkit.getModulesFile().getBazaar().getGui().getNextPage().getItem()));
        gui.addElement(GuiHelper.createPreviousPage(bukkit.getModulesFile().getBazaar().getGui().getPreviousPage().getItem()));

        bukkit.getFoliaLib().getImpl().runNextTick(bukkit, () -> {
            gui.show(player);
        });
    });
}


}
