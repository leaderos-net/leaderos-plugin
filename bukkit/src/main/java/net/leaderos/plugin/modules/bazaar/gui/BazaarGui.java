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

        Bukkit.getFoliaLib().getScheduler().runAsync((task) -> {
            // Gui template as array
            String[] layout = Bukkit.getInstance().getModulesFile().getBazaar().getGui().getLayout().toArray(new String[0]);
            // Inventory object
            String guiName = ChatUtil.replacePlaceholders(Bukkit.getInstance().getLangFile().getGui().getBazaarGui().getGuiName());
            InventoryGui gui = new InventoryGui(Bukkit.getInstance(), null, guiName, layout);
            // Filler item for empty slots
            gui.setFiller(GuiHelper.getFiller(Bukkit.getInstance().getModulesFile().getBazaar().getGui().getFillerItem().isUseFiller(), Bukkit.getInstance().getModulesFile().getBazaar().getGui().getFillerItem().getMaterial()));

            // List creator
            // why throw a cache not found exception here? its not related to cache at all. I will let @SneakyThrows do its job here
            List<PlayerBazaar> playerBazaarList = PlayerBazaar.getBazaarStorage(User.getUser(player.getName()).getId());

            // Add item icon
            gui.addElement(new StaticGuiElement('a', GuiHelper.addItemIcon(), 1, click -> {
                gui.close();
                BazaarAddItemGui.showGui(player, playerBazaarList.size());
                return true;
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
                                    click -> {
                                        gui.close();
                                        if (player.getInventory().firstEmpty() == -1) {
                                            ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getCannotCreateFull());
                                            return true;
                                        }
                                        if (!RequestUtil.canRequest(player.getUniqueId())) {
                                            ChatUtil.sendMessage(player, Bukkit.getInstance().getLangFile().getMessages().getHaveRequestOngoing());
                                            return true;
                                        }
                                        String title = ChatUtil.color(Bukkit.getInstance().getLangFile().getGui().getBazaarGui().getWithdrawTitle());
                                        String subtitleError = ChatUtil.color(Bukkit.getInstance().getLangFile().getGui().getBazaarGui().getWithdrawErrorSubtitle());
                                        String subtitleSuccess = ChatUtil.color(Bukkit.getInstance().getLangFile().getGui().getBazaarGui().getWithdrawSuccessSubtitle());
                                        String subtitleProgress = ChatUtil.color(Bukkit.getInstance().getLangFile().getGui().getBazaarGui().getWithdrawProgressSubtitle());
                                        player.sendTitle(title, subtitleProgress);

                                        RequestUtil.addRequest(player.getUniqueId());

                                        Bukkit.getFoliaLib().getScheduler().runAsync((item_task) -> {
                                            Error error = playerBazaarItem.withdrawItem(player, playerBazaarItem.getId());
                                            if (error == null)
                                                player.sendTitle(title, subtitleSuccess);
                                            else if (error == Error.DELETE_ERROR)
                                                player.sendTitle(title, subtitleError);

                                            RequestUtil.invalidate(player.getUniqueId());
                                        });

                                        return true;
                                    })));
                        }
                );
            gui.addElement(bazaarGui);

            // Next and previous page icons
            gui.addElement(GuiHelper.createNextPage(Bukkit.getInstance().getModulesFile().getBazaar().getGui().getNextPage().getItem()));
            gui.addElement(GuiHelper.createPreviousPage(Bukkit.getInstance().getModulesFile().getBazaar().getGui().getPreviousPage().getItem()));

            Bukkit.getFoliaLib().getScheduler().runNextTick((gui_task) -> {
                gui.show(player);
            });

            RequestUtil.invalidate(player.getUniqueId());
        });
    }
}
