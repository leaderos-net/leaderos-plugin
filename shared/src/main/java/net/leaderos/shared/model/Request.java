package net.leaderos.shared.model;

import lombok.Getter;
import lombok.SneakyThrows;
import net.leaderos.shared.Shared;
import net.leaderos.shared.error.Error;
import net.leaderos.shared.model.request.RequestType;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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
     * Response object
     */
    @Getter
    private Response response;

    /**
     * Request constructor
     *
     * @param api of request
     * @param body of request
     * @param type of request type (POST or GET)
     * @throws IOException for HttpUrlConnection
     */
    public Request(String api, Map<String, String> body, @NotNull RequestType type) throws IOException {
        this.body = encodeFormData(body);
        this.url = new URL( Shared.getLink() + "/api/" + api);
        this.apiKey = Shared.getApiKey();
        this.connection = (HttpURLConnection) this.url.openConnection();
        // Request type selector
        connection.setRequestMethod(type.name().toUpperCase());

        // Request Properties
        this.connection.setRequestProperty("X-Api-Key", this.apiKey);
        this.connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        // Writes body if there is any
        if (this.body != null) {
            this.connection.setDoOutput(true); // We will write OutputStream to this request
            try (OutputStreamWriter writer = new OutputStreamWriter(this.connection.getOutputStream(), StandardCharsets.UTF_8)) {
                writer.write(this.body);
            }
        }

        int responseCode = connection.getResponseCode();
        boolean status = (responseCode == HttpURLConnection.HTTP_CREATED
                || responseCode == HttpURLConnection.HTTP_ACCEPTED
                || responseCode == HttpURLConnection.HTTP_OK);

        String responseString = status ? getStream(connection.getInputStream()) : getStream(connection.getErrorStream());
        try {
            JSONObject obj = getResponseObj(responseString);
            this.response = new Response(responseCode, status, obj, obj.has("error") ? obj.getEnum(Error.class, "error") : null);

            Shared.getDebugAPI().send("Response Code: " + responseCode, false);
            Shared.getDebugAPI().send("URL: " + this.url, false);
            Shared.getDebugAPI().send(this.response.getResponseMessage().toString(), false);
        } catch (Exception e) {
            Shared.getDebugAPI().send("URL: " + this.url, false);
            Shared.getDebugAPI().send(e.getMessage(), true);
            Shared.getDebugAPI().send(responseString, true);
        }
        connection.disconnect();
    }

    /**
     * Gets response of request
     *
     * @return JSONObject of response
     * @throws IOException for reader errors
     */
    private String getStream(InputStream stream) throws IOException {
        // BufferedReader for read data
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        return response.toString();
    }

    /**
     * Gets response of request
     *
     * @return JSONObject of response
     */
    @SneakyThrows
    private JSONObject getResponseObj(String responseString) {
        JSONObject response;
        try {
            response = new JSONObject(responseString);
        }
        catch (JSONException jsonException) {
            JSONArray jsonArray = new JSONArray(responseString);
            response = new JSONObject();
            response.put("array", jsonArray);
        }

        return response;
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
