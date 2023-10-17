package net.leaderos.plugin.modules.donations.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.leaderos.plugin.Main;
import net.leaderos.shared.model.Response;
import net.leaderos.shared.model.request.GetRequest;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * RecentDonationData store donator list
 *
 * @author poyrazinan
 * @since 1.0
 */
@Getter
@Setter
@AllArgsConstructor
public class RecentDonationData {

    /**
     * Data holder
     */
    @Getter
    private static List<RecentDonationData> recentDonationDataList = new ArrayList<>();

    /**
     * Username data type of String
     */
    private String userName;

    /**
     * Credit data type of double
     */
    private double credit;

    /**
     * Credits symbol for placeholder
     */
    private String symbol;

    /**
     * Updates all recent donations data
     */
    public static void updateAllRecentDonationsData() {
        List<RecentDonationData> donationDataList = new ArrayList<>();
        try {
            Response recentDonationResponse = new GetRequest("store/donates/?type=latest&limit=" + Main.getInstance().getModulesFile().getDonations().getRecentDonationLimit()).getResponse();
            if (recentDonationResponse.getResponseCode() == HttpURLConnection.HTTP_OK)
                recentDonationResponse.getResponseMessage().getJSONArray("array").forEach(recentDonation -> {
                    JSONObject donation = (JSONObject) recentDonation;
                    donationDataList.add(new RecentDonationData(donation.getString("username"), donation.getDouble("raw_total"), donation.getString("currency")));
                });
        } catch (IOException ignored) {}
        recentDonationDataList = donationDataList;
    }

    /**
     * Gets recent donation data by index id
     * @param index of list
     * @return RecentDonationData
     */
    public static RecentDonationData getRecentDonation(int index) {
        try {
            return getRecentDonationDataList().get(index);
        }
        catch (IndexOutOfBoundsException exception) {
            return null;
        }
    }
}
