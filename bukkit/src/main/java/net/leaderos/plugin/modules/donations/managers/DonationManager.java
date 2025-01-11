package net.leaderos.plugin.modules.donations.managers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.leaderos.plugin.modules.donations.model.Donation;
import net.leaderos.plugin.modules.donations.model.DonationType;
import net.leaderos.shared.model.Response;
import net.leaderos.shared.model.request.impl.donations.GetDonationsRequest;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Donation manager class
 *
 * @author benfiratkaya
 * @since 1.0
 */
@Getter
@Setter
@AllArgsConstructor
public class DonationManager {
    /**
     * DonationData holds all the donation data statics
     * <p>Which contains AllTime, Annual, Daily, Latest, Monthly</p>
     * <p>DonationType value equals data name and DonationData for object</p>
     */
    @Getter
    private static HashMap<DonationType, DonationManager> managers = new HashMap<>();

    /**
     * Loads all donation data
     */
    public static void updateAllData() {
        if (!managers.isEmpty())
            managers.clear();

        // Loads all data of donation async
        managers.put(DonationType.LATEST, new DonationManager(DonationType.LATEST));
        managers.put(DonationType.TOP_ALLTIME, new DonationManager(DonationType.TOP_ALLTIME));
        managers.put(DonationType.TOP_ANNUAL, new DonationManager(DonationType.TOP_ANNUAL));
        managers.put(DonationType.TOP_MONTHLY, new DonationManager(DonationType.TOP_MONTHLY));
        managers.put(DonationType.TOP_DAILY, new DonationManager(DonationType.TOP_DAILY));
    }

    /**
     * Gets donation data by index id
     * @param type of donation
     * @param index of donation
     * @return Donation
     */
    public static Donation getDonation(DonationType type, int index) {
        try {
            return getManagers().get(type).getDonations().get(index);
        }
        catch (Exception exception) {
            return null;
        }
    }

    /**
     * Gets donations
     * @param type of list
     * @return RecentDonationData
     */
    public static List<Donation> getDonations(DonationType type) {
        try {
            return getManagers().get(type).getDonations();
        }
        catch (Exception exception) {
            return null;
        }
    }

    /**
     * Type of donation
     */
    private DonationType type;

    /**
     * Donation data holder
     */
    private List<Donation> donations;

    /**
     * Loads donation data
     * @param type type of donation
     * @see DonationType
     */
    public DonationManager(DonationType type) {
        this.type = type;
        loadDonationsData();
    }

    /**
     * Loads donator data of donation
     */
    private void loadDonationsData() {
        this.donations = new ArrayList<>();
        List<Donation> donations = new ArrayList<>();
        try {
            Response recentDonationResponse = new GetDonationsRequest(getType().getRequest()).getResponse();
            if (recentDonationResponse.getResponseCode() == HttpURLConnection.HTTP_OK) {
                JSONArray data = recentDonationResponse.getResponseMessage().getJSONArray("array");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject donation = (JSONObject) data.get(i);
                    donations.add(new Donation(i + 1, donation.getString("display_name"), donation.getString("username"), donation.getDouble("raw_total"), donation.getString("currency")));
                }
            }
        } catch (IOException ignored) {}
        setDonations(donations);
    }
}
