package net.leaderos.plugin.modules.donations.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.leaderos.plugin.Main;
import net.leaderos.shared.model.Response;
import net.leaderos.shared.model.request.GetRequest;
import org.bukkit.Bukkit;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * DonationData store donator list
 *
 * @author poyrazinan
 * @since 1.0
 */
@Getter
@Setter
@AllArgsConstructor
public class Donation {

    /**
     * DonationData holds all the donation data statics
     * <p>Which contains AllTime, Annual, Daily, Latest, Monthly</p>
     * <p>DonationType value equals data name and DonationData for object</p>
     */
    @Getter
    private static HashMap<DonationType, Donation> donationData = new HashMap<>();

    /**
     * Loads all donation data
     */
    public static void updateAllData() {
        if (!donationData.isEmpty())
            donationData.clear();

        // Loads all data of donation async
        donationData.put(DonationType.ALLTIME, new Donation(DonationType.ALLTIME));
        donationData.put(DonationType.ANNUAL, new Donation(DonationType.ANNUAL));
        donationData.put(DonationType.DAILY, new Donation(DonationType.DAILY));
        donationData.put(DonationType.LATEST, new Donation(DonationType.LATEST));
        donationData.put(DonationType.MONTHLY, new Donation(DonationType.MONTHLY));
    }

    /**
     * Gets recent donation data by index id
     * @param index of list
     * @return RecentDonationData
     */
    public static DonatorData getDonationData(DonationType type, int index) {
        try {
            return getDonationData().get(type).getDonatorDataList().get(index);
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
    private List<DonatorData> donatorDataList;

    /**
     * Loads donation data
     * @param type type of donation
     * @see DonationType
     */
    public Donation(DonationType type) {
        this.type = type;
        loadDonatorData();
    }

    /**
     * Loads donator data of donation
     */
    private void loadDonatorData() {
        this.donatorDataList = new ArrayList<>();
        List<DonatorData> donatorData = new ArrayList<>();
        try {
            Response recentDonationResponse = new GetRequest("store/donates/?type=" + getType().getRequest() + "&limit=10").getResponse();
            if (recentDonationResponse.getResponseCode() == HttpURLConnection.HTTP_OK)
                recentDonationResponse.getResponseMessage().getJSONArray("array").forEach(recentDonation -> {
                    JSONObject donation = (JSONObject) recentDonation;
                    donatorData.add(new DonatorData(donation.getString("username"), donation.getDouble("raw_total"), donation.getString("currency")));
                });
        } catch (IOException ignored) {}
        setDonatorDataList(donatorData);
    }
}
