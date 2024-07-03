package net.leaderos.shared.helpers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PluginUpdater {

    private URL url;
    private String newVersion = "1.0.0";

    public PluginUpdater(String currentVersion) {
        try {
            this.url = new URL("https://api2.leaderos.net/plugin-version-check.php?version=" + currentVersion);
        } catch (Exception ignored) {}
    }

    public String getLatestVersion() {
        return this.newVersion;
    }

    public boolean checkForUpdates() throws Exception {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        connection.disconnect();

        // Parse JSON response using GSON
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(response.toString(), JsonObject.class);

        if (jsonObject.get("version") != null)
            this.newVersion = jsonObject.get("version").getAsString();

        return jsonObject.get("update").getAsBoolean();
    }

}
