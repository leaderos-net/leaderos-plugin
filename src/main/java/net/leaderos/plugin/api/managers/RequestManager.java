package net.leaderos.plugin.api.managers;

import net.leaderos.plugin.Main;
import net.leaderos.plugin.exceptions.RequestException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author poyrazinan
 * @since 1.0
 */
public class RequestManager {

    /**
     * Constructor of RequestManager
     */
    public RequestManager() {}

    /**
     * PostRequest method
     * @param api link of api
     * @param body body of request
     * @return JSONObject
     */
    public JSONObject postRequest(String api, String body) {
        try {
            //
            // URL as String
            String url = "https://" + Main.getInstance().getConfigFile().getString("settings.url") + "/api/" + api;
            // URL Object
            URL apiUrl = new URL(url);

            // HTTP request object
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("POST"); // POST
            connection.setDoOutput(true); // We will write OutputStream to this request

            connection.setRequestProperty("X-Api-Key", Main.getInstance().getConfigFile().getString("settings.api-key"));
            connection.setRequestProperty("Content-Type", "application/json");

            // OutputStream for write JSON data
            DataOutputStream outStream = new DataOutputStream(connection.getOutputStream());
            outStream.writeBytes(body);

            int responseCode = connection.getResponseCode();
            if (!(responseCode == HttpURLConnection.HTTP_CREATED
                    || responseCode == HttpURLConnection.HTTP_ACCEPTED
                    || responseCode == HttpURLConnection.HTTP_OK))
                throw new RequestException("Connection to website has failed", responseCode, connection.getResponseMessage());

            // BufferedReader for read data
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject jsonObject = new JSONObject(response.toString());
            connection.disconnect();
            return jsonObject;
        } catch (Exception e) {
            // TODO Exception handling
            return null;
        }
    }
}
