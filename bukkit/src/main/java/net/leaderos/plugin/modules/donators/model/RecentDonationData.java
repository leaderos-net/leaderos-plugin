package net.leaderos.plugin.modules.donators.model;

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
     * Updates all recent donations data
     */
    public static void updateAllRecentDonationsData() {
        // TODO Update request
        List<RecentDonationData> donationDataList = new ArrayList<>();
        try {
            Response recentDonationResponse = new GetRequest("donations/" + Main.getShared().getModulesFile().getRecentDonations().getRecentDonationLimit()).getResponse();
            if (recentDonationResponse.getResponseCode() == HttpURLConnection.HTTP_OK)
                recentDonationResponse.getResponseMessage().getJSONArray("array").forEach(recentDonation -> {
                    JSONObject donation = (JSONObject) recentDonation;
                    donationDataList.add(new RecentDonationData(donation.getString("username"), donation.getDouble("credit")));
                });
            else
                // TODO Exception handling
                throw new IOException();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
