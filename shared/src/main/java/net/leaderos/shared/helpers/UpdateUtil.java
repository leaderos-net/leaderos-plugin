package net.leaderos.shared.helpers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author amownyy
 * @since 1.0
 */
public class UpdateUtil {

    /**
     * Constructor of UpdateUtil
     */
    public UpdateUtil(String version) {
        checkForUpdates(version);
    }

    /**
     * Checks for updates
     */
    public void checkForUpdates(String version) {
        try {
            URL url = new URL("https://api2.leaderos.net/plugin-version-check.php?version=" + version);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JsonParser parser = new JsonParser();
                JsonObject jsonObject = parser.parse(response.toString()).getAsJsonObject();

                if (jsonObject.get("update").getAsBoolean()) {
                    System.out.println("There is a new update available for LeaderOS Plugin! Please update to " + jsonObject.get("version").getAsString());
                }
            } else {
                throw new RuntimeException("Failed to check for updates! Response code: " + responseCode);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
