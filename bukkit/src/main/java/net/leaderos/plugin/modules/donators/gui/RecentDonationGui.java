package net.leaderos.plugin.modules.donators.gui;

import de.themoep.inventorygui.DynamicGuiElement;
import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import lombok.SneakyThrows;
import net.leaderos.plugin.Main;
import net.leaderos.plugin.helpers.ChatUtil;
import net.leaderos.plugin.helpers.GuiHelper;
import net.leaderos.plugin.modules.donators.RecentDonations;
import net.leaderos.plugin.modules.donators.model.RecentDonationData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * RecentDonation gui
 * @author poyrazinan
 * @since 1.0
 */
public class RecentDonationGui {


    /**
     * Constructor of gui
     */
    public RecentDonationGui() {}

    /**
     * Opens gui to player
     * @param player to show gui
     */
    @SneakyThrows
    public static void showGui(Player player) {
        // Gui template as array
        String[] layout = Main.getInstance().getLangFile().getGui().getDonationsGui().getLayout().toArray(new String[0]);
        // Inventory object
        String guiName = ChatUtil.color(Main.getInstance().getLangFile().getGui().getDonationsGui().getGuiName());
        InventoryGui gui = new InventoryGui(Main.getInstance(), null, guiName, layout);
        // Filler item for empty slots
        gui.setFiller(GuiHelper.getFiller());

        GuiElementGroup donationItems = getGuiElementGroup();
        gui.addElement(donationItems);
        // Next and previous page icons
        gui.addElement(GuiHelper.createNextPage());
        gui.addElement(GuiHelper.createPreviousPage());
        gui.show(player);
    }

    /**
     * Gets gui element group of donation data
     * @return GuiElementGroup of donation data
     */
    @NotNull
    private static GuiElementGroup getGuiElementGroup() {
        List<RecentDonationData> recentDonationData = RecentDonationData.getRecentDonationDataList();
        // donation items group creator
        GuiElementGroup donationItems = new GuiElementGroup('d');
        if (!recentDonationData.isEmpty())
            recentDonationData.forEach(donationData -> donationItems.addElement(new DynamicGuiElement('s', (viewer)
                    -> new StaticGuiElement('s',
                    RecentDonations.getDonationHead(donationData),
                    1,
                    click ->  {
                        click.getEvent().setCancelled(true);
                        return false;
                    })))
            );
        return donationItems;
    }
}
