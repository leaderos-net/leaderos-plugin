package net.leaderos.plugin.shared.model;

import net.leaderos.plugin.Main;
import net.leaderos.plugin.bukkit.exceptions.RequestException;
import net.leaderos.plugin.shared.model.request.RequestType;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Request abstracted class
 *
 * @author poyrazinan
 * @since 1.0
 */
public abstract class Request {

    /**
     * body of request
     */
    private String body;

    /**
     * url of request
     */
    private URL url;

    /**
     * key of request
     */
    private String apiKey;

    /**
     * HtmlRequest object
     */
    private HttpURLConnection connection;

    /**
     * Request constructor
     *
     * @param api of request
     * @param body of request
     * @param type of request type (POST or GET)
     * @throws IOException for HttpUrlConnection
     * @throws RequestException for response errors
     */
    public Request(String api, String body, @NotNull RequestType type) throws IOException, RequestException {
        this.body = body;
        this.url = new URL( Main.getInstance().getConfigFile().getString("settings.url") + "/api/" + api);
        this.apiKey = Main.getInstance().getConfigFile().getString("settings.api-key");
        this.connection = (HttpURLConnection) this.url.openConnection();
        // Request type selector
        connection.setRequestMethod(type.name().toUpperCase());

        // Request Properties
        this.connection.setRequestProperty("X-Api-Key", this.apiKey);
        this.connection.setRequestProperty("Content-Type", "application/json");

        // Writes body if there is any
        if (this.body != null) {
            this.connection.setDoOutput(true); // We will write OutputStream to this request
            DataOutputStream outStream = new DataOutputStream(this.connection.getOutputStream());
            outStream.writeBytes(this.body);
        }

        int responseCode = connection.getResponseCode();
        if (!(responseCode == HttpURLConnection.HTTP_CREATED
                || responseCode == HttpURLConnection.HTTP_ACCEPTED
                || responseCode == HttpURLConnection.HTTP_OK))
            throw new RequestException("Connection to website has failed", responseCode, connection.getResponseMessage());
    }

    /**
     * Gets response of request
     *
     * @return JSONObject of response
     * @throws IOException for reader errors
     */
    public JSONObject getResponse() throws IOException {
        // BufferedReader for read data
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        return new JSONObject(response.toString());
    }

    /**
     * Closes connection of request
     */
    public void closeConnection() {
        this.connection.disconnect();
    }

}
