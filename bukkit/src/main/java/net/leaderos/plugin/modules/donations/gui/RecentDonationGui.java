package net.leaderos.plugin.modules.donations.gui;

import de.themoep.inventorygui.DynamicGuiElement;
import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import lombok.SneakyThrows;
import net.leaderos.plugin.Main;
import net.leaderos.plugin.helpers.ChatUtil;
import net.leaderos.plugin.helpers.GuiHelper;
import net.leaderos.plugin.modules.donations.Donations;
import net.leaderos.plugin.modules.donations.model.Donation;
import net.leaderos.plugin.modules.donations.model.DonationType;
import net.leaderos.plugin.modules.donations.model.DonatorData;
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
        String[] layout = Main.getInstance().getModulesFile().getDonations().getGui().getLayout().toArray(new String[0]);
        // Inventory object
        String guiName = ChatUtil.color(Main.getInstance().getLangFile().getGui().getDonationsGui().getGuiName());
        InventoryGui gui = new InventoryGui(Main.getInstance(), null, guiName, layout);
        // Filler item for empty slots
        gui.setFiller(GuiHelper.getFiller(Main.getInstance().getModulesFile().getDonations().getGui().getFillerItem().isUseFiller(), Main.getInstance().getModulesFile().getDonations().getGui().getFillerItem().getMaterial()));

        GuiElementGroup allTimeGroup = getGuiElementGroup(DonationType.ALLTIME);
        gui.addElement(allTimeGroup);

        GuiElementGroup annualGroup = getGuiElementGroup(DonationType.ANNUAL);
        gui.addElement(annualGroup);

        GuiElementGroup dailyGroup = getGuiElementGroup(DonationType.DAILY);
        gui.addElement(dailyGroup);

        GuiElementGroup latestGroup = getGuiElementGroup(DonationType.LATEST);
        gui.addElement(latestGroup);

        GuiElementGroup monthlyGroup = getGuiElementGroup(DonationType.MONTHLY);
        gui.addElement(monthlyGroup);
        // Next and previous page icons
        gui.addElement(GuiHelper.createNextPage(Main.getInstance().getModulesFile().getDonations().getGui().getNextPage().getItem()));
        gui.addElement(GuiHelper.createPreviousPage(Main.getInstance().getModulesFile().getDonations().getGui().getPreviousPage().getItem()));
        gui.show(player);
    }

    /**
     * Gets gui element group of donation data
     * @return GuiElementGroup of donation data
     */
    @NotNull
    private static GuiElementGroup getGuiElementGroup(DonationType type) {
        List<DonatorData> donatorData = Donation.getDonationData().get(type).getDonatorDataList();
        // donation items group creator
        GuiElementGroup donationItems = new GuiElementGroup(type.getGuiChar());
        if (!donatorData.isEmpty())
            donatorData.forEach(donationData -> donationItems.addElement(new DynamicGuiElement('s', (viewer)
                    -> new StaticGuiElement('s',
                    Donations.getDonationHead(donationData),
                    1,
                    click ->  {
                        click.getEvent().setCancelled(true);
                        return false;
                    })))
            );
        return donationItems;
    }
}
