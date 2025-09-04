package net.leaderos.plugin.modules.donations.gui;

import de.themoep.inventorygui.DynamicGuiElement;
import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import lombok.SneakyThrows;
import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.helpers.ChatUtil;
import net.leaderos.plugin.helpers.GuiHelper;
import net.leaderos.plugin.modules.donations.DonationsModule;
import net.leaderos.plugin.modules.donations.managers.DonationManager;
import net.leaderos.plugin.modules.donations.model.DonationType;
import net.leaderos.plugin.modules.donations.model.Donation;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * RecentDonation gui
 * @author poyrazinan
 * @since 1.0
 */
public class DonationGui {


    /**
     * Constructor of gui
     */
    public DonationGui() {}

    /**
     * Opens gui to player
     * @param player to show gui
     */
    @SneakyThrows
    public static void showGui(Player player) {
        // Gui template as array
        String[] layout = Bukkit.getInstance().getModulesFile().getDonations().getGui().getLayout().toArray(new String[0]);
        // Inventory object
        String guiName = ChatUtil.replacePlaceholders(Bukkit.getInstance().getLangFile().getGui().getDonationsGui().getGuiName());
        InventoryGui gui = new InventoryGui(Bukkit.getInstance(), null, guiName, layout);
        // Filler item for empty slots
        gui.setFiller(GuiHelper.getFiller(Bukkit.getInstance().getModulesFile().getDonations().getGui().getFillerItem().isUseFiller(), Bukkit.getInstance().getModulesFile().getDonations().getGui().getFillerItem().getMaterial()));

        Bukkit.getFoliaLib().getScheduler().runAsync((task) -> {
            GuiElementGroup latestGroup = getGuiElementGroup(DonationType.LATEST);
            gui.addElement(latestGroup);

            GuiElementGroup allTimeGroup = getGuiElementGroup(DonationType.TOP_ALLTIME);
            gui.addElement(allTimeGroup);

            GuiElementGroup annualGroup = getGuiElementGroup(DonationType.TOP_ANNUAL);
            gui.addElement(annualGroup);

            GuiElementGroup monthlyGroup = getGuiElementGroup(DonationType.TOP_MONTHLY);
            gui.addElement(monthlyGroup);

            GuiElementGroup dailyGroup = getGuiElementGroup(DonationType.TOP_DAILY);
            gui.addElement(dailyGroup);

            gui.draw(player);
        });

        // Info
        GuiElementGroup latestInfoGroup = getGuiElementGroupInfo(DonationType.LATEST);
        gui.addElement(latestInfoGroup);

        GuiElementGroup allTimeInfoGroup = getGuiElementGroupInfo(DonationType.TOP_ALLTIME);
        gui.addElement(allTimeInfoGroup);

        GuiElementGroup annualInfoGroup = getGuiElementGroupInfo(DonationType.TOP_ANNUAL);
        gui.addElement(annualInfoGroup);

        GuiElementGroup monthlyInfoGroup = getGuiElementGroupInfo(DonationType.TOP_MONTHLY);
        gui.addElement(monthlyInfoGroup);

        GuiElementGroup dailyInfoGroup = getGuiElementGroupInfo(DonationType.TOP_DAILY);
        gui.addElement(dailyInfoGroup);

        // Next and previous page icons
        gui.addElement(GuiHelper.createNextPage(Bukkit.getInstance().getModulesFile().getDonations().getGui().getNextPage().getItem()));
        gui.addElement(GuiHelper.createPreviousPage(Bukkit.getInstance().getModulesFile().getDonations().getGui().getPreviousPage().getItem()));
        gui.show(player);
    }

    /**
     * Gets gui element group of donation data
     * @return GuiElementGroup of donation data
     */
    @NotNull
    private static GuiElementGroup getGuiElementGroup(DonationType type) {
        List<Donation> donations = DonationManager.getDonations(type);
        // donation items group creator
        GuiElementGroup donationItems = new GuiElementGroup(type.getGuiChar());
        if (!donations.isEmpty())
            donations.forEach(donation -> donationItems.addElement(new DynamicGuiElement('s', (viewer)
                    -> new StaticGuiElement('s',
                    DonationsModule.getDonationHead(donation))))
            );
        return donationItems;
    }

    /**
     * Gets gui element group of donation data
     * @return GuiElementGroup of donation data
     */
    @NotNull
    private static GuiElementGroup getGuiElementGroupInfo(DonationType type) {
        GuiElementGroup info = new GuiElementGroup(type.getGuiInfoChar());
        if (type == DonationType.LATEST) {
            info.addElement(new DynamicGuiElement('s', (viewer)
                    -> new StaticGuiElement('s',
                    DonationsModule.getDonationInfoItem(
                            Bukkit.getInstance().getModulesFile().getDonations().getGui().getInfoItems().getLatestMaterial(),
                            ChatUtil.color(Bukkit.getInstance().getLangFile().getGui().getDonationsGui().getInfo().getLatest().getDisplayName())
                    ))));
        } else if (type == DonationType.TOP_ALLTIME) {
            info.addElement(new DynamicGuiElement('s', (viewer)
                    -> new StaticGuiElement('s',
                    DonationsModule.getDonationInfoItem(
                            Bukkit.getInstance().getModulesFile().getDonations().getGui().getInfoItems().getTopAllTimeMaterial(),
                            ChatUtil.color(Bukkit.getInstance().getLangFile().getGui().getDonationsGui().getInfo().getTopAllTime().getDisplayName())
                    ))));
        } else if (type == DonationType.TOP_ANNUAL) {
            info.addElement(new DynamicGuiElement('s', (viewer)
                    -> new StaticGuiElement('s',
                    DonationsModule.getDonationInfoItem(
                            Bukkit.getInstance().getModulesFile().getDonations().getGui().getInfoItems().getTopAnnualMaterial(),
                            ChatUtil.color(Bukkit.getInstance().getLangFile().getGui().getDonationsGui().getInfo().getTopAnnual().getDisplayName())
                    ))));
        } else if (type == DonationType.TOP_MONTHLY) {
            info.addElement(new DynamicGuiElement('s', (viewer)
                    -> new StaticGuiElement('s',
                    DonationsModule.getDonationInfoItem(
                            Bukkit.getInstance().getModulesFile().getDonations().getGui().getInfoItems().getTopMonthlyMaterial(),
                            ChatUtil.color(Bukkit.getInstance().getLangFile().getGui().getDonationsGui().getInfo().getTopMonthly().getDisplayName())
                    ))));
        } else if (type == DonationType.TOP_DAILY) {
            info.addElement(new DynamicGuiElement('s', (viewer)
                    -> new StaticGuiElement('s',
                    DonationsModule.getDonationInfoItem(
                            Bukkit.getInstance().getModulesFile().getDonations().getGui().getInfoItems().getTopDailyMaterial(),
                            ChatUtil.color(Bukkit.getInstance().getLangFile().getGui().getDonationsGui().getInfo().getTopDaily().getDisplayName())
                    ))));
        }
        return info;
    }
}
