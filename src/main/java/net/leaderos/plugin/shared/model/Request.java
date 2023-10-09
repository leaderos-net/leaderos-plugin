package net.leaderos.plugin.shared.model;

import net.leaderos.plugin.Main;
import net.leaderos.plugin.bukkit.exceptions.RequestException;
import net.leaderos.plugin.shared.model.request.RequestType;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

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
    public Request(String api, Map<String, String> body, @NotNull RequestType type) throws IOException, RequestException {
        this.body = encodeFormData(body);
        this.url = new URL( Main.getInstance().getConfigFile().getSettings().getUrl()+ "/api/" + api);
        this.apiKey = Main.getInstance().getConfigFile().getSettings().getApiKey();
        this.connection = (HttpURLConnection) this.url.openConnection();
        // Request type selector
        connection.setRequestMethod(type.name().toUpperCase());

        // Request Properties
        this.connection.setRequestProperty("X-Api-Key", this.apiKey);
        this.connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        // Writes body if there is any
        if (this.body != null) {
            this.connection.setDoOutput(true); // We will write OutputStream to this request
            DataOutputStream outStream = new DataOutputStream(this.connection.getOutputStream());
            outStream.writeBytes(this.body);
        }

        int responseCode = connection.getResponseCode();
        // TODO REmove
        System.out.println(responseCode + " " + connection.getResponseMessage());
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
        connection.disconnect();
        // TODO REmove
        System.out.println(response);
        return new JSONObject(response.toString());
    }

    /**
     * Data encoder for request
     * @param data body
     * @return String value
     */
    private static String encodeFormData(Map<String, String> data) {
        try {
            StringBuilder encodedData = new StringBuilder();
            for (Map.Entry<String, String> entry : data.entrySet()) {
                if (encodedData.length() != 0) {
                    encodedData.append("&");
                }
                encodedData.append(entry.getKey());
                encodedData.append("=");
                encodedData.append(entry.getValue());
            }
            return encodedData.toString();
        }
        catch (NullPointerException e) {
            return null;
        }
    }

}
